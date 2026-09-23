package cloud.mmda.core.data.conversion;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * 定点小数和持续时间(INTERVAL DAY(p) TO SECOND(s)) 的双向转换器，整数部分（16位天-16位时-16位分-16位秒），小数部分为纳秒
 */
public class BigDecimalDurationConverter implements BiConverter<BigDecimal, Duration>{
    @Override
    public BigDecimal revert(Duration target) {
        if(target == null) return null;
        else if(target.compareTo(Duration.ZERO) == 0) return BigDecimal.ZERO;
        else{
            var d = target.toDaysPart();
            var h = target.toHoursPart();
            var m = target.toMinutesPart();
            var s = target.toSecondsPart();
            var n = target.toNanosPart();
            var integerPart = d << 48 | h << 32 | m << 16 | s << 16;
            var fractionPart = new BigDecimal("0." + n);
            return new BigDecimal(integerPart).add(fractionPart);
        }
    }

    @Override
    public Duration convert(BigDecimal source) {
        if(source == null) return null;
        else if(source.compareTo(BigDecimal.ZERO) == 0) return Duration.ZERO;
        else {
//                var integerPart = value.toBigInteger();
//                var fractionalPart = value.subtract(new BigDecimal(integerPart));
            var parts = source.divideAndRemainder(BigDecimal.ONE);//一次性获取整数和小数
            //整数部分解析为天、时、分、秒
            var mainPart = parts[0].longValue();
            var d = (short)(mainPart >>> 48);
            var h = (short)((mainPart >>> 32) & 0xFFFF);
            var m = (short)((mainPart >>> 16) & 0xFFFF);
            var s = (short)(mainPart & 0xFFFF);
            //小数部分解析为纳秒
            var strippedDec = parts[1].stripTrailingZeros();// 去除末尾的零
            var fraction = strippedDec.movePointRight(strippedDec.scale());// 移动小数点，将小数变为整数
            var nanos = fraction.longValue();
            return Duration.ofDays(d)
                    .plusHours(h)
                    .plusMinutes(m)
                    .plusSeconds(s)
                    .plusNanos(nanos);
        }
    }
}
