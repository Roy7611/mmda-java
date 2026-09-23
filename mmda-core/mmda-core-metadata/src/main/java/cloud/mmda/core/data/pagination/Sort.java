package cloud.mmda.core.data.pagination;

import cloud.mmda.core.lamda.LambdaGetter;
import cloud.mmda.core.lamda.LambdaUtil;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.utils.BaseUtil;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 排序包含排序字段和升降序，例如 orderNo DESC
 */
public class Sort {
//	public static final String ASC = "ASC";
//	public static final String DESC = "DESC";
	public enum Order {
		ASC,
		DESC
	}

	/**
	 * 排序表达式，可能是字符串、字段或者SQL表达式
	 * @return
	 */
	@Getter
	private final Object sortBy;

	/**
	 * 升序 / 降序
	 */
	@Getter
	private final Order sortOrder;
	private LinkedList<Sort> thenSorts;

	private Sort(final Object sortBy,final Order sortOrder){
		this.sortBy = sortBy;
		this.sortOrder = sortOrder;
	}

	/**
	 * 是否降序
	 * @return
	 */
	public final boolean isDescending(){return Order.DESC.equals(sortOrder);}

	public final Sort thenBy(final Sort sort){
		if(thenSorts == null){
			thenSorts = new LinkedList<>();
		}
		thenSorts.add(sort);
		return this;
	}
	public final Sort thenBy(final Object sortBy){
		return thenBy(new Sort(sortBy, Order.ASC));
	}
	public final Sort thenByDesc(final Object sortBy){
		return thenBy(new Sort(sortBy, Order.DESC));
	}

	private String asSql(){
		if(sortBy instanceof LambdaGetter getter){
			return LambdaUtil.getFieldName(getter) + " " + sortOrder.name();
		}
		return sortBy + " " + sortOrder.name();
	}
	@Override
	public String toString(){
		return stringify(Sort::asSql);
	}

	/**
	 * 使用{@code stringer}函数将排序转化为字符串
	 * @param stringer
	 * @return
	 */
	public String stringify(Function<Sort,String> stringer){
		var sj = new StringJoiner(",");
		sj.add(stringer.apply(this));
		if(BaseUtil.hasAny(thenSorts)){
			for(Sort sort : thenSorts){
				sj.add(stringer.apply(sort));
			}
		}
		return sj.toString();
	}

	/**
	 * 创建排序规则
	 * <pre>
	 *     {@code
	 *     var sort = Sort.of(“partnerID”, Order.DESC)
	 *     		.thenBy("partnerRoles");
	 *     }
	 * </pre>
	 * @param sortBy 排序字段，可以是字符串、{@link cloud.mmda.core.metadata.MetaCol}或者Sql表达式
	 * @param sortOrder 指定升序还是降序
	 * @return 排序规则
	 */
	public static Sort of(final Object sortBy, Order sortOrder){
		return new Sort(sortBy, sortOrder);
	}

	/**
	 * 创建按{@code sortBy}表达式升序排序规则
	 * @param sortBy 表达式可以是字符串、{@link cloud.mmda.core.metadata.MetaCol}或者Sql表达式
	 * @return
	 */
	public static Sort of(final Object sortBy){
		return of(sortBy, Order.ASC);
	}


	/**
	 * 使用Lambda表达式创建排序规则，仅限于单表查询使用。
	 * 如果多表查询的时候不是主表字段，则建议使用{@link Sort#of(Object, Order)}传入<code>MetaCol</code>对象
	 * 指定特定字段
	 * @param sortBy 排序字段的Lambda表达式
	 * @param sortOrder 顺序
	 * @return
	 * @param <T> 实体类型
	 */
	public static <T> Sort of(LambdaGetter<T,?> sortBy, Order sortOrder) {
		return new Sort(sortBy, sortOrder);
	}

	/**
	 * 使用Lambda表达式创建排序规则，仅限于单表查询使用。
	 * 如果多表查询的时候不是主表字段，则建议使用{@link Sort#of(Object)}传入<code>MetaCol</code>对象
	 * 指定特定字段
	 * @param sortBy
	 * @return
	 * @param <T>
	 */
	public static <T> Sort of(LambdaGetter<T,?> sortBy) {
		return of(sortBy, Order.ASC);
	}

	private static Sort parseSingle(String orderBy){
		String[] sortFlds = orderBy.trim().split(" ");
		Sort.Order sortOrder = sortFlds.length > 1 && Order.DESC.name().equalsIgnoreCase(sortFlds[1])
				? Order.DESC : Order.ASC;
		return new Sort(sortFlds[0], sortOrder);
	}

	/**
	 * 从字符串解析出排序规则
	 * @param orderBy 排序字符串，例如：<code>lastModified DESC, amount ASC</code>
	 * @return 排序规则
	 */
	public static Sort parse(String orderBy){
		Objects.requireNonNull(orderBy, "orderBy cannot be null");
		if(orderBy.indexOf(',') >= 0){
			var sorts = Arrays.stream(orderBy.split(","))
					.map(Sort::parseSingle)
					.toList();
			return concat(sorts);
		}
		return parseSingle(orderBy);
	}
	/**
	 * 合并多个排序规则至第一个元素
	 * @param sorts
	 * @return
	 */
	public static Sort concat(final Iterable<Sort> sorts){
		var it = sorts.iterator();
		if(it.hasNext()){
			var sort = it.next();
			while(it.hasNext()){
				sort = sort.thenBy(sort);
			}
			return sort;
		}
		throw new IllegalArgumentException("sorts cannot be empty");
	}

	public static Sort byKey(MetaObject metaObj){
		if(metaObj.isSingleKey()) return of(metaObj.getKeyCols().get(0));
		return concat(metaObj.getKeyCols().stream().map(Sort::of).toList());
	}
}
