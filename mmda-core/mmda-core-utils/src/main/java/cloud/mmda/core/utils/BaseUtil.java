package cloud.mmda.core.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseUtil {
    /**
     * 相当于数据库的ifnull函数
     * @param value 判断值是否为null
     * @param orElse null的替代值
     * @param <T>
     * @return
     */
    public static final <T> T ifNull(T value, T orElse){
        return null==value ? orElse : value;
    }
    /**
     * 判断字符串是否为null或者空字符串
     * @param v 字符串
     * @return boolean true表示空
     */
    public static final boolean isNullOrEmpty(String v){
        return v==null || v.isEmpty();
    }

    /**
     * 判断字符串是否为null、空字符串或者空格
     * @param v
     * @return
     */
    public static final boolean isNullOrWhitesapce(String v){
        return v == null || v.isEmpty() || v.trim().isEmpty();
    }

    public static final boolean hasText(String v){
        return v!=null && !v.isEmpty();
    }

    public static final String repeat(String v, int count, String delimiter){
        return String.join(delimiter, Collections.nCopies(count, v));
    }
    public static final String repeat(String v, int count){
        return String.join("", Collections.nCopies(count, v));
    }
    public static final String repeat(char c, int count){
        return String.format("%0" + count + "d", 0).replace('0', c);
    }

    public static final String quote(final String q, final String v){
        if(v == null) return v;
        return q + v + q;
    }

    public static final String padLeftSpaces(String v, int count){
        return String.format("%1$"+count+"s",v);
    }
    public static final String padLeft(String v, int count, char padding){
        return padLeftSpaces(v,count).replace(' ',padding);
    }
    public static final String padLeftZeros(String v, int count){
        return padLeft(v,count,'0');
    }
    public static final String padRightSpaces(String v, int count){
        return String.format("%1$-"+count+"s",v);
    }
    public static final String padRight(String v, int count, char padding){
        return padRightSpaces(v,count).replace(' ',padding);
    }
    public static final String padRightZeros(String v, int count){
        return padRight(v,count,'0');
    }

    public static final String right(String v, int count){
        if(v==null) return v;

        int len = v.length();
        if(len<count) return v;
        return v.substring(v.length()-count);
    }
    public static final String left(String v, int count){
        if(v==null) return v;
        int len = v.length();
        if(len<count) return v;
        return v.substring(0, count);
    }
    /**
     * 判断数值是否为null或者0
     * @param v
     * @return
     */
    public static final boolean isNullOrZero(BigDecimal v){
        return v == null || v.compareTo(BigDecimal.ZERO)==0;
    }
    public static final boolean isNullOrZero(Long v){
        return v == null || v==0;
    }
    public static final boolean isNullOrZero(Integer v){
        return v == null || v==0;
    }
    public static final boolean isNullOrZero(Short v){
        return v == null || v==0;
    }
    public static final boolean isNullOfFalse(Boolean v){return v == null || v==false;}
    public static final boolean isTrue(Boolean v){return v!=null && v==true;}

    public static final long getValueOrDefault(Long v, long d){
        return v!=null ? v : d;
    }
    public static final long getValueOrZero(Long v){return getValueOrDefault(v,0L);}
    //十六进制数字
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * 将字节数组转换为十六进制字符串
     * @param bytes
     * @return
     */
    public static final String toHexString(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j< len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static final String toBase64String(byte[] bytes){
        byte[] encoded = Base64.getUrlEncoder().encode(bytes);
        return new String(encoded);
    }
    public static final String toBase64String(String value){
        return toBase64String(value.getBytes());
    }

    public static final String fromBase64(byte[] bytes){
        byte[] decoded = Base64.getUrlDecoder().decode(bytes);
        return new String(decoded);
    }
    public static final String fromBase64String(String base64){
        return fromBase64(base64.getBytes());
    }

    public static final <T> T requireNonNull(final T obj, final String argName) {
        return Objects.requireNonNull(obj, argName + "参数不能为空。");
    }
    public static final String requireNonBlank(final String s, final String argName) {
        if (!hasText(s))
            throw new NullPointerException(argName + "参数不能为空。");
        return s;
    }

    public  static final <T> boolean hasAny(Collection<T> tCollection){
        return tCollection!=null && !tCollection.isEmpty();
    }
    public static final <T> Collection<T> requireNoneEmpty(Collection<T> tCollection, final String argName){
        if(tCollection==null || tCollection.isEmpty())
            throw new IllegalArgumentException("参数不能为空");
        return tCollection;
    }

    public static final boolean hasBit(int value, int bit){
        if(bit<0) return false;
        return ((value>>bit) & 1) == 1;
    }
    public static final int getBit(int value, int bit){
        if(bit<0) return value;
        return ((value>>bit) & 1);
    }
    public static final int setBit(int value, int bit, boolean flag){
        if(bit<0) return value;
        return flag ? ((1<<bit) | value) : (~(1<<bit) & value);
    }


    public final static Integer tryParseInt(String value, Integer defaultValue){
        try{
            return Integer.valueOf(value);
        }
        catch(NumberFormatException e){
            return defaultValue;
        }
    }
    public final static Short tryParseShort(String value, Short defaultValue){
        try{
            return Short.valueOf(value);
        }
        catch(NumberFormatException e){
            return defaultValue;
        }
    }
    public final static Byte tryParseByte(String value, Byte defaultValue){
        try{
            return Byte.valueOf(value);
        }
        catch(NumberFormatException e){
            return defaultValue;
        }
    }

    public final static boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim()))
            return s.matches("^[0-9]*$") || s.matches("-?\\d+(\\.\\d+)?");
        else
            return false;
    }

    public static final String generateUniqueNo(String ...field) {
        try {
            // 将字段的值拼接成一个字符串
            String combinedFields = removeEmptyStrings(field);

            // 使用MD5哈希函数
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = digest.digest(combinedFields.getBytes());

            // 将MD5哈希值转换为十六进制字符串
            BigInteger bigInt = new BigInteger(1, hashBytes);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }
    private static String removeEmptyStrings(String... field) {
        // 使用Stream过滤掉空字符串，然后使用Collectors.joining拼接非空字段
        return Arrays.stream(field)
                .filter(s -> s != null && !s.isEmpty())
                .collect(Collectors.joining());
    }
}
