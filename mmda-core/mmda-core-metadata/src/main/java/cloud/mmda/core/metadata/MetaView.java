package cloud.mmda.core.metadata;

import cloud.mmda.core.enums.ExtensionType;
import cloud.mmda.core.utils.BaseUtil;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 元视图，由多个元对象通过元关系组成，这样允许程序临时创建视图，作为生成查询语句的上下文。
 *
 * 构建元视图时尽可能采用{@link MetaView.Builder}
 */
public class MetaView extends MetaObject implements MetaContext {
    private MetaObject t;
    @Getter
    private LinkedHashMap<MetaRelation, MetaObject> relatives;

    @Getter @Setter
    private String whereCondition;

    @Getter @Setter
    private String orderBy;

    private MetaView(MetaObject t, LinkedHashMap<MetaRelation, MetaObject> relatives) {
        this.t = t;
        this.relatives = relatives;

        setNameSpace(t.getNameSpace());
        setObjType("V");

        setPartitionKey(t.getPartitionKey());
        setPartitioned(t.isPartitioned());
        setMinID(t.getMinID());
        setMaxID(t.getMaxID());

        setThumbnailCol(t.getThumbnailCol());
        setNameCol(t.getNameCol());
        setParentIdCol(t.getParentIdCol());
        setGroupByCol(t.getGroupByCol());
        setUniqueKey(t.getUniqueKey());

        setExtendType(ExtensionType.EXTENDS);
        setSuperName(t.getObjName());
    }

    @Override
    public MetaObject get() {
        return t;
    }

    /**
     * 元视图构建器
     */
    public static class Builder implements MetaContext {
        private MetaObject.Builder objBuilder;
        private LinkedHashMap<MetaRelation, MetaObject> relatives;
        private String viewName, displayLabel;
        private Map<String, MetaCol> colAliasMap;
        private String whereCondition;
        private String orderBy;
        /**
         * 构造一个元视图构建器，指定第一个元对象，别名{@code t}
         * @param dbSchema 数据库模式名称
         * @param objName 第一个元对象名称
         */
        public Builder(String dbSchema, String objName){
            objBuilder = new MetaObject.Builder(dbSchema, objName);
            relatives = new LinkedHashMap<>();
            colAliasMap = new LinkedHashMap<>();
        }

        /**
         * 构造一个元视图构建器，指定第一个元对象，别名{@code t}
         * @param metaObject 第一个元对象
         */
        public Builder(MetaObject metaObject){
            objBuilder = new MetaObject.Builder(metaObject);
            relatives = new LinkedHashMap<>();
        }

        @Override
        public MetaObject get() {
            return objBuilder.get();
        }

        @Override
        public LinkedHashMap<MetaRelation, MetaObject> getRelatives() {
            return relatives;
        }

        public Builder withName(String viewName, String displayLabel){
            this.viewName = viewName;
            this.displayLabel = displayLabel;
            return this;
        }

        public Builder hasOne(String joinType, MetaObject relativeObj, String as, String joinOn){
            var metaRelation = new MetaRelation.Builder(objBuilder)
                    .withOne(as)
                    .join(joinType, relativeObj, joinOn)
                    .get();
            relatives.put(metaRelation, relativeObj);
            return this;
        }

        public Builder hasMany(MetaObject relativeObj, String as, String joinOn){
            var metaRelation = new MetaRelation.Builder(objBuilder)
                    .withMany(as)
                    .join(MetaRelation.INNER_JOIN, relativeObj, joinOn)
                    .get();
            relatives.put(metaRelation, relativeObj);
            return this;
        }

        /**
         * 添加列和别名
         * @param colAs t.orderDate AS od
         */
        private void addColAs(String colAs){
            if(colAs.indexOf('*') != -1){
                //Not AS clause in this case
                if(colAs.indexOf('.') != -1){
                    //SELECT t.*
                    var alias = colAs.substring(0,colAs.indexOf('.'));
                    var metaObj = findObject(alias.trim());
                    if(metaObj != null){
                        metaObj.get().getCols().stream()
                                .forEach(col -> colAliasMap.putIfAbsent(col.getColName(),col));
                    }
                }
                else{
                    //SELECT *
                    selectAll();
                }
                return;
            }
            if(colAs.indexOf(" AS ") != -1){
                var colAlias = colAs.split(" AS ");
                var col = findCol(colAlias[0].trim());
                if(col.isPresent()){
                    colAliasMap.put(colAlias[1], col.get());
                }
            }
            else{
                var col = findCol(colAs.trim());
                if(col.isPresent()){
                    colAliasMap.put(col.get().getColName(),col.get());
                }
            }
        }

        /**
         * 选择列
         * @param colNames 列名，支持 t.col1 AS a
         * @return 自身
         */
        public Builder select(String...colNames){
            for(String colName : colNames){
                addColAs(colName);
            }
            return this;
        }

        /**
         * 选择所有列，重名的列不会加入，我们偏向左连接，建立视图的时候用最明细的表作为主对象
         * @return 自身
         */
        public Builder selectAll(){
            get().getCols().stream().forEach(col -> colAliasMap.put(col.getColName(), col));
            relatives.values().stream().forEach(r ->{
                for(var col : r.getCols()){
                    colAliasMap.putIfAbsent(col.getColName(), col);
                }
            });

            return this;
        }

        public Builder orderBy(String orderBy){
            this.orderBy = orderBy;
            return this;
        }
        public Builder where(String whereCondition){
            this.whereCondition = whereCondition;
            return this;
        }

        public MetaView build(){
            var v = new MetaView(get(), relatives);
            v.setObjName(viewName);
            v.setDisplayLabel(displayLabel);

            if(colAliasMap.isEmpty()){
                selectAll();
            }
            v.setNamedCols(colAliasMap);
            return v;
        }
    }
}
