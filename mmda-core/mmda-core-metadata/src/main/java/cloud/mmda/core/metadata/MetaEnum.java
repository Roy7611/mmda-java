package cloud.mmda.core.metadata;

import cloud.mmda.core.LazySingletonSupplier;
import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.enums.EnumValue;
import cloud.mmda.core.exceptions.MetaEnumParseException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cloud.mmda.core.utils.NameValue;
import com.google.common.collect.ImmutableBiMap;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NonNull;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 元枚举定义枚举数据类型
 *
 * <p>
 *     编程语言中的枚举类型通常包括类名称{@link #enumClass}和枚举成员{@link #getMembers()}，在Java中定义如下
 *     <pre>
 *         {@code
 *         public enum DataType {   //enumClass  = "DataType"
 *             INT8 (1,"TINYINT"),  //enumMember = { value = 1, name="INT8", text="TINYINT" }
 *             INT16(2,"SMALLINT"),
 *             INT32(4,"INT"),
 *             INT64(4,"BIGINT"),
 *             ;
 *         }
 *         }
 *     </pre>
 * </p>
 * <p>
 *     其他编程语言类似。我们通过枚举字符串定义一个枚举，例如<code>0;ADD;加|1;SUB;减</code>。
 *     我们通过元语言统一定义枚举的用处是可以生成各种编程语言的枚举代码，实现枚举值和名称的双向映射。
 *     你可以在框架中使用{@link #nameOf(int)}获取枚举值对应的名称，使用{@link #valueOf(String)}获取枚举名称对应的值。
 * </p>
 *
 * @author roy.luo 2026.5 改进，使用了{@link com.google.common.collect.BiMap}实现双向映射，增加了枚举成员
 * TODO: 去掉 parsedEnumMap，改用 members，enumFieldNum 没用
 */
public class MetaEnum extends MetaEntity {
    public static final String ROW_DELIMITER = "\\|";
    public static final String COL_DELIMITER = ";";

    /**
     * 枚举类名称
     */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
    private String enumClass;
    /**
     * 默认显示标签
     */
    @Size(max = 30)
    @Getter @Setter
    private String displayLabel;

    /**
     * 命名空间
     */
    @Size(max = 255)
    @Getter
    @Setter
    private String namespace;

    /**
     * 枚举字符串
     */
    @Size(max = 255)
    @Getter
    @Setter
    private String enumString;

    /**
     * 数据类型
     */
    @Getter
    @Setter
    private DataType dataType;

    /**
     * 是否按位编码枚举值，例如0,1,2,4,……
     */
    @Getter
    @Setter
    private boolean bitwise;
    /**
	 * 创建时间
	 */
	@Getter @Setter
	private Timestamp createDate;
	/**
	 * 最后修改
	 */
	@Getter @Setter
	private Timestamp lastModified;

    /**
     * 获取全称，包含命名空间.类名（计算属性，不参与序列化）
     */
    @JsonIgnore
    public String getQualifiedClassName(){
        return namespace +"."+enumClass;
    }

    /**
     * 解析后的枚举选项
     * 0->{NONE,无}
     */
    private ConcurrentMap<String, NameValue<String,String>> parsedEnumMap;

    @Deprecated(since = "Use getMembers, valueOf and nameOf")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Map<String, NameValue<String,String>> getParsedEnumMap(){
        if(parsedEnumMap==null){
            parsedEnumMap = parseEnumMap(enumString);
        }
        return parsedEnumMap;
    }

    /**
     * 获取所有元枚举成员{@link MetaEnumMember}列表
     * @return
     */
    public List<MetaEnumMember> getMembers(){
        return _members.get();
    }

    private LazySingletonSupplier<List<MetaEnumMember>> _members
            = new LazySingletonSupplier<>(()->parseEnumMemberList(enumString, bitwise));

    // 内部枚举值和名称双向映射，支持nameOf, valueOf
    private LazySingletonSupplier<ImmutableBiMap<Integer,String>> _valueNameMap
            = new LazySingletonSupplier<>(
                    () -> getMembers().stream().collect(ImmutableBiMap.toImmutableBiMap(
                            MetaEnumMember::getIntValue,
                            MetaEnumMember::getName
                            )
                    )
    );

    /**
     * 取得值 value 对应的枚举名称
     * @param value 整型数，例如 1
     * @return 返回枚举名称，例如 LOCKED
     */
    public final String nameOf(int value){
        return _valueNameMap.get().get(value);
    }

    /**
     * 取得值 value 对应的枚举名称集合，仅仅用于{@link #bitwise}为 true 的情况
     * @param value 整型数，例如 3 (1 | 2)
     * @return 返回枚举名称集合，例如 [ LOCKED, BLACKOUT ]
     */
    public final Set<String> nameSetOf(int value){
        return _valueNameMap.get().entrySet().stream()
                .filter(e -> (e.getKey() & value)>0)
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    /**
     * 取得枚举名称 name 对应的枚举值
     * @param name 枚举名称，例如 LOCKED
     * @return 返回整型枚举值，例如 1
     */
    public final int valueOf(String name){
        return _valueNameMap.get().inverse().get(name);
    }

    /**
     * 取得枚举名称集 names 对应的枚举值，仅仅用于{@link #bitwise}为 true 的情况
     * @param names 枚举名称集合，例如 [ LOCKED, BLACKOUT ]
     * @return 返回整型枚举值，例如 3 (1 | 2)
     */
    public final int valueOf(Set<String> names){
        return names.stream().map(name -> valueOf(name))
                .reduce(0, Integer::sum);
    }
    /**
     * 解析为Set
     *
     * @param enumString 枚举设置，形如 a|b|c
     * @return
     */
    public static Set<String> parseEnumSet(@NotNull String enumString){
        Objects.requireNonNull(enumString);
        return Arrays.asList(enumString.split(ROW_DELIMITER)).stream().collect(Collectors.toSet());
    }

    /**
     * 解析为Map
     *
     * @param enumString 枚举设置，形如 a|b|c 或者 0;a|1;b|2;c
     * @return
     */
    public static ConcurrentMap<String, NameValue<String,String>> parseEnumMap(@NotNull String enumString){
        Objects.requireNonNull(enumString);

        Map<String, NameValue<String,String>> result = new LinkedHashMap<>();
        String[] items = enumString.split(ROW_DELIMITER);
        if(items.length>0){
            String[] fields = items[0].split(COL_DELIMITER);
            if(fields.length>2){
                //VALUE -> { NAME, TEXT }
                for(String item : items){
                    fields = item.split(COL_DELIMITER);
                    result.put(fields[0],new NameValue<>(fields[1],fields[2]));
                }
            }
            else if(fields.length==2){
                //VALUE LABEL
                for(String item : items){
                    fields = item.split(COL_DELIMITER);
                    result.put(fields[0],new NameValue<>(fields[0],fields[1]));
                }
            }
            else{
                //LABEL
                for(String item : items){
                    result.put(item,new NameValue<>(item,item));
                }
            }
        }
        return new ConcurrentHashMap<String, NameValue<String,String>>(result);
    }

    /**
     * 转为字符串表示
     * @param map 值和标签的映射，如 0=>a, 1=>b
     * @return 字符串形式，如 0;a|1;b|2;c
     */
    public static String toEnumString(Map<String,String> map){
        return map.entrySet().stream()
                .map(e->e.getKey()+COL_DELIMITER+e.getValue())
                .collect(Collectors.joining(ROW_DELIMITER));
    }

    /**
     * 转为字符串表示
     *
     * @param set 集合，如{ a, b }
     * @return 字符串形式，a|b
     */
    public static String toEnumString(Set<String> set){
        return String.join(ROW_DELIMITER,set);
    }

    /**
     * 将枚举字符串转化为枚举成员列表
     * @param enumString 枚举字符串，例如："0;NEW;新|1;USED;已启用|-1;DEPRECATED;已弃用"
     * @return 列表
     */
    public static List<MetaEnumMember> parseEnumMemberList(@NonNull String enumString, boolean bitwise){
        Objects.requireNonNull(enumString);
        var members = Arrays.stream(enumString.split(ROW_DELIMITER))
                .map(MetaEnumMember::parse)
                .toList();
        if(bitwise && members.stream().anyMatch(m -> m.getValue().startsWith("-"))){
            throw new MetaEnumParseException("Bitset enum value can not be negative");
        }
        return members;
    }
}
