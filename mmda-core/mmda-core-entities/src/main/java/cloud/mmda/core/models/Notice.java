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
import cloud.mmda.core.enums.*;
import jakarta.validation.constraints.*;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.*;

/**
 * 通知
 * 
 * @remarks 通知提醒用户进入其他页面处理代办事项，根据用户的设备订阅，自动推送todo=0的消息，完成后更改status=SENT
 * 
 * @author mmda codebot 
 * @version 4.0.0 
 * @since 2024-08-11 22:39:57.0
 * 
 */
public class Notice extends TenancyEntity<Long> {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 通知标识
	 */
	@NotNull
	@Min(0)
	@Getter @Setter
	private long noticeID;
	/**
	 * 通知时间
	 */
	@NotNull
	@Getter @Setter
	private Timestamp noticeTime;
	/**
	 * 重要性：0;UNKNOWN;-|1;IMPORTANT;重要|2;VERY_IMPORTANT;非常重要
	 */
	@NotNull
	@Getter @Setter
	private Importance importance;
	/**
	 * 紧急：0;NORMAL;普通|1;SENIOR;优先|2;URGENT;紧急
	 */
	@NotNull
	@Getter @Setter
	private Urgency emergency;
	/**
	 * 待办事宜
	 */
	@NotBlank
	@Size(min=1,max=255)
	@Getter @Setter
	private String noticeContent;
	/**
	 * 通知用户ID
	 */
	@NotNull
	@Getter @Setter
	private long noticeToUserID;
	/**
	 * 通知给：REF USER(userID,userName)
	 */
	@NotBlank
	@Size(min=1,max=255)
	@Getter @Setter
	private String noticeTo;
	/**
	 * 通知方式：0;SYSTEM;系统|1;MAIL;邮件|2;SMS;短信|4;PUSH;推送消息
	 */
	@NotNull
	@Getter @Setter
	private MessageChannel notifyingThru;
	/**
	 * 状态：0;NEW;新通知|1;READ;已读|2;DONE;已办
	 */
	@NotNull
	@Getter @Setter
	private NotificationStatus status;
	/**
	 * 创建时间
	 */
	@Getter @Setter
	private Timestamp createDate;
	/**
	 * 未办
	 */
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Setter
	private Boolean todo;
	public Boolean getTodo() {
		return flowTrailID != null && this.status != NotificationStatus.DONE;
	}
	/**
	 * 引用名称
	 */
	@Size(max=30)
	@Getter @Setter
	private String refName;
	/**
	 * 引用单号
	 */
	@Size(max=30)
	@Getter @Setter
	private String refNo;
	/**
	 * 引用标识
	 */
	@Getter @Setter
	private Long refID;
	/**
	 * 引用序号
	 */
	@Getter @Setter
	private Short refItemID;
	/**
	 * 流程追踪标识，当它已消费后反写status为已办。若为null，则已读就结束了
	 */
	@Getter @Setter
	private Long flowTrailID;
	//region partition
	/**
	 * 获取租户分区ID
	 * 实现{@link Tenancy#getPartitionID()}接口
	 */
	@JsonIgnore
	@Override
	public final long getPartitionID(){
		return noticeID;
	}
	/**
	 * 设置租户分区ID
	 * 实现{@link TenancyEntity#setPartitionID(long)}接口
	 */
	@Override
	public final void setPartitionID(final long partitionID){
		noticeID = partitionID;
	}
	//endregion of partition

	/**
	 * 作为主键
	 */
	public final Long getId(){
		return noticeID;
	}

	@Override
	public Class<Long> getIdClass() {
		return Long.class;
	}

	/**
	 * 元数据包括字段和子表关系名称
	 */
	public static abstract class Meta {
		public static final String _noticeID = "noticeID";
		public static final String _noticeTime = "noticeTime";
		public static final String _importance = "importance";
		public static final String _emergency = "emergency";
		public static final String _noticeContent = "noticeContent";
		public static final String _noticeToUserID = "noticeToUserID";
		public static final String _noticeTo = "noticeTo";
		public static final String _notifyingThru = "notifyingThru";
		public static final String _status = "status";
		public static final String _createDate = "createDate";
		public static final String _todo = "todo";
		public static final String _refName = "refName";
		public static final String _refNo = "refNo";
		public static final String _refID = "refID";
		public static final String _refItemID = "refItemID";
		public static final String _flowTrailID = "flowTrailID";
		
	}
	/**
	 * 实现{@link Computable#compute()}接口
	 * @Override public void compute(){
	 *    this.todo = flowTrailID is not null and status < 2;
	 * }
	 */
	//endregion of ~GENERATED PARTS END

	/**
	 * 改为只读计算字段
	@Override public void compute(){
		this.todo = flowTrailID != null && this.status != NotificationStatus.DONE;
	}
	*/
}
