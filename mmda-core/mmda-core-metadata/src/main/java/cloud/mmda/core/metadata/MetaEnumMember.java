package cloud.mmda.core.metadata;

import cloud.mmda.core.exceptions.MetaEnumParseException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;


/**
 * 元枚举成员定义枚举成员，包括值、名称和文本，可生成枚举类型中的一项常量值定义。
 * 例如：
 * <pre>
 *     {@code
 *     "0;NONE;无" =>
 *         {
 *             value: "0",
 *             name: "NONE",
 *             text: "无"
 *         }
 *     }
 * </pre>
 * 枚举的呈现方式在UI层元数据定义
 */
@Data
@AllArgsConstructor
public class MetaEnumMember {
    public static final MetaEnumMember EMPTY = new MetaEnumMember("");
    public MetaEnumMember(String value){
        this.value = value;
        this.name = value;
        this.text = value;
    }
    public MetaEnumMember(String value, String text){
        this.value = value;
        this.name = value;
        this.text = text;
    }

    /**
     * 值，可解析为数值
     */
    private final String value;
    /**
     * 编码，作为枚举类型名称
     */
    private final String name;
    /**
     * 显示文本，支持国际化
     */
    private final String text;

    /**
     * 枚举代码字符串
     * @return NONE(0,"无")
     */
    @Override
    public String toString(){
        return name +"("+value+",\""+text+"\")";
    }

    /**
     * 解析元枚举项
     * @param enumMemberString 形如{@code 0;NONE;无}的字符串
     * @return 一个MetaEnumItem实例
     */
    public static MetaEnumMember parse(String enumMemberString){
        Objects.requireNonNull(enumMemberString, "enumMemberString can not be null");
        String[] elements = enumMemberString.split(MetaEnum.COL_DELIMITER,3);
        if(elements.length < 3){
            throw new MetaEnumParseException("Invalid meta enum member definition: " + enumMemberString);
        }
        return new MetaEnumMember(elements[0], elements[1], elements[2]);
        //不再支持以下松散定义的格式
//        else if(elements.length == 2){
//            return new MetaEnumMember(elements[0], elements[1]);
//        }
//        else{
//            return new MetaEnumMember(elements[0]);
//        }
    }
    public int getIntValue(){
        try{
            return Integer.parseInt(value);
        }
        catch(NumberFormatException e){
            throw new MetaEnumParseException("Enum value(" + value + ") of " + name + " must be an integer");
        }
    }
}
