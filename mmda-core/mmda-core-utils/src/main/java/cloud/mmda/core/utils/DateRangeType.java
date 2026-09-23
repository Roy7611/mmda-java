package cloud.mmda.core.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public enum DateRangeType {
    THIS_WEEK(0x0004,"本周"),
    THIS_MONTH(0x0020,"本月"),
    LAST_90_DAYS(0x0080,"最近90天");

    private final Integer value;
    public Integer getValue() {
        return value;
    }


    private final String text;
    public final String getText(){return text;}

    DateRangeType(Integer value, String text){
        this.value = value;
        this.text = text;
    }

    public final boolean hasFlag(DateRangeType dateRangeType){
        return (dateRangeType.value & this.value) == dateRangeType.value;
    }

    public static final DateRangeType valueOf(int value){
        return enumMap.get(value);
    }
    public static final boolean hasFlag(int flags, DateRangeType dateRangeType){
        return (flags & dateRangeType.value) == dateRangeType.value;
    }

    public static final int THIS_WEEK_VAL = 0x0004;
    public static final int THIS_MONTH_VAL = 0x0020;
    public static final int LAST_90_DAYS_VAL = 0x0080;
    public static final int SIMPLE_VAL =
            THIS_WEEK_VAL |
            THIS_MONTH_VAL
          ;
    //所有
    public static final int ALL_VAL = 0x1FFF;

    public static Map<Integer, DateRangeType> enumMap = new LinkedHashMap<Integer, DateRangeType>(){{
        put(THIS_WEEK_VAL,THIS_WEEK);
        put(THIS_MONTH_VAL,THIS_MONTH);
        put(LAST_90_DAYS_VAL, LAST_90_DAYS);
    }};
}
