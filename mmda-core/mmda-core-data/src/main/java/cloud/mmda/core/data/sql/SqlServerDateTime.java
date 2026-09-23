package cloud.mmda.core.data.sql;

public class SqlServerDateTime implements SqlDateTime {

    /**
     * 测试脚本，DATEPART(WEEKDAY,GETDATE())返回1-7
     SELECT CAST(GETDATE() AS DATE) AS TODAY,
     CAST(DATEADD(DAY,-1,GETDATE()) AS DATE) AS YESTERDAY,
     CAST(DATEADD(DAY,+1,GETDATE()) AS DATE) AS TOMORROW,

     CAST(DATEADD(DAY,-DATEPART(WEEKDAY,GETDATE())+1,GETDATE()) AS DATE) AS MONDAY_OF_THIS_WEEK,
     CAST(DATEADD(DAY,-DATEPART(WEEKDAY,GETDATE())-6,GETDATE()) AS DATE) AS MONDAY_OF_LAST_WEEK,
     CAST(DATEADD(DAY,-DATEPART(WEEKDAY,GETDATE())+8,GETDATE()) AS DATE) AS MONDAY_OF_NEXT_WEEK,

     DATEADD(MONTH,DATEDIFF(MONTH,0,GETDATE()),0) AS FIRST_DAY_OF_THIS_MONTH,
     DATEADD(MONTH,DATEDIFF(MONTH,0,GETDATE())-1,0) AS FIRST_DAY_OF_LAST_MONTH,
     DATEADD(MONTH,DATEDIFF(MONTH,0,GETDATE())+1,0) AS FIRST_DAY_OF_NEXT_MONTH,

     DATEADD(QUARTER,DATEDIFF(QUARTER,0,GETDATE()),0) AS FIRST_DAY_OF_THIS_QUARTER,
     DATEADD(QUARTER,DATEDIFF(QUARTER,0,GETDATE())-1,0) AS FIRST_DAY_OF_LAST_QUARTER,
     DATEADD(QUARTER,DATEDIFF(QUARTER,0,GETDATE())+1,0) AS FIRST_DAY_OF_NEXT_QUARTER,

     DATEADD(YEAR,DATEDIFF(YEAR,0,GETDATE()),0) AS FIRST_DAY_OF_THIS_YEAR,
     DATEADD(YEAR,DATEDIFF(YEAR,0,GETDATE())-1,0) AS FIRST_DAY_OF_LAST_YEAR,
     DATEADD(YEAR,DATEDIFF(YEAR,0,GETDATE())+1,0) AS FIRST_DAY_OF_NEXT_YEAR
     */
    public static final String TODAY = "CAST(GETDATE() AS DATE)";
    public static final String YESTERDAY = "CAST(DATEADD(DAY,-1,GETDATE()) AS DATE)";
    public static final String TOMORROW = "CAST(DATEADD(DAY,+1,GETDATE()) AS DATE)";
    public static final String THIS_MONDAY = "CAST(DATEADD(DAY,-DATEPART(WEEKDAY,GETDATE())+1,GETDATE()) AS DATE)";
    public static final String LAST_MONDAY = "CAST(DATEADD(DAY,-DATEPART(WEEKDAY,GETDATE())-6,GETDATE()) AS DATE)";
    public static final String NEXT_MONDAY = "CAST(DATEADD(DAY,-DATEPART(WEEKDAY,GETDATE())+8,GETDATE()) AS DATE)";
    public static final String FIRST_DAY_OF_THIS_MONTH = "DATEADD(MONTH,DATEDIFF(MONTH,0,GETDATE()),0)";
    public static final String FIRST_DAY_OF_LAST_MONTH = "DATEADD(MONTH,DATEDIFF(MONTH,0,GETDATE())-1,0)";
    public static final String FIRST_DAY_OF_NEXT_MONTH = "DATEADD(MONTH,DATEDIFF(MONTH,0,GETDATE())+1,0)";
    public static final String FIRST_DAY_OF_THIS_QUARTER = "DATEADD(QUARTER,DATEDIFF(QUARTER,0,GETDATE()),0)";
    public static final String FIRST_DAY_OF_LAST_QUARTER = "DATEADD(QUARTER,DATEDIFF(QUARTER,0,GETDATE())-1,0)";
    public static final String FIRST_DAY_OF_NEXT_QUARTER = "DATEADD(QUARTER,DATEDIFF(QUARTER,0,GETDATE())+1,0)";
    public static final String FIRST_DAY_OF_THIS_YEAR = "DATEADD(YEAR,DATEDIFF(YEAR,0,GETDATE()),0)";
    public static final String FIRST_DAY_OF_LAST_YEAR = "DATEADD(YEAR,DATEDIFF(YEAR,0,GETDATE())-1,0)";
    public static final String FIRST_DAY_OF_NEXT_YEAR = "DATEADD(YEAR,DATEDIFF(YEAR,0,GETDATE())+1,0)";

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
    public String getNDaysBefore(int n) {
        return String.format("CAST(DATEADD(DAY,-%1$s,GETDATE()) AS DATE)",-n+1);
    }

    @Override
    public String getNow() {
        return "GETDATE()";
    }

    @Override
    public final String getCurrentTime(){
        return "CURRENT_TIME";
    }

    @Override
    public final String getCurrentTimestamp(){ return getNow();}
}
