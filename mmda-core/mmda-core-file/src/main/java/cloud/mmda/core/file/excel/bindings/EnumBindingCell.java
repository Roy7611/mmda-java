package cloud.mmda.core.file.excel.bindings;

import cloud.mmda.core.enums.EnumBitSet;
import cloud.mmda.core.enums.EnumValue;
import cloud.mmda.core.file.excel.WorkbookStyleRegistry;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaObjectAccess;
import cloud.mmda.core.utils.NameValue;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class EnumBindingCell<T,K> extends SimpleBindingCell<T,K> {
    public EnumBindingCell(MetaObjectAccess<T, K> metaObjAccess, MetaCol metaCol, WorkbookStyleRegistry registry) {
        super(metaObjAccess, metaCol, registry);
    }

    @Override
    public BiConsumer<Cell, T> writer() {
        return (cell, t) -> {
            Map<String, NameValue<String, String>> enumMap = super.metaCol.getEnumMap();
            Object obj = getProperty(t);
            if(obj == null)return;
            if(obj instanceof EnumBitSet){
                EnumBitSet<?> enumBitSet = (EnumBitSet<?>) obj;
                List<String> list = enumBitSet.getEnumSet().stream().map(Enum::name).toList();

                List<String> values = enumMap.values().stream()
                        .filter(stringStringNameValue -> list.contains(String.valueOf(stringStringNameValue).split("=")[0]))
                        .map(stringStringNameValue -> String.valueOf(stringStringNameValue).split("=")[1]).toList();
                String valueStr = String.join(",", values);


                cell.setCellValue(valueStr);
            }else if(obj instanceof EnumValue){
                Object value = ((EnumValue<?>)obj).getValue();
                NameValue<String, String> stringStringNameValue = enumMap.get(String.valueOf(value));
                if(stringStringNameValue != null){
                    cell.setCellValue(stringStringNameValue.getValue());
                }

            }


            if(hasStyle()) cell.setCellStyle(getCellStyle());
        };
    }

    @Override
    public BiFunction<Cell, T, BindingResult> reader() {
        return (cell, t) -> {

            try {

                Class<?> enumClass = Class.forName(metaCol.getMetaEnum().getNamespace().concat(".").concat(metaCol.getMetaEnum().getEnumClass()));
                if(metaCol.isBitSet()){
                    EnumBitSet enumBitSet = BindingCell.readEnumBit(cell,metaCol);
                    setProperty(t,enumBitSet);
                }else{
                    var value = BindingCell.readEnum(cell,metaCol);
                    EnumValue[] enumByKey = (EnumValue[])enumClass.getMethod("values").invoke(enumClass);
                    for (EnumValue enumValue : enumByKey) {
                        Object invoke = enumValue.getValue();
                        if(String.valueOf(value).equals(String.valueOf(invoke))){
                            setProperty(t,enumValue);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return BindingResult.OK;
        };
    }

}
