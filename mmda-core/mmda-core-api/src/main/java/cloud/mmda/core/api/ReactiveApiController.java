package cloud.mmda.core.api;

import cloud.mmda.core.caching.CachePolicy;
import cloud.mmda.core.entities.*;
import cloud.mmda.core.metadata.MetaUi;
import cloud.mmda.core.models.Attachment;
import cloud.mmda.core.models.BackgroundTask;
import cloud.mmda.core.models.ChangeLog;
import cloud.mmda.core.models.ReportTemplate;
import cloud.mmda.core.security.models.UserAccount;
import cloud.mmda.core.services.CustomizedCache;
import cloud.mmda.core.services.DomainService;
import cloud.mmda.core.services.exceptions.DomainException;
import cloud.mmda.core.web.ResponseResult;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;

/**
 * 响应式API控制器
 * <p>
 *      不采用传统的{@link ResponseResult}，因为响应式接口返回的是数据流。
 *      负责认证与授权，通过注解拦截用户角色，通过后天权限数据管理功能权限。
 * </p>
 * <p>
 *      客户端通过检查HttpStatus状态获知是否异常，通常try...catch也能捕获接口返回的异常信息，这样强制客户端做异常处理。
 *      我们保证异常信息格式统一，在3.0版本业务层异常统一继承{@link DomainException}。
 * </p>
 * <p>
 *      4.0版本因WebFlux升级后统一采用RFC 9457的Problem Details for HTTP APIs规范，废弃了GlobalErrorAttributes。
 *      统一按Spring WebFlux新的 @ExceptionHandler方法返回一个{@link ProblemDetail}或者{@link ErrorResponse}值来渲染错误信息。
 *      或者使用{@link ErrorResponseException}包装 DomainException后抛出。
 * </p>
 * <p>
 *
 * </p>
 * @author roshion.luo
 * @version 4.0.0
 * @since 2021.5.23
 *
 * @param <T> 实体类型
 * @param <K> 实体主键类型
 */
public interface ReactiveApiController<T,K> {
    /**
     * 创建一个新的实体原型，供客户端编辑。会设置默认值，生成新的主键和时间戳等。
     * 子类可增加接口参数，从request中获取原型数据proto，
     * 默认会调用{@link cloud.mmda.services.DomainService#create(CachePolicy)}
     * 映射标准：POST /create
     *
     * @param request
     * @param user 用户，由子类注入
     * @param createParam 创建参数，由子类注入
     * @return 返回创建的实体原型
     */
    /**
     * 创建一个新的实体原型，供客户端编辑。会设置默认值，生成新的主键和时间戳等。
     * 子类注入接口参数protoMono，从request中获取原型数据proto，
     * 默认会调用{@link DomainService#create(CachePolicy)}
     * 映射标准：POST /create
     *
     * @param request
     * @param user 创建人用户，由子类注入
     * @param prototypeMono 创建参数，由子类注入
     * @return
     */
    @PostMapping("/create")
    Mono<T> create(ServerHttpRequest request, UserAccount user, Mono<Optional<RefParam>> prototypeMono);
    default Mono<T> create(ServerHttpRequest request, UserAccount user){
        return create(request,user,Mono.just(Optional.empty()));
    }

    /**
     * 保存实体对象，包含关联子表
     * @param request
     * @param tMono 单个实体对象
     * @return 返回影响记录数，指示客户端跳转或者显示异常
     */
    @PostMapping(value = "/save")
    Mono<Integer> save(ServerHttpRequest request, Mono<T> tMono);

    /**
     * 保存实体对象并取回，包含关联子表，有时候客户端需要立即处理
     * @param request
     * @param tMono 单个实体对象
     * @return 返回成功保存后的整个实体对象，包含子表
     */
    Mono<T> saveAndLoad(ServerHttpRequest request, Mono<T> tMono);

    /**
     * 保存实体对象并取回，不含子表
     * @param request
     * @param tMono
     * @return 返回成功保存后的单个实体对象
     */
    Mono<T> saveAndGet(ServerHttpRequest request, Mono<T> tMono);
    /**
     * 保存所有实体对象，包含关联子表，例如批量导入
     * @param request
     * @param tFlux 多个实体对象流
     * @return 返回成功记录数
     */
    @PreAuthorize(ReactiveEntityController.HAS_ROLE_STAFF)
    @PostMapping("/saveAll")
    Mono<Integer> saveAll(ServerHttpRequest request, @RequestBody Flux<T> tFlux);

