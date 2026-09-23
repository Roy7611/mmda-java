package cloud.mmda.core.file.excel.bindings;


import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.enums.EnumBitSet;
import cloud.mmda.core.enums.EnumValue;
import cloud.mmda.core.file.excel.converters.Converters;
import cloud.mmda.core.metadata.MetaCol;

import cloud.mmda.core.utils.NameValue;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

/**
 * 绑定单元格
 * <p>
 *     定义了数据源和Excel单元格之间的双向或单向绑定，包括尺寸、风格和读写转换器。
 *     读取模板文件时解析出一系列绑定单元格，调用
 * </p>
 * @param <T> 实体类型
 *
 * @author Roy Luo
 * @version 4.0
 * @since 2022.11 *
 */
public interface BindingCell<T> {
	/**
	 * 名称，简单绑定时是字段名称，表达式时为变量名称
	 * @return
	 */
	String getName();

	/**
	 * 显示标签指绑定单元格左边或上边的标题
	 * @return
	 */
	String getDisplayLabel();
	/**
	 * 单元格所处行位置
	 */
	int getRowIndex();
	/**
	 * 单元格所处列位置
	 */
	int getColumnIndex();

	/**
	 * 单元格风格，包括字体、颜色、边框等设置
	 */
	CellStyle getCellStyle();
	void setCellStyle(CellStyle cellStyle);

	/**
	 * 缺失单元格策略
	 * @return
	 */
	Row.MissingCellPolicy getMissingCellPolicy();

//	ConversionService getConversionService();
	/**
	 * 写入器，从实体T实例获得数据写入单元格并设置style
	 */
	BiConsumer<Cell, T> writer();
	/**
	 * 读取器，从单元格获取数据赋值给实体T实例
	 */
	BiFunction<Cell, T, BindingResult> reader();

	void setRelationValues(List<String> relationValues);

	default boolean hasStyle(){return getCellStyle()!=null;}

	default void readAndValidate(final Cell cell, final T t){
		var result = reader().apply(cell, t);
		if(result != BindingResult.OK) {
			//set cell comment to show error

		}
		setCellStyle(cell.getCellStyle());
	}
	default void readWithStyle(final Cell cell, final T t){
		reader().apply(cell, t);
		setCellStyle(cell.getCellStyle());
	}
	default void read(final Cell cell, final T t){
		reader().apply(cell, t);
	}
	default void write(final Cell cell, final T t){
		setCellStyle(null);
		writer().accept(cell, t);
		if(hasStyle()) cell.setCellStyle(getCellStyle());
	}
	default void writeLabel(final Cell cell, final CellStyle labelStyle){
		cell.setCellValue(getDisplayLabel());
		cell.setCellStyle(labelStyle);
	}

	static Boolean readBoolean(final Cell cell) {
		Objects.requireNonNull(cell, "cell must not be null");
		return switch (cell.getCellType()) {
			case BOOLEAN	-> cell.getBooleanCellValue();
			case STRING		-> Converters.StringToBoolean(cell.getStringCellValue());
			case NUMERIC    -> cell.getNumericCellValue()>0;
			case FORMULA	-> readFormulaValueAsBoolean(cell);
			default			-> null;
		};
	}

	static String readString(final Cell cell) {
		Objects.requireNonNull(cell, "cell must not be null");
		return switch (cell.getCellType()) {
			case STRING		-> cell.getStringCellValue();//cell.getRichStringCellValue().getString();
			case BOOLEAN	-> String.valueOf(cell.getBooleanCellValue());
			case NUMERIC    -> String.valueOf(cell.getNumericCellValue());
			case FORMULA	-> readFormulaValueAsString(cell);
			default			-> "";
		};
	}

	static Double readNumber(final Cell cell) {
		Objects.requireNonNull(cell, "cell must not be null");
		return switch (cell.getCellType()) {
			case NUMERIC    -> cell.getNumericCellValue();
			case STRING		-> Converters.StringToDouble(cell.getStringCellValue());
			case BOOLEAN	-> cell.getBooleanCellValue() ? 1D : 0D;
			case FORMULA	-> readFormulaValueAsDouble(cell);
			default			-> null;
		};
	}

	static Date readDate(final Cell cell)  {
		Objects.requireNonNull(cell, "cell must not be null");
		return switch (cell.getCellType()) {
			case NUMERIC    -> DateUtil.isCellDateFormatted(cell)
								? cell.getDateCellValue()
								: Converters.DoubleToDate(cell.getNumericCellValue()) ;
			case STRING		-> Converters.StringToDate(cell.getStringCellValue());
			case FORMULA	-> readFormulaValueAsDate(cell);
			default			-> null;
		};
	}

	static Integer readEnum(final Cell cell,MetaCol metaCol)  {
		Objects.requireNonNull(cell, "cell must not be null");


		Map<String, NameValue<String, String>> enumMap = metaCol.getEnumMap();

		return switch (cell.getCellType()) {
			case NUMERIC    -> matchEnum(enumMap,String.valueOf(cell.getNumericCellValue()));
			case STRING		-> matchEnum(enumMap,cell.getStringCellValue());
			default			-> null;
		};
	}

