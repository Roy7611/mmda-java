package cloud.mmda.core.file.excel.bindings;

import cloud.mmda.core.data.jdbc.repository.Repository;
import cloud.mmda.core.file.excel.WorkbookStyleRegistry;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.metadata.MetaRelation;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 绑定Sheet
 * @see <a href="https://poi.apache.org/components/spreadsheet/quick-guide.html#NamedRanges">参考命名区域</a>
 *
 * @param <T>
 * @param <K>
 */
public class BindingSheet<T,K> {
    private static final Logger logger = Logger.getLogger(BindingSheet.class.getName());
    private final Repository<T,K> repository;
    private final MetaObject metaObj;
    private final List<BindingCell<T>> bindingCells = new ArrayList();
    private final List<BindingRow<?,?>> bindingRows = new ArrayList();

    public BindingSheet(final Repository<T,K> repository){
        this.repository = repository;
        this.metaObj = repository.getMetaObject();
    }

    private <U,V> void parseSubTable(final Sheet sheet, final MetaRelation relation) {
        var wb = sheet.getWorkbook();
        var namedArea = wb.getName(relation.getRelationName());
        if (namedArea == null) return;

        AreaReference aref = new AreaReference(namedArea.getRefersToFormula(), SpreadsheetVersion.EXCEL2007);
        for(var refCell : aref.getAllReferencedCells()){
            var cell = wb.getSheet(refCell.getSheetName())
                    .getRow(refCell.getRow())
                    .getCell(refCell.getCol());

        }
        var firstCell = aref.getFirstCell();
        var lastCell = aref.getLastCell();

    }
    public void parse(final Sheet sheet){
        int startRow = sheet.getFirstRowNum();
        var styleRegistry=new WorkbookStyleRegistry(sheet.getWorkbook());
        if(startRow == -1) return;
        var builder = SimpleBindingCell.builder(repository.getMetaObjectAccess(),styleRegistry);
        for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
            var row = sheet.getRow(i);
            int startCol = row.getFirstCellNum();
            for (int j = startCol; j <= row.getLastCellNum(); j++) {
                var cell = row.getCell(j);
                if(cell == null || cell.getCellType() == CellType.BLANK) continue;

                var cellValue = cell.getStringCellValue();
                if(StringUtil.isBlank(cellValue) || cellValue.charAt(0) != '#') continue;
                var fieldName = cellValue.substring(1);
                if(fieldName.indexOf('.') == -1) {
                    //simple field
                    var metaCol = metaObj.getCol(fieldName);
                    if(metaCol == null) {
                        logger.warning("Invalid field '" + fieldName + "' in sheet '" + sheet.getSheetName() + "' at " + cell.getAddress().formatAsR1C1String());
                        continue;
                    }
                    var bindingCell = builder.metaCol(metaCol).cell(cell).build();
                    bindingCells.add(bindingCell);
                }
                else{
                    //items field such as items.materialCode

                }
            }
        };

    }

}
