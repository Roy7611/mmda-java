package cloud.mmda.core.entities;



import lombok.Getter;

import java.io.Serializable;

/**
 * 属性校验错误
 *
 * 用于服务器端业务校验某个字段后的响应结果
 *
 * @author roshion
 * @since 2019
 * @version 3.0.0
 *
 */
public class ValidationError implements Serializable {
	/**
	 * 校验字段
	 */
	@Getter
	private final String field;

	/**
	 * 错误，当passed==false有值
	 */
	@Getter
	private final String error;


	private ValidationError(String field, String error){
		this.field=field;
		this.error=error;
	}


	/**
	 * 失败
	 * @param field
	 * @param error
	 * @return
	 */
	public static ValidationError valueOf(String field, String error){
		return new ValidationError(field,error);
	}
}
