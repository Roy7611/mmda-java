package cloud.mmda.core.sql.dialects;

import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.metadata.MetadataProvider;
import cloud.mmda.core.sql.types.DbDataType;
import cloud.mmda.core.sql.types.KingbaseDataTypes;

public class KingbaseDialect extends PostgreSqlDialect {
    public KingbaseDialect(MetadataProvider metadataProvider) {
        super(metadataProvider);
    }

    @Override
    public String name() {
        return "King base";
    }

    @Override
    public DbDataType getDbDataType(DataType genericDataType) {
        return KingbaseDataTypes.getType(genericDataType);
    }
}
