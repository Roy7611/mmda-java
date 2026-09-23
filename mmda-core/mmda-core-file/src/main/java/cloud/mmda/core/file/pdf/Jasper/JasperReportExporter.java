package cloud.mmda.core.file.pdf.Jasper;


import cloud.mmda.core.utils.BaseUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sourceforge.barbecue.env.Environment;
import net.sourceforge.barbecue.env.EnvironmentFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class JasperReportExporter {
//public class JasperReportExporter<T, K> {
    private static final Log logger = LogFactory.getLog(JasperReportExporter.class);

    private static final String TABLE_DATA = "tableData";

    //private Repository<T, K> repository;
    //
    //public JasperReportExporter(Repository<T, K> repository) {
    //    this.repository = repository;
    //}
    public JasperReportExporter() {}

    static {
        System.setProperty("java.awt.headless", "true");
    }

    /**
     * 使用field导出数据
     *
     * @param reportName
     * @param bindingData
     * @param fileName
     * @param suffix
     * @return
     * @throws IOException
     * @throws JRException
     */
    public File exportFile(String reportName, Locale locale, List<Object> bindingData, String fileName, String suffix)
            throws IOException, JRException {
//        Resource resource = new ClassPathResource("jaspers"+ File.separator+reportName +".jasper");
        InputStream fis = getInputStream(reportName);
        File file = File.createTempFile(fileName, ".".concat(suffix.toLowerCase()));

        Map parameters = new HashMap();
        parameters.put(JRParameter.REPORT_LOCALE, locale);

        JasperPrint print = getJasperPrint(fis, parameters, bindingData);

        return JasperReportUtil.export(print, file, suffix);

    }

    /**
     * 使用field导出数据
     *
     * @param reportName
     * @param bindingData
     * @param fileName
     * @param suffix
     * @return
     * @throws IOException
     * @throws JRException
     */
    public File exportFile(String reportName, Locale locale, List<Object> bindingData, Map<String, Object> params, String fileName, String suffix)
            throws IOException, JRException {
        InputStream fis = getInputStream(reportName);
        File file = File.createTempFile(fileName, ".".concat(suffix.toLowerCase()));

        Map parameters = new HashMap();
        if (params != null) parameters = params;
        parameters.put(JRParameter.REPORT_LOCALE, locale);

        JasperPrint print = getJasperPrint(fis, parameters, bindingData);

        return JasperReportUtil.export(print, file, suffix);

    }

    /**
     * 使用JRDataSource和 parameters和table控件导出数据
     *
     * @param reportName
     * @param bindingData
     * @param params
     * @param fileName
     * @param suffix
     * @return
     * @throws IOException
     * @throws JRException
     */
    public File exportTableFile(String reportName, Locale locale, List<Object> bindingData, Map<String, Object> params, String fileName, String suffix)
            throws IOException, JRException {
        //1.引入jasper文件
        InputStream fis = getInputStream(reportName);

        //2.创建JasperPrint,向jasper文件中填充数据
        File file = File.createTempFile(fileName, ".".concat(suffix.toLowerCase()));
        Map parameters = new HashMap();
        if (params != null) parameters = params;
        parameters.put(JRParameter.REPORT_LOCALE, locale);
        JasperPrint print = null;
        if (CollectionUtils.isEmpty(bindingData)) {
            print = JasperFillManager.fillReport(fis,
                    parameters, new JREmptyDataSource());
        } else {
            List<Object> mlist = new ArrayList<>();
            ModelTableSource mst = new ModelTableSource();
            mst.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            mst.setTableData(new JRBeanCollectionDataSource(bindingData));
            mlist.add(mst);
            print = getJasperPrint(fis, parameters, mlist);
        }
        return JasperReportUtil.export(print, file, suffix);

    }

    // 用于处理Map类型对象的方法
    private Map<String, Object> handleMapObject(Object object) {
        Map<String, Object> dataMap = (Map<String, Object>) object;
        if (dataMap.containsKey(TABLE_DATA)) {
            dataMap.put("tableData", new JRBeanCollectionDataSource((List) dataMap.get(TABLE_DATA)));
            //当前时间(查询时间)
            dataMap.put("date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        return dataMap;
    }


    private JasperPrint getJasperPrint(InputStream inputStream, Map<String, Object> parameters, List<Object> beanCollection) throws IOException, JRException {
        Environment env = new Environment() {
            public final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 20);

            @Override
            public int getResolution() {
                return 60;
            }

            @Override
            public Font getDefaultFont() {
                return DEFAULT_FONT;
            }
        };
        EnvironmentFactory.setDefaultEnvironment(env);
        if (CollectionUtils.isEmpty(beanCollection)) {
            JasperPrint print = JasperFillManager.fillReport(inputStream,
                    parameters, new JREmptyDataSource());
            return print;
        }
        JRBeanCollectionDataSource beanColDataSource =
                new JRBeanCollectionDataSource(beanCollection);
        JasperPrint print = JasperFillManager.fillReport(inputStream,
                parameters, beanColDataSource);
        return print;
    }

    public InputStream getInputStream(String reportName) throws IOException {
        InputStream fis = this.getClass().getClassLoader().getResourceAsStream("jaspers" + File.separator + reportName + ".jasper");
        if (fis == null) {
            logger.error("未找到文件" + reportName + ".jasper");
            //Resource resource = new ClassPathResource("jaspers"+ File.separator+reportName +".jasper");
            //fis = new FileInputStream(resource.getFile());
            // 如果.japser文件在src/main/resources/jaspers目录下
            fis = getClass().getResourceAsStream("/jaspers/" + reportName + ".jasper");

        }
            //fis=getClass().getResourceAsStream("/jaspers/" + reportName + ".jasper");
        return fis;
    }


    /**
     * 使用数据库导出getAll
     *
     * @param reportName
     * @param minEntityID
     * @param maxEntityID
     * @param searchWord
     * @param condition
     * @param sort
     * @param fileName
     * @param suffix
     * @return
     * @throws IOException
     * @throws JRException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @Deprecated
    public File exportFile(String reportName, long minEntityID, long maxEntityID, String searchWord, String condition, String sort, String fileName, String suffix) throws IOException, JRException, ClassNotFoundException, SQLException {
        //Resource resource = new ClassPathResource("jaspers"+ File.separator+reportName +".jasper");
        //FileInputStream fis = new FileInputStream(resource.getFile());
        InputStream fis = this.getClass().getClassLoader().getResourceAsStream("jaspers" + File.separator + reportName + ".jasper");
        File file = File.createTempFile(fileName, ".".concat(suffix.toLowerCase()));
        String url = "jdbc:mysql://123.60.56.125/sycloud_wms?characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url, "root", "Icanfly8686");
        Map<String, Object> parameters = new HashMap<>();
        condition = BaseUtil.isNullOrEmpty(condition) ? "1=1" : condition;
        parameters.put("condition", condition);
        // parameters.put("sort",sort);
        parameters.put("searchWord", StringUtils.isEmpty(searchWord) ? "%%" : "%" + searchWord + "%");
        parameters.put("minEntityID", minEntityID);
        parameters.put("maxEntityID", maxEntityID);

        JasperPrint jasperPrint = JasperFillManager.fillReport(fis, parameters, conn);
        return JasperReportUtil.export(jasperPrint, file, suffix);


    }

    @Deprecated
    public File exportFile(String reportName, Map<String, Object> parameters, String fileName, String suffix) throws IOException, JRException, ClassNotFoundException, SQLException {
        //Resource resource = new ClassPathResource("jaspers"+ File.separator+reportName +".jasper");
        //FileInputStream fis = new FileInputStream(resource.getFile());
        InputStream fis = this.getClass().getClassLoader().getResourceAsStream("jaspers" + File.separator + reportName + ".jasper");
        File file = File.createTempFile(fileName, ".".concat(suffix.toLowerCase()));
        String url = "jdbc:mysql://123.60.56.125/sycloud_wms?characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url, "root", "Icanfly8686");
        JasperPrint jasperPrint = JasperFillManager.fillReport(fis, parameters, conn);
        return JasperReportUtil.export(jasperPrint, file, suffix);


    }


}
