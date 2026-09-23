package cloud.mmda.core.data.pagination;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

import java.io.Serializable;

/**
 * 分页器，查询数据库时使用
 * <p>
 *     Spring {@link PageRequest}从0页开始，我们的从1页开始
 * </p>
 * @since 4.0.0
 * @author roshion 2026.4.18 优化
 */
@Getter
public final class Paginator implements Serializable {
	/**
	 * 默认分页大小
	 */
	public static final int DEFAULT_PAGE_SIZE = 20;
	/**
	 * 排序规则
	 */
	@Setter
	private Sort sort;

	/**
	 * 分页大小
	 */
	@Setter
    private int pageSize;
	/**
	 * 页码，从1开始
	 */
	@Setter
    private int pageNo;
	/**
	 * 总记录数
	 */
    private int recordCount;
	/**
	 * 总页数
	 */
	private int pageCount;
	/**
	 * 当前页的记录从
	 */
	private int from;
	/**
	 * 当前页的记录至
	 */
	private int to;
		
	public Paginator(){
		this.pageSize = 10;
		this.pageNo = 1;
	}
	public Paginator(int pageSize){
		this.pageSize = pageSize;
		this.pageNo = 1;
	}
	public Paginator(int pageSize, int pageNo){
		this.pageSize = pageSize;
		this.pageNo = pageNo;
	}
	public Paginator(int pageSize, int pageNo, String orderBy){
		this.pageSize = pageSize;
		this.pageNo = pageNo;
		this.sort = Sort.parse(orderBy);
	}
	public Paginator(int pageSize, int pageNo, Sort sort){
		this.pageSize = pageSize;
		this.pageNo = pageNo;
		this.sort = sort;
	}
	
	public Paginator(String orderBy){
		this();
		this.sort = Sort.parse(orderBy);
	}
	public Paginator(Sort sort){
		this();
		this.sort = sort;
	}

	@JsonIgnore
	public String getOrderBy() {
		return sort.toString();
	}
	public void setOrderBy(String orderBy) {
		this.sort = Sort.parse(orderBy);
	}


	public boolean hasNextPage(){
		return pageNo < pageCount;
	}

	/**
	 * 设置总记录数，自动计算分页结果
	 * @param recordCount 记录数
	 */
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
		this.from = this.pageSize * (this.pageNo - 1) + 1;
		this.to = this.pageSize * this.pageNo;
		if(to > this.recordCount) to=this.recordCount;
		this.pageCount = this.pageSize > 0
			? recordCount/pageSize + ((recordCount % pageSize)>0?1:0)
			: 0;
	}

	
	public String toJson(){
		return String.format(
				"{\"sort\": \"%1$s\", \"pageNo\": %2$d, \"pageSize\": %3$d, \"pageCount\": %4$d, \"recordCount\": %5$d, \"from\": %6$d, \"to\": %7$d}",
				sort,pageNo,pageSize,pageCount,recordCount,from, to);
	}

	@Override
	public String toString(){
		return String.format("%1$s-%2$d/%3$d", sort,pageNo,pageSize)
				.replace(' ','-');
	}

	/**
	 * 按{@code sort}排序后取一条记录的分页器
	 * @param sort 排序规则
	 * @return
	 */
	public static Paginator limitOne(Sort sort){
		return new Paginator(1,1,sort);
	}
}
