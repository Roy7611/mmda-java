/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 *
 */
package cloud.mmda.core.data.jdbc.repository;

import cloud.mmda.core.data.jdbc.mappers.AttachmentRowMapper;
import cloud.mmda.core.data.pagination.PagedList;
import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.data.sql.SqlExpression;
import cloud.mmda.core.models.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static cloud.mmda.core.models.Attachment.Meta._objID;
import static cloud.mmda.core.models.Attachment.Meta._objName;

/**
 * 附件Repository
 *
 * @author syclive code robot
 * @version 3.0.0
 * @since 2023-11-28 00:20:35.0
 *
 */
@Repository
public class AttachmentRepository extends TenancyEntityRepository<Attachment, Attachment.Key> {
    //region ~GENERATED PARTS BEGIN
    /**
     * 构造函数
     * @param ds 数据源
     */
    @Autowired
    public AttachmentRepository(DataSource ds) {
        super(ds);
    }

    /**
     * 创建RowMapper
     */
    @Override
    protected RowMapper<Attachment> createRowMapper() {
        return new AttachmentRowMapper();
    }

    /**
     * 使用默认值创建新实体
     * 未存储至数据库，供客户端新建使用
     */
    public Attachment create() {
        Attachment t = new Attachment();
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        t.setUploadTime(now);
        t.setCreated();
        return t;
    }

    //endregion of ~GENERATED PARTS END

    /**
     * 查找一个实体的所有附件
     * @param objName 实体名称
     * @param objId 实体标识
     * @return
     * @throws DataAccessException
     */
    public List<Attachment> findAllBy(final String objName, long objId) throws DataAccessException{
        SqlExpression exp = this.expressionBuilder()
                .exp("objName").equal(objName)
                .and("objID").equal(objId)
                .result();
        return this.findAllBy(exp);
    }

    /**
     * 按文件名搜索一个实体所有附件
     * @param paginator 分页器
     * @param objName 限定在此对象名称
     * @param objId 限定在此对象标识
     * @param filename 模糊搜索此文件名
     * @return
     * @throws DataAccessException
     */
    public PagedList<Attachment> searchAllByFilename(Paginator paginator, final String objName, long objId, final String filename) throws DataAccessException{
        SqlExpression exp = this.expressionBuilder()
                .exp("objName").equal(objName)
                .and("objID").equal(objId)
                .and("filename").contains(filename)
                .result();
        return this.findAllBy(paginator,exp);
    }

    /**
     * 按文件名搜索多个实体所有附件
     * @param paginator 分页器
     * @param keys 限定在多个对象名称
     * @param filename 模糊搜索此文件名
     * @return
     * @throws DataAccessException
     */
    public PagedList<Attachment> searchAllByFilename(Paginator paginator, List<Attachment.Key> keys, final String filename) throws DataAccessException{
        Attachment.Key key=keys.get(0);
        SqlExpression exp = this.expressionBuilder()
                .exp(_objName).equal(key.getObjName())
                .and(_objID).equal(key.getObjID())
                .and(Attachment.Meta._fileName).contains(filename)
                .result();
        for (int i=1;i<keys.size();i++) {
            key=keys.get(i);
            exp.or( this.expressionBuilder()
                    .exp(_objName).equal(key.getObjName())
                    .and(_objID).equal(key.getObjID())
                    .and(Attachment.Meta._fileName).contains(filename)
                    .result());
        }
        return this.findAllBy(paginator,exp);
    }
    /**
     * 按文件名搜索多个实体所有附件
     * @param keys 限定在多个对象名称
     * @param filename 模糊搜索此文件名
     * @return
     * @throws DataAccessException
     */
    public List<Attachment> searchAllByFilename(List<Attachment.Key> keys, final String filename) throws DataAccessException{
        Attachment.Key key=keys.get(0);
        SqlExpression exp = this.expressionBuilder()
                .exp(_objName).equal(key.getObjName())
                .and(_objID).equal(key.getObjID())
                .and(Attachment.Meta._fileName).contains(filename)
                .result();
        for (int i=1;i<keys.size();i++) {
            key=keys.get(i);
            exp.or( this.expressionBuilder()
                    .exp(_objName).equal(key.getObjName())
                    .and(_objID).equal(key.getObjID())
                    .and(Attachment.Meta._fileName).contains(filename)
                    .result());
        }
        return this.findAllBy(exp);
    }
    public List<Attachment> findAllBy(List<String> objNames,  long objId) throws DataAccessException {
        SqlExpression condition = this.expressionBuilder()
                .exp(_objID).equal(objId)
                .and(_objName).in(objNames.stream().toArray(String[]::new))
                .result();
        return this.findAllBy(condition);
    }
    /**
     * 查找一个实体的所有附件(跨数据源）
     * @param objName 实体名称
     * @param objId 实体标识
     * @return
     * @throws DataAccessException
     */
    public List<Attachment> findAllBy(final String objName, long objId, String dbSchema) throws DataAccessException{
        SqlExpression exp = this.expressionBuilder()
                .exp(_objName).equal(objName)
                .and(_objID).equal(objId)
                .result();
        if (!metadataProvider.getCurrentDbName().contains(dbSchema)){
            String sql = "SELECT (@ROW_NUMBER:=@ROW_NUMBER + 1) AS rowNum,t.*  FROM `"+dbSchema+"`.`Attachment` AS t,(SELECT @ROW_NUMBER:=0) AS a WHERE (t.`objName`=? AND (t.`objID`=?))";
            var dataSource=getDataSource(dbSchema);
            var template = new JdbcTemplate(dataSource);
            return template.query(sql, new Object[]{objName,objId}, rowMapper);
        }
        return this.findAllBy(exp);
    }
}
