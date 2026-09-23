package cloud.mmda.core.enums;

import lombok.Getter;

/**
 * 数据类型扩展属性，用于实例化实际数据类型实例时添加自定义内容
 */
public enum DataTypeAttribute implements EnumBitValue {
    NONE        (-1),
    MAX_LENGTH  (0),//大小，例如字符串的长度
    PRECISION   (1),//精度，用于数值、时间
    SCALE       (2),//小数位
    TYPE_HANDLER(3),//数据类型处理器提供序列化和反序列化函数
    VALUE_RANGE (4),//值限制(min,max)
    AUTO_INCR   (5),//自增
    BYTE_RANGE  (6),//存储长度，可以是固定的，或者一个范围
    CUSTOM      (7)
    ;

    @Getter
    private Integer value;
    @Getter
    private int bit;

    DataTypeAttribute(int bit) {
        this.value = bit<0 ? 0 : (1 << bit);
        this.bit = bit;
    }


    @Override
    public String getText() {
        return "";
    }
}
