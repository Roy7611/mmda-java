package cloud.mmda.core.file.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 */
@Slf4j
public class ReflectionUtil {


    /**
     * 执行对象方法
     * @param t 目标对象
     * @param methodName 方法名
     */
    public static Boolean executeMethod(Object t, String methodName){
        try{
            Method method = t.getClass().getMethod(methodName);
            method.setAccessible(true);
            method.invoke(t);
            return true;
        }catch (Exception e){
            log.info("没有"+methodName+"方法");
            return false;
        }
    }

    /**
     * 执行对象方法并获取返回值
     * @param t 目标对象
     * @param methodName 方法名
     */
    public static Object executeMethodAndGet(Object t, String methodName){
        try{
            Method method = t.getClass().getMethod(methodName);
            method.setAccessible(true);
            return method.invoke(t);

        }catch (Exception e){
            log.info("没有"+methodName+"方法");
            return null;
        }
    }

    /**
     * 为对象设置 rowNum 的值
     * @param t   目标对象
     * @param value   要赋的值
     */
    public static <T>void setT(Object t, String fieldName,Object value) throws Exception {
        Class<?> clazz = t.getClass();
        Method setterMethod = findSetterMethod(clazz, fieldName);
        if (setterMethod != null) {
            setterMethod.invoke(t, value);
        } else {
            log.info("未找到"+fieldName+"set方法");
        }
    }

    public static <T>Object getT(Object t, String fieldName) throws Exception {
        Class<?> clazz = t.getClass();
        Method setterMethod = findGetterMethod(clazz, fieldName);
        if (setterMethod != null) {
            return setterMethod.invoke(t);
        } else {
            log.info("未找到"+fieldName+"set方法");
            return null;
        }
    }

    /**
     * 递归查找类及其父类的 setter 方法
     */
    private static Method findSetterMethod(Class<?> clazz, String fieldName) {
        if (clazz == null) return null;

        String setterName = "set" + capitalize(fieldName);
        try {
            Field declaredField = clazz.getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            // 查找当前类的方法
            Method method = clazz.getDeclaredMethod(setterName, declaredField.getType());
            method.setAccessible(true); // 允许访问私有方法
            return method;
        } catch (Exception e) {
            // 递归查找父类
            return findSetterMethod(clazz.getSuperclass(), fieldName);
        }
    }


    /**
     * 递归查找类及其父类的 Getter 方法
     */
    private static Method findGetterMethod(Class<?> clazz, String fieldName) {
        if (clazz == null) return null;

        String setterName = "get" + capitalize(fieldName);
        try {
            Field declaredField = clazz.getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            // 查找当前类的方法
            Method method = clazz.getDeclaredMethod(setterName);
            method.setAccessible(true); // 允许访问私有方法
            return method;
        } catch (Exception e) {
            // 递归查找父类
            return findSetterMethod(clazz.getSuperclass(), fieldName);
        }
    }

    /**
     * 将字符串首字母大写（符合 JavaBean 规范）
     */
    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
