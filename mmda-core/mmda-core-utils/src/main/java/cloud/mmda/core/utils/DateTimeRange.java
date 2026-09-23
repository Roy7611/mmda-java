package cloud.mmda.core.utils;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 日期时间范围，用于日期过滤查询
 */
public class DateTimeRange {
    private final LocalDateTime begin;
    private final LocalDateTime end;

    private DateTimeRange(LocalDateTime begin, LocalDateTime end){
        Objects.requireNonNull(begin,"begin");
        Objects.requireNonNull(end,"end");
        this.begin = begin;
        this.end = end;
    }
    public final LocalDateTime getBegin() {
        return begin;
    }

    public final LocalDateTime getEnd() {
        return end;
    }

    public final boolean include(DateTimeRange other){
        return other.begin.compareTo(begin)>=0 && other.end.compareTo(end)<=0;
    }

    public final DateRange toDateRange(){
        return DateRange.of(begin.toLocalDate(),end.toLocalDate());
    }
    @Override
    public String toString() {
        return begin + " ~ " + end;
    }

    public static final DateTimeRange of(final LocalDateTime begin, final LocalDateTime end){
        return new DateTimeRange(begin,end);
    }
    public static final DateTimeRange valueOf(String v){
        String[] values = v.split(" ~ ");
        return new DateTimeRange(LocalDateTime.parse(values[0]),LocalDateTime.parse(values[1]));
    }

}
