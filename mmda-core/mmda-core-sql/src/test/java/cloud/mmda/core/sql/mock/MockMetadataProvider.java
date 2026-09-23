package cloud.mmda.core.sql.mock;

import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.enums.ModuleType;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.metadata.Module;

import javax.swing.text.html.parser.Entity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class MockMetadataProvider implements MetadataProvider {
    private MetaObject metaPartner = new MetaObject.Builder("crm","Partner")
            .withBigIdCol("partnerID").add()
            .withNoCol("partnerNo", 15).add()
            .withVarcharCol("partnerName", 100, false).add()
            .withIntCol("orderCount", false).withDefaultValue("0").add()
            .withPartition("partnerID", false)
            .get();
    private MetaObject metaOrder = new MetaObject.Builder("crm", "Order")
            .withBigIdCol("orderID").add()
            .withNoCol("orderNo", 30).add()
            .withVarcharCol("orderName", 255, false).add()
            .withDecimalCol("orderAmount", 19,4,true).add()
            .withBigIntCol("partnerID", false).add()
            .withPartition("orderID", false)
            .get();
    private MetaObject metaUser = new MetaObject.Builder("base","User")
            .withBigIdCol("userID").add()
            .withVarcharCol("username", 100, false).add()
            .withVarcharCol("mobile", 30, false).add()
            .withVarcharCol("email", 255, false).add()
            .withIntCol("status", false).withDefaultValue("0").add()
            .withBoolCol("active", false).add()
            .withPartition("userID", false)
            .get();

    @Override
    public List<MetaObject> findMetaObjects(Paginator pager) {
        return List.of();
    }

    @Override
    public List<MetaObject> findMetaObjects(String dbSchema, Paginator pager) {
        return List.of();
    }

    @Override
    public MetaDataType getMetadataType(Integer dataType) {
        return null;
    }

    @Override
    public Module getModule(String moduleCode, final String locale) {
        return null;
    }

    @Override
    public Module getModule(MetaObject metaObj, final String locale) {
        return null;
    }

    @Override
    public List<Module> getModules(String systemCode, ModuleType moduleType) {
        return List.of();
    }


    @Override
    public <T> MetaObject getMetaObject(Class<T> objClass) {
        if(objClass.equals(Partner.class)) return metaPartner;
        if(objClass.equals(Order.class)) return metaOrder;
        if(objClass.equals(User.class)) return metaUser;
        throw new IllegalArgumentException("Unknown class " + objClass);
    }



    @Override
    public MetaObject getMetaObject(String objName) {
        if("Partner".equals(objName)) return metaPartner;
        if("Order".equals(objName)) return metaOrder;
        if("User".equals(objName)) return metaUser;
        throw new IllegalArgumentException("Unknown class " + objName);
    }

    @Override
    public MetaObject getMetaObject(String dbSchema, String objName) {
        return getMetaObject(objName);
    }

    @Override
    public MetaEnum getMetaEnum(String enumClassName) {
        return null;
    }

    @Override
    public List<MetaEnum> getMetaEnums() {
        return List.of();
    }

    @Override
    public Collection<MetaIndex> getIndexes(MetaObject metaObject) {
        return List.of();
    }

    @Override
    public Collection<MetaForeignKey> getForeignKeys(final String dbSchema) {
        return List.of();
    }

    @Override
    public Collection<MetaForeignKey> getForeignKeys(MetaObject metaObject) {
        return List.of();
    }

    @Override
    public MetaUi getMetaUi(String dbName, String objName, String locale, boolean reload) {
        return null;
    }

    @Override
    public MetaUi getMetaUi(String objName, String locale, boolean reload) {
        return null;
    }

    @Override
    public MetaUi getTenancyMetaUi(int tenantID, String dbName, String objName, String locale, boolean reload) {
        return null;
    }

    @Override
    public Optional<MetaUiField> getMetaUiField(MetaCol col) {
        return Optional.empty();
    }

    @Override
    public List<MetaRelation> getManyToOneRelations(String dbName, String objName) {
        return List.of();
    }

    @Override
    public boolean hasExtensions(String dbName, String objName) {
        return false;
    }


}
