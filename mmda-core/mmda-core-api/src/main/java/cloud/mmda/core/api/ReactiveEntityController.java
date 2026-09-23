package cloud.mmda.core.api;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.caching.CachePolicy;
import cloud.mmda.core.clients.FileClient;
import cloud.mmda.core.data.jdbc.metadata.DbMetadataProvider;
import cloud.mmda.core.data.pagination.PagedList;
import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.data.pagination.Sort;
import cloud.mmda.core.entities.*;
import cloud.mmda.core.enums.ModuleAuthScope;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.models.Attachment;
import cloud.mmda.core.models.BackgroundTask;
import cloud.mmda.core.models.ChangeLog;
import cloud.mmda.core.models.ReportTemplate;
import cloud.mmda.core.security.models.UserAccount;
import cloud.mmda.core.services.*;
import cloud.mmda.core.services.exceptions.DataInvalidException;
import cloud.mmda.core.services.exceptions.DomainException;
import cloud.mmda.core.api.exceptions.QueryParameterException;
import cloud.mmda.core.services.exceptions.OperationFailedException;
import cloud.mmda.core.services.i18n.LocalizedMessage;
import cloud.mmda.core.services.i18n.LocalizedMessageService;
import cloud.mmda.core.utils.*;
import cloud.mmda.core.web.ResponseResult;
import cloud.mmda.core.web.WebLocaleResolver;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 响应式实体控制器基类
 *
 * 要求所有RestController都继承此类，使用其提供的便捷方法
 *
 * @author roshion
 * @since 2019.05
 * @version 3.0.0
 *
 * 安全和错误处理方式参考了以下内容：
 * <ul>
 *     <li>https://www.baeldung.com/get-user-in-spring-security</li>
 *     <li>https://www.baeldung.com/spring-security-expressions</li>
 *     <li>https://www.baeldung.com/spring-webflux-errors</li>
 * </ul>
 *
 * 在2.0.0基础上增加了：
 * <ol>
 *     <li>租户识别功能，根据域名以及HttpHeader中内容自动识别</li>
 *     <li>删除了@Deprecated注解的方法</li>
 *     <li>从Request中解析参数getIntParam改为popIntParam</li>
 * </ol>
 *
 * 定制返回数据
 * JsonView
 * JsonFilter & ResponseBodyAdvice
 * @param <T>
 */
public abstract class ReactiveEntityController<T extends Entity<K>, K> implements ReactiveApiController<T,K>, MessageSourceAware {
	private static final Log logger = LogFactory.getLog(ReactiveEntityController.class);
	private static final String URL_ENCODE = StandardCharsets.UTF_8.toString();
	//方法安全常用常数
	public static final String HAS_ROLE_USER 		= "hasRole('USER')";//用户
	public static final String HAS_ROLE_TOURIST 	= "hasRole('TOURIST')";//游客
	public static final String HAS_ROLE_STAFF 		= "hasRole('STAFF')";//员工
	public static final String HAS_ROLE_SUPERVISOR 	= "hasRole('SUPERVISOR')";//主管
	public static final String HAS_ROLE_MANAGER 	= "hasRole('MANAGER')";//经理
	public static final String HAS_ROLE_DIRECTOR 	= "hasRole('DIRECTOR')";//总监

	public static final String HAS_ROLE_PARTNER 	= "hasRole('PARTNER')";//贸易伙伴
	public static final String HAS_ROLE_CUSTOMER 	= "hasRole('CUSTOMER')";//客户
	public static final String HAS_ROLE_SUPPLIER 	= "hasRole('SUPPLIER')";//供应商
	public static final String HAS_ROLE_CARRIER 	= "hasRole('CARRIER')";//承运商

	public static final String HAS_ROLE_CARGO_OWNER = "hasRole('CARGO_OWNER')";//货主
	public static final String HAS_ROLE_ADMIN 		= "hasRole('ADMIN')";//管理员

	public static final String HAS_ROLE_WAREHOUSE_KEEPER = "hasRole('WAREHOUSE_KEEPER')";//仓管

	//二维码相关常数
	private static final String QR_USER = "USER";
	private static final String QR_SPLITTER = "&&";
	private static final String QR_SIGNATURE = "MMDA";
	private static final int QR_HASH_SEED = 2019;
	private static final int QR_EXPIRING_SECONDS=180;//动态二维码3分钟有效

	//头部键
	private static final String PAGER_HEADER = "x-pager";//分页器
	private static final String TENANT_ID_HEADER = "x-tenant-id";//租户
	public static final List<String> IP_HEADERS = Arrays.asList(
			"X-Forwarded-For",
			"Proxy-Client-IP",
			"WL-Proxy-Client-IP",
			"HTTP_CLIENT_IP",
			"HTTP_X_FORWARDED_FOR");

	//默认配置
	private static final Duration DEF_CACHE_DURATION = CachePolicy.DEF_EXPIRED_10_MINUTES;

	public static final String BY = "by";
	public static final String DEF_PAGE_SIZE = "10";
	public static final String DEF_PAGE_NO = "1";
	public static final String GUEST = "guest";

	protected DbMetadataProvider metadataProvider;

