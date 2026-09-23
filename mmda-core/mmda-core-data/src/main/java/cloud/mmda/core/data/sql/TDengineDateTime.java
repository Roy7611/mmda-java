package cloud.mmda.core.data.sql;

public class TDengineDateTime implements SqlDateTime{
    public static final String TODAY = "CURRENT_DATE";
    public static final String YESTERDAY = "SUB_DATE(CURRENT_DATE, 1)";
    public static final String TOMORROW = "ADD_DATE(CURRENT_DATE, 1)";
    public static final String THIS_MONDAY = "SUB_DATE(CURRENT_DATE, WEEKDAY(CURRENT_DATE))";
    public static final String LAST_MONDAY = "SUB_DATE(CURRENT_DATE, WEEKDAY(CURRENT_DATE)+7)";
    public static final String NEXT_MONDAY = "ADD_DATE(CURRENT_DATE, 7-WEEKDAY(CURRENT_DATE))";
    public static final String FIRST_DAY_OF_THIS_MONTH = "SUB_DATE(CURRENT_DATE, DAY(CURRENT_DATE)-1)";
    public static final String FIRST_DAY_OF_LAST_MONTH = "SUB_DATE(DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH), DAY(CURRENT_DATE)-1)";
    public static final String FIRST_DAY_OF_NEXT_MONTH = "ADD_DATE(LAST_DAY(CURRENT_DATE), 1)";
    public static final String FIRST_DAY_OF_THIS_QUARTER = "MAKE_DATE(YEAR(CURRENT_DATE), 1)+INTERVAL QUARTER(CURRENT_DATE)-1 QUARTER";
    public static final String FIRST_DAY_OF_LAST_QUARTER = "MAKE_DATE(YEAR(CURRENT_DATE), 1)+INTERVAL QUARTER(CURRENT_DATE)-2 QUARTER";
    public static final String FIRST_DAY_OF_NEXT_QUARTER = "MAKE_DATE(YEAR(CURRENT_DATE), 1)+INTERVAL QUARTER(CURRENT_DATE) QUARTER";
    public static final String FIRST_DAY_OF_THIS_YEAR = "MAKE_DATE(YEAR(CURRENT_DATE), 1)";
    public static final String FIRST_DAY_OF_LAST_YEAR = "MAKE_DATE(YEAR(CURRENT_DATE)-1, 1)";
    public static final String FIRST_DAY_OF_NEXT_YEAR = "MAKE_DATE(YEAR(CURRENT_DATE)+1, 1)";
    @Override
    public final String getToday() {
        return TODAY;
    }

    @Override
    public final String getYesterday() {
        return YESTERDAY;
    }

    @Override
    public final String getTomorrow() {
        return TOMORROW;
    }

    @Override
    public final String getThisMonday() {
        return THIS_MONDAY;
    }

    @Override
    public final String getLastMonday() {
        return LAST_MONDAY;
    }

    @Override
    public final String getNextMonday() {
        return NEXT_MONDAY;
    }

    @Override
    public final String getFirstDayOfThisMonth() {
        return FIRST_DAY_OF_THIS_MONTH;
    }

    @Override
    public final String getFirstDayOfLastMonth() {
        return FIRST_DAY_OF_LAST_MONTH;
    }

    @Override
    public final String getFirstDayOfNextMonth() {
        return FIRST_DAY_OF_NEXT_MONTH;
    }

    @Override
    public final String getFirstDayOfThisQuarter() {
        return FIRST_DAY_OF_THIS_QUARTER;
    }

    @Override
    public final String getFirstDayOfLastQuarter() {
        return FIRST_DAY_OF_LAST_QUARTER;
    }

    @Override
    public final String getFirstDayOfNextQuarter() {
        return FIRST_DAY_OF_NEXT_QUARTER;
    }

    @Override
    public final String getFirstDayOfThisYear() {
        return FIRST_DAY_OF_THIS_YEAR;
    }

    @Override
    public final String getFirstDayOfLastYear() {
        return FIRST_DAY_OF_LAST_YEAR;
    }

    @Override
    public final String getFirstDayOfNextYear() {
        return FIRST_DAY_OF_NEXT_YEAR;
    }

    @Override
    public final String getNDaysBefore(int n) {
        return String.format("SUBDATE(CURRENT_DATE, %1$d)",n);
    }

    //SELECT NOW(),CURRENT_TIME,CURRENT_DATE,CURRENT_TIMESTAMP
    @Override
    public final String getNow() {
        return "NOW()";
    }

    @Override
    public final String getCurrentTime(){
        return "CURRENT_TIME";
    }
}
