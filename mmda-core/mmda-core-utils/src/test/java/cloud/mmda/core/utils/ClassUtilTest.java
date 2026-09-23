package cloud.mmda.core.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClassUtilTest {

    class Role {

        private List<UUID> uuids = new ArrayList<>();

        public List<UUID> getUuids() {
            return uuids;
        }
    }
    @Test
    void listIsNotArray() {
        var a = new ArrayList<Integer>();
        var b = ClassUtil.isArray(a.getClass());
        assertFalse(b);
    }

    @Test
    void getGenericType(){
        var role = new Role();
        Class<?> c = role.getUuids().getClass();

        var typeParams = c.getTypeParameters();
        var classes = c.getClasses();
        var componentType = c.componentType();
        var GenericInterfaces = c.getGenericInterfaces();
        for(var i : GenericInterfaces){
            if(i instanceof Class && List.class.isAssignableFrom((Class<?>) i) ){
                Type elementClass = ((ParameterizedType) i).getActualTypeArguments()[0];
                System.out.println(elementClass);
                break;
            }
            if(i instanceof ParameterizedType pt ){
                Type elementType = pt.getActualTypeArguments()[0];
                var rawType = (Class<?>) pt.getRawType();
                var elementClass = (Class<?>) elementType;
                System.out.println(elementClass);
                break;
            }
        }

    }
}