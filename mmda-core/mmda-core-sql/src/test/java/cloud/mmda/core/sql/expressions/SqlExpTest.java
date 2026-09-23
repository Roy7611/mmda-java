package cloud.mmda.core.sql.expressions;

import cloud.mmda.core.data.pagination.Sort;
import cloud.mmda.core.lamda.LambdaUtil;
import cloud.mmda.core.metadata.MetadataProvider;
import cloud.mmda.core.sql.SqlQuery;
import cloud.mmda.core.sql.dialects.MySqlDialect;
import cloud.mmda.core.sql.dialects.SqlDialect;
import cloud.mmda.core.sql.mock.MockMetadataProvider;
import cloud.mmda.core.sql.mock.Partner;
import cloud.mmda.core.sql.mock.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static cloud.mmda.core.sql.expressions.SqlExp.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Sql表达式测试")
class SqlExpTest {
    private static MetadataProvider provider;
    private static SqlDialect mySqlDialect;

    @BeforeAll
    static void initAll() {
        provider = new MockMetadataProvider();
        mySqlDialect = new MySqlDialect(provider);
    }

//    @BeforeEach
//    void init() {
//
//    }

    @Test
//    @Disabled("for demonstration purposes")
    @DisplayName("SqlExpBuilder 表达式构建器")
    public void testCreate(){
        var metaObject = provider.getMetaObject("Partner");
        var exp = create(metaObject)
                // partnerID = 3
                .start("partnerID").eq(3L).not()
                // AND partnerNo = partnerNo + 4
                .and("partnerNo").eq(
                        // 简单表达式的构建，包在里面的无需调用 withContext
                        exp("partnerNo").plus(4)
                        //.withContext(metaObject) //包在里面的无需调用withContext
                )
                // OR partnerID < 12
                .or("partnerID").lt(12L)
                .and("partnerName").isNotNull()
                .end();

        assertNotNull(exp);
        var sql = exp.toString();
        System.out.println(sql);
        assertTrue(sql.contains("((partnerID = 3) AND (partnerNo = (partnerNo+4))) OR (partnerID < 12)"));
    }

    @Test
//    @Disabled("for demonstration purposes")
    @DisplayName("SqlExp.andAll Map构建查询条件")
    public void testColExp(){
        var equations = new HashMap<>();
        equations.put("partnerID", 90);
        equations.put("partnerNo", "P1029232");

        var metaObject = provider.getMetaObject("Partner");
        var criteria = allEqualsAnd(equations);

        assertNotNull(criteria);

        var sql = criteria.toString();
        System.out.println(sql);
        assertTrue(sql.contains("(partnerNo = P1029232) AND (partnerID = 90)"));
    }

    @Test
    @DisplayName("SqlArithmeticExp 算术表达式")
    public void testArithmeticExp(){
        var metaObject = provider.getMetaObject("Partner");
        var exp = SqlExp.exp("orderCount").plus(3).mul(10).bitAnd(0xFFFF);
        assertNotNull(exp);
        var result = exp.compile(mySqlDialect, metaObject, false);
        var sql = exp.toString();
        System.out.println(sql);
//        assertEquals("(((orderCount+3)*10)&65535)", sql);
    }

    @Test
    public void testSqlExpVisitor(){
        var exp = SqlExp.exp("orderCount").plus(3).mul(10).sub(100);
        var metaObject = provider.getMetaObject("Partner");
        var sql = exp.compile(mySqlDialect, metaObject, false);
        System.out.println(sql);
//        assertEquals(3,exp.getParameterTypes().size());
    }

    @Test
    @DisplayName("LambdaUtil 从getter获取字段名")
    public void testLambdaField(){
        var fieldName = LambdaUtil.getFieldName(User::getUserId);
        System.out.println(fieldName);
        assertEquals(fieldName,"userId");

        fieldName = LambdaUtil.getFieldName(User::isActive);
        System.out.println(fieldName);
        assertEquals(fieldName,"active");
    }

    public void testExists(){
        var metaObject = provider.getMetaObject("Partner");
        var exp = SqlExp.exists(
                SqlQuery.create(provider)
                        .from(metaObject)
                        .where(col(Partner::getPartnerNo).eq("ABC"))
                        .orderBy(
                                Sort.of(Partner::getPartnerID, Sort.Order.DESC)
                        ).selectAll()
        );
    }

//    @AfterEach
//    void tearDown() {
//    }
//
//    @AfterAll
//    static void tearDownAll() {
//    }

}