package cloud.mmda.core.file.excel.converters;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.util.StringUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Converters {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final DataFormatter formatter = new DataFormatter();
    private static final StringToBooleanConverter stringToBooleanConverter = new StringToBooleanConverter();
    private static final DefaultConversionService defaultConversionService = new DefaultConversionService();
    static {
        defaultConversionService.addConverter(new StringToBooleanConverter());
    }
    public static final Boolean StringToBoolean(final String s){
        return stringToBooleanConverter.convert(s);
    }
    public static final double StringToDouble(final String s){
        return StringUtil.isNotBlank(s) ? Double.parseDouble(s) : 0;
    }
    public static final Date StringToDate(final String s)  {
        try{
            return DATE_FORMAT.parse(s);
        }
        catch (ParseException ex){
            return null;
        }
    }
    public static final String getFormattedString(final Cell cell) {
        return formatter.formatCellValue(cell);
    }

    public static final Date DoubleToDate(double v) {
        return new Date((long)v);
    }
}
