package cloud.mmda.core.utils;

import org.jspecify.annotations.NonNull;

/**
 * 命名工具提供代码生成静态函数，支持PascalCase，lower_snake_case，UPPER_SNAKE_CASE，camelCase的相互转换
 */
public final class NamingUtil {
    private NamingUtil() {}
    @Deprecated(since = "Use Character.isUpperCase")
    public static final boolean isUpperCase(char c){
        return c>='A' && c<='Z';
    }
    @Deprecated(since = "Use Character.toUpperCase")
    public static final char toUpperCase(char c){
        return Character.toUpperCase(c);
    }

    @Deprecated(since = "Use Character.toLowerCase")
    public static final char toLowerCase(char c){
        return Character.toLowerCase(c);
    }
    /**
     * 首字母大写
     * @param s 名称字符串
     * @return
     */
    public static String firstLetterUpper(String s){
        if(s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    /**
     * 首字母小写
     * @param s 名称字符串
     * @return
     */
    public static String firstLetterLower(String s){
        if(s == null || s.isEmpty()) return s;
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }
    /**
     * 取第一个单词
     * @param s 例如RevenueContract
     * @return 返回第一个单词Revenue
     */
    public static String firstWord(final String s){
        char[] cs=s.toCharArray();
        int upperCase = 0;
        int pos = -1;
        for(int i=0; i<cs.length; i++){
            if(isUpperCase(cs[i])){
                upperCase++;
            }
            if(upperCase > 1 || cs[i] == ' ' || cs[i] == '-' || cs[i] == '_') {
                pos = i;
                break;
            }
        }
        if(pos != -1){
            return s.substring(0,pos);
        }
        return s;
    }
    public static String lastWord(final String s){
        char[] cs=s.toCharArray();
        int lastPos = 0;
        for(int i=0; i<cs.length; i++){
            if(isUpperCase(cs[i]) || cs[i] == ' ' || cs[i] == '-' || cs[i] == '_'){
                lastPos = i;
            }
        }

        if(isUpperCase(cs[lastPos])) return s.substring(lastPos);
        else return s.substring(lastPos+1);
    }
    /**
     * 简写（大写）
     * @param s 例如jobStatus
     * @return JS
     */
    public static final String toUpperAbbreviation(String s){
        StringBuilder sb = new StringBuilder();
        char[] cs=s.toCharArray();
        sb.append(Character.toUpperCase(cs[0]));
        for(int i=1; i<cs.length; i++){
            if(Character.isUpperCase(cs[i])){
                sb.append(cs[i]);
            }
        }
        if(sb.length()>1) return sb.toString();
        return s.toUpperCase();
    }
    /**
     * 将驼峰命名转为蛇形式命名，例如
     * 将backgroundColor=>background-color
     * @param s 要转换的字符串，如backgroundColor
     * @param delimiter 分隔符，如-
     * @return background-color
     */
    public static final String toSnakeCase(String s, char delimiter){
        StringBuilder sb = new StringBuilder();
        char[] cs=s.toCharArray();
        sb.append(Character.toLowerCase(cs[0]));
        for(int i=1; i<cs.length; i++){
            boolean isUpper = Character.isUpperCase(cs[i]);
            if(isUpper){
                sb.append(delimiter);
                cs[i]+=32;//小写
            }
            sb.append(cs[i]);
        }
        return sb.toString();
    }
    public static final String toSnakeCase(String s){
        return toSnakeCase(s,'_');
    }

    /**
     * 划线命名转为驼峰命名
     * 将background-color转换为backgroundColor
     * @param s 要转换的字符串，如background-color
     * @param delimiter 分隔符，如-
     * @return backgroundColor
     */
    public static final String toCamelCase(String s, char delimiter){
        StringBuilder sb = new StringBuilder();
        char[] cs=s.toCharArray();
        sb.append(Character.toLowerCase(cs[0]));
        boolean found = false;
        for(int i=1; i<cs.length; i++){
            if(cs[i]==delimiter){
                found = true;
                continue;
            }
            if(found){
                if(Character.isLowerCase(cs[i])) cs[i]-=32;
                found = false;
            }
            sb.append(cs[i]);
        }
        return sb.toString();
    }
    public static final String toCamelCase(String s){
        return toCamelCase(s, '_');
    }

    /**
     * 字段名转分词，用于英文标签。
     * @param s 字段名称如 partnerID
     * @return 转化为英文标签 Partner ID
     */
    public static final String toDisplayLabel(String s){
        StringBuilder sb = new StringBuilder();
        char[] cs=s.toCharArray();
        sb.append(Character.toUpperCase(cs[0]));
        int upperCase = 1;
        for(int i=1; i<cs.length; i++){
            if(Character.isUpperCase(cs[i])){
                if(upperCase == 0) sb.append(' ');
                upperCase++;
            }
            else{
                upperCase = 0;
            }
            sb.append(cs[i]);
        }
        return sb.toString();
    }
    private static final String CONSONANTS = "bcdfghjklmnpqrstvwxz";
    /**
     * 复数形式
     * @param singular 单数形式
     * @return
     */
    public static String makePlural(String singular)
    {
        // Handle ending with "o" (if preceeded by a consonant, end with -es, otherwise -s: Potatoes and Radios)
        if (singular.endsWith("o") && CONSONANTS.contains(String.valueOf(singular.charAt(singular.length() - 2))))
        {
            return singular + "es";
        }
        // Handle ending with "y" (if preceeded by a consonant, end with -ies, otherwise -s: Companies and Trays)
        if (singular.endsWith("y") && CONSONANTS.contains(String.valueOf(singular.charAt(singular.length() - 2))))
        {
            return singular.substring(0, singular.length() - 1) + "ies";
        }
        // Ends with a whistling sound: boxes, buzzes, churches, passes
        if (singular.endsWith("s") || singular.endsWith("sh") || singular.endsWith("ch") || singular.endsWith("x") || singular.endsWith("z"))
        {
            return singular + "es";
        }
        return singular + "s";
    }

    /**
     * 将getter/setter转换成字段名，例如：getUserId => userId
     * @param getterName 属性获取函数名
     * @return 返回字段名
     */
    public static @NonNull String toFieldName(@NonNull String getterName) {
        if (getterName.startsWith("is")) {
            getterName = getterName.substring(2);
        } else if (getterName.startsWith("get") || getterName.startsWith("set")) {
            getterName = getterName.substring(3);
        } else {
            throw new IllegalArgumentException("name should start with 'is', 'get' or 'set'.");
        }
        if (!getterName.isEmpty()) {
            getterName = firstLetterLower(getterName);
        }
        return getterName;
    }
}
