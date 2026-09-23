package cloud.mmda.core.file.exceptions;

public class ExcelException extends RuntimeException{
    protected String code = "excel.error";

    public String getCode() {return this.code;}

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String code, String message) {
        super(message);
        this.code = code;
    }


    public static class CodeConstant {
        public static final ExcelException TEMPLATE_NOT_SAME =
                new ExcelException("template.not.same","上传的数据模版与选择的模版不一致");//导入模版不一致
        public static final ExcelException TEMPLATE_NOT_DEFAULT =
                new ExcelException("template.not.default","上传的数据文件是模版文件，请选择对应模版后导入");
    }
}
