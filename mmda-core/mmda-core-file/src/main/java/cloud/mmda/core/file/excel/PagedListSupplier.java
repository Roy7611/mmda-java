package cloud.mmda.core.file.excel;

import cloud.mmda.core.data.pagination.PagedList;
import cloud.mmda.core.data.pagination.Paginator;

import java.util.function.Function;

public class PagedListSupplier<T> {
    private final int pageSize;
    private final Function<Paginator, PagedList<T>> pageFetcher;
    public PagedListSupplier(final int pageSize, final Function<Paginator, PagedList<T>> pageFetcher) {
        this.pageSize = pageSize;
        this.pageFetcher = pageFetcher;
    }
    public PagedList<T> page(int pageNo) {
        return pageFetcher.apply(new Paginator(pageSize,pageNo));
    }
}
