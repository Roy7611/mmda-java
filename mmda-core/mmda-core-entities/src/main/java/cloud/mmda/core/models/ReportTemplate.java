/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.models;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.entities.TenancyEntity;
import jakarta.validation.constraints.*;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.*;

/**
 * 报表模板
 * 
 * @remarks 报表模板
 * 
 * @author mmda codebot 
 * @version 4.0.0 
 * @since 2024-08-26 23:51:57.0
 * 
 */
public class ReportTemplate extends TenancyEntity<Long> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 模板标识
	 */
	@NotNull
	@Min(0)
	@Getter @Setter
	private long templateID;
	/**
	 * 对象名称
	 */
	@NotBlank
	@Size(min=1,max=50)
	@Getter @Setter
	private String objName;
	/**
	 * 模板名称
	 */
	@NotBlank
	@Size(min=1,max=255)
	@Getter @Setter
	private String templateName;
	/**
	 * 模板文件，Excel报表模板
	 */
	@NotBlank
	@Size(min=1,max=255)
	@Getter @Setter
	private String templateFile;
	/**
	 * 上传人
	 */
	@Size(max=50)
	@Getter @Setter
	private String uploader;
	/**
	 * 上传时间
	 */
	@Getter @Setter
	private Timestamp uploadTime;
	//region partition
	/**
	 * 获取租户分区ID
	 * 实现{@link Tenancy#getPartitionID()}接口
	 */
	@JsonIgnore
	@Override
	public final long getPartitionID(){
		return templateID;
	}
	/**
	 * 设置租户分区ID
	 * 实现{@link TenancyEntity#setPartitionID(long)}接口
	 */
	@Override
	public final void setPartitionID(final long partitionID){
		templateID = partitionID;
	}
	//endregion of partition

	/**
	 * 作为主键
	 */
	public final Long getId(){
		return templateID;
	}

	@Override
	public Class<Long> getIdClass() {
		return Long.class;
	}

	/**
	 * 元数据包括字段和子表关系名称
	 */
	public static abstract class Meta {
		public static final String _templateID = "templateID";
		public static final String _objName = "objName";
		public static final String _templateName = "templateName";
		public static final String _templateFile = "templateFile";
		public static final String _uploader = "uploader";
		public static final String _uploadTime = "uploadTime";
	}
	//endregion of ~GENERATED PARTS END

}