	static EnumBitSet readEnumBit(Cell cell, MetaCol metaCol) throws Exception {
		Map<String, NameValue<String, String>> enumMap = metaCol.getEnumMap();
		Class<?> enumClass = Class.forName(metaCol.getMetaEnum().getNamespace().concat(".").concat(metaCol.getMetaEnum().getEnumClass()));
		Class<?> enumSetClass = Class.forName(metaCol.getMetaEnum().getNamespace().concat(".").concat(metaCol.getMetaEnum().getEnumClass()).concat("Set"));

		Constructor<?> constructor = enumSetClass.getDeclaredConstructor();
		constructor.setAccessible(true);
		EnumBitSet enumBitSet = (EnumBitSet)constructor.newInstance();
		String methodName = "add";
		Class<?>[] parameterTypes = { enumClass };
		Method method = enumSetClass.getMethod(methodName, parameterTypes);
		method.setAccessible(true);

		String value = "";
		if(cell.getCellType() == NUMERIC){
			value = String.valueOf(cell.getNumericCellValue());
		}else if(cell.getCellType() == STRING){
			value = cell.getStringCellValue();
		}
		for (String s : value.split(",")) {
			Integer valuei = matchEnum(enumMap,s);
			EnumValue[] enumByKey = (EnumValue[])enumClass.getMethod("values").invoke(enumClass);
			for (EnumValue enumValue : enumByKey) {
				Object invoke = enumValue.getValue();
				if(String.valueOf(valuei).equals(String.valueOf(invoke))){
					method.invoke(enumBitSet,enumValue);
				}
			}
		}
		return enumBitSet;
	}


	static Integer matchEnum(Map<String, NameValue<String, String>> enumMap,String name){
		for (String key: enumMap.keySet()){
			NameValue<String, String> stringStringNameValue = enumMap.get(key);
			if(stringStringNameValue.getValue().equals(name)) return Integer.valueOf(key);
		}
		return null;
	}

	static String readFormulaValueAsString(final Cell cell) {
		Objects.requireNonNull(cell, "cell must not be null");
		return switch (cell.getCachedFormulaResultType()) {
			case BOOLEAN 	-> String.valueOf(cell.getBooleanCellValue());
			case NUMERIC 	-> String.valueOf(cell.getNumericCellValue());
			case STRING	 	-> cell.getRichStringCellValue().getString();
			default 	 	-> "";
		};
	}

	static Boolean readFormulaValueAsBoolean(final Cell cell) {
		Objects.requireNonNull(cell, "cell must not be null");
		return switch (cell.getCachedFormulaResultType()) {
			case BOOLEAN 	-> cell.getBooleanCellValue();
			case NUMERIC 	-> cell.getNumericCellValue()>0;
			case STRING	 	-> Converters.StringToBoolean(cell.getRichStringCellValue().getString());
			default 	 	-> null;
		};
	}

	static Double readFormulaValueAsDouble(final Cell cell) {
		Objects.requireNonNull(cell, "cell must not be null");
		return switch (cell.getCellType() != CellType.FORMULA ? cell.getCellType():cell.getCachedFormulaResultType()) {
			case NUMERIC 	-> cell.getNumericCellValue();
			case BOOLEAN 	-> cell.getBooleanCellValue() ? 1.0D : 0.0D;
			case STRING	 	-> Converters.StringToDouble(cell.getStringCellValue());
			default 	 	-> null;
		};
	}
	static Date readFormulaValueAsDate(final Cell cell) {
		Objects.requireNonNull(cell, "cell must not be null");
		return switch (cell.getCachedFormulaResultType()) {
			case NUMERIC    -> DateUtil.isCellDateFormatted(cell)
								? cell.getDateCellValue()
								: Converters.DoubleToDate(cell.getNumericCellValue()) ;
			case STRING		-> Converters.StringToDate(cell.getStringCellValue());
			default 	 	-> null;
		};
	}

	static void writeBoolean(final Cell cell, final Object value) {
		if(value == null) cell.setBlank();
		else cell.setCellValue((boolean)value);
	}
	static void writeString(final Cell cell, final Object value) {
		if(value == null) cell.setBlank();
		else cell.setCellValue(String.valueOf(value));
	}
	static void writeDate(final Cell cell, final Object value) {
		if(value == null) cell.setBlank();
		else cell.setCellValue((Date) value);
	}
	static void writeDouble(final Cell cell, final Object value) {
		if(value == null) cell.setBlank();
		else cell.setCellValue((Double) value);
	}

	static Class<?> requiredType(final MetaCol metaCol){
		if(DataType.BOOL == metaCol.getDataType()){
			return Boolean.class;
		}
		else if(metaCol.getDataType().isDateOrTime()){
			return Double.class;
		}
		else if(metaCol.getDataType().isNumber()){
			return Double.class;
		}
		else{
			return String.class;
		}
	}

	static CellType requiredCellType(final MetaCol metaCol){
		if(DataType.BOOL == metaCol.getDataType()){
			return CellType.BOOLEAN;
		}
		else if(metaCol.getDataType().hasDatePart()){
			return NUMERIC;
		}
		else if(metaCol.getDataType().isNumber()){
			return NUMERIC;
		}
		else{
			return STRING;
		}
	}

	//TODO
//	static <T,K> Optional<BindingCell<T>> from(final Cell cell, final Repository<T,K> repository) {
//		if(cell == null || cell.getCellType() == CellType.BLANK) return Optional.empty();
//		var cellValue = cell.getStringCellValue();
//		if(cellValue == null || StringUtil.isBlank(cellValue)) return Optional.empty();
//
//		var metaObj = repository.getMetaObject();
//		if(cellValue.charAt(0) == '#'){
//			var bindingPath = cellValue.substring(1);
//			if(bindingPath.indexOf('.') != -1) {
//				var segments = bindingPath.split("\\.");
//			}
//			else{
//				var metaCol = metaObj.getCol(bindingPath);
//				if(metaCol == null) {
////					logger.warn("Column '" + colName + "' doesn't exists in " + metaObj.getObjName());
//					return Optional.empty();
//				}
//			}
//
//		}
//	}
}
