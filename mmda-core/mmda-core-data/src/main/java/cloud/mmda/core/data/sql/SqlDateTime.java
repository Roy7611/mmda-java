package cloud.mmda.core.data.sql;

import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaDataType;

public interface SqlDateTime {
    //region 日期表达式
    String getToday();
    String getYesterday();
    String getTomorrow();

    String getThisMonday();
    String getLastMonday();
    String getNextMonday();

    String getFirstDayOfThisMonth();
    String getFirstDayOfLastMonth();
    String getFirstDayOfNextMonth();

    String getFirstDayOfThisQuarter();
    String getFirstDayOfLastQuarter();
    String getFirstDayOfNextQuarter();

    String getFirstDayOfThisYear();
    String getFirstDayOfLastYear();
    String getFirstDayOfNextYear();

    String getNDaysBefore(int n);

    default String between(String colName, String dateStart, String dateEnd){
        return String.format("%1$s>=%2$s AND %1$s<%3$s",colName,dateStart,dateEnd);
    }
    default String on(String colName, String dayExp){
        return String.format("%1$s=%2$s",colName,dayExp);
    }

    default String onToday(MetaCol col){
        if(col.getDataType()== DataType.DATE)
            return on(col.getColName(),getToday());
        return between(col.getColName(),getToday(),getTomorrow());
    }
    default String onYesterday(MetaCol col){
        if(col.getDataType()== DataType.DATE)
            return on(col.getColName(),getYesterday());
        return between(col.getColName(),getYesterday(),getToday());
    }
    default String inThisWeek(MetaCol col){
        return between(col.getColName(), getThisMonday(),getNextMonday());
    }
    default String inLastWeek(MetaCol col){
        return between(col.getColName(), getLastMonday(),getThisMonday());
    }
    default String inLastNDays(MetaCol col, int days){
        return between(col.getColName(), getNDaysBefore(days),getTomorrow());
    }

    default String inThisMonth(MetaCol col){
        return between(col.getColName(), getFirstDayOfThisMonth(),getFirstDayOfNextMonth());
    }
    default String inLastMonth(MetaCol col){
        return between(col.getColName(), getFirstDayOfLastMonth(),getFirstDayOfThisMonth());
    }
    default String inThisQuarter(MetaCol col){
        return between(col.getColName(), getFirstDayOfThisQuarter(),getFirstDayOfNextQuarter());
    }
    default String inLastQuarter(MetaCol col){
        return between(col.getColName(), getFirstDayOfLastQuarter(),getFirstDayOfThisQuarter());
    }
    default String inThisYear(MetaCol col){
        return between(col.getColName(), getFirstDayOfThisYear(),getFirstDayOfNextYear());
    }
    default String inLastYear(MetaCol col){
        return between(col.getColName(), getFirstDayOfLastYear(),getFirstDayOfThisYear());
    }
    default String inEarlier(MetaCol col){
        return String.format("%1$s<%2$s", col.getColName(),getFirstDayOfLastYear());
    }
    //endregion

    //region 时间表达式

    String getCurrentTime();

    //endregion

    //region 日期时间
    String getNow();
    default String getCurrentTimestamp(){
        return "CURRENT_TIMESTAMP";
    }
    //endregion
}
