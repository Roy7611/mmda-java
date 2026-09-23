package cloud.mmda.core.enums;

/**
 * 整形值位元枚举值
 */
public interface EnumBitValue extends EnumValue<Integer> {
    /**
     * 在值中处于第几位，-1,0,1,2,...
     */
    int getBit();

    /**
     * 返回枚举对应的文本
     */
    String getText();

    @Override
    default Integer getValue(){
        if(getBit()>=0) return 1<<getBit();
        return 0;
    }

    static boolean hasBit(int value, int bit){
        if(bit<0) return false;
        return ((value>>bit) & 1) == 1;
    }
    static int bitOf(int value, int bit){
        if(bit<0) return value;
        return ((value>>bit) & 1);
    }
    static int withBit(int value, int bit, boolean flag){
        if(bit<0) return value;
        return flag ? ((1<<bit) | value) : (~(1<<bit) & value);
    }
}