    /**
     * 保存所有实体对象并取回（不含子表）
     * 例如批量导入单表数据
     * @param request
     * @param tFlux 多个实体对象流
     * @return 返回保存后的多个实体对象流
     */
    Flux<T> saveAndGetAll(ServerHttpRequest request, Flux<T> tFlux);
    /**
     * 保存所有实体对象并取回（含子表）
     * 例如批量导入订单及明细
     * @param request
     * @param tFlux 多个实体对象流
     * @return 返回保存后的多个实体对象流
     */
    Flux<T> saveAndLoadAll(ServerHttpRequest request, Flux<T> tFlux);

    /**
     * 更新实体对象本身，不包含关联子表
     * @param request
     * @param tMono 单个实体对象
     * @return 返回影响记录数，指示客户端跳转或者显示异常
     */
    Mono<Integer> update(ServerHttpRequest request, Mono<T> tMono);

    /**
     * 更新实体对象本身并取回（不包含子表）
     * @param request
     * @param tMono 单个实体对象
     * @return 返回更新后的单个实体对象
     */
    Mono<T> updateAndGet(ServerHttpRequest request, Mono<T> tMono);
    /**
     * 更新实体对象并取回（含子表）
     * @param request
     * @param tMono 单个实体对象
     * @return 返回更新后的整个实体对象，包含子表
     */
    Mono<T> updateAndLoad(ServerHttpRequest request, Mono<T> tMono);
    /**
     * 更新多个实体对象本身，不包含关联子表
     * @param request
     * @param tFlux 多个实体对象流
     * @return 返回成功记录数
     */
    Mono<Integer> updateAll(ServerHttpRequest request, Flux<T> tFlux);

    /**
     * 删除
     * @param request
     * @param k 主键
     * @return 返回成功否
     */
    Mono<Integer> delete(ServerHttpRequest request, K k);

    /**
     * 批量删除
     * @param request
     * @param kFlux 多个主键流
     * @return 返回删除记录数
     */
    @PostMapping("/deleteAll")
    Mono<Integer> deleteAll(ServerHttpRequest request, Flux<K> kFlux);

    /**
     * 更新属性
     * @param request
     * @param k 主键
     * @param mapMono 属性集合
     * @return 返回影响记录数
     */
    Mono<Integer> partialUpdate(ServerHttpRequest request, K k, Mono<Map<String,Object>> mapMono);

    /**
     * 批量更新属性
     * @param request
     * @param mapFlux 多个实体属性字典，每个字典中包含实体主键值
     * @return 返回影响记录数
     */
    Mono<Integer> partialUpdateAll(ServerHttpRequest request, Flux<Map<String,Object>> mapFlux);

    /**
     * 读取（含子表）
     * @param request
     * @param k 主键
     * @return 返回单个实体对象，或者{@link cloud.mmda.core.services.exceptions.NotFoundException}
     */
    Mono<T> get(ServerHttpRequest request, K k);

    /**
     * 读取列表。
     * 分页控制URL?pageSize=10&pageNo=1&sort=field [ASC|DESC]
     * 查询条件URL?field1=condition1&field2=condition2，例如
     * <ul>
     *     <li>精确匹配 orderNo=001</li>
     *     <li>在列表 orderNo=IN(001,002)</li>
     *     <li>在区间 orderDate=BETWEEN 2021-05-01 AND 2021-05-24</li>
     *     <li>包含 orderNo=LIKE RN2021</li>
     * </ul>
     * 模糊搜索URL?searchWord=boy
     * @param request
     * @param response
     * @return 返回一页实体对象数据流，分页器数据放入头部（x-pager）
     */
    @GetMapping("")
    Flux<T> getAll(ServerHttpRequest request, ServerHttpResponse response);

    // region 元数据

    /**
     * 元数据缓存时长，用于控制HTTP Cache
     * @return
     */
    default Duration getMetadataCacheDuration(){
        return CachePolicy.DEF_EXPIRED_1_WEEK;
    }
    /**
     * 获取元界面数据。
     * <p>
     *     用于客户端自动构建UI视图。
     * </p>
     * @param exchange
     * @param locale 语言区域，如zh-CN,en-US
     * @return
     */
    @GetMapping("/metaUi")
    Mono<ResponseEntity<MetaUi>> getMetaUi(ServerWebExchange exchange, Locale locale);

    /**
     * 获取元界面数据包，其中包含metaUi,Filters,Sorts
     * @param exchange
     * @param locale
     * @return
     */
    @GetMapping("/metaUiPack")
    Mono<ResponseEntity<Map<String,Object>>> getMetaUiPack(ServerWebExchange exchange, Locale locale);
    //Mono<ResponseEntity<?>> getMetaUiPack(ServerWebExchange exchange, Locale locale);

    /**
     * 获取元界面过滤器和排序设置
     * @param exchange
     * @param locale
     * @return
     */
    @GetMapping("/metaFilterAndSorts")
    Mono<Map<String,Object>> getFilterAndSorts(ServerWebExchange exchange, Locale locale);

    // endregion of 元数据

