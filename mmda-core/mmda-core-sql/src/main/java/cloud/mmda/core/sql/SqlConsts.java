/*
 *  Copyright (c) 2022-2025, Mybatis-Flex (fuhai999@gmail.com).
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cloud.mmda.core.sql;

/**
 * SQL 关键字常量池。
 *
 */
public final class SqlConsts {

    private SqlConsts() {
    }
    public static final String MIN_ID = "minId";
    public static final String MAX_ID = "maxId";

    // === 常用符号 ===

    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final String ASTERISK = "*";
    public static final String COMMA = ",";
    public static final String BACKQUOTE = "`";
    public static final String DOT = ".";
    public static final String SEMICOLON = ";";
    public static final String COLON = ":";
    public static final String QUESTION_MARK = "?";
    public static final String PLACEHOLDER = QUESTION_MARK;
    public static final String EXCLAMATION = "!";
    public static final String PERCENT_SIGN = "%";
    public static final String AT_SIGN = "@";
    public static final String SINGLE_QUOTE = "'";
    public static final String DOUBLE_QUOTE = "\"";
    public static final String PARENTHESES_START = "(";
    public static final String PARENTHESES_END = ")";
    public static final String BRACKETS_START = "[";
    public static final String BRACKETS_END = "]";
    public static final String BRACES_START = "{";
    public static final String BRACES_END = "}";
    public static final String HINT_START = "/*+ ";
    public static final String HINT_END = " */ ";
    public static final String DELIMITER = "//";


    // === SQL CRUD 关键字 ===

    public static final String AS = " AS ";
    public static final String OR = " OR ";
    public static final String END = " END";
    public static final String AND = " AND ";
    public static final String SET = " SET ";
    public static final String CASE = "CASE";
    public static final String WHEN = " WHEN ";
    public static final String THEN = " THEN ";
    public static final String ELSE = " ELSE ";
    public static final String FROM = " FROM ";
    public static final String DUAL = "DUAL";
    public static final String WHERE = " WHERE ";
    public static final String SELECT = "SELECT ";
    public static final String VALUES = " VALUES ";
    public static final String DELETE = "DELETE ";
    public static final String UPDATE = "UPDATE ";
    public static final String HAVING = " HAVING ";
    public static final String DISTINCT = "DISTINCT ";
    public static final String GROUP_BY = " GROUP BY ";
    public static final String ORDER_BY = " ORDER BY ";
    public static final String INSERT = "INSERT";
    public static final String INTO = " INTO ";
    public static final String WITH = "WITH ";
    public static final String RECURSIVE = "RECURSIVE ";
    public static final String INSERT_INTO = INSERT + INTO;
    public static final String DELETE_FROM = DELETE + FROM;
    public static final String SELECT_ALL_FROM = SELECT + ASTERISK + FROM;
    public static final String COUNT = "COUNT";
    public final static String COUNT_ALL = COUNT + "(*) ";
    public final static String COUNT_ALL_DISTINCT = COUNT + "(DISTINCT *) ";
    public static final String SELECT_COUNT_ALL_FROM = SELECT + COUNT + PARENTHESES_START + ASTERISK + PARENTHESES_END + FROM;

    // === Oracle SQl ===

    public static final String INSERT_ALL = "INSERT ALL ";
    public static final String INSERT_ALL_END = " SELECT 1 FROM DUAL";


    // === Limit Offset ===

    public static final String TO = " TO ";
    public static final String TOP = " TOP ";
    public static final String ROWS = " ROWS ";
    public static final String SKIP = " SKIP ";
    public static final String FIRST = " FIRST ";
    public static final String LIMIT = " LIMIT ";
    public static final String OFFSET = " OFFSET ";
    public static final String START_AT = " START AT ";
    public static final String ROWS_ONLY = " ROWS ONLY";
    public static final String ROWS_FETCH_NEXT = " ROWS FETCH NEXT ";


    // === 联表查询关键字 ===

    public static final String ON = " ON ";
    public static final String JOIN = " JOIN ";
    public static final String UNION = " UNION ";
    public static final String UNION_ALL = " UNION ALL ";
    public static final String LEFT_JOIN = " LEFT JOIN ";
    public static final String FULL_JOIN = " FULL JOIN ";
    public static final String FULL_OUTER_JOIN = "FULL OUTER JOIN"; //当左表或右表中存在匹配项时，返回所有行
    public static final String RIGHT_JOIN = " RIGHT JOIN ";
    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String CROSS_JOIN = " CROSS JOIN ";


    // === 逻辑符号 ===

    public static final String GT = " > ";
    public static final String GE = " >= ";
    public static final String LT = " < ";
    public static final String LE = " <= ";
    public static final String LIKE = " LIKE ";
    public static final String NOT_LIKE = " NOT LIKE ";
    public static final String EQUALS = " = ";
    public static final String NOT_EQUALS = " != ";
    public static final String IS_NULL = " IS NULL ";
    public static final String IS_NOT_NULL = " IS NOT NULL ";
    public static final String IN = " IN ";
    public static final String NOT_IN = " NOT IN ";
    public static final String BETWEEN = " BETWEEN ";
    public static final String NOT_BETWEEN = " NOT BETWEEN ";


    // === 排序相关关键字 ===

    public static final String ASC = " ASC";
    public static final String DESC = " DESC";
    public static final String NULLS_FIRST = " NULLS FIRST";
    public static final String NULLS_LAST = " NULLS LAST";


    // === 数学运算符 ===

    public static final String PLUS = " + ";
    public static final String MINUS = " - ";
    public static final String DIVIDE = " / ";
    public static final String MULTIPLY = " * ";
    public static final String MODULUS = " % ";

    // === 其他拼接需要的字符串 ===

