/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.metadata;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 元数据库
 * <p>
 * dbSchema+dbSchema 数据库名和模式名在不同数据库厂商实现方式不太一样，
 * 但总的来说，都可统一为schema.Table的访问方式。
 * <p>
 * SQL Server: crm.dbo.Partner 我们把crm.dbo理解为 schema <br>
 * MySQL	 : crm.Partner，crm是数据库 <br>
 * Oracle	 : crm.Partner，crm是用户，不支持跨数据库查询 <br>
 * PostgreSQL: crm.Partner，crm是schema，不支持跨数据库查询 <br>
 * Kingbase	 : 支持 Postgre 和 Oracle 两种模式 <br>
 * DM		 : 跟 Oracle 一样 <br>
 *
 * @remarks 元数据库定义了一个独立数据库模式下所有默认配置，例如字符集
 * 
 * @author mmda codebot 
 * @version 3.0.0 
 * @since 2024-07-21 00:28:29.0
 * 
 */
public class MetaDb extends MetaEntity {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 数据库名称
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String dbName;
	/**
	 * 数据库模式
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String dbSchema;

	/**
	 * 数据库主人
	 */
	@Getter @Setter
	private String dbOwner;
	/**
	 * 命名空间
	 */
	@Size(max=255)
	@Getter @Setter
	private String namespace;
	/**
	 * 系统编码
	 */
	@Size(max=1)
	@Getter @Setter
	private String systemCode;
	/**
	 * 中文名称
	 */
	@Size(max=30)
	@Getter @Setter
	private String displayLabel;

	/**
	 * 默认字符集合
	 */
	@Getter @Setter
	private String defaultCharset;
	/**
	 * 默认校验规则
	 */
	@Getter @Setter
	private String defaultCollation;

	/**
	 * 表空间
	 */
	@Getter @Setter
	private String tablespace;
	/**
	 * 描述
	 */
	@Size(max=255)
	@Getter @Setter
	private String description;
	/**
	 * 作为主键
	 */
	public final String asKey(){
		return dbSchema;
	}
	//endregion of ~GENERATED PARTS END

}
