/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.metadata;

import cloud.mmda.core.enums.ModuleAllowOpSet;
import cloud.mmda.core.enums.ModuleStatus;
import cloud.mmda.core.enums.ModuleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 模块
 * 
 * 功能模块，分为三级子系统、模块、功能，可用于生成系统菜单。
 *
 * 
 * @author mmda code robot
 * @version 4.0.0
 * @since 2020-08-17T00:28:36.513
 * @Remark 2024.6.29 增加操作流，用于操作完成后自动生成下一步操作提醒。
 */
public class Module extends MetaEntity {
	//region ~GENERATED PARTS BEGIN
	/**
	 * 模块编码，A.01.001
	 */
	@NotBlank
	@Size(min=1,max=15)
	@Getter @Setter
	private String moduleCode;
	/**
	 * 模块标签
	 */
	@NotBlank
	@Size(min=1,max=60)
	@Getter @Setter
	private String moduleLabel;
	/**
	 * 短标签
	 */
	@Size(max=30)
	@Getter @Setter
	private String shortLabel;
	/**
	 * 模块类型：0;SYSTEM;子系统|1;MODULE;模块组|2;FEATURE;功能项
	 */
	@NotNull
	@Min(0)
	@Getter @Setter
	private ModuleType moduleType;
	/**
	 * 模块图标
	 */
	@Size(max=255)
	@Getter @Setter
	private String moduleIcon;

	/**
	 * 数据库名称
	 */
	@Size(max=30)
	@Getter @Setter
	private String dbSchema;
	/**
	 * 元对象名称
	 */
	@Size(max=30)
	@Getter @Setter
	private String objName;
	/**
	 * 允许操作：0;NONE;无|1;READ;读取|2;EDIT;编辑|4;CREATE;创建|8;DELETE;删除|15;CRUD;增删改|16;PRINT;打印|32;IMPORT;导入|64;EXPORT;导出|127;ALL;所有
	 */
	@NotNull
	@Min(0)
	@Getter @Setter
	@JsonSerialize(converter = ModuleAllowOpSet.ToIntConverter.class)
	@JsonDeserialize(converter = ModuleAllowOpSet.FromIntConverter.class)
	private ModuleAllowOpSet allowOps;
	/**
	 * 模块Url
	 */
	@NotBlank
	@Size(min=1,max=255)
	@Getter @Setter
	private String moduleUrl;
	/**
	 * 必须有创建参数
	 */
	@NotNull
	@Getter @Setter
	private boolean requiredCreateParam;
	/**
	 * 默认过滤器
	 */
	@Size(max=255)
	@Getter @Setter
	private String defaultFilter;
	/**
	 * 默认分组
	 */
	@Size(max=100)
	@Getter @Setter
	private String defaultGroupBy;
	/**
	 * 默认排序
	 */
	@Size(max=50)
	@Getter @Setter
	private String defaultSort;
	/**
	 * 状态：0;DEV;开发中|1;TESTING;测试中|2;RELEASED;已发布|-1;REMOVED;已下架
	 */
	@NotNull
	@Getter @Setter
	private ModuleStatus status;
	/**
	 * 描述
	 */
	@Size(max=255)
	@Getter @Setter
	private String description;
	/**
	 * 串烧
	 */
	@Getter @Setter
	@Valid
	private List<ModuleSkewer> skewers;
	public final boolean hasSkewers(){
		return hasAny(skewers);
	}
	/**
	 * 操作
	 */
	@Getter @Setter
	@Valid
	private List<ModuleAction> actions;
	public final boolean hasActions(){
		return hasAny(actions);
	}
	public final Optional<ModuleAction> getActionByName(final String actionName) {
		if(hasAny(actions)){
			return actions.stream()
					.filter(a->a.getActionName().equals(actionName))
					.findFirst();
		}
		return Optional.empty();
	}
	/**
	 * 操作流
	 */
	@Getter @Setter
	@Valid
	private List<ModuleFlow> flows;

	/**
	 * 获取指定名称的操作完成后的下一步操作列表
	 * @param actionName 操作名称
	 * @return
	 */
	public final Optional<List<ModuleAction>> getNextActions(String actionName){
		if(hasAny(flows)){
			var actionCode = moduleCode+":"+actionName;
			var nextActions = flows.stream()
					.filter(f->actionCode.equals(f.getActionCode()))
					.map(ModuleFlow::getNextAction)
					.toList();
			return Optional.of(nextActions);
		}

		return Optional.empty();
	}
	public final Optional<List<ModuleFlow>> getNextFlows(String actionName) {
		if(hasAny(flows)){
			var actionCode = moduleCode+":"+actionName;
			var nextFlows = flows.stream()
					.filter(f->actionCode.equals(f.getActionCode()))
					.toList();
			return Optional.of(nextFlows);
		}

		return Optional.empty();
	}
	/**
	 * 作为主键
	 */
	public final String asKey(){
		return moduleCode;
	}
	//endregion of ~GENERATED PARTS END

	/**
	 * 子模块
	 */
	@Getter
	private List<Module> subModules;

	public void addSubModule(Module module){
		if(subModules == null){
			subModules = new ArrayList<>();
		}
		subModules.add(module);
	}
	/**
	 * 是否功能组第一项，便于客户端创建分割线
	 * @return
	 */
	public final boolean isDivider(){
		return this.moduleType == ModuleType.FEATURE && this.moduleCode.endsWith("0");
	}

	/**
	 * 获取父模块编码
	 * @return
	 */
	@JsonIgnore
	public final String getParentModuleCode(){
		return getParentModuleCode(this.moduleCode);
	}

	/**
	 * 获取上级模块编码，例如 W.01.001 => W.01
	 * @param moduleCode 模块编码
	 * @return 返回上级模块编码
	 */
	public static final String getParentModuleCode(String moduleCode){
		int lastDotPos = moduleCode.lastIndexOf(".");
		if(lastDotPos==-1) return null;
		return moduleCode.substring(0,lastDotPos);
	}

	/**
	 * 获取模块的子系统编码，例如 W.01.001 => W
	 * @param moduleCode 模块编码
	 * @return 返回子系统编码
	 */
	public static final String getSystemCode(String moduleCode){
		int firstDotPos = moduleCode.indexOf(".");
		if(firstDotPos==-1) return moduleCode;
		return moduleCode.substring(0,firstDotPos);
	}
}
