package cloud.mmda.core.data.conversion;

import java.time.Period;

/**
 * 长整形（16位年-16位月-32位天）至期间(INTERVAL YEAR TO DAY) 的双向转换器
 */
public class LongPeriodConverter implements BiConverter<Long, Period> {
    /**
     * 期间转化为长整型数（16位年-16位月-32位天）
     * @param target 目的类型值
     * @return
     */
    @Override
    public Long revert(Period target) {
        if(target == null) return null;
        long y = Math.clamp(target.getYears(),Short.MIN_VALUE,Short.MAX_VALUE);
        long m = Math.clamp(target.getMonths(),Short.MIN_VALUE,Short.MAX_VALUE);
        return (y << 48) | (m << 32) | target.getDays();
    }

    /**
     * 长整形转化为期间类型
     * @param source 长整形数（16位年-16位月-32位天）
     * @return
     */
    @Override
    public Period convert(Long source) {
        if(source == null) return null;
        else if(source == 0L) return Period.ZERO;
        var y = (short)(source >>> 48);
        var m = (short)((source >>> 32) & 0xFFFF);
        var d = (int)(source & 0xFFFFFFFFL);
        return Period.of(y, m, d);
    }
}
