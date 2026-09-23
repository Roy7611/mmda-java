package cloud.mmda.core.data.pagination;

import java.util.List;


/**
 * 分页后的列表
 * @author roshion
 * 
 */
public final class PagedList<T> {
	private final List<T> data;
	private final Paginator pagination;
	
	public PagedList(List<T> data, Paginator p){
		this.data = data;
		this.pagination = p;
	}
	public final List<T> getData() {
		return data;
	}
	public final Paginator getPagination() {
		return pagination;
	}
	public boolean hasNextPage(){
		return pagination.hasNextPage();
	}
	public final boolean isEmpty(){
		return data == null || data.isEmpty();
	}

	public  static <U> PagedList<U> of(List<U> data, Paginator p) {
		return new PagedList<U>(data, p);
	}

}