    //region 导出
    /**
     * 导出成EXCEL
     * @param request
     * @param colNames
     * @return
     */
    @PreAuthorize(ReactiveEntityController.HAS_ROLE_STAFF)
    @PostMapping("/exportAll")
    Mono<Void> exportAll(ServerHttpRequest request, ServerHttpResponse response, @RequestBody(required = false) Collection<String> colNames);

    @PreAuthorize(ReactiveEntityController.HAS_ROLE_STAFF)
    @PostMapping("/exportOne")
    Mono<Void> exportOne(ServerHttpRequest request,ServerHttpResponse response, K k);

    @PreAuthorize(ReactiveEntityController.HAS_ROLE_STAFF)
    @PostMapping("/exportAllByTask")
    Mono<BackgroundTask> exportAllByTask(ServerHttpRequest request, ServerHttpResponse response,
                                         @RequestBody(required = false) EntityExport export) ;
    //endregion of 导出

    //region 导入
    /**
     * 批量导入
     * @param request
     * @param fileParts
     * @return
     */
    @PostMapping("/importAll")
    Mono<ResponseResult> importAll(ServerHttpRequest request,
                                   @RequestPart("files") Flux<FilePart> fileParts);

    /**
     * 单个导入
     * @param request
     * @param filePart
     * @return
     */
    @PostMapping("/importOne")
    Mono<T> importOne(ServerHttpRequest request,
                                   @RequestPart("file") Mono<FilePart> filePart);
    //endregion of 导入

    //region 异常处理

    //endregion of 异常处理

    //region 模板

    /**
     * 获取报表模板列表
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/getAllTemplate")
    Flux<ReportTemplate> getAllTemplate(ServerHttpRequest request, ServerHttpResponse response);


    /**
     * 批量上传模板
     * @param templateFlux
     * @param user
     * @param request
     * @return
     */
    @PreAuthorize(ReactiveEntityController.HAS_ROLE_STAFF)
    @PostMapping("/uploadTemplates")
     Flux<ReportTemplate> uploadTemplates(@RequestBody Flux<ReportTemplate> templateFlux,
                                                @AuthenticationPrincipal UserAccount user,
                                                ServerHttpRequest request);
    /**
     * 上传模板
     * @param templateMono
     * @param user
     * @param request
     * @return
     */
    @PreAuthorize(ReactiveEntityController.HAS_ROLE_STAFF)
    @PostMapping("/uploadTemplate")
    Mono<ReportTemplate> uploadTemplate(@RequestBody Mono<ReportTemplate> templateMono,
                                               @AuthenticationPrincipal UserAccount user,
                                               ServerHttpRequest request
    );
    //endregion of 模板

    //region 附件
    @PreAuthorize(ReactiveEntityController.HAS_ROLE_STAFF)
    @PostMapping("/{objID}/uploadAttachments")
    Mono<Integer> uploadAttachments(@PathVariable("objID") long objID,
                                           @RequestBody Flux<Attachment> attachmentFlux,
                                           @AuthenticationPrincipal UserAccount user,
                                           ServerHttpRequest request) ;

    /**
     * 上传附件
     *
     * @param objID   标识ID
     * @param attachmentMono
     * @param user
     * @return
     */
    @PreAuthorize(ReactiveEntityController.HAS_ROLE_STAFF)
    @PostMapping("/{objID}/uploadAttachment")
    Mono<Boolean> uploadAttachment(@PathVariable("objID") long objID,
                                          @RequestBody Mono<Attachment> attachmentMono,
                                          @AuthenticationPrincipal UserAccount user,
                                          ServerHttpRequest request);

    //endregion
    @PostMapping("/downloadTemplate")
    Mono<Void> downloadTemplate(@RequestParam long templateID, ServerHttpRequest request, ServerHttpResponse response);

    @PreAuthorize(ReactiveEntityController.HAS_ROLE_STAFF)
    @PostMapping("/saveCache")
    Mono<Boolean> saveCustomizedCache(ServerHttpRequest request, @RequestBody @Valid Mono<CustomizedCache> cache);

    @PreAuthorize(ReactiveEntityController.HAS_ROLE_STAFF)
    @PostMapping("/getCache")
    Mono<Object> getCustomizedCache(ServerHttpRequest request, @RequestParam String cacheKey);

    @PreAuthorize(ReactiveEntityController.HAS_ROLE_STAFF)
    @GetMapping("/{refKey}/getAllChangeLog")
    public Flux<ChangeLog> getAllChangeLog(ServerHttpRequest request, @PathVariable("refKey") String refKey);

    @PreAuthorize(ReactiveEntityController.HAS_ROLE_STAFF)
    @GetMapping("/readChangeLog")
    public Flux<ChangeWrapper> readChangeLog(ServerHttpRequest request, @RequestParam long logID);

}
