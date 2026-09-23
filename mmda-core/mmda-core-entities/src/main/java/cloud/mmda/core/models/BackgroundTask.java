/**
 * Copyright (c) 2006, 2024, www.mmda.cloud All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.models;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.entities.TenancyEntity;
import cloud.mmda.core.enums.BackgroundTaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 后台任务
 * 
 * @remarks 后台任务
 * 
 * @author mmda codebot 
 * @version 4.0.0 
 * @since 2025-12-25 11:16:07.0
 * 
 */
public class BackgroundTask extends TenancyEntity<Long> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 任务ID
	 */
	@NotNull
	@Min(0)
	@Getter @Setter
	private long taskID;
	/**
	 * 任务编号
	 */
	@NotBlank
	@Size(min=1,max=36)
	@Getter @Setter
	private String taskNo;
	/**
	 * 任务名称
	 */
	@NotBlank
	@Size(min=1,max=255)
	@Getter @Setter
	private String taskName;
	/**
	 * 任务状态：0;NEW;新任务|1;RUNNING;执行中|2;SUSPENDED;已暂停|8;SUCCEEDED;成功|-4;CANCELED;已取消|-8;FAILED;失败
	 */
	@NotNull
	@Getter @Setter
	private BackgroundTaskStatus status;
	/**
	 * 预计完成时间
	 */
	@Getter @Setter
	private Timestamp expectedFinish;
	/**
	 * 开始时间
	 */
	@Getter @Setter
	private Timestamp startedTime;
	/**
	 * 完成时间
	 */
	@Getter @Setter
	private Timestamp finishedTime;
	/**
	 * 进度
	 */
	@NotNull
	@Getter @Setter
	private BigDecimal taskProgress;
	/**
	 * 任务结果，可以是失败原因或者一个超链接用于查看和下载文件
	 */
	@Size(max=255)
	@Getter @Setter
	private String taskResult;
	/**
	 * 备注
	 */
	@Size(max=255)
	@Getter @Setter
	private String remark;
	/**
	 * 自定义
	 */
	@Size(max=2000)
	@Getter @Setter
	private String customJson;
	/**
	 * 创建部门：REF Department(deptID,deptName)
	 */
	@Getter @Setter
	private Long deptID;
	/**
	 * 创建人：REF User(userID,userName)
	 */
	@Getter @Setter
	private Long creatorID;
	/**
	 * 创建时间
	 */
	@Getter @Setter
	private Timestamp createDate;
	//region partition
	/**
	 * 获取租户分区ID
	 * 实现{@link Tenancy#getPartitionID()}接口
	 */
	@JsonIgnore
	@Override
	public final long getPartitionID(){
		return taskID;
	}
	/**
	 * 设置租户分区ID
	 * 实现{@link TenancyEntity#setPartitionID(long)}接口
	 */
	@Override
	public final void setPartitionID(final long partitionID){
		taskID = partitionID;
	}
	//endregion of partition

	//region key & sn
	/**
	 * 作为主键
	 */
	@Override
	public final Long getId(){
		return taskID;
	}

	@Override
	public Class<Long> getIdClass() {
		return Long.class;
	}

	@Override
	public void setDefaultUniqueKey() {
		if(createDate == null) createDate = Timestamp.valueOf(LocalDateTime.now());
		setTaskNo(generateNo("BT", createDate, taskID));
	}
	//endregion of key & sn

	/**
	 * 元数据包括字段和子表关系名称
	 */
	public static abstract class Meta {
		public static final String _taskID = "taskID";
		public static final String _taskNo = "taskNo";
		public static final String _taskName = "taskName";
		public static final String _status = "status";
		public static final String _expectedFinish = "expectedFinish";
		public static final String _startedTime = "startedTime";
		public static final String _finishedTime = "finishedTime";
		public static final String _taskProgress = "taskProgress";
		public static final String _taskResult = "taskResult";
		public static final String _remark = "remark";
		public static final String _customJson = "customJson";
		public static final String _deptID = "deptID";
		public static final String _creatorID = "creatorID";
		public static final String _createDate = "createDate";
		
	}
	/**
	 * 设置创建人
	 * @param creatorID 创建人标识
	 * @param deptID 部门标识
	 * @return 返回实体对象本身
	 */
	public BackgroundTask createBy(long creatorID, long deptID){
		this.creatorID = creatorID;
		this.deptID = deptID;
		return this;
	}
	//endregion of ~GENERATED PARTS END

}
