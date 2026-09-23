package cloud.mmda.core.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public enum DateRangeKind {
    TODAY(0x0001, "今天"),
    YESTERDAY(0x0002,"昨天"),
    THIS_WEEK(0x0004,"本周"),
    LAST_WEEK(0x0008,"上周"),
    LAST_7_DAYS(0x0010,"最近7天"),
    THIS_MONTH(0x0020,"本月"),
    LAST_MONTH(0x0040,"上月"),
    LAST_30_DAYS(0x0080,"最近30天"),
    LAST_90_DAYS(0x0090,"最近90天"),
    THIS_QUARTER(0x0100,"本季度"),
    LAST_QUARTER(0x0200,"上季度"),
    THIS_YEAR(0x0400,"今年"),
    LAST_YEAR(0x0800,"去年"),
    EARLIER(0x1000,"更早");

    private final Integer value;
    public Integer getValue() {
        return value;
    }


    private final String text;
    public final String getText(){return text;}

    DateRangeKind(Integer value, String text){
        this.value = value;
        this.text = text;
    }

    public final boolean hasFlag(DateRangeKind dateRangeKind){
        return (dateRangeKind.value & this.value) == dateRangeKind.value;
    }

    public static final DateRangeKind valueOf(int value){
        return enumMap.get(value);
    }
    public static final boolean hasFlag(int flags, DateRangeKind dateRangeKind){
        return (flags & dateRangeKind.value) == dateRangeKind.value;
    }

    public static final int TODAY_VAL = 0x0001;
    public static final int YESTERDAY_VAL = 0x0002;
    public static final int THIS_WEEK_VAL = 0x0004;
    public static final int LAST_WEEK_VAL = 0x0008;
    public static final int LAST_7_DAYS_VAL = 0x0010;
    public static final int THIS_MONTH_VAL = 0x0020;
    public static final int LAST_MONTH_VAL = 0x0040;
    public static final int LAST_30_DAYS_VAL = 0x0080;
    public static final int LAST_90_DAYS_VAL = 0x0090;
    public static final int THIS_QUARTER_VAL = 0x0100;
    public static final int LAST_QUARTER_VAL = 0x0200;
    public static final int THIS_YEAR_VAL = 0x0400;
    public static final int LAST_YEAR_VAL = 0x0800;
    public static final int EARLIER_VAL = 0x1000;

    //简单型
    public static final int SIMPLE_VAL = TODAY_VAL | YESTERDAY_VAL |
            THIS_WEEK_VAL |
            THIS_MONTH_VAL |
            THIS_QUARTER_VAL |
            THIS_YEAR_VAL | LAST_YEAR_VAL |
            EARLIER_VAL;
    //所有
    public static final int ALL_VAL = 0x1FFF;

    public static Map<Integer, DateRangeKind> enumMap = new LinkedHashMap<Integer, DateRangeKind>(){{
        put(TODAY_VAL,TODAY);
        put(YESTERDAY_VAL,YESTERDAY);
        put(THIS_WEEK_VAL,THIS_WEEK);
        put(LAST_WEEK_VAL,LAST_WEEK);
        put(LAST_7_DAYS_VAL,LAST_7_DAYS);
        put(THIS_MONTH_VAL,THIS_MONTH);
        put(LAST_MONTH_VAL,LAST_MONTH);
        put(LAST_30_DAYS_VAL,LAST_30_DAYS);
        put(LAST_90_DAYS_VAL,LAST_90_DAYS);
        put(THIS_QUARTER_VAL,THIS_QUARTER);
        put(LAST_QUARTER_VAL,LAST_QUARTER);
        put(THIS_YEAR_VAL,THIS_YEAR);
        put(LAST_YEAR_VAL,LAST_YEAR);
        put(EARLIER_VAL,EARLIER);
    }};
}
