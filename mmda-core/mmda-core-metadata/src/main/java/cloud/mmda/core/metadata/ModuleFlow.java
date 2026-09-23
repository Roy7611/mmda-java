/**
 * Copyright (c) 2006, 2024, www.mmda.cloud All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.metadata;

import cloud.mmda.core.enums.Importance;
import cloud.mmda.core.enums.Urgency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 模块流程
 *
 * 连接两个模块操作{@link ModuleAction}，当第一个操作完成时自动生成通知用户进行下一个操作。
 */
public class ModuleFlow extends MetaEntity {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 流编码
	 */
	@NotBlank
	@Size(min=1,max=128)
	@Getter @Setter
	private String flowCode;
	/**
	 * 操作：HAS_ONE ModuleAction(actionCode,actionName) AS action
	 */
	@NotBlank
	@Size(min=1,max=64)
	@Getter @Setter
	private String actionCode;
	/**
	 * 下一个操作：HAS_ONE ModuleAction(actionCode,actionName) AS nextAction
	 */
	@NotBlank
	@Size(min=1,max=64)
	@Getter @Setter
	private String nextActionCode;
	/**
	 * 模块编码
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String moduleCode;
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
	 * 标准工作时长(min)
	 */
	@Getter @Setter
	private Integer sopDuration;
	/**
	 * 通知
	 */
	@Size(max=255)
	@Getter @Setter
	private String notice;
	/**
	 * 多重性，下一个操作是多实例的，提醒所有角色
	 */
	@NotNull
	@Getter @Setter
	private int multiplicity;
	/**
	 * 缺省
	 */
	@NotNull
	@Getter @Setter
	private boolean fallback;
	/**
	 * 操作
	 * @see #actionCode
	 */
	@Getter @Setter
	private ModuleAction action;
	/**
	 * 下一个操作
	 * @see #nextActionCode
	 */
	@Getter @Setter
	private ModuleAction nextAction;
	//region key & sn
	/**
	 * 主键类
	 */
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Key implements Serializable {
		/**
		 * 流编码
		 */
		@NotBlank
		@Size(min=1,max=128)
		@Getter @Setter
		private String flowCode;
		/**
		 * 模块编码
		 */
		@NotBlank
		@Size(min=1,max=30)
		@Getter @Setter
		private String moduleCode;
		/**
		 * 转为逗号隔开的字符串形式
		 * @return 返回字符串，如k1,k2
		 */
		@Override
		public String toString() {
			return flowCode + COMPOSITE_KEY_SEPARATOR + moduleCode;
		}
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Key key)) return false;
			return flowCode.equals(key.flowCode) && moduleCode.equals(key.moduleCode);
		}
		@Override
		public int hashCode() {
			return Objects.hash(flowCode, moduleCode);
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
		return new Key(flowCode, moduleCode);
	}
	//endregion of key & sn

	//endregion of ~GENERATED PARTS END

}
