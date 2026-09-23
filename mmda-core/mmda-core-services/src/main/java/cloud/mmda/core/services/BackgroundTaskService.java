/**
 * Copyright (c) 2006, 2024, www.mmda.cloud All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.services;
import cloud.mmda.core.Tenancy;
import cloud.mmda.core.app.ApplicationContextProvider;
import cloud.mmda.core.caching.CachePolicy;
import cloud.mmda.core.caching.redis.DistributedLock;
import cloud.mmda.core.data.jdbc.repository.BackgroundTaskRepository;
import cloud.mmda.core.data.jdbc.repository.EntityRepository;
import cloud.mmda.core.data.pagination.PagedList;
import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.data.pagination.Sort;
import cloud.mmda.core.data.sql.SqlExpression;
import cloud.mmda.core.entities.*;
import cloud.mmda.core.enums.BackgroundTaskStatus;
//import cloud.mmda.core.mq.MmdaStreamSender;
import cloud.mmda.core.file.excel.ExcelExportProgressListener;
import cloud.mmda.core.file.excel.ExcelPagedWriter;
import cloud.mmda.core.file.exceptions.ExcelException;
import cloud.mmda.core.models.BackgroundTask;
import cloud.mmda.core.models.ReportTemplate;
import cloud.mmda.core.security.models.UserAccount;
import cloud.mmda.core.services.exceptions.DomainException;
import cloud.mmda.core.services.exceptions.NotFoundException;
import cloud.mmda.core.services.exceptions.OperationFailedException;
import cloud.mmda.core.services.exceptions.OperationNotAllowException;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.JsonUtil;
import cloud.mmda.core.utils.NamingUtil;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.math.BigDecimal;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static cloud.mmda.core.models.BackgroundTask.Meta.*;

/**
 * 后台任务Service
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2025-12-25 11:16:07.0
 * 
 */
@Service
public class BackgroundTaskService extends TenancyEntityService<BackgroundTask,Long> {

	// 配置全局最大并发
	@Value("${mmda.maxGlobalConcurrentTasks:100}")
	private  int MAX_GLOBAL_CONCURRENT_TASKS ; // 服务器同时最多执行20个任务

	@Value("${mmda.pageSize:1000}")
	private Integer maxPageSize;

	@Autowired
	private StringRedisTemplate redisTemplate;

	//region ~GENERATED PARTS BEGIN
	private static final Log logger = LogFactory.getLog(BackgroundTaskService.class);
	private final BackgroundTaskRepository repository;
	/**
	 * 构造函数
	 * @param repository 底层数据库访问仓储
	 * @param factory  redis连接工厂
	 */
	@Autowired
	public BackgroundTaskService(final BackgroundTaskRepository repository, final RedisConnectionFactory factory) {
		super(repository, factory);
		this.repository = repository;
	}

	//region load & save
	//endregion
	//endregion of ~GENERATED PARTS END

	/**
	 * 创建导出任务
	 * @param user 当前用户
	 * @param export 导出参数
	 * @param p 缓存策略
	 * @return 导出任务
	 */
	public BackgroundTask save(UserAccount user, EntityExport export, CachePolicy p) {
		//判断该用户是否存在进行中的任务
		if (countWorkingTaskByUser( user.getUserID(),p)>0)
			throw new OperationNotAllowException("not.allow.add.task.exits.working");

		BackgroundTask task = create(p);
		task.setCreatorID(user.getUserID());
		task.setDeptID(user.getDeptID());

		var serviceName = getServiceName(export.getObjName());
		var service = ApplicationContextProvider.getApplicationContext().getBean(serviceName, DomainService.class);

		task.setTaskName(service.getMetaObject().getDisplayLabel());

		// 计算记录数
		int recordCount = service.prepareExportRecordCount(export.getSearchWord(),export.getPayload(),export.getCondition(), p);
		export.setRecordCount(recordCount);

		if (export.getRecordCount()==0){
			throw new OperationFailedException("executeExportTask.no.record");
		}
		Timestamp expectedFinish = estimateExportTime( export);
		task.setCustomJson(JsonUtil.toJson(export));
		task.setExpectedFinish(expectedFinish);
		int result = save(task);

		if (result > 0) {
			try {
				//通知MQ异步执行任务
				send(BACKGROUND_TASK, task.getTaskID());
			}catch (Exception e){
				logger.error("通知MQ异步执行任务失败", e);
				var message =i18n.getLocalizedMessage("executeExportTask.failed").getMessage();
				updateStatus(task.getTaskID(), BackgroundTaskStatus.FAILED,message,p);
			}

		}
		return task;
	}

