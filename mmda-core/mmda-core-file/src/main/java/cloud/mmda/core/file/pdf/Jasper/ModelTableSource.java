package cloud.mmda.core.file.pdf.Jasper;

import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ModelTableSource {
    /**
     * 当前时间(查询时间)
     */
    @Setter
    @Getter
    private String date;

    /**
     * 注入table组件的数据源
     */
    @Setter
    @Getter
    private JRBeanCollectionDataSource tableData;
}
