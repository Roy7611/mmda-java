package cloud.mmda.core.file.excel.bindings;

import cloud.mmda.core.data.jdbc.repository.Repository;
import cloud.mmda.core.entities.EntityState;
import cloud.mmda.core.entities.SequencedRow;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.file.util.ReflectionUtil;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.metadata.MetaRelation;
import io.micrometer.common.util.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BindingName <T extends SequencedRow, K> {
    protected final Repository<T, K> repository;
    private final MetaObject metaObj;
    private final int tenantID;
    private final List<BindingCell<T>> bindingCells;
    private final boolean hasMany;
    private final String HasMayName;


    public BindingName(final Repository<T, K> repository, List<BindingCell<T>> bindingCells, final int tenantID, final boolean hasMany, final String HasMayName) {
        this.repository = repository;
        this.metaObj = repository.getMetaObject();
        this.tenantID = tenantID;
        this.bindingCells = bindingCells;
        this.hasMany = hasMany;
        this.HasMayName = HasMayName;
    }

    public void writeLabel(Sheet sheet) {
        for (BindingCell<T> bindingCell : this.bindingCells) {
            bindingCell.writer().accept(null,null);
        }
    }

    public void reader(Sheet sheet, T t) throws Exception {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setRootObject(t);
        List<Object> objects = new ArrayList<>();
        List<Integer> ints = new ArrayList<>();
        if(hasMany){
            parser.parseExpression(HasMayName).setValue(t, objects);
        }else {
            parser.parseExpression("customProperties").setValue(t, new HashMap<>());
            for (MetaRelation relation : repository.getMetaObject().getRelations()) {
                String relativeDbName = relation.getRelativeDbSchema();
                MetaObject metaObject = repository.getMetaObject(StringUtils.isNotEmpty(relativeDbName) ? relativeDbName : relation.getDbSchema(), relation.getRelativeObjName());
                if(relation.getRelationType() == MetaRelationType.HAS_ONE){
                    String colName = relation.getJoinOn().split("@")[1];
                    MetaCol col = repository.getMetaObject().getCol(colName);
                    String[] split = col.getEnumSet().split(" AS ");
                    String hasOneName = "";
                     if(split.length == 2){
                         Pattern enumSetPattern = Pattern.compile("^(?<relationType>\\w+)\\s+(?<relativeObj>\\w+(\\.\\w+)?)\\((?<relativeCols>[\\w|,]+)\\)(\\s+AS\\s+(?<relationName>\\w+))?(\\s+WHERE\\s*\\((?<where>.+)\\))?(\\s+GROUP\\s+BY\\s+(?<groupBy>\\w+))?(\\s+(?<readOnly>READONLY))?$");
                         Matcher matcher = enumSetPattern.matcher(col.getEnumSet());
                         if(matcher.matches()){
                             hasOneName = matcher.group("relationName");
                         }
                     }else {
                         String objName = metaObject.getObjName();
                         hasOneName = Character.toLowerCase(objName.charAt(0)) + objName.substring(1);
                     }

                    Object obj = initObjByRelation(repository, relation.getRelationName());
                     try {
                         parser.parseExpression(hasOneName).setValue(t, obj);
                     }catch (Exception e){
                         e.printStackTrace();
                     }
                }
            }
        }

        int rowNum = -1;
        for (BindingCell<T> bindingCell : this.bindingCells) {
            Cell cell = sheet.getRow(bindingCell.getRowIndex()).getCell(bindingCell.getColumnIndex());
            if(hasMany && !ints.contains(bindingCell.getRowIndex())){
                int rowIndex = bindingCell.getRowIndex();
                objects = (List<Object>)parser.parseExpression(HasMayName).getValue(t);
                Object newObj = initObjByRelation(repository, HasMayName);
                ReflectionUtil.setT(newObj,"rowNum",(long)(objects.size()+1));
                try {
                    ReflectionUtil.setT(newObj,"entityState", EntityState.CREATED);
                    ExpressionParser parser1 = new SpelExpressionParser();
                    parser1.parseExpression("customProperties").setValue(newObj, new HashMap<>());
                    parser1.parseExpression("customProperties[$excelRowNum]").setValue(newObj, bindingCell.getRowIndex()+1);
                }catch (Exception e){

                }
                objects.add(newObj);
                parser.parseExpression(HasMayName).setValue(t, objects);
            }else {
                if(rowNum == -1){
                    try {
                        rowNum = cell.getRowIndex();
                        ExpressionParser parser1 = new SpelExpressionParser();
                        parser1.parseExpression("customProperties[$excelRowNum]").setValue(t, rowNum+1);
                    }catch (Exception e){

                    }

                }
            }

            ints.add(bindingCell.getRowIndex());
            bindingCell.reader().apply(cell,t);
        }
    }

    public Object initObjByRelation(Repository repository,String relationName) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        MetaRelation relation = repository.getMetaObject().getRelation(relationName);
        String relativeDbName = relation.getRelativeDbSchema();
        MetaObject metaObject = repository.getMetaObject(StringUtils.isNotEmpty(relativeDbName) ? relativeDbName : relation.getDbSchema(), relation.getRelativeObjName());
        Class<?> aClass = Class.forName(metaObject.getNameSpace().concat(".").concat(metaObject.getObjName()));
        return aClass.getDeclaredConstructor().newInstance();
    }
}
