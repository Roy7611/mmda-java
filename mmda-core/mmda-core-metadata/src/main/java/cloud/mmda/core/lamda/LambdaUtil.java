package cloud.mmda.core.lamda;

import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.utils.ClassUtil;
import cloud.mmda.core.utils.MapUtil;
import cloud.mmda.core.utils.NamingUtil;
import cloud.mmda.core.utils.StringUtil;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Lambda表达式工具
 */
public final class LambdaUtil {
    private static final Map<Class<?>, String> fieldNameMap = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Class<?>> implClassMap = new ConcurrentHashMap<>();

    public static <T> String getFieldName(LambdaGetter<T,?> getter) {
        return fieldNameMap.computeIfAbsent(getter.getClass(), aClass -> {
            SerializedLambda lambda = getSerializedLambda(getter);
            // 兼容 Kotlin KProperty 的 Lambda 解析
            if (lambda.getCapturedArgCount() == 1) {
                Object capturedArg = lambda.getCapturedArg(0);
                try {
                    return (String) capturedArg.getClass()
                            .getMethod("getName")
                            .invoke(capturedArg);
                } catch (Exception e) {
                    // 忽略这个异常，使用其他方式获取方法名
                }
            }
            String methodName = lambda.getImplMethodName();
            return NamingUtil.toFieldName(methodName);
        });
    }
    public static <T,U> String getFieldName(LambdaSetter<T,U> setter) {
        return fieldNameMap.computeIfAbsent(setter.getClass(), aClass -> {
            SerializedLambda lambda = getSerializedLambda(setter);
            // 兼容 Kotlin KProperty 的 Lambda 解析
            if (lambda.getCapturedArgCount() == 2) {
                Object capturedArg = lambda.getCapturedArg(0);
                try {
                    return (String) capturedArg.getClass()
                            .getMethod("setName")
                            .invoke(capturedArg);
                } catch (Exception e) {
                    // 忽略这个异常，使用其他方式获取方法名
                }
            }
            String methodName = lambda.getImplMethodName();
            return NamingUtil.toFieldName(methodName);
        });
    }

    public static <T> Class<?> getImplClass(LambdaGetter<T,?> getter) {
        return MapUtil.computeIfAbsent(implClassMap, getter.getClass(), aClass -> {
            SerializedLambda lambda = getSerializedLambda(getter);
            return getImplClass0(lambda);
        });
    }


//    public static <T> String getAliasName(LambdaGetter<T> getter, boolean withPrefix) {
//        MetaCol queryColumn = getQueryColumn(getter);
//        if (queryColumn != null) {
//            String alias = StringUtil.hasText(queryColumn.getAlias()) ? queryColumn.getAlias() : queryColumn.getName();
//            return withPrefix ? queryColumn.getTable().getName() + "$" + alias : alias;
//        }
//        return getFieldName(getter);
//    }

//    public static <T> MetaCol getMetaCol(LambdaGetter<T> getter) {
//        return MapUtil.computeIfAbsent(fieldColMap, getter.getClass(), aClass -> {
//            SerializedLambda lambda = getSerializedLambda(getter);
//            Class<?> entityClass = getImplClass0(lambda);
//            TableInfo tableInfo = TableInfoFactory.ofEntityClass(entityClass);
//            String propertyName = getFieldName(getter);
//            return tableInfo.getQueryColumnByProperty(propertyName);
//        });
//    }


    private static SerializedLambda getSerializedLambda(Serializable getter) {
        try {
            Method method = getter.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            return (SerializedLambda) method.invoke(getter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> getImplClass0(SerializedLambda lambda) {
        ClassLoader classLoader = ClassUtil.getDefaultClassLoader();
        String implClass = getImplClassName(lambda);
        try {
            return Class.forName(implClass.replace("/", "."), true, classLoader);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Lambda.getImplClass0", e);
        }
    }

    private static String getImplClassName(SerializedLambda lambda) {
        String type = lambda.getInstantiatedMethodType();
        return type.substring(2, type.indexOf(";"));
    }

    private LambdaUtil() {}
}
