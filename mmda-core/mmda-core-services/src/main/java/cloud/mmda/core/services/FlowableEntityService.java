package cloud.mmda.core.services;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.caching.CachePolicy;
import cloud.mmda.core.data.jdbc.repository.TenancyEntityRepository;
import cloud.mmda.core.entities.*;
import cloud.mmda.core.metadata.ModuleAction;
import cloud.mmda.core.metadata.ModuleFlow;
import cloud.mmda.core.models.FlowTrail;
import cloud.mmda.core.models.Notice;
import cloud.mmda.core.security.UserAccountService;
import cloud.mmda.core.security.models.User;
import cloud.mmda.core.security.models.UserAccount;
import cloud.mmda.core.services.exceptions.OperationFailedException;
import cloud.mmda.core.utils.BaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 支持流程追踪的实体服务
 * @param <S> 实体状态枚举类型
 * @param <T> 实体类型
 */
public abstract class FlowableEntityService<S extends Enum<S>, T extends FlowableEntity<S>> extends TenancyEntityService<T,Long> {
    protected final FlowTrailService flowTrailService;
    protected final NoticeService noticeService;

    private UserAccountService userAccountService;
    @Autowired
    public void setUserAccountService(@Qualifier("serviceUserAccountService") final UserAccountService userAccountService){
        this.userAccountService = userAccountService;
    }
    /**
     * 构造函数
     * @param repository 底层数据库访问仓储
     * @param factory redis连接工厂
     * @param flowTrailService 流程追踪服务
     */
    public FlowableEntityService(final TenancyEntityRepository<T, Long> repository, final RedisConnectionFactory factory,
                                 final FlowTrailService flowTrailService, final NoticeService noticeService) {
        super(repository, factory);
        this.flowTrailService = flowTrailService;
        this.noticeService = noticeService;
    }

    /**
     * 获取所有主键keys对应的实体列表
     * @param keys 主键列表
     * @param p 缓存策略
     * @return 实体对象列表
     * @throws OperationFailedException
     */
    public List<T> getAllByIDs(List<Long> keys, CachePolicy p) throws OperationFailedException {
        BaseUtil.requireNonNull(keys,"keys");
        BaseUtil.requireNonNull(p,"p");
        var keyField = getMetaObject().getKeyCols().get(0).getColName();
        var keyList = keys.stream().map(k -> k.toString()).collect(Collectors.joining(","));
        return super.getAllIn(keyField, keyList, true, p);
    }
    //region do & undo
    @Override
    @Transactional
    public T doAction(final T t, final UserAccount user, final DomainActionParam actionParam){
        return doAction(t, user, false, actionParam);
    }

    @Override
    @Transactional
    public T doAction(T t, UserAccount user, final boolean isAuthorized, DomainActionParam actionParam) {
        Assert.notNull(t, "t must not be null");
        var fromStatus = t.getStatus();
        T result = super.doAction(t, user,isAuthorized, actionParam);

        //flowTrails
        var toStatus = t.getStatus();
        var flowTrail = flowTrailService.buildFlowTrail(user,
                getMetaObject(), t.getId(),
                actionParam, fromStatus, toStatus, t.getNewChangeLogID()
        );

//        if(result instanceof ChangeLoggable loggable){
//            //sets the change log id so that it can be undone
//            flowTrail.setChangeLogID(loggable.getChangeLogID());
//        }
//        else{
//            flowTrail.setChangeLogID(t.getNewChangeLogID());
//        }
        processPreviousFlowTrailsAsDone(getMetaObject().getObjName(),t.getId());

        int r = flowTrailService.save(flowTrail);
        if(r > 0){
            //notice every one in copyTo
            if(actionParam.getOwnerID() != null){
                notifyTo(actionParam.getOwnerID().longValue(), flowTrail);
                if(actionParam.getCopyTo()!=null && !actionParam.getCopyTo().isEmpty()){
                    for(var cc : actionParam.getCopyTo()){
                        notifyTo(cc.longValue(), flowTrail);
                    }
                }
            }

        }

        return result;
    }

    public void notifyTo(long userId, final FlowTrail flowTrail){
        flowTrailService.assembleSingle(flowTrail);
        var notice = new Notice();
        notice.setImportance(flowTrail.getImportance());
        notice.setEmergency(flowTrail.getUrgency());
        var toUser = userAccountService.loadUserByUserIdWithDevices(userId);
        noticeService.pushNotices(toUser, flowTrail);
    }
    /**
     * 将前置 FlowTrail 标记为已处理并通知改为已办
     */
    private void processPreviousFlowTrailsAsDone(String objName,Long objID) {
//        String[] trailIDs = flowTrailService.getNewKeysBy(objName, objID)
//                .stream()
//                .map(String::valueOf)
//                .toArray(String[]::new);
        var trailIDs = flowTrailService.getNewKeysBy(objName, objID);
        if(trailIDs==null || trailIDs.isEmpty()) return;

        int result = flowTrailService.updateDoneByIds(trailIDs);
        if (result > 0) {
            noticeService.updateDoneByFlowTrailIDs(trailIDs);
        }
    }

