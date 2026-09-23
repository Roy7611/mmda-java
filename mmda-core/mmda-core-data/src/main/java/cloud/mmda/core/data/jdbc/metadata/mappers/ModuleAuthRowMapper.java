package cloud.mmda.core.data.jdbc.metadata.mappers;

import cloud.mmda.core.enums.ModuleAllowOpSet;
import cloud.mmda.core.enums.ModuleStatus;
import cloud.mmda.core.enums.ModuleType;
import cloud.mmda.core.metadata.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModuleAuthRowMapper implements RowMapper<ModuleAuth> {

    @Override
    public ModuleAuth mapRow(ResultSet rs, int rowNum) throws SQLException {
        ModuleAuth t = new ModuleAuth();
        t.setRowNum(rowNum);
        t.setModuleCode(rs.getString("moduleCode"));
        t.setModuleType(ModuleType.valueOf(rs.getByte("moduleType")));
        t.setModuleIcon(rs.getString("moduleIcon"));
        t.setModuleLabel(rs.getString("moduleLabel"));
        t.setShortLabel(rs.getString("shortLabel"));
        t.setDbSchema(rs.getString("dbSchema"));
        t.setObjName(rs.getString("objName"));
        t.setAllowOps(ModuleAllowOpSet.valueOf(rs.getByte("allowOp")));
        t.setModuleUrl(rs.getString("moduleUrl"));
        t.setRequiredCreateParam(rs.getBoolean("requiredCreateParam"));
        t.setDefaultFilter(rs.getString("defaultFilter"));
        t.setDefaultSort(rs.getString("defaultSort"));
        t.setStatus(ModuleStatus.valueOf(rs.getShort("status")));
        t.setDescription(rs.getString("description"));
        return t;
    }
}
