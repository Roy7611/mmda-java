package cloud.mmda.core.data.sql;

public class OracleDateTime implements SqlDateTime {

    /**
     测试脚本 WEEKDAY(CURRENT_DATE) 返回0-6，DAYOFWEEK(CURRENT_DATE)返回1-7
     SELECT CURRENT_DATE AS TODAY,
     SUBDATE(CURRENT_DATE, 1) AS YESTERDAY,
     ADDDATE(CURRENT_DATE, 1) AS TOMORROW,

     SUBDATE(CURRENT_DATE, WEEKDAY(CURRENT_DATE)) AS MONDAY_OF_THIS_WEEK,
     SUBDATE(CURRENT_DATE, WEEKDAY(CURRENT_DATE)+7) AS MONDAY_OF_LAST_WEEK,
     ADDDATE(CURRENT_DATE, 7-WEEKDAY(CURRENT_DATE)) AS MONDAY_OF_NEXT_WEEK,

     SUBDATE(CURRENT_DATE, DAY(CURRENT_DATE)-1) AS FIRST_DAY_OF_THIS_MONTH,
     SUBDATE(DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH), DAY(CURRENT_DATE)-1) AS FIRST_DAY_OF_LAST_MONTH,
     ADDDATE(LAST_DAY(CURRENT_DATE), 1) AS FIRST_DAY_OF_NEXT_MONTH,

     MAKEDATE(YEAR(CURRENT_DATE), 1)+INTERVAL QUARTER(CURRENT_DATE)-1 QUARTER AS FIRST_DAY_OF_THIS_QUARTER,
     MAKEDATE(YEAR(CURRENT_DATE), 1)+INTERVAL QUARTER(CURRENT_DATE)-2 QUARTER AS FIRST_DAY_OF_LAST_QUARTER,
     MAKEDATE(YEAR(CURRENT_DATE), 1)+INTERVAL QUARTER(CURRENT_DATE) QUARTER AS FIRST_DAY_OF_NEXT_QUARTER,

     MAKEDATE(YEAR(CURRENT_DATE), 1) AS FIRST_DAY_OF_THIS_YEAR,
     MAKEDATE(YEAR(CURRENT_DATE)-1, 1) AS FIRST_DAY_OF_LAST_YEAR,
     MAKEDATE(YEAR(CURRENT_DATE)+1, 1) AS FIRST_DAY_OF_NEXT_YEAR
     */
    public static final String TODAY = "CURRENT_DATE";
    public static final String YESTERDAY = "DATE_ADD(CURRENT_DATE(),INTERVAL -1 DAYS)";
    public static final String TOMORROW = "DATE_ADD(CURRENT_DATE(),INTERVAL 1 DAYS)";
    public static final String THIS_MONDAY = "DATE_ADD(WEEK,DATEDIFF(WEEK,0,GETDATE()),0)";
    public static final String LAST_MONDAY = "DATEADD(WEEK,DATEDIFF(WEEK,0,GETDATE())-1,0)";
    public static final String NEXT_MONDAY = "DATEADD(WEEK,DATEDIFF(WEEK,0,GETDATE())+1,0)";
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
