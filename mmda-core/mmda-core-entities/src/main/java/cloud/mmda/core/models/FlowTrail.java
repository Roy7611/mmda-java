/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 *
 */
package cloud.mmda.core.models;
import cloud.mmda.core.Tenancy;
import cloud.mmda.core.entities.Computable;
import cloud.mmda.core.entities.TenancyEntity;
import jakarta.validation.constraints.*;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.*;

import cloud.mmda.core.enums.Importance;
import cloud.mmda.core.enums.Urgency;
import cloud.mmda.core.enums.FlowTokenStatus;
/**
 * 流程追踪
 * 
 * @remarks 流程追踪
 * 
 * @author mmda codebot 
 * @version 4.0.0 
 * @since 2024-08-11 22:39:57.0
 * 
 */
public class FlowTrail extends TenancyEntity<Long> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 审计标识
	 */
	@NotNull
	@Min(0)
	@Getter @Setter
	private long trailID;
	/**
	 * 对象名称
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String objName;
	/**
	 * 对象标识
	 */
	@NotNull
	@Getter @Setter
	private long objID;
	/**
	 * 行动时间
	 */
	@NotNull
	@Getter @Setter
	private Timestamp actTime;
	/**
	 * 行动人：REF User(userID,userName)
	 */
	@NotNull
	@Getter @Setter
	private long actorID;
	/**
	 * 操作状态转移，例如close(SUBMITTED=>CLOSED)
	 */
	@Size(max=255)
	@Getter @Setter
	private String asTransition;
	/**
	 * 修改日志标识，引用ChangeLog.logID
	 */
	@Getter @Setter
	private Long changeLogID;
	/**
	 * 重要性：0;UNKNOWN;-|1;IMPORTANT;重要|2;VERY_IMPORTANT;非常重要
	 */
	@NotNull
	@Getter @Setter
	private Importance importance;
	/**
	 * 紧急性：0;NORMAL;普通|1;SENIOR;优先|2;URGENT;紧急
	 */
	@NotNull
	@Getter @Setter
	private Urgency urgency;
	/**
	 * 通知
	 */
	@NotBlank
	@Size(min=1,max=255)
	@Getter @Setter
	private String notification;
	/**
	 * 主办人：REF User(userID,userName)
	 */
	@Getter @Setter
	private Long ownerID;
	/**
	 * 办理状态：0;NEW;未办理|1;DONE;已办理|-1;CANCELLED;已取消|-2;TERMINATED;已终止
	 */
	@NotNull
	@Getter @Setter
	private FlowTokenStatus status;
	/**
	 * 办理时间
	 */
	@Getter @Setter
	private Timestamp consumedTime;
	/**
	 * 花费时间(min)
	 */
	@Getter @Setter
	private Integer costTime;
	//region partition
	/**
	 * 获取租户分区ID
	 * 实现{@link Tenancy#getPartitionID()}接口
	 */
	@JsonIgnore
	@Override
	public final long getPartitionID(){
		return trailID;
	}
	/**
	 * 设置租户分区ID
	 * 实现{@link TenancyEntity#setPartitionID(long)}接口
	 */
	@Override
	public final void setPartitionID(final long partitionID){
		trailID = partitionID;
	}
	//endregion of partition

	/**
	 * 作为主键
	 */
	public final Long getId(){
		return trailID;
	}

	@Override
	public Class<Long> getIdClass() {
		return Long.class;
	}

	/**
	 * 元数据包括字段和子表关系名称
	 */
	public static abstract class Meta {
		public static final String _trailID = "trailID";
		public static final String _objName = "objName";
		public static final String _objID = "objID";
		public static final String _actTime = "actTime";
		public static final String _actorID = "actorID";
		public static final String _asTransition = "asTransition";
		public static final String _changeLogID = "changeLogID";
		public static final String _importance = "importance";
		public static final String _urgency = "urgency";
		public static final String _notification = "notification";
		public static final String _ownerID = "ownerID";
		public static final String _status = "status";
		public static final String _consumedTime = "consumedTime";
		public static final String _costTime = "costTime";
		
	}
	/**
	 * 实现{@link Computable#compute()}接口
	 * @Override public void compute(){
	 *    this.costTime = timestampdiff(MINUTE,actTime,consumedTime);
	 * }
	 */
	//endregion of ~GENERATED PARTS END

	public String getActionName(){
		return asTransition.substring(0, asTransition.indexOf('('));
	}
}
