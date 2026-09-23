package cloud.mmda.core.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static java.time.temporal.IsoFields.QUARTER_OF_YEAR;

public class DateRange {
    private final LocalDate begin;
    private final LocalDate end;

    private DateRange(LocalDate begin, LocalDate end){
        Objects.requireNonNull(begin,"begin");
        Objects.requireNonNull(end,"end");
        this.begin = begin;
        this.end = end;
    }

    public final LocalDate getBegin(){
        return this.begin;
    }
    public final LocalDate getEnd(){
        return this.end;
    }
    public final int toDays(){
        return Period.between(begin, end).getDays();
    }
    public final DateTimeRange toDateTimeRange(){
        return DateTimeRange.of(DateTimeUtil.toBeginTime(begin), DateTimeUtil.toEndTime(end));
    }

    @Override
    public String toString() {
        return begin + " ~ " + end;
    }

    public static final DateRange of(LocalDate begin, LocalDate end){
        return new DateRange(begin,end);
    }
    public static final DateRange valueOf(String v){
        String[] values = v.split(" ~ ");
        return new DateRange(LocalDate.parse(values[0]),LocalDate.parse(values[1]));
    }
    public static final DateRange onDay(LocalDate date){
        return new DateRange(date,date);
    }
    public static final DateRange onToday(){
        return onDay(LocalDate.now());
    }
    public static final DateRange onYesterday(){
        return onDay(LocalDate.now().minusDays(1L));
    }
    public static final DateRange onTomorrow(){
        return onDay(LocalDate.now().plusDays(1L));
    }
    public static final DateRange inTheWeek(LocalDate date, DayOfWeek firstDayOfWeek){
        LocalDate begin = date.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
        LocalDate end = date.with(TemporalAdjusters.nextOrSame(DateTimeUtil.getLastDayOfWeek(firstDayOfWeek)));
        return new DateRange(begin,end);
    }
    public static final DateRange inTheWeek(LocalDate date){
        return inTheWeek(date,DayOfWeek.MONDAY);
    }
    public static final DateRange inThisWeek(){
        return inTheWeek(LocalDate.now());
    }
    public static final DateRange inLastWeek(){
        return inTheWeek(LocalDate.now().minusWeeks(1L));
    }
    public static final DateRange inNextWeek(){
        return inTheWeek(LocalDate.now().plusWeeks(1L));
    }
    public static final DateRange inLastNDays(long days){
        LocalDate today = LocalDate.now();
        return new DateRange(today.minusDays(days-1),today);
    }
    public static final DateRange inTheMonth(LocalDate date){
        LocalDate begin = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = date.with(TemporalAdjusters.lastDayOfMonth());
        return new DateRange(begin,end);
    }

    public static final DateRange inThisMonth(){
        return inTheMonth(LocalDate.now());
    }
    public static final DateRange inLastMonth(){
        return inTheMonth(LocalDate.now().minusMonths(1L));
    }
    public static final DateRange inNextMonth(){
        return inTheMonth(LocalDate.now().plusMonths(1L));
    }
    public static final DateRange inTheQuarter(LocalDate date){
        /*
        LocalDate firstDayOfQuarter = date.with(today.getMonth().firstMonthOfQuarter())
                .with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfQuarter = firstDayOfQuarter.plusMonths(2)
                .with(TemporalAdjusters.lastDayOfMonth());
        return new DateRange(firstDayOfQuarter,lastDayOfQuarter);
        */
        int year = date.getYear();
        int quarter = date.get(QUARTER_OF_YEAR);
        LocalDate begin = YearMonth.of(year, 1)      // January of given year
                .with(QUARTER_OF_YEAR, quarter)             // becomes first month of given quarter
                .atDay(1);                      // becomes first day of given quarter
        LocalDate end = YearMonth.of(year, 3)        // March of given year
                .with(QUARTER_OF_YEAR, quarter)             // becomes 3rd (last) month of given quarter
                .atEndOfMonth();                            // becomes last day of given quarter
        return new DateRange(begin,end);
    }
    public static final DateRange inThisQuarter(){
        return inTheQuarter(LocalDate.now());
    }
    public static final DateRange inLastQuarter(){
        return inTheQuarter(LocalDate.now().minusMonths(3L));
    }
    public static final DateRange inNextQuarter(){
        return inTheQuarter(LocalDate.now().plusMonths(3L));
    }

    public static final DateRange inTheYear(LocalDate date){
        LocalDate begin = date.with(TemporalAdjusters.firstDayOfYear());
        LocalDate end = date.with(TemporalAdjusters.lastDayOfYear());
        return new DateRange(begin,end);
    }
    public static final DateRange inThisYear(){
        return inTheYear(LocalDate.now());
    }
    public static final DateRange inLastYear(){
        return inTheYear(LocalDate.now().minusYears(1L));
    }
    public static final DateRange inNextYear(){
        return inTheYear(LocalDate.now().plusYears(1L));
    }
    public static final DateRange inEarlier(){
        LocalDate end = LocalDate.now().with(TemporalAdjusters.lastDayOfYear());
        return new DateRange(LocalDate.of(1900,1,1),end);
    }

    private static final Map<String, Supplier<DateRange>> dateRangers = new LinkedHashMap<String, Supplier<DateRange>>(){
        {
            put(DateRangeKind.TODAY.name(), ()->onToday());
            put(DateRangeKind.YESTERDAY.name(), ()->onYesterday());
            put(DateRangeKind.THIS_WEEK.name(), ()->inThisWeek());
            put(DateRangeKind.LAST_WEEK.name(), ()->inLastWeek());
            put(DateRangeKind.LAST_7_DAYS.name(), ()->inLastNDays(7L));
            put(DateRangeKind.THIS_MONTH.name(), ()->inThisMonth());
            put(DateRangeKind.LAST_MONTH.name(), ()->inLastMonth());
            put(DateRangeKind.LAST_30_DAYS.name(), ()->inLastNDays(30L));
            put(DateRangeKind.THIS_QUARTER.name(), ()->inThisQuarter());
            put(DateRangeKind.LAST_QUARTER.name(), ()->inLastQuarter());
            put(DateRangeKind.THIS_YEAR.name(), ()->inThisYear());
            put(DateRangeKind.LAST_YEAR.name(), ()->inLastYear());
            put(DateRangeKind.EARLIER.name(), ()->inEarlier());
        }
    };
    public static final DateRange kindOf(String dateRangeKind){
        return dateRangers.get(dateRangeKind).get();
    }
    public static final DateRange kindOf(DateRangeKind dateRangeKind){
        return dateRangers.get(dateRangeKind.name()).get();
    }
    public static final boolean hasKind(String kind){
        return dateRangers.containsKey(kind);
    }
    public static final boolean hasAnyKind(String value){
        Objects.requireNonNull(value);
        boolean found = false;
        for(String kind : dateRangers.keySet()){
            if(value.indexOf(kind)!=-1) {
                found = true;
                break;
            }
        }
        return found;
    }
}
