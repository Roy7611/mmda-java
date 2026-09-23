/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.api;
import cloud.mmda.core.models.AuditTrail;
import cloud.mmda.core.caching.CachePolicy;

import cloud.mmda.core.services.AuditTrailService;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 审计追踪控制器
 * <p>
 *     开放审计追踪记录数据访问API，数据自动生成，默认不缓存，仅允许管理员删除
 * </p>
 * @author syclive code robot
 * @author Roy Luo
 * @version 4.0.0
 * @since 2024-07-17 07:38:57.0
 * 
 */
@RestController
@RequestMapping("/AuditTrails")
public class AuditTrailController extends ReactiveEntityController<AuditTrail,Long> {
	//region ~GENERATED PARTS BEGIN
	//强类型service用于访问业务逻辑
	private AuditTrailService service;
	/**
	 * 构造函数
	 * @param service 商业服务
	 */
	public AuditTrailController(AuditTrailService service) {
		super(service);
		this.service = service;
	}

	
	//region 00 缓存策略（不缓存）
	
	/**
	 * 根据实体决定缓存策略，CRUD时使用，
	 * @param request
	 * @param t 审计追踪
	 * @return 单个对象接口的缓存策略
	 */
	@Override
	protected Mono<CachePolicy> getCachePolicy(ServerHttpRequest request, AuditTrail t) {
		return getNoCachePolicy(request);//不缓存
	}

	/**
	 * 默认缓存策略，通常读取后5分钟失效
	 * @param request 请求
	 * @return 默认使用的缓存策略
	 */
	@Override
	protected Mono<CachePolicy> getDefaultCachePolicy(ServerHttpRequest request) {
		return getNoCachePolicy(request);//不缓存
	}

	//endregion of cache
	
	//region 03 读取 （get & getAll）
	@PreAuthorize(HAS_ROLE_STAFF)
	@Override @GetMapping("")
	public Flux<AuditTrail> getAll(ServerHttpRequest request, ServerHttpResponse response){
		return super.getAll(request, response);
	}
	@PreAuthorize(HAS_ROLE_STAFF)
	@Override @GetMapping("/{trailID}")
	public Mono<AuditTrail> get(ServerHttpRequest request, @PathVariable Long trailID) {
		return super.get(request, trailID);
	}
	//endregion of get


	//region 05 删除
	/**
	 * 删除，使用DELETE方法根据主键删除
	 * @param request 请求
	 * @param trailID 主键
	 * @return 返回删除记录数
	 */
	@PreAuthorize(HAS_ROLE_ADMIN)
	@Override @DeleteMapping("/{trailID}")
	public Mono<Integer> delete(ServerHttpRequest request, @PathVariable Long trailID) {
		validateParam(trailID,"trailID", id -> id>0);
		return super.delete(request, trailID);
	}
	/**
	 * 删除，使用POST方法，用于小程序不支持DELETE的情况
	 * @param request 请求
	 * @param trailID 主键
	 * @return 返回删除记录数
	 */
	@PreAuthorize(HAS_ROLE_ADMIN)
	@PostMapping("/{trailID}/delete")
	public Mono<Integer> deleteByPost(ServerHttpRequest request, @PathVariable Long trailID){
		return delete(request,trailID);
	}
	/**
	 * 批量删除，使用POST方法
	 * @param request 请求
	 * @param idFlux 在请求Body中的主键集合
	 * @return 返回删除记录数
	 */
	@PreAuthorize(HAS_ROLE_ADMIN)
	@Override @PostMapping("/deleteAll")
	public Mono<Integer> deleteAll(ServerHttpRequest request, @RequestBody Flux<Long> idFlux) {
		return super.deleteAll(request, idFlux);
	}
	//endregion of delete

	//region 06 统计接口

	//endregion of actions

	//endregion of ~GENERATED PARTS END

}
