package cloud.mmda.core.metadata;

import com.google.common.collect.Range;
import lombok.Getter;

/**
 * 对数据范围进行定义，包括默认值。
 *
 * 可用于对一种数据类型进行限制
 *
 * @param <C> 数据类型
 */
public final class MetaRange<C extends Comparable<C>> {
    @Getter
    private final Range<C> range;
    @Getter
    private final C defaultValue;

    public MetaRange(Range<C> range, C defaultValue){
        this.range = range;
        this.defaultValue = defaultValue;
    }
    public static <C extends Comparable<C>> MetaRange closed(C min, C max, C defaultValue){
        return new MetaRange(Range.closed(min, max), defaultValue);
    }
    public static <C extends Comparable<C>> MetaRange closed(C min, C max){
        return new MetaRange(Range.closed(min, max), null);
    }

    public static <C extends Comparable<C>> MetaRange all(C defaultValue){
        return new MetaRange(Range.all(), defaultValue);
    }
}
