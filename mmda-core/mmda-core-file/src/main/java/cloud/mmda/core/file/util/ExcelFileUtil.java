package cloud.mmda.core.file.util;

import cloud.mmda.core.file.clients.FileExcelClient;
import cloud.mmda.core.utils.BaseUtil;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class ExcelFileUtil {

    private final static String fileSheetName = "file_Sheet_Name";

    private final static String dataSplit = "-data-";

    private final static String cellSplit = "-cell-";

    public static File getFileByUrl(String urlStr) {
        try {
            String lastSegment = urlStr.substring(urlStr.lastIndexOf('/') + 1);
            String[] split = urlStr.split("/");
            String fileName = split[split.length - 1];
            String encodedChinesePart = UriUtils.encode(fileName, "UTF-8");
            urlStr = urlStr.replace(fileName, encodedChinesePart);
            File temp = Files.createTempFile("temp", lastSegment).toFile();
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) return null;
            InputStream inputStream = httpConn.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(temp);
            // 创建一个缓冲区
            byte[] buffer = new byte[1024];
            int bytesRead;

            // 从输入流读取数据并写入输出流
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            // 关闭流
            fileOutputStream.close();
            inputStream.close();
            return temp;
        } catch (Exception e) {
            log.info("ExcelFileUtil: getFileByUrl文件下载异常" + urlStr);
            return null;
        }
    }

    public static Mono<String> uploadFile(String filename, File file){
        return  fileExcelClient.uploadFile("excelImport",System.currentTimeMillis(),filename,file);
    }

    public static Mono<File> uploadExcelFile(String filename, File file){
        return  fileExcelClient.uploadExcelFile("excelExport",System.currentTimeMillis(),filename,file);
    }

    public static File setExcelPicsForMmdafile(File file, Path rootPath) throws IOException {

        file.setReadable(true,false);
        file.setWritable(true,false);

        if(isZipOrXlsx(file)){ //zip
            String tempDirPath = System.getProperty("java.io.tmpdir");
            File tempDir = new File(tempDirPath, file.getName());
            List<File> files = FileUtil.unzipToFileList(file,tempDir);
            List<File> list = files.stream().map(file1 -> {
                try {
                    return setExcelPic(file1, rootPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).filter(Objects::nonNull).toList();
            FileUtil.zipFiles(list,file);
            return file;
        }else { //xlsx
            return setExcelPic(file,rootPath);

        }
    }

    public static File setExcelPic(File file, Path rootPath) throws IOException {
        try (final Workbook workbook = new XSSFWorkbook(file.getPath())) {
            Sheet sheet = workbook.getSheet(fileSheetName);
            Row row;
            Cell cell;
            if(sheet == null || (row = sheet.getRow(0)) == null || (cell = row.getCell(0)) == null)return null;
            int numericCellValue = (int) cell.getNumericCellValue();
            for (int i = 1; i <= numericCellValue; i++) {
                Row row1;
                Cell cell1;
                if((row1 = sheet.getRow(i)) == null || (cell1 = row1.getCell(i)) == null)continue;
                String stringCellValue = cell1.getStringCellValue();
                String[] split = stringCellValue.split(dataSplit);
                if(split.length != 3)continue;
                String sheetName = split[0];
                String fileTarget = split[2];
                String[] split1 = split[1].split(cellSplit);
                if(split1.length != 2)continue;
                Sheet sheet1 = workbook.getSheet(sheetName);
                if(sheet1 == null) continue;
                Row row2 = sheet1.getRow(Integer.parseInt(split1[0]));
                if(row2 == null) continue;
                Cell cell2 = row2.getCell(Integer.parseInt(split1[1]));
                if(cell2 == null) continue;
                setExcelPics(cell2,fileTarget,rootPath);
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            return file;

        }
    }

    /**
     * true zip false xlsx
     * @param file
     * @return
     */
    public static boolean isZipOrXlsx(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] header = new byte[4];
            if (fis.read(header) != 4) return false;
            // 判断是否为 ZIP/XLSX 的魔数
            return (header[0] == 0x50 && header[1] == 0x4B &&
                    header[2] == 0x03 && header[3] == 0x04);
        } catch (Exception e) {
            return false;
        }
    }

    public static void setExcelPics(Cell cell,String target,Path rootPath){
        cell.setCellValue("");
        List<File> list = Arrays.stream(target.split(";"))
                .map(url -> getLocalFileByUrl(url, rootPath))
                .filter(Objects::nonNull)
                .toList();
        setExcelPic(cell, list);
    }

    /**
     *  .andRoute(GET("/files/{dir}/{filename}"), handler::downloadFile)
     *  .andRoute(GET("/files/{dir}/{id}/{filename}"), handler::downloadFile)
     * @param url
     * @param rootPath
     * @return
     */
    public static File getLocalFileByUrl(String url,Path rootPath) {
        String[] split = url.replace("//","").split("/");
        Path path = null;
        if(split.length == 4){
            path = rootPath.resolve(split[2]).resolve(split[3]);
        }else if(split.length == 5){
            path = rootPath.resolve(split[2]).resolve(split[3]).resolve(split[4]);
        }

        return path == null ? null : path.toFile();
    }

    public static void setExcelPics(Cell cell,String target){
//        if(1==1){
//            return;
//        }
        cell.setCellValue(target);
        if(StringUtil.isBlank(target)){
            return;
        }

        String sheetName = cell.getSheet().getSheetName();
        Workbook workbook = cell.getSheet().getWorkbook();
        Sheet sheet = workbook.getSheet(fileSheetName);
        if(sheet==null)sheet = workbook.createSheet(fileSheetName);
        Row row = sheet.getRow(0);
        if (row == null) row = sheet.createRow(0);
        Cell cell1 = row.getCell(0);
        int rowNum = 1;
        if (cell1 == null){
            cell1 = row.createCell(0);
        }else {
            rowNum = (int) (cell1.getNumericCellValue()+1);
        }
        cell1.setCellValue(rowNum);
        Cell cell2 = sheet.createRow(rowNum).createCell(0);
        cell2.setCellValue(sheetName+ dataSplit +cell.getRow().getRowNum()+cellSplit+cell.getColumnIndex()+dataSplit+target);
    }

    public static void setExcelPics(Cell cell,String target,String a){
        if(StringUtil.isBlank(target)){
            cell.setCellValue(target);
            return;
        }
        String[] split = target.split(";");
        List<File> list = Arrays.stream(split).map(ExcelFileUtil::getFileByUrl).filter(Objects::nonNull).toList();
        if(CollectionUtils.isEmpty(list)){
            cell.setCellValue(target);
        }else {
            setExcelPic(cell, list);
        }
    }



    @SneakyThrows
    public static void setExcelPic(Cell cell, List<File> pics){
        cell.setCellValue("");
        Sheet sheet = cell.getSheet();
        Workbook workbook = sheet.getWorkbook();
        float height = 120*pics.size()+20;
        cell.getRow().setHeightInPoints(Math.max(cell.getRow().getHeightInPoints(),height));

        sheet.setColumnWidth(cell.getColumnIndex(),6000);
        CreationHelper helper = workbook.getCreationHelper();
        Drawing<?> drawing = sheet.getDrawingPatriarch() != null ? sheet.getDrawingPatriarch() : sheet.createDrawingPatriarch();


        int dx = 20;

        for (File file : pics) {
            byte[] bytes = Files.readAllBytes(file.toPath());

            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            ClientAnchor anchor = helper.createClientAnchor();
            // 设置图片位置（从A1单元格开始）
            anchor.setCol1(cell.getColumnIndex());
            anchor.setRow1(cell.getRowIndex());
            anchor.setRow2(cell.getRowIndex());
            anchor.setCol2(cell.getColumnIndex());

            anchor.setDy1(dx * 12700);
            anchor.setDy2((dx = dx+100)*12700);
            anchor.setDx2(150*9525);
            dx = dx + 20;
            Picture picture = drawing.createPicture(anchor, pictureIdx);

        }
    }


    /**
     * 删除行时更新图片锚点
     * @param sheet
     * @param deletedRowIndex
     * @param num
     */
    public static void updatePicAnchorWhenDelRow(Sheet sheet, Integer deletedRowIndex,Integer num) {
        num = BaseUtil.isNullOrZero(num) ? 1 : num;
        Drawing<?> drawing  = sheet.getDrawingPatriarch();

        if (drawing instanceof XSSFDrawing) {
            for (XSSFShape shape : ((XSSFDrawing) drawing).getShapes()) {
                if (shape instanceof XSSFPicture) {
                    XSSFClientAnchor picAnchor = (XSSFClientAnchor) shape.getAnchor();
                    if (picAnchor.getRow1() >= deletedRowIndex) picAnchor.setRow1(picAnchor.getRow1() + num);
                    if (picAnchor.getRow2() >= deletedRowIndex) picAnchor.setRow2(picAnchor.getRow2() + num);
                }
            }
        }

        Sheet sheet1 = sheet.getWorkbook().getSheet(fileSheetName);
        if(sheet1==null)return;
        Row row = sheet1.getRow(0);
        if(row==null || row.getCell(0) == null )return;
        Cell cell1 = row.getCell(0);
        int numericCellValue = (int)cell1.getNumericCellValue();
        for (int i = 1; i <= numericCellValue; i++) {
            Row row1 = sheet1.getRow(i);
            if(row1==null || row1.getCell(0) == null )continue;
            Cell cell2 = row1.getCell(0);
            String stringCellValue = cell2.getStringCellValue();
            int rowNum = Integer.parseInt(stringCellValue.split(dataSplit)[1].split(cellSplit)[0]);

            stringCellValue = stringCellValue.replace(dataSplit +rowNum+cellSplit, dataSplit +(rowNum+num) +cellSplit);
            cell2.setCellValue(stringCellValue);
        }

    }

    /**
     * 将整个工作簿的图片都上传并且将链接写入到对应cell中
     * @param workbook
     */
    public static void findAndUploadPic(Workbook workbook) {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            findAndUploadPic(sheet);
        }

    }


    public static void findAndUploadPic(Sheet sheet){
        try {
            Drawing<?> drawing = sheet.getDrawingPatriarch();
            if (drawing == null) {
                return; // 当前 Sheet 无图片
            }
            List<XSSFShape> shapes = ((XSSFDrawing) drawing).getShapes();

            for (XSSFShape shape : shapes) {
                if (shape instanceof XSSFPicture) {
                    XSSFPicture pic = (XSSFPicture) shape;
                    // 获取图片的锚点信息
                    ClientAnchor anchor = pic.getPreferredSize();
                    // 解析图片所在单元格
                    int rowIndex = anchor.getRow1(); // 起始行
                    int colIndex = anchor.getCol1(); // 起始列
                    Row row = sheet.getRow(rowIndex);
                    row = row == null ? sheet.createRow(rowIndex) : row;
                    Cell cell1 = row.getCell(colIndex);
                    cell1 = cell1 == null ? row.createCell(colIndex) : cell1;


                    // 获取图片二进制数据
                    XSSFPictureData picData = pic.getPictureData();
                    String picName = "picture_" + UUID.randomUUID() + "." + picData.suggestFileExtension();
                    File temp = Files.createTempFile("temp", picName).toFile();
                    byte[] imageBytes = picData.getData();
                    try (FileOutputStream fos = new FileOutputStream(temp.getPath())) {
                        fos.write(imageBytes);
                    }
                    AtomicReference<String> url = new AtomicReference<>("");
                    Mono<String> mono = uploadFile(picName, temp);
                    CompletableFuture<String> future = mono.toFuture();
                    boolean checkHasValue = cell1.getCellType() == CellType.STRING && StringUtil.isNotBlank(cell1.getStringCellValue()) && cell1.getStringCellValue().startsWith("http");
                    if(checkHasValue) cell1.setCellValue(cell1.getStringCellValue().concat(";").concat(future.get())) ;
                    else cell1.setCellValue(future.get());
                }
            }
        }catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    /**
     * 给单元格设置下拉框(通过Excel表达式创建下拉框，但是这种方式下拉框数据长度最多255，适用于数据比较少的情况)
     *
     * @param cell
     * @param data
     */
    public static void setOption(Cell cell, String[] data) {
        if (data == null || data.length <= 0) {
            return;
        }
        DataValidationHelper dataValidationHelper = cell.getSheet().getDataValidationHelper();
        DataValidationConstraint explicitListConstraint = dataValidationHelper.createExplicitListConstraint(data);
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex());

        DataValidation dataValidation = dataValidationHelper.createValidation(explicitListConstraint, cellRangeAddressList);
        cell.getSheet().addValidationData(dataValidation);
    }

    /**
     * 给单元格设置下拉框 (下拉框数据会存放在新的sheet页中，适用于下拉框数据比较多的情况)
     * @param cell
     * @param data
     * @param sheet1
     */
    public static void setOption(Cell cell, String[] originalData,Sheet sheet1) {
        Sheet sheetMain = cell.getSheet();
        if (originalData == null || originalData.length <= 0) {
            return;
        }
        Workbook workbook = sheet1.getWorkbook();
        workbook.setSheetHidden(workbook.getSheetIndex(sheet1.getSheetName()),true);

        // 1. 创建一个新数组，长度比原数组多1
        String[] newData = new String[originalData.length + 1];

        // 2. 将原数组的所有元素复制到新数组中，从新数组的索引1开始存放
        System.arraycopy(originalData, 0, newData, 1, originalData.length);

        // 3. 将新数组的第一个元素设置为null
        newData[0] = null;


        for (int i = 0; i < newData.length; i++) {
            sheet1.createRow(i).createCell(0, CellType.STRING).setCellValue(newData[i]);
        }

        String formula = "\"" + sheet1.getSheetName() + "!$A$1:$A$" + newData.length + "\"";

        CellRangeAddressList regions = new CellRangeAddressList(cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex());

        // 这句话是关键 引用ShtDictionary 的单元格
        DataValidationHelper dvHelper = sheetMain.getDataValidationHelper();
        DataValidationConstraint constraint = dvHelper.createFormulaListConstraint("INDIRECT(" + formula + ")");
        DataValidation validation = dvHelper.createValidation(constraint, regions);
        validation.setShowPromptBox(true);
        validation.createPromptBox("输入提示!", "请选择下拉列表里的选项!");
        sheetMain.addValidationData(validation);

    }


    @Autowired
    private FileExcelClient excelClient;

    private static FileExcelClient fileExcelClient;

    @PostConstruct
    public void init(){
        fileExcelClient = excelClient;
    }

    public static String getSheetName(String sheetName) {
        String regex = "[^0-9a-zA-Z\u4e00-\u9fa5]";
        return sheetName.replaceAll(regex, "");
    }
}
