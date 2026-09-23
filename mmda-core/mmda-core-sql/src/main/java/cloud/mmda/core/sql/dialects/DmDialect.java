package cloud.mmda.core.sql.dialects;

import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.metadata.MetadataProvider;
import cloud.mmda.core.sql.types.DbDataType;
import cloud.mmda.core.sql.types.DmDataTypes;

public class DmDialect extends OracleDialect {
    public DmDialect(MetadataProvider metadataProvider) {
        super(metadataProvider);
    }

    @Override
    public String name() {
        return "DM";
    }

    @Override
    public DbDataType getDbDataType(DataType genericDataType) {
        return null;
    }
}
