/**
 * Copyright (c) 2006, 2024, www.mmda.cloud All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.metadata;

import cloud.mmda.core.enums.FlowTokenRequirement;
import cloud.mmda.core.enums.MessageLevel;
import cloud.mmda.core.enums.ModuleActionPrompt;
import cloud.mmda.core.enums.ModuleActionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * 模块操作
 * 
 * @remarks 模块功能操作，增删改查以外的扩展功能
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2020-08-17T00:28:36.577
 *
 * @Remark 2024.6.29 增加actionType，flowTokenRequired, confirmToAct
 */
public class ModuleAction extends MetaEntity {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 模块编码
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String moduleCode;
	/**
	 * 操作名称
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String actionName;
	/**
	 * 操作编码，concat(`moduleCode`,':',`actionName`)
	 */
	@NotBlank
	@Size(max=64)
	@Getter @Setter
	private String actionCode;
	/**
	 * 操作类型：0;USER_TASK;用户任务|1;SERVICE_TASK;服务任务|2;DICISION;自动判断
	 */
	@NotNull
	@Getter @Setter
	private ModuleActionType actionType;
	/**
	 * 操作标签
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String displayLabel;
	/**
	 * 显示图标
	 */
	@Size(max=255)
	@Getter @Setter
	private String displayIcon;
	/**
	 * 显示暗示：0;INFO;信息|1;SUCCESS;成功|2;WARNING;警告|4;DANGER;危险
	 */
	@NotNull
	@Getter @Setter
	private MessageLevel displayHint;
	/**
	 * 仅负责人允许
	 */
	@NotNull
	@Getter @Setter
	private boolean ownerOnly;
	/**
	 * 激活令牌：0;NONE;不要求|1;ANY;任意一个|2;ALL;所有
	 */
	@NotNull
	@Getter @Setter
	private FlowTokenRequirement incomingTokensRequired;
	/**
	 * 可执行条件
	 */
	@Size(max=255)
	@Getter @Setter
	private String executableExpression;
	/**
	 * 状态转移，例如NEW,SUBMITTED=>CLOSED
	 */
	@Size(max=255)
	@Getter @Setter
	private String statusTransition;
	/**
	 * 交互类型：0;NONE;-|1;CONFIRM;确认|2;FLOW_TO;流转
	 */
	@NotNull
	@Getter @Setter
	private ModuleActionPrompt promptType;
	/**
	 * 描述
	 */
	@Size(max=255)
	@Getter @Setter
	private String description;
	//region key & sn
	/**
	 * 主键类
	 */
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Key implements Serializable{
		/**
		 * 模块编码
		 */
		@NotBlank
		@Size(min=1,max=30)
		@Getter @Setter
		private String moduleCode;
		/**
		 * 操作名称
		 */
		@NotBlank
		@Size(min=1,max=30)
		@Getter @Setter
		private String actionName;
		/**
		 * 转为逗号隔开的字符串形式
		 * @return 返回字符串，如k1,k2
		 */
		@Override
		public String toString() {
			return moduleCode + COMPOSITE_KEY_SEPARATOR + actionName;
		}
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Key key)) return false;
			return moduleCode.equals(key.moduleCode) && actionName.equals(key.actionName);
		}
		@Override
		public int hashCode() {
			return Objects.hash(moduleCode, actionName);
		}
		
		/**
		 * 解析字符串
		 * @param ks 主键字符串形式，多个字段值逗号隔开，如k1,k2
		 * @return 主键对象
		 */
		public static final Key valueOf(String ks) {
			Objects.requireNonNull(ks);
			String[] keys = ks.split(COMPOSITE_KEY_SEPARATOR);
			return new Key(keys[0],keys[1]);
		}
	}

	/**
	 * 作为主键
	 */
	public final Key asKey(){
		return new Key(moduleCode, actionName);
	}
	//endregion of key & sn

	/**
	 * 实现{@link Computable#compute()}接口
	 * @Override public void compute(){
	 *    this.actionCode = concat(moduleCode,':',actionName);
	 * }
	 */
	//endregion of ~GENERATED PARTS END

}
