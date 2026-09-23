/**
 * Copyright (c) 2006, 2024, www.mmda.cloud All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 *
 */
package cloud.mmda.core.enums;
import lombok.Getter;

import java.nio.ByteOrder;
import java.util.LinkedHashMap;
import java.util.Map;
import cloud.mmda.core.enums.EnumValue;
/**
 * 字节序
 * <p/>
 * 0;LITTLE;小端|1;BIG;大端
 *
 * @author mmda code robot
 * @version 4.0
 * @since 2024-10-17 10:33:52.0
 * @see <a href="https://en.wikipedia.org/wiki/Endianness">参考文档</a>
 */
public enum Endianness implements EnumValue<Byte> {
    //region ~GENERATED PARTS BEGIN
    LITTLE(0,"小端"),
    BIG(1,"大端");

    private final Byte value;
    @Override
    public final Byte getValue(){
        return this.value;
    }


    @Getter
    private final String text;

    Endianness(int value, String text){
        this.value = (byte)value;
        this.text = text;
    }

    public static final Endianness valueOf(byte value){
        return enumMap.get(value);
    }

    public static final byte LITTLE_VAL = 0;//小端
    public static final byte BIG_VAL = 1;//大端

    public static Map<Byte,Endianness> enumMap = new LinkedHashMap<Byte,Endianness>(){{
        put(LITTLE_VAL,LITTLE);
        put(BIG_VAL,BIG);
    }};
    //endregion of ~GENERATED PARTS END

    /**
     * Java字节序
     */
    public ByteOrder byteOrder(){
        return this == BIG ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
    }
}
