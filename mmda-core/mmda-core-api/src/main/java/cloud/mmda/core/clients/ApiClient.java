package cloud.mmda.core.clients;


import cloud.mmda.core.web.ResponseResult;
import lombok.Getter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Api调用客户端
 *
 * 用于微服务之间相互调用，采用Restful风格，Basic认证。
 * 各个微服务自行继承此类或者具备认证功能的{@link OAuthClient}实现自定义的客户端。
 *
 * @author roshion
 * @since 2020.7.2
 * @version 3.0.0
 *
 */
public class ApiClient {
    //微服务之间Basic认证用户名密码
    protected static final String BASIC_CLIENT_ID = "mmda-client";
    protected static final String BASIC_CLIENT_SECRET = "888888";

    private static final Log logger = LogFactory.getLog(ApiClient.class);

    private ReactorLoadBalancerExchangeFilterFunction lbFunction;

    @Getter
    private boolean useSSL;//暂时没用

    /**
     * 远端服务名称，如eshop-services
     */
    @Getter
    private String serviceName;

    /**
     * 调用客户端名称，如society-services
     * 用于获得合法授权，例如eshop授权society可以访问
     */
    @Getter
    protected String clientName;

    /**
     * 响应式调用Web客户端
     */
    @Getter
    protected WebClient webClient;

    public ApiClient(String clientName, String serviceName, ReactorLoadBalancerExchangeFilterFunction  lbFunction, boolean useSSL){
        this.clientName = clientName;
        this.serviceName = serviceName;
        this.lbFunction = lbFunction;
        this.useSSL = useSSL;
        this.webClient = buildWebClient(getBaseUrl(serviceName));
    }
    public ApiClient(String clientName, String serviceName, ReactorLoadBalancerExchangeFilterFunction  lbFunction){
        this(clientName, serviceName,lbFunction,false);
    }
    public ApiClient(String clientName, String serviceName){
        this(clientName, serviceName,null,false);
    }

    protected String getBaseUrl(String serviceName){
        return (useSSL ? "https://" : "http://") + serviceName;
    }
    protected WebClient buildWebClient(String baseUrl){
        if(lbFunction!=null){
            return WebClient.builder()
                    .baseUrl(baseUrl)
//                    .clientConnector(httpConnector)
                    .filter(ExchangeFilterFunctions.basicAuthentication(BASIC_CLIENT_ID, BASIC_CLIENT_SECRET))
                    .filter(lbFunction)
                    .build();
        }
        return WebClient.builder()
                .baseUrl(baseUrl)
//                .clientConnector(httpConnector)
                .filter(ExchangeFilterFunctions.basicAuthentication(BASIC_CLIENT_ID, BASIC_CLIENT_SECRET))
                .build();

    }

    protected void logError(String action, Throwable error, String uri, Object...uriVariables){
        String msg = String.format("%1$s API调用 %2$s/%3$s 异常，参数 %4$s。", action, serviceName, uri, uriVariables);
        logger.error(msg, error);
    }

    protected void logTrace(String msg){
        logger.trace(msg);
    }

    /**
     * 获取单个实体
     * @param requiredType 实体类型
     * @param uri 相对地址，如/accounts/{acctId}
     * @param uriVariables 变量，如acctId
     * @param <T>
     * @return 实体T
     */
    public <T> Mono<T> get(Class<T> requiredType, String uri, Object...uriVariables){
        return webClient
                .get()
                .uri(uri,uriVariables)
                .retrieve()
                .bodyToMono(requiredType)
                .doOnError(error -> logError("get",error,uri,uriVariables));
    }

    /**
     * 获取实体列表
     * @param requiredType 实体类型
     * @param uri 相对地址，如/accounts?status={status}
     * @param uriVariables 变量，如1
     * @param <T>
     * @return 实体列表Flux<T>
     */
    public <T> Flux<T> getAll(Class<T> requiredType, String uri, Object...uriVariables){
        return webClient
                .get()
                .uri(uri,uriVariables)
                .retrieve()
                .bodyToFlux(requiredType)
                .doOnError(error -> logError("getAll",error,uri,uriVariables));
    }

    public <T, R> Mono<R> post(T data, Class<R> requiredType, String uri, Object...uriVariables){
        return webClient
                .post()
                .uri(uri,uriVariables)
                .syncBody(data)
                .retrieve()
                .bodyToMono(requiredType)
                .doOnError(error -> logError("post",error,uri,uriVariables));
    }

    public <T, R> Mono<R> put(T data, Class<R> requiredType, String uri, Object...uriVariables){
        return webClient
                .put()
                .uri(uri,uriVariables)
                .syncBody(data)
                .retrieve()
                .bodyToMono(requiredType)
                .doOnError(error -> logError("put",error,uri,uriVariables));
    }

    public Mono<Boolean> delete(String uri, Object...uriVariables){
        return webClient
                .delete()
                .uri(uri,uriVariables)
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(error -> logError("delete",error,uri,uriVariables));
    }
    /**
     * 校验数据合法性
     *
     * 例如校验手机号是否可用，密码是否正确等
     * @param uri 相对地址，如/accounts/validate/{acctId}/txpwd/{txPwd}?token={token}
     * @param uriVariables 参数，如：acctId,txPwd, getAccessToken()
     * @return 校验结果
     */
    public Mono<ResponseResult> validate(String uri, Object...uriVariables){
        return webClient
                .get()
                .uri(uri,uriVariables)
                .retrieve()
                .bodyToMono(ResponseResult.class)
                .doOnError(error -> logError("validate",error,uri,uriVariables));
    }


    //region 返回ResponseResult版本

    public Mono<ResponseResult> get(String uri, Object...uriVariables){
        return get(ResponseResult.class,uri,uriVariables);
    }

    public <T> Mono<ResponseResult> post(T data, String uri, Object...uriVariables){
        return post(data,ResponseResult.class,uri,uriVariables);
    }

    public <T> Mono<ResponseResult> put(T data, String uri, Object...uriVariables){
        return put(data,ResponseResult.class,uri,uriVariables);
    }
    //endregion
}
