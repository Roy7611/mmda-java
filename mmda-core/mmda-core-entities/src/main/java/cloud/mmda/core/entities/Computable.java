package cloud.mmda.core.entities;

import cloud.mmda.core.Tenancy;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * 可计算的实体接口
 * 例如有计算属性的实体实现此接口，在存入数据库前或
 */
public interface Computable {
    void compute();


    default String concat(CharSequence...args){
        return String.join("",args);
    }
    default String trimEndZeros(BigDecimal arg){
        return arg.stripTrailingZeros().toString();
    }

    default String isnull(String value, String defaultValue){
        return value == null ? defaultValue : value;
    }
    default BigDecimal isnull(BigDecimal value, BigDecimal defaultValue){
        return value == null ? defaultValue : value;
    }
    default String join(String delimiter, CharSequence...args){
        return Arrays.stream(args)
                .filter(arg->arg != null && !arg.isEmpty())
                .collect(Collectors.joining(delimiter));
    }
    default Integer computeDays(java.sql.Date startDate, java.sql.Date endDate){
        if(startDate == null || endDate == null) return null;
        //计算有误   如 2024-12-26   --  2025-01-31   period.getDays() 得出天数为 5
        //var period = Period.between(startDate.toLocalDate(), endDate.toLocalDate());
        //return period.getDays();

        //to_days(endDate) - to_days(startDate) + 1
        long days = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
        return Long.valueOf(days).intValue()+1;
    }
    default Long computeHours(Timestamp startDate, Timestamp endDate){
        if(startDate == null || endDate == null) return null;
        var duration = Duration.between(startDate.toInstant(), endDate.toInstant());
        return duration.toHours();
    }
}