    public static final String EQUALS_PLACEHOLDER = " = ? ";
    public static final String AND_PLACEHOLDER = SPACE + PLACEHOLDER + AND + PLACEHOLDER + SPACE;

    // === DDL ===
    public static final String ADD = "ADD "; //在现有表中添加一列
    public static final String ADD_CONSTRAINT = "ADD CONSTRAINT "; //在创建表后添加约束
    public static final String ALTER = "ALTER "; //添加，删除或修改表中的列，或更改表中列的数据类型
    public static final String ALTER_COLUMN = "ALTER COLUMN "; //更改表中列的数据类型
    public static final String ALTER_TABLE = "ALTER TABLE "; //添加，删除或修改表中的列
    public static final String ALL = "ALL"; //如果所有子查询值都满足条件，则返回true
    public static final String ANY = "ANY"; //如果任何子查询值满足条件，则返回true
    public static final String BACKUP_DATABASE = "BACKUP DATABASE "; //创建现有数据库的备份
    public static final String CASCADE = "CASCADE"; //级联
    public static final String CHECK = "CHECK "; //限制可以放置在列中的值的约束
    public static final String COLUMN = "COLUMN"; //更改列的数据类型或删除表中的列
    public static final String CONSTRAINT = "CONSTRAINT "; //添加或删除约束
    public static final String COMMENT = "COMMENT "; //注释
    public static final String COMMENT_ON_TABLE = "COMMENT ON TABLE "; //注释表
    public static final String COMMENT_ON_COLUMN = "COMMENT ON COLUMN "; //注释字段
    public static final String CREATE = "CREATE "; //创建数据库，索引，视图，表或过程
    public static final String CREATE_DATABASE = "CREATE DATABASE "; //创建一个新的SQL数据库
    public static final String CREATE_INDEX = "CREATE INDEX "; //在表上创建索引（允许重复值）
    public static final String CREATE_OR_REPLACE_VIEW = "CREATE OR REPLACE VIEW "; //更新视图
    public static final String CREATE_TABLE = "CREATE TABLE "; //在数据库中创建一个新表
    public static final String CREATE_PROCEDURE = "CREATE PROCEDURE "; //创建一个存储过程
    public static final String CREATE_UNIQUE_INDEX = "CREATE UNIQUE INDEX "; //在表上创建唯一索引（无重复值）
    public static final String CREATE_VIEW = "CREATE VIEW "; //根据SELECT语句的结果集创建视图
    public static final String DATABASE = "DATABASE"; //创建或删除SQL数据库
    public static final String DEFAULT = " DEFAULT "; //为列提供默认值的约束
    public static final String DROP = "DROP "; //删除列，约束，数据库，索引，表或视图
    public static final String DROP_COLUMN = "DROP COLUMN "; //删除表中的列
    public static final String DROP_CONSTRAINT = "DROP CONSTRAINT "; //删除UNIQUE，PRIMARY KEY，FOREIGN KEY或CHECK约束
    public static final String DROP_DATABASE = "DROP DATABASE "; //删除现有的SQL数据库
    public static final String DROP_DEFAULT = "DROP DEFAULT "; //删除默认约束
    public static final String DROP_INDEX = "DROP INDEX "; //删除表中的索引
    public static final String DROP_TABLE = "DROP TABLE "; //删除数据库中的现有表
    public static final String DROP_VIEW = "DROP VIEW "; //删除视图
    public static final String DROP_PROC = "DROP PROCEDURE "; //删除数据库中的现有表
    public static final String DROP_FUNC = "DROP FUNCTION "; //删除数据库中的现有表
    public static final String IF_EXISTS = "IF EXISTS ";
    public static final String IF_NOT_EXISTS = "IF NOT EXISTS ";
    public static final String EXEC = "EXEC "; //执行存储过程
    public static final String EXISTS = "EXISTS"; //测试子查询中是否存在任何记录
    public static final String FOREIGN_KEY = " FOREIGN KEY "; //外键约束是用于将两个表链接在一起的键
    public static final String REFERENCES = " REFERENCES "; //外键引用
    public static final String INDEX = "INDEX "; //创建或删除表中的索引
    public static final String USING_BTREE = " USING BTREE";
    public static final String IS = "IS "; //仅包含条件不成立的行
    public static final String NOT = "NOT "; //仅包含条件不成立的行
    public static final String NOT_NULL = "NOT NULL"; //强制列不接受NULL值的约束
    public static final String NULL = "NULL"; //强制列不接受NULL值的约束
    public static final String OUTER_JOIN = " OUTER JOIN "; //当左表或右表中存在匹配项时，返回所有行
    public static final String PRIMARY_KEY = "PRIMARY KEY "; //唯一标识数据库表中每个记录的约束
    public static final String PROCEDURE = "PROCEDURE"; //存储过程
    public static final String ROW_NUM = "ROWNUM"; //指定要在结果集中返回的记录数
    public static final String SELECT_DISTINCT = "SELECT DISTINCT "; //仅选择不同的（不同的）值
    public static final String SELECT_INTO = "SELECT INTO "; //将数据从一个表复制到新表中
    public static final String SELECT_TOP = "SELECT TOP "; //指定要在结果集中返回的记录数
    public static final String TABLE = "TABLE"; //创建表，或添加，删除或修改表中的列，或删除表或表中的数据
    public static final String TABLESPACE = "TABLESPACE "; //表空间
    public static final String TRUNCATE_TABLE = "TRUNCATE TABLE "; //删除表中的数据，但不删除表本身
    public static final String UNIQUE = "UNIQUE"; //确保列中所有值唯一的约束
    public static final String VIEW = "VIEW"; //创建，更新或删除视图
    public static final char TAB = '\t';
}