	private void handleExportTask(long taskID){
		// 分布式锁或状态检查
		String cacheKey = "ExecuteTask:"+taskID;
		logger.debug(cacheKey + " 开始... " + LocalDateTime.now());
		DistributedLock redisLock = new DistributedLock(cacheKey, redisTemplate);
		try {
			if (!redisLock.tryLock(Duration.ofMinutes(5))) {
				logger.warn(cacheKey + " 未能获取分布式锁，跳过本次任务。");
				// 延迟重新投递 MQ
//				sendDelayed(BACKGROUND_TASK, taskID, Duration.ofMinutes(2));
				return;
			}
			// ------------------
			// 全局并发控制
			// ------------------
			int runningTasks = countWorkingTaskGlobal(getDefaultCachePolicy(Tenancy.parseTenantID(taskID))); // 当前全局正在执行的任务数

			if (runningTasks >= MAX_GLOBAL_CONCURRENT_TASKS) {
				// 超过全局并发限制 → 延迟重新投递 MQ
				logger.info("全局并发任务数达到上限，延迟执行 taskID=" + taskID);
				sendDelayed(BACKGROUND_TASK, taskID, Duration.ofMinutes(2));
				return;
			}
			executeExportTask(taskID);

		} catch (Exception e) {
			logger.error(cacheKey + " 发生异常: " + e.getLocalizedMessage(), e);
			sendDelayed(BACKGROUND_TASK, taskID, Duration.ofMinutes(2));
		} finally {
			try {
				redisLock.tryRelease();
			} catch (Exception e) {
				logger.error(cacheKey + " 释放分布式锁失败: " + e.getLocalizedMessage(), e);
			}
		}
		logger.debug(cacheKey + " 结束... " + LocalDateTime.now());

	}

	/**
	 * 执行任务导出数据
	 *
	 * @param taskId 导出任务标识
	 */
	private <T extends Entity<?>> void executeExportTask(long taskId) {
		ExcelPagedWriter<T, ?> writer = null;
		CachePolicy p = getDefaultCachePolicy(Tenancy.parseTenantID(taskId));
		String file = null;
		BackgroundTask task = null;
		//内存运行状态
//		boolean isRunning = isRunning(taskId,p);
		AtomicBoolean running = new AtomicBoolean(true);
		try {
			task = getByKey(taskId, p).orElse(null);
			if (task == null || (task.getStatus()!=BackgroundTaskStatus.NEW && task.getStatus()!=BackgroundTaskStatus.RUNNING))
				return;

			task.setStartedTime(Timestamp.valueOf(LocalDateTime.now()));
			task.setStatus(BackgroundTaskStatus.RUNNING);
			task.setModified();
			save(task);

			EntityExport export = JsonUtil.fromJson(task.getCustomJson(), EntityExport.class);
			// 获取 Repository
			String repositoryName = NamingUtil.firstLetterLower(export.getObjName()) + "Repository";
			EntityRepository<T, ?> repositoryBean = ApplicationContextProvider.getApplicationContext()
					.getBean(repositoryName, EntityRepository.class);
			String serviceName = getServiceName(export.getObjName());
			var service = ApplicationContextProvider.getApplicationContext()
					.getBean(serviceName, DomainService.class);

			// 模板文件
			String templateUrl = null;
			if (!BaseUtil.isNullOrZero(export.getTemplateId())) {
				try {
					ReportTemplate reportTemplate = reportTemplateService.getById(export.getTemplateId());
					templateUrl = reportTemplate.getTemplateFile();
				} catch (Exception e) {
					logger.error("获取模板文件失败", e);
				}
			}

			// 分页导出监听器
			ExcelExportProgressListener listener = updateProgress(export, task, running);

			// 创建 ExcelPagedWriter
			writer = new ExcelPagedWriter<>(
					p.getTenantID(), repositoryBean, export.getColumns(),
					templateUrl,
					service.getMetaObject().getDisplayLabel(),
					export.getFormat(),
					null,
					listener
			);

			// 计算分页
			int recordCount = export.getRecordCount();
			int pageSize = maxPageSize == null ? 1000 : maxPageSize;
			int pages = recordCount / pageSize + (recordCount % pageSize > 0 ? 1 : 0);
			Sort sort = service.getDefaultSort();

			for (int i = 1; i <= pages && running.get(); i++) {
				Paginator paginator = new Paginator( pageSize,i, sort);
				// 低频检查是否被取消（每页一次）
				if (!isRunning(taskId, p)) {
					running.set(false);
					return;
				}

				PagedList<T> pagedList=service.getAllExportWithParams(paginator,export.getSearchWord(),p,export.getPayload(),export.getCondition());

				if (pagedList == null || pagedList.getData().isEmpty()) return;

				for (T entity : pagedList.getData()) {
					service.assemble(entity);
				}

				List<T> batch = pagedList.getData();

				if (BaseUtil.hasText(templateUrl))
					writer.writeTemplate( batch);
				else
					writer.write(batch);

			}
			// 完成写入
			if (running.get()) {
				file = writer.finish();
			}

		} catch (DomainException ex) {
			updateStatus(taskId, BackgroundTaskStatus.FAILED, ex.getLocalizedMessage(), p);
		} catch (ExcelException ex) {
			var message = BaseUtil.hasText(ex.getLocalizedMessage()) ?
					ex.getLocalizedMessage() :
					i18n.getLocalizedMessage(ex.getCode()).getMessage();
			updateStatus(taskId, BackgroundTaskStatus.FAILED, message, p);
		} catch (Exception e) {
			logger.error("导出任务失败 taskId=" + taskId, e);
			var message = i18n.getLocalizedMessage("executeExportTask.failed").getMessage();
			updateStatus(taskId, BackgroundTaskStatus.FAILED, message, p);
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException ex) {
				logger.error("关闭文件写入失败", ex);
			}
		}

