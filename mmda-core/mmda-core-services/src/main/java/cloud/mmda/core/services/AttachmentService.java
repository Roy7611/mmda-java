/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.services;

import cloud.mmda.core.caching.CachePolicy;
import cloud.mmda.core.data.jdbc.repository.AttachmentRepository;
import cloud.mmda.core.data.pagination.PagedList;
import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.models.Attachment;
import cloud.mmda.core.security.models.UserAccount;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 附件Service
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2024-07-17 07:38:57.0
 * 
 */
@Service
public class AttachmentService extends TenancyEntityService<Attachment,Attachment.Key>{
	//region ~GENERATED PARTS BEGIN
	private static final Log logger = LogFactory.getLog(AttachmentService.class);
	private final AttachmentRepository repository;
	/**
	 * 构造函数
	 * @param repository 底层数据库访问仓储
	 * @param factory  redis连接工厂
	 */
	@Autowired
	public AttachmentService(final AttachmentRepository repository, final RedisConnectionFactory factory) {
		super(repository, factory);
		this.repository = repository;
	}

	//region load & save
	//endregion
	//endregion of ~GENERATED PARTS END
		
    /**
     * 获取实体对象所有附件
     * @param objName 实体类名称
     * @param id 对象标识
     * @return
     */
    public List<Attachment> getAllById(String objName, long id) {
        return getList(() -> repository.findAllBy(objName,id));
    }

    /**
     * 按文件名称模糊搜索单个实体对象的附加文件
     * @param objName 实体类名称
     * @param id 对象标识
     * @param filename 模糊搜索此文件名称
     * @param page 第几页
     * @param size 每页几条
     * @return
     */
    public PagedList<Attachment> searchAllByFilename(String objName, long id, String filename, int page, int size) {
        return getPagedList(() -> repository.searchAllByFilename(new Paginator(size, page),objName,id, filename));
    }

	/**
	 * 按文件名称模糊搜索多个实体对象的附加文件
	 * @param paginator 分页器
	 * @param keys 限定在多个对象名称
	 * @param filename 模糊搜索此文件名
	 * @return
	 */
	public PagedList<Attachment> searchAllByFilename(Paginator paginator, List<Attachment.Key> keys, String filename) {
		return getPagedList(() -> repository.searchAllByFilename(paginator,keys, filename));
	}

	/**
	 * 按文件名称模糊搜索多个实体对象的附加文件
	 * @param keys 限定在多个对象名称
	 * @param filename 模糊搜索此文件名
	 * @return
	 */
	public List<Attachment> searchAllByFilename( List<Attachment.Key> keys, String filename) {
		return repository.searchAllByFilename(keys, filename);
	}

	public int saveAttachment(long objId, String objName, final UserAccount user,
							  Attachment t, boolean checkExists,
							  CachePolicy p) {
		t.setObjName(objName);
		t.setObjID(objId);
		t.setUploader(user.getUsername());
		t.setUploadTime(Timestamp.valueOf(LocalDateTime.now()));
		return save(t,p,checkExists);
	}

	/**
	 * 获取实体对象所有附件(跨数据源)
	 * @param objName 实体类名称
	 * @param id 对象标识
	 * @return
	 */
	public List<Attachment> getAllById(String objName, long id,String dbSchema) {
		return getList(() -> repository.findAllBy(objName,id,dbSchema));
	}
	public List<Attachment> getAllById(List<String> objNames,  long objId) throws DataAccessException {
		return getList(() -> repository.findAllBy(objNames,objId));
	}

	public int insertMany(int tenantID, List<Attachment> attachments) throws DataAccessException {
		var result = repository.insertMany(tenantID,attachments);
		return Arrays.stream(result).sum();
	}
}