	//国际化消息
	protected MessageSource messageSource;
	protected LocalizedMessageService i18n;

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
		this.i18n = new LocalizedMessageService(messageSource);
	}

	private EntityService<T,K> service;

	public ReactiveEntityController(EntityService<T,K> service) {
		this.service = service;
		this.metadataProvider = service.getMetadataProvider();
	}

	protected final void throwDomainException(final DomainException ex){
		throw Exceptions.propagate(ex);
	}
	/**
	 * 获取当前租户ID
	 * @param request 请求
	 * @return 租户ID
	 */
	protected final Mono<Integer> getTenantID(ServerHttpRequest request){
//		String tid = request.getHeaders().getFirst(TENANT_ID_HEADER);
//		if(!BaseUtils.isNullOrEmpty(tid)){
//			return Mono.just(Short.parseShort(tid));
//		}
		return getCurrUser()
				.map(u-> Tenancy.parseTenantID(u.getUserID()))
				.switchIfEmpty(Mono.just(Tenancy.NO_TENANT_ID))
				.onErrorReturn(Tenancy.NO_TENANT_ID);
	}

	//region ipAddr
	/**
	 * 获取客户端ip地址，优先使用由Gateway中继过来的属性
	 * @param request
	 * @return
	 */
	protected String getRemoteIpAddr(ServerHttpRequest request){
		String ip = request.getHeaders().getFirst("INNER-USER");
		if(BaseUtil.isNullOrEmpty(ip)) {
			ip = getRemoteProxiedIpAddr(request);
		}
		return ip;
	}

	/**
	 * 从请求头中获取真实的客户端地址，可能经过代理服务器
	 * @param request
	 * @return
	 */
	protected String getRemoteProxiedIpAddr(ServerHttpRequest request){
		for (String ipHeader : IP_HEADERS) {
			String ip = request.getHeaders().getFirst(ipHeader);
			if (!BaseUtil.isNullOrEmpty(ip) && !ip.equalsIgnoreCase("unknown")) {
				//使用容器发布后，会这样 223.104.210.102,172.20.1.133
				if(ip.indexOf(",")!=-1){
					ip = ip.split(",")[0];
				}
				return ip;
			}
		}
		return request.getRemoteAddress().getAddress().toString();
	}

	//endregion

	//region user

	/**
	 * 获取当前用户，未登录返回guest
	 * @return
	 */
	protected Mono<String> getCurrUsername(){
		return ReactiveSecurityContextHolder.getContext()
				.map(c->c.getAuthentication().getName())
				.switchIfEmpty(Mono.just(GUEST))
				.onErrorReturn(GUEST);
	}

	/**
	 * 获取当前用户详情
	 *
	 * 未登录时抛出异常
	 * @throws AuthenticationCredentialsNotFoundException
	 * @return
	 */
	protected Mono<UserAccount> getCurrUser(){
		return ReactiveSecurityContextHolder.getContext()
				.handle((c, sink) -> {
					Authentication authentication = c.getAuthentication();
					if(authentication == null){
						sink.error(new AuthenticationCredentialsNotFoundException("not.authenticated"));
					}
					else if(authentication.getPrincipal() instanceof UserAccount user){
						sink.next(user);
					}
					else{
						sink.error(new BadCredentialsException("not.authenticated"));
					}
				});
	}

	protected Mono<Tuple2<Long,String>> getCurrUserIDNameOrIp(ServerHttpRequest request){
		return getCurrUser()
				.map(u->Tuples.of(u.getUserID(),u.getUsername()))
				.switchIfEmpty(Mono.just(Tuples.of(0L,getRemoteIpAddr(request))))
				.onErrorReturn(Tuples.of(0L,getRemoteIpAddr(request)));
	}

	protected Mono<String> getCurrUserQrCode(){
		return getCurrUser()
				.map(user->{
						StringBuilder qrCodeBuilder = user.buildQrCode(QR_SPLITTER);
						//签名
						HashFunction hf = Hashing.murmur3_32(QR_HASH_SEED);
						HashCode hc = hf.newHasher()
								.putString(QR_SIGNATURE, Charsets.US_ASCII)
								.putString(qrCodeBuilder.toString(), Charsets.US_ASCII)
								.hash();
						String qrCode = qrCodeBuilder
								.append("@")
								.append(hc.toString())
								.toString();
						return QR_USER+QR_SPLITTER+ //区分多种内部二维码
								Base64.getEncoder().encodeToString(qrCode.getBytes(Charsets.US_ASCII));
				})
				.onErrorReturn(GUEST);
	}
	public Mono<Map<String,String>> parseUserQrCode(String value){
		return Mono.fromCallable(
				()->{
					if(!value.startsWith(QR_USER)){
						LocalizedMessage localizedMessage = i18n.getLocalizedMessage("qrcode.type.mismatch");
						throw Exceptions.propagate(new DataInvalidException(localizedMessage));
					}
					String code = value.substring((QR_USER+QR_SPLITTER).length());
					byte[] qrCodeBytes = Base64.getDecoder().decode(code);
					String qrCodeWithSig = new String(qrCodeBytes,Charsets.US_ASCII);
					String[] qrCodeNSig = qrCodeWithSig.split("@");
					//验证签名
					HashFunction hf = Hashing.murmur3_32(QR_HASH_SEED);
					HashCode hc = hf.newHasher()
							.putString(QR_SIGNATURE, Charsets.US_ASCII)
							.putString(qrCodeNSig[0], Charsets.US_ASCII)
							.hash();
					if(!hc.toString().equals(qrCodeNSig[1])){
						LocalizedMessage localizedMessage = i18n.getLocalizedMessage("qrcode.invalid");
						throw Exceptions.propagate(new DataInvalidException(localizedMessage));
					}
					Map<String,String> result = UserAccount.parseQrCode(QR_SPLITTER,qrCodeNSig[0]);
					LocalDateTime genTime = LocalDateTime.parse(result.get("timestamp"));
					Duration duration = Duration.between(genTime,LocalDateTime.now());
					if(duration.getSeconds()>QR_EXPIRING_SECONDS){
						LocalizedMessage localizedMessage = i18n.getLocalizedMessage("qrcode.expired");
						throw Exceptions.propagate(new DataInvalidException(localizedMessage));
					}
					return result;
				}
		);
	}
	/**
	 * 获取当前用户，未登录返回ip地址或guest
	 * @return
	 */
	protected Mono<String> getCurrUsernameOrIp(ServerHttpRequest request){
		return ReactiveSecurityContextHolder.getContext()
				.map(c->c.getAuthentication().getName())
				.switchIfEmpty(Mono.just(getRemoteIpAddr(request)))
				.onErrorReturn(GUEST);
	}
	//endregion

	/**
	 * 添加分页器数据到Http响应（Flux没有分页概念）
	 * @param response Http响应
	 * @param paginator 分页器
	 */
	protected void addPager(ServerHttpResponse response, Paginator paginator){
		response.getHeaders().add(PAGER_HEADER, paginator.toJson());
	}

	//region mono & flux
	/**
	 * 创建一个Mono对象
	 * @param callable
	 * @return
	 */
	protected <U> Mono<U> createGenericMono(Callable<U> callable){
		return Mono.fromCallable(callable)
				.subscribeOn(Schedulers.boundedElastic());
	}

	protected Mono<T> createMono(Callable<T> callable){
		return createGenericMono(callable);
	}

	/**
	 * 创建Flux List
	 * @param listSupplier
	 * @return
	 */
	protected Flux<T> createFlux(Supplier<Collection<T>> listSupplier){
		return Flux.<T>create(emitter -> {
			try{
				//从数据库获取列表
				Collection<T> list = listSupplier.get();
				//构造流数据
				list.forEach(t -> emitter.next(t));
				emitter.complete();

			}
			catch(Exception e){
				emitter.error(e);
			}
		})
		//create doesn’t parallelize your code nor does it make it asynchronous
		//Even with the use of subscribeOn, Use the subscribeOn(Scheduler, false)
		//will use the Scheduler thread for the create and still let data flow
		//by performing request in the original thread
		//.subscribeOn(Schedulers.boundedElastic(),false)
		.subscribeOn(Schedulers.boundedElastic());
	}
	/**
	 * 创建Flux PagedList
	 * @param plistSupplier
	 * @return
	 */
	protected Flux<T> createFluxPage(Supplier<PagedList<T>> plistSupplier, ServerHttpResponse response){
		return Flux.<T>create(emitter -> {
			try{
				//从数据库获取列表
				PagedList<T> pList = plistSupplier.get();
				//写分页器到头部
				addPager(response, pList.getPagination());
				//构造流数据
				pList.getData().forEach(t -> emitter.next(t));
				emitter.complete();

			}
			catch(DataAccessException e){
				emitter.error(e);
			}
		})
		.subscribeOn(Schedulers.boundedElastic());
	}

	//endregion

	//region queryParam
	protected String getDefaultSort(){
		return service.getDefaultSort().toString();
	}

	protected Map<String,String> getSingleQueryParamMap(ServerHttpRequest request){
		MultiValueMap<String,String> targetMap = request.getQueryParams();
		LinkedHashMap<String, String> singleValueMap = new LinkedHashMap(targetMap.size());
		targetMap.forEach((key, values) -> {
			if (values != null && !values.isEmpty()) {
				if(values.size()==1)
					singleValueMap.put(key, values.get(0));
				else
					singleValueMap.put(key, IN+"("+String.join(",",values)+")");
			}
		});
		return singleValueMap;
	}
	/**
	 * 判断是否在URL中提供了命名参数
	 * @param request
	 * @param paramName 参数名称
	 * @return 是否提供参数
	 */
	protected final boolean hasQueryParam(ServerHttpRequest request, final String paramName){
		return request.getQueryParams().containsKey(paramName);
	}
	private final String decodeParam(String value){
		try{
			return URLDecoder.decode(value, URL_ENCODE);
		}
		catch (Exception ex){
			return value;
		}
	}
	/**
	 * 校验参数，用于你已经注入获取的参数校验
	 * @param paramValue 参数值
	 * @param paramName 参数名称
	 * @param validator 校验器
	 * @param <P> 参数类型
	 * @return 返回true 或者抛出异常
	 * @throws QueryParameterException 参数非法异常
	 */
	protected final <P> boolean validateParam(final P paramValue, final String paramName,Function<P,Boolean> validator) {
		Objects.requireNonNull(validator);
		//校验
		if(!validator.apply(paramValue)) {
			LocalizedMessage msg = i18n.getLocalizedMessage(QueryParameterException.CODE_INVALID,paramName);
			throwDomainException(new QueryParameterException(msg));
		}
		return true;
	}
	protected final DomainActionParam validateDomainActionParam(final DomainActionParam param, long userId, final String actionName){
		Assert.notNull(param,"param must not be null");
		Assert.notNull(param,"actionName must not be null");
		if(param.getOwnerID()!=null && !Tenancy.isSameTenant(userId, param.getOwnerID())){
			throwDomainException(new QueryParameterException("ownerID"));
		}
		if(param.getOwnerDeptID()!=null && !Tenancy.isSameTenant(userId, param.getOwnerDeptID())){
			throwDomainException(new QueryParameterException("ownerDeptID"));
		}
		if(!actionName.equals(param.getActionName())) param.setActionName(actionName);
		return param;
	}
	protected final K getKeyParam(final String ks, Function<String,K> parser){
		Objects.requireNonNull(parser);
		try{
			return parser.apply(ks);
		}
		catch (Exception ex){
			LocalizedMessage msg = i18n.getLocalizedMessage(QueryParameterException.CODE_INVALID, "id");
			throw Exceptions.propagate(new QueryParameterException(msg));
		}
	}

	/**
	 * 要求提供<P>类型的URL查询参数，
	 * 如果不是必须参数请使用{@link this#getQueryParam(ServerHttpRequest, String, Function, Function)}
	 * @param request
	 * @param paramName 参数名称
	 * @param parser 参数解析器，从字符串转换为<P>类型
	 * @param validator 参数校验器
	 * @param <P> 参数类型
	 * @return 返回Long类型的查询参数值
	 * @throws QueryParameterException 未提供参数、参数格式错误或者参数校验失败
	 */
	protected final <P> P requireQueryParam(ServerHttpRequest request, final String paramName,
											Function<String,P> parser, Function<P,Boolean> validator) {
		if(!request.getQueryParams().containsKey(paramName)){
			LocalizedMessage msg = i18n.getLocalizedMessage(QueryParameterException.CODE_REQUIRED,paramName);
			throwDomainException(new QueryParameterException(msg));
		}
		return getQueryParam(request,paramName,parser,validator);
	}

	/**
	 * 从请求中获取URL查询参数（<P>类型）
	 * @param request
	 * @param paramName 参数名称
	 * @param parser 参数解析器，从字符串转换为<P>类型
	 * @param validator 参数校验器
	 * @param <P> 参数类型
	 * @return 返回Long类型的查询参数值，如果未提供返回null
	 * @throws QueryParameterException 参数格式错误或者参数校验失败
	 */
	protected final <P> P getQueryParam(ServerHttpRequest request, final String paramName,
										Function<String,P> parser, Function<P,Boolean> validator)
			throws QueryParameterException{
		String param = request.getQueryParams().getFirst(paramName);
		if(BaseUtil.isNullOrWhitesapce(param)) return null;

		try{
			//尝试解析
			P value = parser.apply(param);

			//校验
			if(validator!=null && !validator.apply(value)) {
				LocalizedMessage msg = i18n.getLocalizedMessage(QueryParameterException.CODE_INVALID,paramName);
				throw new QueryParameterException(msg);
			}
			return value;
		}
		catch (NullPointerException ex){
			LocalizedMessage msg = i18n.getLocalizedMessage(QueryParameterException.CODE,paramName);
			throw Exceptions.propagate(new QueryParameterException(msg));
		}
		catch (Exception ex){
			throw Exceptions.propagate(ex);
		}
	}

	protected final boolean hasCreateByQueryParam(ServerHttpRequest request){
		return hasQueryParam(request,BY);
	}
	/**
	 * 创建类型参数，用于识别调用service层createByXxx
	 * @param request
	 * @return
	 */
	protected final String getCreateByQueryParam(ServerHttpRequest request){
		return getQueryParam(request,BY, Function.identity(),null);
	}
	/**
	 * 从请求中获取URL查询参数（Boolean类型）
	 * @param request
	 * @param paramName 参数名称
	 * @return 返回Boolean类型的查询参数值，如果未提供返回FALSE
	 * @throws QueryParameterException
	 */
	protected final Boolean getBooleanQueryParam(ServerHttpRequest request, final String paramName) {
		String param = request.getQueryParams().getFirst(paramName);
		if(BaseUtil.isNullOrWhitesapce(param)) return Boolean.FALSE;
		//尝试解析，1代表TRUE
		Boolean value = Boolean.valueOf(param);
		if("1".equals(param) || "true".equalsIgnoreCase(param)) value = Boolean.TRUE;
		return value;
	}
	/**
	 * 获取是否强制覆盖，批量上传数据后控制保存行为，
	 * 可不使用实体状态，而通过先查询实体是否存在，然后决定是insert还是update
	 *
	 * @param request
	 * @return
	 */
	protected final Boolean getCheckExistsParam(ServerHttpRequest request){
		return getBooleanQueryParam(request,"checkExists");
	}

	protected final Locale getLocale(ServerHttpRequest request){
		String lang = request.getQueryParams().getFirst("lang");
		Optional<Locale> localeOptional = WebLocaleResolver.resolveLocaleFromString(lang);
		return localeOptional.orElse(WebLocaleResolver.DEFAULT_LOCALE);
	}
	/**
	 * 从map中解析整形参数，会从map中移除
	 * @param name
	 * @param parameters
	 * @param defVal
	 * @return
	 */
	protected final int popIntParam(final String name, final Map<String,String> parameters, final String defVal){
		String value = popStringParam(name,parameters,defVal);
		return Integer.parseInt(value);
	}
	protected final String popStringParam(final String name, final Map<String,String> parameters, final String defVal){
		String value = parameters.remove(name);
		if(BaseUtil.isNullOrEmpty(value)) return defVal;
//		else value = decodeParam(value);
		// 定义常见的不需要解码的特殊字符模式
		String specialCharsPattern = "[+&@#$^]";
		Pattern pattern = Pattern.compile(specialCharsPattern);
		Matcher matcher = pattern.matcher(value);
		if (!matcher.find()) {
			value = decodeParam(value);
		}
		//数据库特殊字符转义
		if ("searchWord".equals(name)){
			value=metadataProvider.escapeSpecialCharacters(value);
		}
		return value;
	}
	/**
	 * 从请求参数map中解析出分页器，相关参数移出map
	 * @param parameters
	 * @return
	 */
	protected final Paginator popPagerParam(final Map<String,String> parameters){
		int pageSize= popIntParam("pageSize",parameters,DEF_PAGE_SIZE);
		int pageNo = popIntParam("pageNo", parameters, DEF_PAGE_NO);
		String sort = popStringParam("sort", parameters, getDefaultSort());
		return new Paginator(pageSize,pageNo,sort);
	}
	protected final Sort popSortParam(final Map<String,String> parameters){
		String sort = popStringParam("sort", parameters, getDefaultSort());
		return Sort.parse(sort);
	}

	//////////////////////////////////////////////////////////////////////////
	// 过滤条件解析，值可支持BETWEEN a AND b 或者 IN a,b,c
	//////////////////////////////////////////////////////////////////////////
	private static final String BETWEEN = "BETWEEN ";
	protected static final String IN = "IN ";
	protected static final String NOT_IN = "NOT IN ";
	private static final String LIKE = "LIKE ";
	private static final String NOT_LIKE = "NOT LIKE ";
	private static final String IS_NULL = "IS NULL";
	protected static final String IS_NOT_NULL = "IS NOT NULL";
	private static final String NOT_EQUAL = "<>";
	private static final String NOT_EQUAL_SYMBOL = "NEQ ";
	private static final String GREATER_THAN = ">";
	private static final String GREATER_THAN_SYMBOL = "GT ";
	private static final String GREATER_OR_EQUAL = ">=";
	private static final String GREATER_OR_EQUAL_SYMBOL = "GTEQ ";
	private static final String LESS_THAN = "<";
	private static final String LESS_THAN_SYMBOL = "LT ";
	private static final String LESS_OR_EQUAL = "<=";
	private static final String LESS_OR_EQUAL_SYMBOL = "LTEQ ";
	private static final String AND = "AND";
	public static final String AND_WITH_SPACE = " AND ";
	protected static final String OR_WITH_SPACE = " OR ";
	private static final String EQUAL = "=";
	private static final String Q = "'";
	private static final String SPACE = " ";

	private static final String quoteString(final String value){
		return Q+value+Q;
	}
	private List<DateTimeRange> mergeDateTimeRages(List<DateTimeRange> dateTimeRanges){
		int size = dateTimeRanges.size();
		if(size<=1) return dateTimeRanges;

		List<DateTimeRange> result = new ArrayList<>();
		for(int i=0; i<size; i++){
			DateTimeRange dtr = dateTimeRanges.get(i);
			boolean included = false;
			for(int j=0; j<size; j++){
				if(j==i) continue;
				if(dateTimeRanges.get(j).include(dtr)){
					included = true;
					break;
				}
			}
			if(!included) result.add(dtr);
		}
		return result;
	}

	private static String getAliasDotColName(MetaObject metaObject,String colName){
		if(colName.indexOf('.')==-1){
			String alias = metaObject.hasCol(colName) ? "t." : "";
			colName = alias+colName;
		}
		return colName;
	}
	private static String getQuotedValue(MetaObject metaObject, String colName, String colValue){
		Objects.requireNonNull(colValue);
		if(!colValue.startsWith(Q) || !colValue.endsWith(Q)) {
			if(colName.startsWith("t.")) colName=colName.substring(2);
			MetaCol col = metaObject.getCol(colName);
			if(col!=null){
				//处理枚举
				if(col.isEnumType()){
					for (Map.Entry<String, NameValue<String,String>> entry : col.getEnumMap().entrySet()){
						if (colValue.equals(entry.getValue().getName())
								|| colValue.equals(entry.getValue().getValue())
								|| colValue.equals(entry.getKey()))
							return entry.getKey();
					}
				}
				else if(col.getDataType().isQuotable()) {
					return quoteString(colValue);
				}
			}
		}
		return colValue;
	}
	private static String[] getQuotedValues(MetaObject metaObject,String colName, String[] colValues){
		Objects.requireNonNull(colValues);
		if (colValues.length==0) return colValues;

		if(!colValues[0].startsWith(Q) || !colValues[0].endsWith(Q)){
			MetaCol col = metaObject.getCol(colName);
			if(col!=null){
				if(col.isEnumType()){
					Map<String,String> namedValues = col.getEnumMap().entrySet().stream()
							.collect(Collectors.toMap(e->e.getValue().getValue(),e->e.getKey()));
					return Arrays.stream(colValues).map(colValue-> namedValues.get(colValue)).toArray(String[]::new);
				}
				else if(col.getDataType().isQuotable()){
					return Arrays.stream(colValues).map(colValue-> quoteString(colValue)).toArray(String[]::new);
				}
			}
		}
		return colValues;
	}
	private String parseCondition(Map.Entry<String,String> entry, MetaObject metaObject){
		String value = decodeParam(entry.getValue());
		String colName = getAliasDotColName(metaObject,entry.getKey());
		if(IS_NULL.equalsIgnoreCase(value) || IS_NOT_NULL.equalsIgnoreCase(value)){
			String columnName = (colName.startsWith("t.")) ? colName.substring(2) : colName;
			MetaCol col = metaObject.getCol(columnName);
			if (col != null) {
				if (col.getDataType().isString())
					return "(" + String.join(OR_WITH_SPACE, (colName + SPACE + value), (colName + SPACE + (IS_NULL.equalsIgnoreCase(value) ? EQUAL : NOT_EQUAL) + "''")) + ")";
			}
			return colName + SPACE + value;
		}
		else if(DateRange.hasKind(value)){
			//支持预定义的日期范围，如TODAY, THIS_WEEK，用于日期过滤器
			DateTimeRange dateTimeRange = DateRange.kindOf(value).toDateTimeRange();
			return String.join(SPACE,colName,BETWEEN,quoteString(dateTimeRange.getBegin().toString()),AND_WITH_SPACE,quoteString(dateTimeRange.getEnd().toString()));
		}
		else if(value.toUpperCase().startsWith(BETWEEN)){
			String[] values= getQuotedValues(metaObject,colName,value.toUpperCase().replace(BETWEEN,"").split(AND_WITH_SPACE));
			return String.join(SPACE,colName,BETWEEN,values[0],AND_WITH_SPACE,values[1]);
		}
		else if(value.toUpperCase().startsWith(IN) || value.toUpperCase().startsWith(NOT_IN)){
			String inOrNot = value.toUpperCase().startsWith(NOT_IN) ? NOT_IN : IN;
			String[] values = value.substring(inOrNot.length()).split(",");
			//如果是多个日期过滤，dlvDate IN(TODAY,YESTERDAY,...)
			if(DateRange.hasKind(values[0])){
				List<DateTimeRange> dateTimeRanges = mergeDateTimeRages(
						Arrays.stream(values)
							.map(v->DateRange.kindOf(v).toDateTimeRange())
							.collect(Collectors.toList())
				);
				return dateTimeRanges.stream()
						.map(dateTimeRange -> String.join(SPACE,colName,BETWEEN,
								quoteString(dateTimeRange.getBegin().toString()),
								AND_WITH_SPACE,
								quoteString(dateTimeRange.getEnd().toString())))
						.collect(Collectors.joining(OR_WITH_SPACE));

			} else if (isEnumType(colName,metaObject)) {
				String list = Arrays.stream(values).map(v -> getQuotedValue(metaObject, colName, v)).collect(Collectors.joining("','"));
				return String.join(SPACE, colName, inOrNot, "(", quoteString(list), ")");
			}
			//普通的直接使用IN
			else{
				String list = String.join("','",values);
				return String.join(SPACE,colName,inOrNot,"(",quoteString(list),")");
			}
		}
		else if(value.toUpperCase().startsWith(LIKE) || value.toUpperCase().startsWith(NOT_LIKE)){
			String likeOrNot = value.toUpperCase().startsWith(NOT_LIKE) ? NOT_LIKE : LIKE;
			String likeValue = value.substring(likeOrNot.length()).replace("'","");

			//规避搜索值里面有"%_"特殊字符,数据库需要转义,
			String resultValue=metadataProvider.escapeSpecialCharacters(likeValue.replaceAll("^%|%$", ""));
            //if (likeValue .startsWith("%")) likeValue="%"+resultValue;
            //if (likeValue .endsWith("%")) likeValue=resultValue+"%";
			if (likeValue .startsWith("%") && !likeValue .endsWith("%")) likeValue="%"+resultValue;
			if (likeValue .endsWith("%") && !likeValue .startsWith("%")) likeValue=resultValue+"%";
			if (likeValue .startsWith("%") && likeValue .endsWith("%")) likeValue="%"+resultValue+"%";

			//客户端已经拼接好了
			if(likeValue.startsWith("'") && likeValue.endsWith("'"))
				return String.join(SPACE,colName,likeOrNot,likeValue);

//			//如果客户端只传值，中间没有单引号，则自动加%
//			if(likeValue.indexOf('%') == -1){
//				if(!likeValue.startsWith("%")) likeValue = "%"+likeValue;
//				if(!likeValue.endsWith("%")) likeValue += "%";
//			}

			// 如果客户端只传值，中间没有单引号，则自动加%
			if (likeValue.indexOf('%') == -1) {
				likeValue = "%" + likeValue + "%";
			}

			return String.join(SPACE,colName,likeOrNot,quoteString(likeValue));
		}
//		else if(value.startsWith(GREATER_THAN) || value.startsWith(LESS_THAN)){
//			return colName + value;
//		}
//		else if(value.startsWith(GREATER_THAN_SYMBOL)){
//			return colName + value.replace(GREATER_THAN_SYMBOL,GREATER_THAN);
//		}
//		else if(value.startsWith(GREATER_OR_EQUAL_SYMBOL)){
//			return colName + value.replace(GREATER_OR_EQUAL_SYMBOL,GREATER_OR_EQUAL);
//		}
//		else if(value.startsWith(LESS_THAN_SYMBOL)){
//			return colName + value.replace(LESS_THAN_SYMBOL,LESS_THAN);
//		}
//		else if(value.startsWith(LESS_OR_EQUAL_SYMBOL)){
//			return colName + value.replace(LESS_OR_EQUAL_SYMBOL,LESS_OR_EQUAL);
//		}
//		else if(value.startsWith(NOT_EQUAL_SYMBOL)){
//			return colName + value.replace(NOT_EQUAL_SYMBOL,NOT_EQUAL);
//		}
		else{
			String op = EQUAL;
			if(value.startsWith(GREATER_THAN)){
				op = GREATER_THAN;
				value = value.substring(GREATER_THAN.length());
			}
			else if(value.startsWith(GREATER_THAN_SYMBOL)){
				op = GREATER_THAN;
				value = value.substring(GREATER_THAN_SYMBOL.length());
			}
			else if(value.startsWith(GREATER_OR_EQUAL_SYMBOL)){
				op = GREATER_OR_EQUAL;
				value = value.substring(GREATER_OR_EQUAL_SYMBOL.length());
			}
			else if(value.startsWith(NOT_EQUAL)){
				op = NOT_EQUAL;
				value = value.substring(NOT_EQUAL.length());
			}
			else if(value.startsWith(LESS_THAN)){
				op = LESS_THAN;
				value = value.substring(LESS_THAN.length());
			}
			else if(value.startsWith(LESS_THAN_SYMBOL)){
				op = LESS_THAN;
				value = value.substring(LESS_THAN_SYMBOL.length());
			}
			else if(value.startsWith(LESS_OR_EQUAL_SYMBOL)){
				op = LESS_THAN;
				value = value.substring(LESS_OR_EQUAL_SYMBOL.length());
			}
			else if(value.startsWith(NOT_EQUAL_SYMBOL)){
				op = NOT_EQUAL;
				value = value.substring(NOT_EQUAL_SYMBOL.length());
			}
			return String.join(SPACE,colName,op,getQuotedValue(metaObject,colName,value));
		}
	}

	private boolean isEnumType(String colName, MetaObject metaObject) {
		if(colName.startsWith("t.")) colName=colName.substring(2);
		MetaCol col = metaObject.getCol(colName);
		if(col!=null){
			//处理枚举
            return col.isEnumType();
		}
		return false;
	}

	/**
	 * 与表达式
	 * @param a 条件a，可能为空
	 * @param b 条件b，可能为空
	 * @return
	 */
	private String and(String a, String b){
		if(!BaseUtil.hasText(a)) return b;
		if(!BaseUtil.hasText(b)) return a;
		return "("+a+") AND ("+b+")";
	}

	/**
	 * 解决日期条件表达式
	 * @param exp 例如 (dlvDate=TODAY OR dlvDate=THIS_WEEK)
	 * @return 返回Sql查询表达式
	 */
	private String resolveDateRangeExpression(String exp){
		if(exp.startsWith("(")) exp = exp.substring(1,exp.length()-1);//去()
		String[] expArr = exp.split(OR_WITH_SPACE);
		List<DateTimeRange> dateTimeRangeList = new ArrayList<>();
		String colName = null;
		for(int i=0; i<expArr.length; i++){
			String[] e = expArr[i].split(EQUAL);
			colName = e[0];
			DateTimeRange dateTimeRange = DateRange.kindOf(e[1]).toDateTimeRange();
			dateTimeRangeList.add(dateTimeRange);
		}
		List<DateTimeRange> dateTimeRanges = mergeDateTimeRages(dateTimeRangeList);
		final String leftExp = colName;
		String result = dateTimeRanges.stream()
				.map(dateTimeRange -> String.join(SPACE,leftExp,BETWEEN,
						quoteString(dateTimeRange.getBegin().toString()),
						AND_WITH_SPACE,
						quoteString(dateTimeRange.getEnd().toString())))
				.collect(Collectors.joining(OR_WITH_SPACE));
		return "("+result+")";
	}
	/**
	 * 构建查询条件
	 * @param parameters
	 * @return
	 */
	protected final String getCondParam(final Map<String,String> parameters){
		// 先解析过滤器条件
		return getCondParam(parameters,service.getMetaObject());
	}
	protected final String getCondParam(final Map<String,String> parameters,MetaObject metaObject){
		// 先解析过滤器条件
		String filterCond = popStringParam("filter",parameters,null);
		if(BaseUtil.hasText(filterCond) && DateRange.hasAnyKind(filterCond)){
			//解析日期区间条件
			String[] filterCondArr = filterCond.split(AND_WITH_SPACE);
			for(int i=0; i<filterCondArr.length; i++){
				String cond = filterCondArr[i];
				if(DateRange.hasAnyKind(cond)){
					filterCondArr[i] = resolveDateRangeExpression(cond);
				}
			}
			filterCond = Arrays.stream(filterCondArr).collect(Collectors.joining(AND_WITH_SPACE));
		}
		//如果没有其他条件，则返回
		if(parameters.isEmpty()) return filterCond;

		//与上其他条件，field1=value1形式的
		String otherCond = parameters.entrySet().stream()
				.filter(entry-> !BaseUtil.isNullOrEmpty(entry.getValue()))
				.map(entry->parseCondition(entry,metaObject))
				.collect(Collectors.joining(AND_WITH_SPACE));
		return and(filterCond,otherCond);
	}

	//endregion

	//region file
	//////////////////////////////////////////////////////////////////////////
	// 上传下载文件
	//////////////////////////////////////////////////////////////////////////
	private static final String xlsMimeType = "application/vnd.ms-excel";
	private static final String xlsxMimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";


	protected String[] popExportFieldsParam(Map<String,String> parameters){
		String fields = parameters.remove("exportFields");
		if(BaseUtil.isNullOrEmpty(fields)) return new String[0];
		return fields.split(",");
	}

	protected Mono<Void> downloadFile(File file, ServerHttpResponse response){
		String fullFileName = file.getName();
		int dotPos = fullFileName.lastIndexOf('.');
		String fileName="",fileExt="";
		if(dotPos>0){
			fileName = fullFileName.substring(0,dotPos-1);
			fileExt = fullFileName.substring(dotPos);
		}
		else{
			fileName = fullFileName;
		}

		HttpHeaders httpHeaders = response.getHeaders();

		try{
			String encodedFileName = URLEncoder.encode(fileName,"UTF-8") + fileExt;
			httpHeaders.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+encodedFileName);

			MediaType mediaType=null;
			String contentType = Files.probeContentType(file.toPath());
			//TODO windows服务出现contentType为null，Invalid mime type \"null\": 'mimeType' must not be empty"
			if (!StringUtils.isEmpty(contentType)) {
				MimeType mimeType = MimeTypeUtils.parseMimeType(contentType);
				mediaType=MediaType.asMediaType(mimeType);
//				logger.warn(mediaType.getType());
			}else{
				mediaType=MediaTypeFactory.getMediaType(file.getName()).orElse(MediaType.APPLICATION_OCTET_STREAM);
//				logger.warn(mediaType.getType());
			}
			httpHeaders.setContentType(mediaType);
		}
		catch (IOException ex){
			httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		}
		httpHeaders.setContentLength(file.length());
		httpHeaders.setLastModified(file.lastModified());

		ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
		return zeroCopyResponse.writeWith(file, 0, file.length());
	}

	protected Mono<File> uploadFile(@RequestPart("file") Mono<FilePart> filePart) throws IOException {
//		Path tempFile = Files.createTempFile("temp", filePart.filename());
//		AsynchronousFileChannel channel =
//				AsynchronousFileChannel.open(tempFile, WRITE);
		//异步的文件流，可能导致文件不完整
//		DataBufferUtils.write(filePart.content(), channel, 0)
//				.doOnComplete(() -> {
//					System.out.println("finish");
//				})
//				.subscribe();

		//也可以这样
		return filePart.handle((f, sink) -> {
            try {
				//https://www.baeldung.com/spring-reactive-read-flux-into-inputstream
//				PipedOutputStream outputStream = new PipedOutputStream();
//				PipedInputStream inputStream = new PipedInputStream(1024*10);
//				inputStream.connect(outputStream);
//				OutputStream outputStream = new ByteArrayOutputStream();
//				DataBufferUtils.write(DataBufferUtils.join(f.content()),outputStream);

                File file = Files.createTempFile("temp", f.filename()).toFile();
				f.transferTo(file);
				sink.next(file);
            } catch (IOException e) {
				sink.error(new RuntimeException(e));
            }
		});
	}

	protected Flux<File> uploadFiles(@RequestPart("files") Flux<FilePart> fileParts)  {
		return fileParts.flatMap(filePart -> {
			try {
				File file = Files.createTempFile("temp",filePart.filename()).toFile();
				filePart.transferTo(file);
				return Mono.just(file);
            } catch (IOException e) {
				return Flux.error(e);
            }
		});
	}


 	//endregion

	//region cache policy

	/**
	 * 获取实体对象默认的缓存策略（子类必须实现此接口）。
	 * 例如已经最终确定的数据可缓存，处理过程中的数据缓存没有意义。
	 * @param request
	 * @param t 实体对象，可根据其状态采取不同的缓存策略
	 * @return
	 */
	protected abstract Mono<CachePolicy> getCachePolicy(ServerHttpRequest request, T t);

	/**
	 * 获取默认的缓存策略，此函数决定了实体<T>是否采用缓存（子类必须实现此接口）。
	 * @param request
	 * @return
	 */
	protected abstract Mono<CachePolicy> getDefaultCachePolicy(ServerHttpRequest request);

	/**
	 * 获取默认的缓存策略
	 *
	 * 使用READ_THRU策略，10分钟失效
	 * @param request
	 * @return
	 */
	protected Mono<CachePolicy> getCachePolicyOf10Min(ServerHttpRequest request){
		return getCachePolicy(request,CachePolicy.READ_THRU,DEF_CACHE_DURATION);
	}
	protected Mono<CachePolicy> getNoCachePolicy(ServerHttpRequest request){
		return getTenantID(request)
				.map(tenantID->CachePolicy.of(tenantID,CachePolicy.NONE,Duration.ZERO));
	}
	protected Mono<CachePolicy> getCachePolicy(ServerHttpRequest request, int policy, Duration duration){
		return getTenantID(request)
				.map(tenantID->CachePolicy.of(tenantID,policy,duration));
	}
	//endregion

	//region open api
	protected void applyConsumerIfNonNull(T t, Consumer<T> consumer){
		if(consumer==null || t==null) return;
		consumer.accept(t);
	}
	protected T applyFunctionIfNonNull(T t, Function<T, T> func){
		if(func==null || t==null) return t;
		return func.apply(t);
	}
	/**
	 * 默认创建器，
	 * 子类需重写，负责设置creatorID, deptID等相关字段
	 * @param user 创建人
	 * @param p 缓存策略
	 * @return
	 */
	protected T defaultCreator(final UserAccount user, final CachePolicy p){
		var t = service.create(p);
		if(t instanceof Auditable auditable){
			auditable.createBy(user.getUserID(), user.getDeptID());
		}
		return t;
	}
	protected T parameterCreator(final UserAccount user, final CachePolicy cachePolicy, final Optional<RefParam> prototype){
		return defaultCreator(user,cachePolicy);
	}

	public Mono<T> create(ServerHttpRequest request, UserAccount user, Mono<Optional<RefParam>> prototypeMono){
		 return getOrCreateFromCache(request,user,prototypeMono);
	}
	public Mono<T> createAndCache(ServerHttpRequest request, UserAccount user, Mono<Optional<RefParam>> prototypeMono){
		return prototypeMono.zipWhen(
				s -> getDefaultCachePolicy(request),
				(s,p) -> parameterCreator(user,p,s)
		).map(t->{
			if (getBooleanQueryParam(request, "cache"))
				try {
					String cacheKey = "Temporary:"+service.getCacheProvider().getCacheKey(t.getId());
					service.getCacheProvider().putAny(cacheKey,t,CachePolicy.DEF_EXPIRED_10_MINUTES);
				}catch (Exception e){
					logger.error("createToCache error:"+e.getMessage(),e);
				}
			return t;
		}).map(t -> service.assembleSingle(t));
	}

	// 获取缓存对象，如果缓存不存在则调用 create 创建对象
	public Mono<T> getOrCreateFromCache(ServerHttpRequest request, UserAccount user, Mono<Optional<RefParam>> prototypeMono) {
		// 从请求的查询参数中获取 id
		String id = popStringParam("id", request.getQueryParams().toSingleValueMap(), null);
		// 如果 id 存在，则尝试从缓存中获取对象
		if (BaseUtil.hasText(id)) {
			try {
				K k = (K)Long.valueOf(id);
				String cacheKey = "Temporary:"+service.getCacheProvider().getCacheKey(k);
				// 尝试从缓存中获取对象
				T t = service.getCacheProvider().getAny(cacheKey);
				return Mono.just(service.assembleSingle(t));
			} catch (Exception e) {
				logger.error("Error retrieving from cache: " + e.getMessage(), e);
			}
		}
		// 如果没有缓存或发生异常，调用 create 方法创建对象
		return createAndCache(request, user, prototypeMono);
	}

	@Override
	public Mono<Integer> save(ServerHttpRequest request, Mono<T> tMono){
		return tMono.zipWhen(
				t->getCachePolicy(request, t),
				(t,p)->service.save(t,p, getCheckExistsParam(request),null)
		).subscribeOn(Schedulers.boundedElastic());
	}

	/**
	 * 保存并读取（不含子表）
	 * @param request
	 * @param tMono 单个实体对象
	 * @return
	 */
	@Override
	public Mono<T> saveAndGet(ServerHttpRequest request, Mono<T> tMono){
		return tMono.zipWhen(
				t->getCachePolicy(request, t),
				(t,p)->service.saveAndGet(t,p, getCheckExistsParam(request))
		).subscribeOn(Schedulers.boundedElastic())
				.map(t -> service.assembleSingle(t));
	}
	/**
	 * 保存并载入（含子表）
	 * @param request
	 * @param tMono 单个实体对象
	 * @return
	 */
	@Override
	public Mono<T> saveAndLoad(ServerHttpRequest request, Mono<T> tMono){
		return tMono.zipWhen(
				t->getCachePolicy(request, t),
				(t,p)->service.saveAndLoad(t,p, getCheckExistsParam(request))
		).subscribeOn(Schedulers.boundedElastic())
				.map(t -> service.assemble(t));
	}

	@Override
	public Mono<Integer> saveAll(ServerHttpRequest request,@RequestBody Flux<T> tFlux){
//		boolean autoSave = getCheckExistsParam(request);
		return tFlux.flatMap(
				t -> getCachePolicy(request, t)
					.map(p -> service.save(t, p, getCheckExistsParam(request)))
		).subscribeOn(Schedulers.boundedElastic()).reduce(
		  0,
				(cnt, r)->cnt + r
		);
	}
	@Override
	public Flux<T> saveAndGetAll(ServerHttpRequest request, Flux<T> tFlux){
		return tFlux.flatMap(t ->
				getCachePolicy(request, t)
						.map(p -> service.saveAndGet(t, p, getCheckExistsParam(request)))
		)
				//保存多条记录使用弹性线程池
				.subscribeOn(Schedulers.boundedElastic())
				//这里负责组装
				.map(t -> service.assembleSingle(t));
	}
	@Override
	public Flux<T> saveAndLoadAll(ServerHttpRequest request, Flux<T> tFlux){
		return tFlux.flatMap(t ->
					getCachePolicy(request, t)
						.map(p -> service.saveAndLoad(t, p, getCheckExistsParam(request)))
				)
				//保存多条记录使用弹性线程池
				.subscribeOn(Schedulers.boundedElastic())
				//这里负责组装
				.map(t -> service.assemble(t));
	}

	@Override
	public Mono<Integer> update(ServerHttpRequest request, Mono<T> tMono){
		return tMono.zipWhen(
				t->getCachePolicy(request, t),
				(t,p)->service.update(t,p)
		).subscribeOn(Schedulers.boundedElastic());
	}

	@Override
	public Mono<T> updateAndGet(ServerHttpRequest request, Mono<T> tMono){
		return tMono.zipWhen(
				t->getCachePolicy(request, t),
				(t,p)->service.updateAndGet(t,p)
		).subscribeOn(Schedulers.boundedElastic())
				.map(t -> service.assembleSingle(t));
	}
	@Override
	public Mono<T> updateAndLoad(ServerHttpRequest request, Mono<T> tMono){
		return tMono.zipWhen(
				t->getCachePolicy(request, t),
				(t,p)->service.updateAndLoad(t,p)
		).subscribeOn(Schedulers.boundedElastic())
				.map(t -> service.assemble(t));
	}

	@Override
	public Mono<Integer> updateAll(ServerHttpRequest request, Flux<T> tFlux){
		return tFlux.flatMap(
				t -> getCachePolicy(request, t)
						.map(p -> service.update(t, p))
		).subscribeOn(Schedulers.boundedElastic()).reduce(
				0,
				(cnt, r)->cnt + r
		);
	}


	@Override
	public Mono<Integer> delete(ServerHttpRequest request, final K k){
		return get(request, k)
				.zipWhen(
						t -> getCachePolicy(request, t),
						(t,p)-> service.delete(t,p)
				);
	}

	@Override
	public Mono<Integer> deleteAll(ServerHttpRequest request, Flux<K> kFlux){
		return kFlux.collectList()
				.zipWhen(
						kList -> getDefaultCachePolicy(request), //只要使用过缓存，指示服务层清除
						(kList, p) -> service.deleteAll(kList, p)
				);
	}
	@Override
	public Mono<Integer> partialUpdate(ServerHttpRequest request, final K k, Mono<Map<String,Object>> mapMono){
		return mapMono.zipWhen(
				attributes -> getDefaultCachePolicy(request),
				(attributes, p) -> service.partialUpdate(k, attributes, p)
		);
	}
	@Override
	public Mono<Integer> partialUpdateAll(ServerHttpRequest request, Flux<Map<String,Object>> mapFlux){
		return mapFlux.collectList()
				.zipWhen(
						listOfAttributes -> getDefaultCachePolicy(request),
						(list, p) -> service.partialUpdateAll(list, p)
				);
	}
	@Override
	public Mono<T> get(ServerHttpRequest request, final K k){
		return getDefaultCachePolicy(request)
				.map(p -> service.load(k, p))
				.map(t -> service.assemble(t));
	}

	protected void beforeQuery(UserAccount user,Map<String,String> queryParam,Locale locale){
		//预处理查询参数
	}
	//region 用户权限列表过滤

	/**
	 * 验证用户是否具有强权限类型
	 */
	protected boolean isStrongTypePermission() {
		return false;
	}

	private static final String CREATORID = "creatorID";
	private static final String DEPTID = "deptID";
	private static final String OWNERID = "ownerID";
	private static final String OWNERDEPTID = "ownerDeptID";
	private static final String PROJECTID = "projectID";
	private static  final String PROJECTMEMBERSQL  = " EXISTS (  SELECT m.projectID FROM mmda_mes.projectmember m WHERE m.projectID=t.projectID AND m.memberID=%d )";

	protected void addQueryParamsBasedOnAuthority(UserAccount user, MetaObject metaObj, Map<String, String> queryParams) {
		String moduleCode = popStringParam("moduleCode", queryParams, "");

		if (metaObj == null) metaObj = service.getMetaObject();
		// 如果没有相关列，直接返回
		if (!(metaObj.hasCol(CREATORID) || metaObj.hasCol(DEPTID) || metaObj.hasCol(OWNERID) || metaObj.hasCol(OWNERDEPTID))) {
			return;
		}
		var module = service.getModule(LocaleContextHolder.getLocale());
		Authority authority =
				module.isPresent()
				? (isStrongTypePermission()
						? service.getModuleAuthority(user.getUserID(), LocaleContextHolder.getLocale())
						: service.getModuleAuthority(user.getUserID(), moduleCode, LocaleContextHolder.getLocale()))
				: Authority.READ_ONLY;

		buildAuthCondBasedOnScope(authority.getAuthScope(), user, metaObj, queryParams);

	}
	/**
	 * 根据权限范围构建授权条件
	 */
	private void buildAuthCondBasedOnScope( ModuleAuthScope moduleAuthScope, UserAccount user, MetaObject metaObj,Map<String, String> queryParams) {
		// 获取并构建初始的过滤条件
		String filterCond = popStringParam("filter", queryParams, "");
		StringBuilder authCond = new StringBuilder();
		switch (moduleAuthScope) {
			case SELF -> {
				// 处理SELF权限，构建条件
				if (metaObj.hasCol(CREATORID)) {
					authCond.append("t.").append(CREATORID).append(EQUAL).append(user.getUserID());
				}
				if (metaObj.hasCol(OWNERID)) {
					if (authCond.length() > 0) authCond.append(OR_WITH_SPACE);
					authCond.append("t.").append(OWNERID).append(EQUAL).append(user.getUserID());
				}
			}
			case DEPARTMENT -> {
				// 处理DEPARTMENT权限，构建条件
				if (metaObj.hasCol(DEPTID)) {
					authCond.append("t.").append(DEPTID).append(EQUAL).append(user.getDeptID());
				}
				if (metaObj.hasCol(OWNERDEPTID)) {
					if (authCond.length() > 0) authCond.append(OR_WITH_SPACE);
					authCond.append("t.").append(OWNERDEPTID).append(EQUAL).append(user.getDeptID());
				}
			}
			case DIVISION -> {
				// 处理DIVISION权限，构建条件
				List<Long> deptIDs = service.getDescendantDivisionDepartmentIDs(user.getDeptID());
				deptIDs.add(user.getDeptID());
				if (metaObj.hasCol(DEPTID)) {
					authCond.append("t.").append(DEPTID).append(IN).append("(")
							.append(String.join(",", deptIDs.stream().map(String::valueOf).collect(Collectors.toList())))
							.append(")");
				}
				if (metaObj.hasCol(OWNERDEPTID)) {
					if (authCond.length() > 0) authCond.append(OR_WITH_SPACE);
					authCond.append("t.").append(OWNERDEPTID).append(IN).append("(")
							.append(String.join(",", deptIDs.stream().map(String::valueOf).collect(Collectors.toList())))
							.append(")");
				}
			}
			case GROUP -> {
				if (metaObj.hasCol(PROJECTID)) {
					// 处理GROUP权限，构建条件
					authCond.append(String.format(PROJECTMEMBERSQL,user.getUserID()));
				}
			}

		}


		// 如果构建了授权条件，则将其加入过滤条件
		if (authCond.length() > 0) {
			if (BaseUtil.hasText(filterCond)) {
				filterCond += AND_WITH_SPACE + " ( " + authCond.toString() + " ) ";
			} else {
				filterCond = authCond.toString();
			}
		}
		// 更新 queryParams 中的过滤条件
		queryParams.put("filter", filterCond);
	}
	//endregion
	protected Flux<T> getAllInternal(CachePolicy p,UserAccount user,ServerHttpRequest request, ServerHttpResponse response){
		Map<String, String> queryParams = request.getQueryParams().toSingleValueMap();
		//用户权限过滤
		addQueryParamsBasedOnAuthority(user,service.getMetaObject(),queryParams);

		beforeQuery(user,queryParams, getLocale(request));

		//搜索
		String searchWord = popStringParam("searchWord",queryParams, null);
		if(BaseUtil.hasText(searchWord)){
			Paginator paginator = popPagerParam(queryParams);
			String condition = getCondParam(queryParams);
			Supplier<PagedList<T>> pagedListSupplier = BaseUtil.hasText(condition)
					? () -> service.searchAll(paginator,searchWord,p,condition)
					: () -> service.searchAll(paginator,searchWord,p);
			return createFluxPage(pagedListSupplier, response);
		}
		//列表
		else{
			boolean hasPageSize = queryParams.containsKey("pageSize");
			//分页
			if(hasPageSize){
				Paginator paginator = popPagerParam(queryParams);
				String condition = getCondParam(queryParams);
				Supplier<PagedList<T>> pagedListSupplier = BaseUtil.hasText(condition)
						? () -> service.getAllBy(paginator,condition,p)
						: () -> service.getAll(paginator,p);
				return createFluxPage(pagedListSupplier, response);
			}
			//不分页
			else{
				String condition = getCondParam(queryParams);
				Supplier<Collection<T>> listSupplier = BaseUtil.hasText(condition)
						? ()-> service.getAllBy(condition,p)
						: ()-> service.getAll(p);
				return createFlux(listSupplier);
			}
		}
	}

	@Override
	public Flux<T> getAll(ServerHttpRequest request, ServerHttpResponse response) {
		return getNoCachePolicy(request)
				.flatMapMany(p ->
						getCurrUser()
								.flatMapMany(user -> getAllInternal(p, user, request, response)))
				.subscribeOn(Schedulers.boundedElastic())
				.map(t -> service.assembleSingle(t));
	}

	@Override
	public Mono<Void> exportAll(ServerHttpRequest request, ServerHttpResponse response, @RequestBody(required = false) Collection<String> colNames) {
		return getNoCachePolicy(request)
				.flatMap(p -> {
							Map<String, String> queryParams = request.getQueryParams().toSingleValueMap();
					        Long  templateId =Long.parseLong(popStringParam("tpID", queryParams, "0"));
					        String  format =popStringParam("format", queryParams, "xlsx");
					        boolean print=getBooleanQueryParam(request, "print");
							return getCurrUser()
									.map(user -> {

										beforeQuery(user, queryParams, getLocale(request));
										addQueryParamsBasedOnAuthority(user,service.getMetaObject(), queryParams);

										queryParams.remove("pageSize");
										queryParams.remove("pageNo");
										Sort sort = popSortParam(queryParams);
										Supplier<List<T>> listSupplier;
										//搜索
										String searchWord = popStringParam("searchWord", queryParams, null);
										if (BaseUtil.hasText(searchWord)) {
											String condition = getCondParam(queryParams);
											listSupplier = BaseUtil.hasText(condition)
													? () -> service.searchAll(searchWord, sort, p, condition)
													: () -> service.searchAll(searchWord, sort, p);
										}
										//列表
										else {
											String condition = getCondParam(queryParams);
											listSupplier = BaseUtil.hasText(condition)
													? () -> service.getAllBy(condition, p)
													: () -> service.getAll(p);
										}
										//结果组装
										List<T> tList = listSupplier.get().stream()
												.map(t -> service.assemble(t))
												.collect(Collectors.toList());
										return tList;
									})
									.map(tList -> service.exportExcel(tList,templateId, colNames,format,print,p.getTenantID()))
									.subscribeOn(Schedulers.boundedElastic());
						}).flatMap(file->downloadFile(file,response));

	}


	public Mono<BackgroundTask> exportAllByTask(ServerHttpRequest request, ServerHttpResponse response,
                                                @RequestBody(required = false) EntityExport export) {
		return getNoCachePolicy(request)
				.flatMap(p -> {
					EntityExport entityExport=export==null?new EntityExport():export;
					Map<String, String> queryParams = request.getQueryParams().toSingleValueMap();
					Long  templateId =Long.parseLong(popStringParam("tpID", queryParams, "0"));
					String  format =popStringParam("format", queryParams, "xlsx");
					boolean print=getBooleanQueryParam(request, "print");
					return getCurrUser()
							.map(user -> {
								beforeQuery(user, queryParams, getLocale(request));
								addQueryParamsBasedOnAuthority(user,service.getMetaObject(), queryParams);

								queryParams.remove("pageSize");
								queryParams.remove("pageNo");
								Sort sort = popSortParam(queryParams);
								//搜索
								String searchWord = popStringParam("searchWord", queryParams, null);
								String condition = getCondParam(queryParams);

								entityExport.setSearchWord(searchWord);
								entityExport.setCondition(condition);
								entityExport.setObjName(service.getMetaObject().getObjName());
								entityExport.setPrint( print);
								entityExport.setTemplateId(templateId);
								entityExport.setFormat( format);

//								EntityExport entityExport = new EntityExport(service.getMetaObject().getObjName()
//										, condition,searchWord, colNames,print,templateId,format);
								return service.addBackgroundTask(user, entityExport,p);
//								return new BackgroundTask();

							})
							.subscribeOn(Schedulers.boundedElastic());
				});

	}

	@Override
	public Mono<Void> exportOne(ServerHttpRequest request, ServerHttpResponse response, K k) {
		return get(request,k)
				.zipWhen(
						t -> getCachePolicy(request, t),
						(t,p)-> service.exportExcel(new ArrayList<>() {{add(t);}},
								Long.parseLong(popStringParam("tpID", request.getQueryParams().toSingleValueMap(), "0")),null,
								popStringParam("format", request.getQueryParams().toSingleValueMap(), "xlsx"),
		                        getBooleanQueryParam(request, "print")
								,p.getTenantID())
				).flatMap(file->downloadFile(file,response));
	}

	@Autowired(required = false)
	private FileClient fileClient;

	@Override
	public Mono<ResponseResult> importAll(ServerHttpRequest request,
										  @RequestPart("files") Flux<FilePart> fileParts) {
		return getCurrUser()
						.flatMapMany(user -> {
							return fileParts
									.flatMap(filePart -> {
										try {
											Path tempFile = Files.createTempFile("temp", filePart.filename());
											return filePart.transferTo(tempFile)
													.thenReturn(Tuples.of(tempFile.toFile(), filePart.filename()));

										} catch (IOException e) {
											throw new OperationFailedException("file.context.error");
										}
									})
									.flatMap(t -> {
										Map<String, String> queryParams = request.getQueryParams().toSingleValueMap();
										Long  templateId =Long.parseLong(popStringParam("tpID", queryParams, "0"));
										var result = service.importExcelFile(t.getT1(),templateId,
												getCheckExistsParam(request),
												getBooleanQueryParam(request, "ignoreError") ,
												getBooleanQueryParam(request, "enableSave"),
												service.getDefaultCachePolicy(Tenancy.parseTenantID(user.getUserID())), user, queryParams);
										result.setFileName(t.getT2());
										if (!CollectionUtils.isEmpty(result.getErrors())){
											File failedFile=service.exportFailedExcel(t.getT1(),templateId,result.getErrors());
											return fileClient.uploadFile("file-errors-"+service.getMetaObject().getObjName(), user.getUserID(), t.getT2(), failedFile)
													.map(url->{
														result.setFailedUrl( url);
														return result;
													});
										}
										return Mono.just(result);
									})
									.subscribeOn(Schedulers.boundedElastic());
						}).collectList()
						.map(results -> {
//							LocalizedMessage msg = i18n.getLocalizedMessage("import.success");
							return ResponseResult.ok("导入完成", results);
						});

	}

	//单个导入
	@Override
	public Mono<T> importOne(ServerHttpRequest request, @RequestPart("file") Mono<FilePart> filePart) {
		return getCurrUser()
				.flatMap(user -> {
					Map<String, String> queryParams=request.getQueryParams().toSingleValueMap();
					Long  templateId =Long.parseLong(popStringParam("tpID",  queryParams, "0"));
					boolean enableSave=getBooleanQueryParam(request, "enableSave");
					return filePart
							.flatMap(part -> {
								try {
									Path tempFile = Files.createTempFile("temp", part.filename());
									return part.transferTo(tempFile)
											.thenReturn(tempFile.toFile());
								} catch (IOException e) {
									throw new OperationFailedException("file.context.error");
								}
							}).map(file -> service.importOne(file,templateId,enableSave,user,service.getDefaultCachePolicy(Tenancy.parseTenantID(user.getUserID())),queryParams));
				});
	}


	protected Mono<Integer> execute(ServerHttpRequest request, K k, BiFunction<T,CachePolicy,Integer> action){
		return get(request,k)
				.zipWhen(
						t -> getCachePolicy(request,t),
						(t,p) -> action.apply(t,p)
				);
	}

	protected Mono<Void> redirectTo(ServerHttpResponse response, String url){
		return createGenericMono(()->{
			response.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
			response.getHeaders().add("Location", url);
			return true;
		}).then();
	}

	protected Mono<Boolean> doAction(ServerHttpRequest request, UserAccount user,
								  final String actionName, K k, Mono<DomainActionParam> paramMono){
		return paramMono
				.map(param -> validateDomainActionParam(param, user.getUserID(), actionName))
				.switchIfEmpty(Mono.just(DomainActionParam.of(actionName)))
				.zipWith(get(request, k))
				.map(t -> service.doAction(t.getT2(), user, t.getT1()))
				.thenReturn(true);
	}

	protected Mono<DomainActionParam> prepareAction(ServerHttpRequest request, UserAccount user,
									 final String actionName, K k){
		return get(request, k)
				.map(t -> service.prepareAction(t,actionName, user));
	}

	//endregion



	//region metadata

	@Override
	public Mono<Map<String,Object>> getFilterAndSorts(ServerWebExchange exchange, Locale locale){
		var request = exchange.getRequest();
		return getCachePolicy(request,CachePolicy.READ_THRU,CachePolicy.DEF_EXPIRED_1_DAY)
				.map(p->{
					String lang = WebLocaleResolver.getSupportLangFromLocale(locale);
					Map<String,Object> result = new HashMap<String,Object>();
					boolean reload = getBooleanQueryParam(request,"reload")== Boolean.TRUE;
					result.put("filters", service.getEntityFilters(lang,p, reload));
					result.put("sorts", service.getEntitySorts(lang,p, reload));
					return result;
				}).subscribeOn(Schedulers.boundedElastic());
	}

	@Override
	public Mono<ResponseEntity<MetaUi>> getMetaUi(ServerWebExchange exchange, Locale locale){
		var request = exchange.getRequest();
		return getCachePolicy(request,CachePolicy.READ_THRU,CachePolicy.DEF_EXPIRED_1_DAY)
				.map(p->{
					Boolean reload = getBooleanQueryParam(request,"reload");
					MetaUi metaUi = service.getMetaUi(WebLocaleResolver.getSupportLangFromLocale(locale),p,reload);
					var lastModified = metaUi.getLastModified().toInstant();
					if(!reload && exchange.checkNotModified(lastModified))
						return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
					return ResponseEntity.ok()
							.cacheControl(CacheControl.maxAge(getMetadataCacheDuration()))
							.lastModified(lastModified)
							.body(metaUi);
				});
	}

	@Override
	public Mono<ResponseEntity<Map<String,Object>>> getMetaUiPack(ServerWebExchange exchange, Locale locale){
		var request = exchange.getRequest();
		return getCachePolicy(request,CachePolicy.READ_THRU,Duration.ZERO)
				.map(p->{
					String lang = WebLocaleResolver.getSupportLangFromLocale(locale);
					boolean reload = getBooleanQueryParam(request, "reload") == Boolean.TRUE;
					Map<String,Object> result = new HashMap<String,Object>();
					result.put("metaUi", service.getMetaUi(lang,p, reload));
					result.put("filters", service.getEntityFilters(lang,p, reload));
					result.put("sorts", service.getEntitySorts(lang,p, reload));
					return ResponseEntity.ok(result);
				}).subscribeOn(Schedulers.boundedElastic());
	}
	/*@Override
	public Mono<ResponseEntity<?>> getMetaUiPack(ServerWebExchange exchange, Locale locale){
		var request = exchange.getRequest();
		return getCachePolicy(request,CachePolicy.READ_THRU,CachePolicy.DEF_EXPIRED_1_DAY)
				.map(p->{
					String lang = WebLocaleResolver.getSupportLangFromLocale(locale);
					boolean reload = getBooleanQueryParam(request,"reload")== Boolean.TRUE;
					MetaUi metaUi = service.getMetaUi(lang,p, reload);

					var lastModified = metaUi.getLastModified().toInstant();
					if(!reload && exchange.checkNotModified(lastModified))
						return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();

					Map<String,Object> result = new HashMap<String,Object>();
					result.put("metaUi", metaUi);
					result.put("filters", service.getEntityFilters(lang,p, reload));
					result.put("sorts", service.getEntitySorts(lang,p, reload));
					return ResponseEntity.ok()
							.cacheControl(CacheControl.maxAge(getMetadataCacheDuration()))
							.lastModified(lastModified)
							.body(result);
				}).subscribeOn(Schedulers.boundedElastic());
	}*/

	//endregion of metadata

	//region 上传附件
	@Autowired(required = false)
	private AttachmentService attachmentService;


	/**
	 * 上传附件
	 *
	 * @param objID   标识ID
	 * @param attachmentMono
	 * @param user
	 * @return
	 */
	@Override
	public Mono<Boolean> uploadAttachment(@PathVariable("objID") long objID,
										  @RequestBody Mono<Attachment> attachmentMono,
										  @AuthenticationPrincipal UserAccount user,
										  ServerHttpRequest request
	) {
		return attachmentMono.zipWhen(
				t->getDefaultCachePolicy(request),
				(t,p)->attachmentService.saveAttachment(objID,service.getMetaObject().getObjName(), user,  t, getCheckExistsParam(request),p) > 0
		).subscribeOn(Schedulers.boundedElastic());
	}

	/**
	 * 批量上传附件
	 *
	 * @param objID  标识ID
	 * @param attachmentFlux
	 * @param user
	 * @param request
	 * @return
	 */
	@Override
	public Mono<Integer> uploadAttachments(@PathVariable("objID") long objID,
										   @RequestBody Flux<Attachment> attachmentFlux,
										   @AuthenticationPrincipal UserAccount user,
										   ServerHttpRequest request) {
		return getDefaultCachePolicy(request)
				.flatMap(p->{
					return attachmentFlux
							.map(t -> attachmentService.saveAttachment(objID,service.getMetaObject().getObjName(), user,  t, getCheckExistsParam(request), p))
							.reduce(0,Integer::sum)
							.subscribeOn(Schedulers.boundedElastic());
				});
	}
	//endregion

	//region 模板


	/**
	 * 上传模板
	 * @param templateMono
	 * @param user
	 * @param request
	 * @return
	 */
    @Override
	public Mono<ReportTemplate> uploadTemplate(@RequestBody Mono<ReportTemplate> templateMono,
                                               @AuthenticationPrincipal UserAccount user,
                                               ServerHttpRequest request
	) {
		return templateMono.zipWhen(
				t->getDefaultCachePolicy(request),
				(t,p)->{
					t.setObjName(service.getMetaObject().getObjName());
					t.setUploader(user.getUsername());
					t.setUploadTime(Timestamp.valueOf(LocalDateTime.now()));
					service.saveReportTemplate(t, getCheckExistsParam(request), p);
					return t;
				}
		).subscribeOn(Schedulers.boundedElastic());

	}

	@Override
	public Mono<Void> downloadTemplate(@RequestParam long templateID, ServerHttpRequest request, ServerHttpResponse response) {
		return getDefaultCachePolicy(request)
				.map(p->service.exportTemplate(templateID,p))
				.flatMap(file->downloadFile(file,response));
	}

	/**
	 * 批量上传模板
	 * @param templateFlux
	 * @param user
	 * @param request
	 * @return
	 */
	@Override
	public Flux<ReportTemplate> uploadTemplates(@RequestBody Flux<ReportTemplate> templateFlux,
									@AuthenticationPrincipal UserAccount user,
									ServerHttpRequest request
	) {
		return getDefaultCachePolicy(request)
				.flatMapMany(p->{
					return templateFlux
							.map(t -> {
								t.setObjName(service.getMetaObject().getObjName());
								t.setUploader(user.getUsername());
								t.setUploadTime(Timestamp.valueOf(LocalDateTime.now()));
								 service.saveReportTemplate(t, getCheckExistsParam(request), p);
								 return t;
							})
//							.reduce(0,Integer::sum)
							.subscribeOn(Schedulers.boundedElastic());
				});

	}



	@Override
	public Flux<ReportTemplate> getAllTemplate(ServerHttpRequest request, ServerHttpResponse response) {
		return getNoCachePolicy(request)
				.flatMapMany(p -> {
							return Flux.<ReportTemplate>create(emitter -> {
								try {
									//从数据库获取列表
									Collection<ReportTemplate> list = service.getReportTemplates( p);
									//构造流数据
									list.forEach(t -> emitter.next(t));
									emitter.complete();

								} catch (Exception e) {
									emitter.error(e);
								}
							}).subscribeOn(Schedulers.boundedElastic());
						}

				);
	}

	//endregion


	//region 自定义缓存
	@Autowired(required = false)
	private CustomizedCacheService customizedCacheService;
	public Mono<Boolean> saveCustomizedCache(ServerHttpRequest request, @RequestBody @Valid Mono<CustomizedCache> cache) {
		return getCurrUser()
				.flatMap(user ->
						cache.map(c -> {
							 c.setUserID(user.getUserID());
					         return customizedCacheService.saveCustomizedCache(c);
						})
				);
	}
	public Mono<Object> getCustomizedCache(ServerHttpRequest request, @RequestParam String cacheKey) {
		return getCurrUser()
				.map(user ->customizedCacheService.getCacheKey(user.getUserID(), cacheKey));
	}

	//endregion

	//region 变更日志
	public Flux<ChangeLog> getAllChangeLog(ServerHttpRequest request,
                                           @PathVariable("refKey") String refKey) {
		return getNoCachePolicy(request)
				.flatMapMany(p -> {
							return Flux.<ChangeLog>create(emitter -> {
								try {
									//从数据库获取列表
									Collection<ChangeLog> list = service.getChangeLogs(refKey);
									//构造流数据
									list.forEach(t -> emitter.next(t));
									emitter.complete();

								} catch (Exception e) {
									emitter.error(e);
								}
							}).subscribeOn(Schedulers.boundedElastic());
						}

				);
	}
	public Flux<ChangeWrapper> readChangeLog(ServerHttpRequest request,
											 @RequestParam long logID) {
		return getNoCachePolicy(request)
				.flatMapMany(p -> {
							return Flux.<ChangeWrapper>create(emitter -> {
								try {
									//从数据库获取列表
									Collection<ChangeWrapper> list = service.readChangeLog(logID,p);
									//构造流数据
									list.forEach(t -> emitter.next(t));
									emitter.complete();

								} catch (Exception e) {
									emitter.error(e);
								}
							}).subscribeOn(Schedulers.boundedElastic());
						}

				);
	}
	//endregion
}
