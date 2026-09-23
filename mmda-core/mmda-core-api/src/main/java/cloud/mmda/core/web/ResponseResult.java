package cloud.mmda.core.web;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 响应结果
 *
 * 用于微服务间调用时传递数据
 *
 * @author roshion
 * @since 2019
 * @version 3.0.0
 */
public final class ResponseResult implements Serializable {
    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";

    /**
     * 响应状态，与Http状态一致
     */
    @Getter
    private final int status;

    /**
     * 成功或失败消息，用于给用户提示
     */
    @Getter
    private final String message;

    /**
     * 成功返回数据，失败时为null
     */
    @Getter
    private final Object data;

    /**
     * 业务层错误编码，例如access.denied
     */
    @Getter
    private final String code;
    /**
     * 失败导致的原因，用于排查调试
     * 仅debug模式下写给客户端
     */
    @Getter
    private final Throwable cause;

    private ResponseResult(int status, String code, String message, Object data, Throwable cause){
        this.status=status;
        this.code = code;
        this.message=message;
        this.data = data;
        this.cause=cause;
    }

    /**
     * 构造成功响应结果
     * @param status 状态
     * @param message 消息
     * @param data 数据
     */
    private ResponseResult(int status, String message, Object data){
        this(status,SUCCESS,message, data,null);
    }

    /**
     * 构造失败响应结果
     * @param status 状态
     * @param code 错误编码
     * @param message 错误消息
     * @param cause 错误原因
     */
    private ResponseResult(int status, String code, String message, Throwable cause){
        this(status, code, message, null,cause);
    }


    public static final ResponseResult success(int status, String message){
        return new ResponseResult(status, message, null);
    }
    public static final ResponseResult success(int status, String message, Object data){
        return new ResponseResult(status, message, data);
    }


    public static final ResponseResult ok(String message){
        return new ResponseResult(HttpStatus.OK.value(), message, null);
    }
    public static final ResponseResult ok(String message, Object data){
        return new ResponseResult(HttpStatus.OK.value(), message, data);
    }

    public static final ResponseResult failed(int status, String code, String message, Throwable cause){
        return new ResponseResult(status,code,message,cause);
    }
    private ResponseResult(){
        this(HttpStatus.OK.value(),SUCCESS,SUCCESS, null,null);
    }
}
