package cloud.mmda.core.file.excel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExcelConstant {
    public static  final  String  EXTENSIONS="小计";
    public static  final  String  FOOTINGS="总记";
    public static  final  String  AT="@";
    public static  final  String  NUMERICAL_DECIMAL_FORMAT="#,##0.0000";
    public static  final  String  NUMERICAL_INT_FORMAT="0";
    public static  final  String   DATETIME_FORMAT="yyyy-mm-dd hh:mm:ss";
    public static  final  String   DATE_FORMAT="yyyy-mm-dd";
    public static  final  String  REQUIRED_FIELDS_PROMPT= "*红色的为必填项";
    public static  final  String[] IGNORE_FIELDS =new String[]{"creatorID","createDate","lastModifierID","lastModified","deptID","refName","refID","refItemID","ownerID","ownerDeptID","signInPwd"};

    public static  final  String[] MULTI_ENUM_FIELDS=new String[]{"materialTypes","allowQaStatuses","partnerRoles"};
    public static  final  String  HELPER_COLUMN_NAME="全称";

    public static  final   Map<String, String> WHETHERMAPS = new ConcurrentHashMap<String, String>(){{
        put("true", "是");
        put("false", "否");
    }};
    public static  final  String DEFAULT_EXCEL_COL="A";
    public static  final  String  NOT_REQUIRED_PROMPT="非必填";
    public static  final  String  REQUIRED_PROMPT="必填项,不能为空";
    public static  final  String  MEX_SIZE_PROMPT="字符最大长度";
    public static  final  String  REPEAT_PROMPT="有重复数据";
    public static  final  String  NOT_REQUIRED_AOTU_PROMPT= "非必填，会自动生成";
    public static  final  String  ID_NOT_REQUIRED_AOTU_PROMPT="标识系统自动生成，如需添加相关详细信息请用1~99999代替";
    public static  final  String MAX_INT_CONSTRAINT ="999999999999999999";
    public static  final  String  MIN_DECIMAL_CONSTRAINT ="-99999999999999.9999";
    public static  final  String  MAX_DECIMAL_CONSTRAINT ="99999999999999.9999";
    public static  final  String  EXCEL_ROW_NUM ="$excelRowNum";

}
