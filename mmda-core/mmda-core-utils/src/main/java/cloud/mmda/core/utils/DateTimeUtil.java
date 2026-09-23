package cloud.mmda.core.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * 日期时间工具集
 */
public abstract class DateTimeUtil {
    private static final DayOfWeek defFirstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();

    public static final DayOfWeek getFirstDayOfWeek(Locale locale){
        return WeekFields.of(locale).getFirstDayOfWeek();
    }
    public static final DayOfWeek getDefaultFirstDayOfWeek(){
        return defFirstDayOfWeek;
    }
    public static final DayOfWeek getLastDayOfWeek(DayOfWeek firstDayOfWeek){
        return DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);
    }

    public static final LocalDateTime toBeginTime(LocalDate d){
        return d.atTime(0,0,0,0);
    }
    public static final LocalDateTime toEndTime(LocalDate d){
        return d.atTime(23,59,59,999_999_999);
    }

}
