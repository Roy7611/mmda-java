package cloud.mmda.core.file.excel.bindings;

import cloud.mmda.core.entities.Entity;
import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.file.excel.WorkbookStyleRegistry;
import cloud.mmda.core.file.util.ExcelFileUtil;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaDataType;
import cloud.mmda.core.metadata.MetaObjectAccess;
import cloud.mmda.core.metadata.MetaRelation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.util.StringUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@Slf4j
public class StringBindingCell<T,K> extends SimpleBindingCell<T,K> {

    public StringBindingCell(MetaObjectAccess<T, K> metaObjAccess, MetaCol metaCol, WorkbookStyleRegistry styleRegistry) {
        super(metaObjAccess, metaCol, styleRegistry);
    }

    @Override
    public BiConsumer<Cell, T> writer() {
        return (cell, t) -> {
            Object property = getProperty(t);
            if (property == null) return;
            String value = String.valueOf(property);
            var relationType = super.metaCol.getRelationType();
            List<String> picList = List.of("pic","pica","picb","logo");
            if (relationType.hasOneOrRef()) {
                String valueByOption = getValueByOption(cell, value);
                cell.setCellValue(valueByOption);
            }else if (t instanceof Entity o && o.getRefProperty(metaCol.getColName())!=null){
                cell.setCellValue(o.getRefProperty(metaCol.getColName()));
            }
            else if ((picList.stream().anyMatch(pic -> super.metaCol.getColName().toLowerCase().endsWith(pic)) ||
                    super.metaCol.getColName().equalsIgnoreCase("avatar") ||
                    super.metaCol.getColName().equalsIgnoreCase("certificate"))
                    && StringUtil.isNotBlank(value) && value.startsWith("http")) {
                writePic(cell, value);
            } else if (super.metaCol.getColName().contains("Rate")) {
                try {
                    cell.setCellValue(new BigDecimal(value).multiply(new BigDecimal(100)).toString());
                } catch (Exception e) {
                    cell.setCellValue(value);
                }
            } else {
                cell.setCellValue(value);
            }

            if (hasStyle()) cell.setCellStyle(getCellStyle());
        };
    }

    private static void writePic(Cell cell, String value) {
        try {
//            File fileByUrl = ExcelFileUtil.getFileByUrl(value);
//            if(fileByUrl == null) {
//                cell.setCellValue(value);
//            }else {
//                ExcelFileUtil.setExcelPic(cell, fileByUrl);
//
//            }
            ExcelFileUtil.setExcelPics(cell,value);
        } catch (Exception e) {
            log.info(value +"文件下载失败 error:"+e.getMessage());
        }
    }



    @Override
    public BiFunction<Cell, T, BindingResult> reader() {
        return (cell, t) -> {
            String value = BindingCell.readString(cell);
            if(metaCol.getDataType() == DataType.TIME){
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                try {
                    Time time = new Time(sdf.parse(value).getTime());
                    setProperty(t,time);
                }catch (Exception e){
                }
            }else if(super.metaCol.getColName().contains("Rate")){
                try {
                    value = new BigDecimal(value).divide(new BigDecimal(100), 4, RoundingMode.HALF_UP).toString();
                    setProperty(t, value);
                }catch (Exception e){
                    setProperty(t,value);
                }
            }else {
                setProperty(t,value);
            }
            return BindingResult.OK;
        };
    }

}