		// 更新任务状态
		if (running.get())
			updateLastTaskStatus(task, file, p);

	}

	private void updateLastTaskStatus(BackgroundTask task, String file, CachePolicy p) {
		if (task==null || isCanceled( task.getTaskID(),p)) return;
		try {
			if (BaseUtil.hasText(file)) {
				task.setStatus(BackgroundTaskStatus.SUCCESS);
				task.setTaskResult(file);
				task.setFinishedTime(new Timestamp(System.currentTimeMillis()));
			} else {
				task.setStatus(BackgroundTaskStatus.FAILED);
				task.setTaskResult(i18n.getLocalizedMessage("upload.file.error").getMessage());
			}
			task.setModified();
			save(task);
		}catch (Exception e){
			logger.error("导出任务失败 taskId=" + task.getTaskID(), e);
			var message =i18n.getLocalizedMessage("updateTask.failed").getMessage();
			if (e instanceof DomainException ex){
				message=ex.getLocalizedMessage();
			}
			updateStatus(task.getTaskID(), BackgroundTaskStatus.FAILED,message,p);
		}
	}

	/**
	 * 更新导出进度
	 * @param export 导出参数
	 * @param task 任务
	 * @return
	 */
	public ExcelExportProgressListener updateProgress(EntityExport export, BackgroundTask task,AtomicBoolean running) {

		CachePolicy p = getDefaultCachePolicy(task.getTenantID());
		// 假设每批写入监听器传入 (totalRowsWritten, lastBatchSize)
		ExcelExportProgressListener listener = new ExcelExportProgressListener() {

			private final long startTime = task.getStartedTime()==null?System.currentTimeMillis():task.getStartedTime().getTime();
			private int lastSavedProgress = 0; // 上一次写入数据库的百分比

			@Override
			public void onProgress(int totalRowsWritten, int lastBatchSize) {
				try {

					int totalRows = export.getRecordCount(); // 总行数
					if (totalRows == 0 || !running.get() ) return;

					// 当前进度百分比
					int percent = (int) (totalRowsWritten * 100.0 / totalRows);

					// 每隔至少 5% 才写数据库，避免频繁更新
					if (percent - lastSavedProgress >= 5 || totalRowsWritten == totalRows) {
						lastSavedProgress = percent;

						// 已经耗时
						long elapsed = System.currentTimeMillis() - startTime;

						// 预计总耗时
						long estimatedTotal = (long) (elapsed / (totalRowsWritten / (double) totalRows));

						// 剩余时间
						long remainingMillis = estimatedTotal - elapsed;

						// 如果小于0，默认设置为3分钟
						if (remainingMillis < 0) {
							remainingMillis = 3 * 60 * 1000;
						}
						// 预计完成时间
						task.setExpectedFinish(new Timestamp(System.currentTimeMillis() + remainingMillis));

						// 更新任务对象
						task.setTaskProgress(new BigDecimal(percent).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
						export.setFinished(totalRowsWritten);
						task.setCustomJson(JsonUtil.toJson(export));
						task.setModified();
						save(task); // ⚠ 批量/异步可优化

					}
				}catch (DomainException  e){
					logger.error("修改task状态失败"+e.getLocalizedMessage(), e);
				}
				catch (Exception e) {
					logger.error("更新进度失败"+e.getLocalizedMessage(), e);
				}
			}
		};

		return listener;

	}

	/**
	 * 估计导出时间
	 * @param export 导出实体
	 * @return 预计完成时间
	 */
	public Timestamp estimateExportTime(EntityExport export) {
		int recordCount = export.getRecordCount();
		int pageSize = maxPageSize==null?1000:maxPageSize;
		int pages = (recordCount + pageSize - 1) / pageSize;


		int columnCount = export.getRecordCount(); // 假设你有这个字段
		double templateFactor = BaseUtil.isNullOrZero(export.getTemplateId()) ? 2.0 : 1.0;

		// 单页预计耗时(ms)
		long estimatedPageMs = (long) ((50 + columnCount * 5 + pageSize * 0.1) * templateFactor);
		long estimatedTotalMs = estimatedPageMs * pages;

		// 限制最大耗时 2 小时
		long maxMs = 2 * 60 * 60 * 1000; // 2小时
		if (estimatedTotalMs > maxMs) {
			estimatedTotalMs = maxMs;
		}

		long eta = System.currentTimeMillis() + estimatedTotalMs;
		Timestamp result = new Timestamp(eta);

//		logger.info("预计总耗时: {} s, 预计完成: {}", estimatedTotalMs / 1000, new java.util.Date(eta));

		return result;
	}


	public Optional<BackgroundTask> getByKey( long taskID,  CachePolicy p){
		try{
			BackgroundTask t = get(taskID, p);
			return Optional.of(t);
		}
		catch (NotFoundException ex){
			return Optional.ofNullable(null);
		}
	}

	private int countWorkingTaskGlobal(CachePolicy p) {
		// 查询全局正在执行的任务
		SqlExpression condition = sqlExpressionBuilder()
				.exp(_status).greaterOrEqual(BackgroundTaskStatus.NEW)
				.and(_status).lessOrEqual(BackgroundTaskStatus.RUNNING)
				.result();
		return countBy(condition, p);
	}

	private boolean isCanceled(long taskID, CachePolicy p) {
		// 查询全局正在执行的任务
		SqlExpression condition = sqlExpressionBuilder()
				.exp(_status).equal(BackgroundTaskStatus.CANCELED)
				.and(_taskID).equal(taskID)
				.result();
		return countBy(condition, p)>0;
	}
	private boolean isRunning(long taskID, CachePolicy p) {
		// 查询全局正在执行的任务
		SqlExpression condition = sqlExpressionBuilder()
				.exp(_status).equal(BackgroundTaskStatus.RUNNING)
				.and(_taskID).equal(taskID)
				.result();
		return countBy(condition, p)>0;
	}


	public int updateStatus(long taskID, BackgroundTaskStatus newStatus,String taskResult,CachePolicy p){
		SqlExpression att=sqlExpressionBuilder()
				.exp(_status).equal(newStatus)
				.and(_taskResult).equal(taskResult)
				.result();
		SqlExpression condition=sqlExpressionBuilder()
				.exp(_taskID).equal(taskID)
				.and(_status).notEqual(newStatus)
				.result();
		return partialUpdateAll(att, condition, p);


	}
	public int countWorkingTaskByUser(long userID,CachePolicy p){
		SqlExpression condition=sqlExpressionBuilder()
				.exp(_creatorID).equal(userID)
				.and(_status).greaterOrEqual(BackgroundTaskStatus.NEW)
				.and(_status).lessOrEqual(BackgroundTaskStatus.RUNNING)
				.result();
		return countBy(condition, p);


	}
   private static  final String BACKGROUND_TASK= "backgroundTask-out-0";


	//region MQ
    @Resource
    private StreamBridge streamBridge;

    public boolean send(String bindingName, Object data) {
        return streamBridge.send(bindingName,
                MessageBuilder
                        .withPayload(data)
                        .build()
        );
    }

    @Bean
    public Consumer<Long> backgroundTask() {
        return taskID -> {
			// 处理任务
			handleExportTask(taskID);
        };
    }
	@Bean
	public Consumer<Long> resumeBackgroundTask() {
		return taskID -> {
			// 处理任务
			handleExportTask(taskID);
		};
	}
	private void sendDelayed(String bindingName,long taskID, Duration delay) {
		Executors.newSingleThreadScheduledExecutor().schedule(
				() -> send(bindingName, taskID),
				delay.toMillis(),
				TimeUnit.MINUTES
		);
	}

	//endregion MQ


	public void resume(String taskID,String dbSchema){

	}
}
