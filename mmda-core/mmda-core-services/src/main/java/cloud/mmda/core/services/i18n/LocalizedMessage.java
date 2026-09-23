package cloud.mmda.core.services.i18n;

/**
 * 本地化消息
 */
public class LocalizedMessage {
    private String code;
    private Object[] args;
    private String message;

    public LocalizedMessage(String message, String code, Object[] args){
        this.args=args;
        this.code=code;
        this.message=message;
    }

    /**
     * 消息编码
     * @return
     */
    public String getCode(){
        return this.code;
    }

    /**
     * 消息参数
     * @return
     */
    public Object[] getArgs(){
        return this.args;
    }

    /**
     * 本地化消息文本
     * @return
     */
    public String getMessage(){
        return this.message;
    }
}
