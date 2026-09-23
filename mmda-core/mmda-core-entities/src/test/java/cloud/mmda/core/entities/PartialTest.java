package cloud.mmda.core.entities;

import cloud.mmda.core.models.Attachment;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static cloud.mmda.core.models.Attachment.Meta.*;
import static org.junit.jupiter.api.Assertions.*;

public class PartialTest {
    @Test
    void testPartial(){
        var t = new Attachment();
        var p = Partial.of(t, a -> a.getObjID()).get();
        //实际上和 Map.of没区别
        var p1 = Map.of(_objID, 1L);
        var p2 = Map.of(_objID, 2L, _objName, "User");
    }

    @Test
    void testPartialBuilder(){
        //避免直接使用字符串，编译器检查
        var t = new Attachment();
        var p = Partial.of(t) //元字符串
                .with(_objID, 1L) //元字符串，性能最好
                .with(Attachment::getFileName) //Lambda 表达式需要转一下
                .with(Attachment::getObjName)
                .result();

        assertNotNull(p);
        assertEquals(1L, p.get(_objID));
    }
}
