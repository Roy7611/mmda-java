package cloud.mmda.core.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.util.HashMap;
import java.util.Map;

/**
 * 对象转换工具
 *
 * POJO to/from JSON
 * POJO to/from Map
 * JSON to/from Map
 * 参考{@literal https://github.com/FasterXML/jackson-databind/}
 */
public final class JsonUtil {
    private JsonUtil() {}
    private static ObjectMapper objectMapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private static ObjectReader objectReader = objectMapper.readerFor(Map.class);

    public static Map<String,Object> parseJsonAsMap(String json){
        //如果有内容解析
        if(json!=null && !json.isEmpty()){
            try{
                return objectReader.readValue(json);
            }
            catch (Exception ex){
                //parse error
            }
        }

        return null;
    }
    public static <T> Map<String,T> parseJsonAsMapT(String json){
        //如果有内容解析
        if(json!=null && !json.isEmpty()){
            try{
                return objectReader.readValue(json);
            }
            catch (Exception ex){
                //parse error
            }
        }

        return null;
    }
    public static Map<String,Object> parseJsonStreamAsMap(String json){
        //如果有内容解析
        if(json!=null && !json.isEmpty()){
            try{
                //Stream API
                JsonFactory factory = new JsonFactory();
                JsonParser parser  = factory.createParser(json);
                Map<String,Object> propertyMap = new HashMap<>();
                while(!parser.isClosed()){
                    JsonToken jsonToken = parser.nextToken();

                    if(JsonToken.FIELD_NAME.equals(jsonToken)){
                        String fieldName = parser.getCurrentName();

                        jsonToken = parser.nextToken();
                        propertyMap.put(fieldName,parser.getCurrentValue());
                    }
                }
                return propertyMap;

            }
            catch (Exception ex){
                //parse error
            }
        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> tClass){
        try{
            return objectMapper.readValue(json, tClass);
        }
        catch (Exception ex){
            return null;
        }
    }
    public static String toJson(Object value){
        try{
            return objectMapper.writeValueAsString(value);
        }
        catch (Exception ex){
            return null;
        }
    }

    public static <T> T fromMap(Map propertyMap, Class<T> tClass){
        try{
            return objectMapper.convertValue(propertyMap, tClass);
        }
        catch (Exception ex){
            return null;
        }
    }

    public static Map<String,Object> toMap(Object value){
        try{
            return objectMapper.convertValue(value, Map.class);
        }
        catch (Exception ex){
            return null;
        }
    }

    public static byte[] convertBase64ToBytes(String base64){
        try {
            return objectMapper.convertValue(base64, byte[].class);
        }
        catch (Exception ex){
            return null;
        }
    }
}