    @Transactional
    public boolean undo(final T t, final UserAccount user, Long flowTrailId){
       return executeUndo(t, user, flowTrailId,true);
    }
    public boolean executeUndo(final T t, final UserAccount user, Long flowTrailId,boolean deleteFlowTrail){
        Assert.notNull(t, "t must not be null");
        Assert.notNull(flowTrailId, "flowTrailId must not be null");
        //先校验流程追踪数据合法
        var lastTrail = flowTrailService.getLastByObj(tClassName,t.getId());
        //要撤消的必须是最后一步操作
        Assert.isTrue(lastTrail.isPresent() && flowTrailId == lastTrail.get().getTrailID(), "flow trail must be the last one");
        //确保流程发起人是当前用户
//        Assert.isTrue(lastTrail.get().getActorID() == user.getUserID(), "flow trail actor mismatch to request user");
        Long id = t.getId();
        Assert.isTrue( id.longValue() == lastTrail.get().getObjID(),"Flow trail object id mismatch to the entity");

        //undo
        var actionName = lastTrail.get().getActionName();
        var domainAction = getDomainAction(actionName);
        if(domainAction.isEmpty()) throw operationFailed(actionName,t, new RuntimeException("Action not found"));

        var action = domainAction.get();
        if(!action.canUndo(t,user)){
            throw operationFailed(actionName,t, new RuntimeException("Undo action not allowed"));
        }

        //如果action本身有自定义撤消逻辑，执行
        boolean undone = action.undo(t,user,flowTrailId);

        //如果没有自定义撤消逻辑，使用默认的
        if(!undone) {
            //恢复数据并删除修改日志，流程追踪不需要保留修改日志，重做可点击Action
            undone = super.undo(t, user, lastTrail.get().getChangeLogID(),true);
        }

        if(undone && deleteFlowTrail){
            //删除流程追踪记录
            flowTrailService.delete(lastTrail.get());
        }
        return undone;
    }

    //endregion

    //region update status

    /**
     * 更新实体状态
     * @param id 实体对象标识
     * @param newStatus 更新到此状态
     * @return 影响的记录数
     */
    public int updateStatus(Long id, S newStatus){
        var attrs = this.sqlExpressionBuilder()
                .exp("status").equal(newStatus)
                .result();
        var tenantId = Tenancy.parseTenantID(id);
        return this.partialUpdate(id, attrs, CachePolicy.of(tenantId));
    }
    /**
     * 更新实体状态
     * @param id 实体对象标识
     * @param oldStatus 原状态必须是此值
     * @param newStatus 更新到此状态
     * @return 影响的记录数
     */
    public int updateStatus(Long id, S oldStatus, S newStatus){
        var attrs = this.sqlExpressionBuilder()
                .exp("status").equal(newStatus)
                .result();
        var cond = this.sqlExpressionBuilder()
                .exp("status").equal(oldStatus)
                .result();
        var tenantId = Tenancy.parseTenantID(id);
        return this.partialUpdate(id, attrs, cond, CachePolicy.of(tenantId));
    }
    //endregion


    @Override
    public DomainActionParam prepareAction(T t, String actionName, UserAccount user) {
        var param = super.prepareAction(t, actionName, user);
        // 准备流程相关的用户信息
        if (CollectionUtils.isEmpty(param.getUsers())) {
            prepareFlowActionUsers(t.getTenantID(),param, actionName);
        }
        return param;
    }

    /**
     * 准备流程操作相关的用户信息
     */
    protected void prepareFlowActionUsers(int tenantID,DomainActionParam param, final String actionName) {
        var opModule = getModule(LocaleContextHolder.getLocale());
        if (opModule.isEmpty()) return;

        var module = opModule.get();
        // 获取模块权限用户
        var users = userAccountService.getUsersByModulePermission(tenantID,module.getModuleCode());
        param.setUsers(users);
        param.setOwners(users);

        var nextFlows=module.getNextFlows(actionName);
        if (nextFlows.isPresent()) {
            // 处理下一步操作的用户
            List<String> actionNames = nextFlows.get().stream()
                    .map(ModuleFlow::getAction)
                    .map(ModuleAction::getActionName)
                    .collect(Collectors.toList());
            var owners = userAccountService.getUsersByModuleActionPermission(tenantID,module.getModuleCode(), actionNames);
            param.setOwners(owners);
        }

        setFirstOwner(param);
    }

    protected void setFirstOwner( DomainActionParam param) {
        if (BaseUtil.hasAny(param.getOwners())) {
            var owner = param.getOwners().get(0);;
            param.setOwnerID(owner.getUserID());
            param.setOwnerDeptID(owner.getDeptID());
        }
    }
    protected List<User> getUsersByModulePermission(int tenantID,String moduleCode){
        return  userAccountService.getUsersByModulePermission(tenantID,moduleCode);
    }

    protected User loadUserByUserIdWithDevices(long userId){
        return userAccountService.loadUserByUserIdWithDevices(userId);
    }

}
