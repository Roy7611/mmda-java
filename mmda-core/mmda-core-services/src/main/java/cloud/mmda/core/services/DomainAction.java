package cloud.mmda.core.services;

import cloud.mmda.core.entities.Entity;
import cloud.mmda.core.entities.EntityAction;
import cloud.mmda.core.models.FlowTrail;
import cloud.mmda.core.enums.ModuleActionType;
import cloud.mmda.core.security.models.UserAccount;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 业务操作
 * <p>指领域对象的商业逻辑操作接口，被{@link DomainService}内部使用。</p>
 * <p>
 *     我们在元数据{@link cloud.mmda.core.metadata.ModuleAction}定义了一个功能模块具有的业务操作，
 *     一个模块会有关联的领域实体T，倘若用户拥有操作权限并且能执行，则会在加载完整数据的实体详情对象里组装
 *     actions列表，列表中每一项是{@link cloud.mmda.core.entities.EntityAction}（VO）。
 *     你也可以自定义EntityAction组装进{@link cloud.mmda.core.entities.TenancyEntity#setActions(List)}中。
 * </p>
 * <p>
 *     如果支持反向操作，则有另一个反向的Undo DomainAction，它们之间成对出现，对于反向操作，
 *     你可在{@link DomainAction#canExecute(Object, UserAccount)}中判断实体流程追踪记录中是否已经有正向执行记录。
 * </p>
 * @param <T> 实体类型
 *
 */
public interface DomainAction<T,K> {
    /**
     * 操作名称，例如submit,close
     * @return
     */
    String getName();

    /**
     * 获取操作类型
     * @return
     */
    ModuleActionType getType();
    /**
     * 获取实体操作元数据
     * @param locale 语言区域
     * @return {@link EntityAction} 实体操作
     */
    EntityAction getMetadata(final Locale locale);


    /**
     * 是否能执行，如果能则会组装到实体的actions里面
     * @param t 实体对象
     * @param user 执行用户
     * @return
     */
    boolean canExecute(final T t, final UserAccount user);

    /**
     * 执行前校验数据合法性或者拦截，
     * <p>
     *     会再次调用{@link this#canExecute(Object, UserAccount)}，
     *     可抛出异常表明为什么不能继续。如果不抛出异常则继续执行execute
     * </p>
     * @param t 实体对象
     * @return 返回校验是否通过
     */
    default void beforeExecute(final T t){}

    /**
     * 执行此操作，要负责存储数据
     * <p>
     *     如果实体实现了流程追踪{@link cloud.mmda.core.entities.Flowable}接口，
     *     则自动生成实体对象t的流程追踪记录{@link FlowTrail}。
     * </p>
     * @param t 实体对象
     * @param user 执行用户
     * @param param 执行参数
     * @return 执行操作后更改的实体对象（可能与数据库中的不一致）
     */
    T execute(final T t, final UserAccount user, Map<String,Object> param);

    /**
     * 是否能撤消，默认不允许
     * @param t 实体对象
     * @param user 执行用户
     * @return
     */
    default boolean canUndo(final T t, final UserAccount user){
        return false;
    }
    /**
     * 撤消此操作，默认不支持
     * <p>
     *     实现类如果没有定制化的逻辑，不用重写此函数。
     *     框架会使用默认实现{@link EntityService#undo(Entity, UserAccount, Long)}执行数据回滚。
     * </p>
     * @param t
     * @param user
     * @return
     */
    default boolean undo(final T t, final UserAccount user, Long flowTrailId){
        return false;
    }


    /**
     * 准备操作相关的用户信息
     */
    default void prepareActionUsers(final T t, DomainActionParam param, final UserAccount user) {}
}
