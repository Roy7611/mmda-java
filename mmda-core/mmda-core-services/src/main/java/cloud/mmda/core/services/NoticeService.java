/**
 * Copyright (c) 2006, 2024, www.syclive.com All rights reserved.
 * MMDA.CLOUD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Please don't modify any code between GENERATED PARTS BEGIN and END
 * 
 */
package cloud.mmda.core.services;
import cloud.mmda.core.Tenancy;
import cloud.mmda.core.caching.CachePolicy;
import cloud.mmda.core.data.sql.SqlExpression;
import cloud.mmda.core.entities.*;
import cloud.mmda.core.enums.*;
import cloud.mmda.core.messaging.config.MessagingConfiguration;
import cloud.mmda.core.messaging.email.EmailMessage;
import cloud.mmda.core.messaging.email.EmailSender;
import cloud.mmda.core.messaging.sms.SmsMessage;
import cloud.mmda.core.messaging.sms.SmsSender;
import cloud.mmda.core.messaging.wechat.WeChatMessage;
import cloud.mmda.core.messaging.wechat.WeChatSender;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.models.FlowTrail;
import cloud.mmda.core.models.Notice;
import cloud.mmda.core.security.models.User;
import cloud.mmda.core.security.models.UserOpenIdentity;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cloud.mmda.core.data.jdbc.repository.NoticeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

import static cloud.mmda.core.models.Notice.Meta.*;

/**
 * 通知Service
 * 
 * @author mmda code robot 
 * @version 4.0.0 
 * @since 2024-08-11 22:39:57.0
 * 
 */
@Service
public class NoticeService extends TenancyEntityService<Notice,Long>{
	//region ~GENERATED PARTS BEGIN
	private static final Log logger = LogFactory.getLog(NoticeService.class);
	private final NoticeRepository repository;
	/**
	 * 构造函数
	 * @param repository 底层数据库访问仓储
	 * @param factory  redis连接工厂
	 */
	@Autowired
	public NoticeService(final NoticeRepository repository, final RedisConnectionFactory factory) {
		super(repository, factory);
		this.repository = repository;
	}

	//region load & save
	//endregion
	//endregion of ~GENERATED PARTS END
	@Autowired
	private  SmsSender smsSender;
	@Autowired
	private EmailSender emailSender;
	@Autowired
	private WeChatSender weChatSender;
	@Autowired
	private MessagingConfiguration configuration;
	/**
	 * 推送通知，
	 * @param user
	 * @param t
	 * @return
	 */
	public int pushNotices(User user, FlowTrail t){
		ArrayList<Notice> notices = new ArrayList<>();
		int cnt = 0;
		//system notice
		var notice = this.repository.create(t.getTenantID());
		notice.setImportance(t.getImportance());
		notice.setEmergency(t.getUrgency());
		notice.setNoticeContent(t.getNotification());
		notice.setNoticeToUserID(user.getUserID());
		notice.setNoticeTo(user.getUsername());
		notice.setNotifyingThru(MessageChannel.INTERNAL);
		notice.setRefName(t.getObjName());
		notice.setRefID(t.getObjID());
		if(t.getOwnerID() == user.getUserID()) notice.setFlowTrailID(t.getTrailID());
		notice.setStatus(NotificationStatus.SENT);//No need to be really sent
		notices.add(notice);
		cnt++;
		NoticeParam noticeParam = new NoticeParam();
		noticeParam.setUserName(BaseUtil.isNullOrEmpty(t.getRefProperty("actorID"))? t.getRefProperty("ownerID") : t.getRefProperty("actorID"));
		noticeParam.setActionName(t.getRefProperty("actionName"));
		noticeParam.setObjName(t.getObjName());
		noticeToUser(user,notice,noticeParam);
		this.repository.insertMany(t.getTenantID(),notices);
		return cnt;
	}
	public void noticeToUser(User user, Notice t, NoticeParam param) {
		//ERP消息通知：${userNameAndAction}了${thing}，留言：${notice}，重要性：${import}，紧急性：${urgency}。
		MetaObject metaObject = repository.getMetadataProvider().getMetaObject(param.getObjName());
		StringBuilder notification = new StringBuilder().append("ERP消息通知：")
				.append(param.getUserName()).append(param.getActionName()).append("了").append(metaObject.getDisplayLabel())
				.append("，留言：").append(t.getNoticeContent())
				.append("，重要性：").append(t.getImportance().getText())
				.append("，紧急性：").append(t.getEmergency().getText())
				.append("。");
		if (user.getSubscribedChannels().hasFlag(MessageChannel.MAIL) && StringUtils.hasText(user.getEmail())) {
			EmailMessage emailMessage = new EmailMessage();
			emailMessage.setTo(user.getEmail());
			emailMessage.setTitle("ERP消息通知");
			emailMessage.setText(notification.toString());
			emailSender.send(emailMessage);
		}
		if (user.getSubscribedChannels().hasFlag(MessageChannel.SMS) && StringUtils.hasText(user.getMobile())) {
			// push sms notice
			SmsMessage smsMessage = new SmsMessage();
			smsMessage.setTo(user.getMobile());
			smsMessage.setSignature(configuration.getSms().getSignature());
			String[] templateCodeSplit = configuration.getSms().getTemplateCode().split(";");
			String templateCode = templateCodeSplit[0];
			smsMessage.setTemplateCode(templateCode);
			//${actorName}在${actTime}的时候${action}了${thing}，说：${notice}，事情很${importance},请及时处理！！！！
			//${actorName}在${actTime}的时候${action}了${thing}，说：${notice}，事情很${importance},请知悉！！！！
			//ERP消息通知：${userNameAndAction}了${thing}，留言：${notice}，重要性：${import}，紧急性：${urgency}。
			String noticeContent = t.getNoticeContent();
			noticeContent = noticeContent.replaceAll("\\[.*?]", "");//不支持[]
			noticeContent = noticeContent.trim().replaceAll("\\.", "");//不支持【.】
			if (noticeContent.length() > 35)
				noticeContent = noticeContent.substring(0, 35);
			smsMessage.setTemplateParams(JsonUtil.toJson(Map.of(
							"userNameAndAction", (param.getUserName() + param.getActionName()).trim().replaceAll("\\.", ""),
							"thing", metaObject.getDisplayLabel(),
							"notice", noticeContent,
							"import", t.getImportance().getText(),
							"urgency", t.getEmergency().getText()
					))
			);
				smsSender.send(smsMessage);
		}
		if (user.getSubscribedChannels().hasFlag(MessageChannel.PUSH) && StringUtils.hasText(user.getMobile())) {
			//TODO push notification to app
			logger.error("PUSH");
		}
		if (user.getSubscribedChannels().hasFlag(MessageChannel.WECHAT) && !user.getOpenIdentities().isEmpty() && user.getOpenIdentities().stream().anyMatch(o -> o.getOpenIDType().equals(MessageChannel.WECHAT.getText()))) {
			// push notification to WECHAT
			Optional<UserOpenIdentity> optional =
					user.getOpenIdentities().stream().filter(o -> o.getOpenIDType().equals(MessageChannel.WECHAT.getText())).findAny();
			if (optional.isPresent()) {
				UserOpenIdentity openIdentity = optional.get();
				WeChatMessage body = new WeChatMessage();
				//接收消息微信OpenID
				body.setTo(openIdentity.getOpenID());
				//String notification = t.getNoticeContent();
				body.setText(notification.toString());
				body.setType(0);
				weChatSender.send(body);
			}
		}
	}

	public int read(Long noticeID){
		try {
			SqlExpression att=sqlExpressionBuilder()
					.exp(_status).equal(NotificationStatus.READ)
					.result();
			return partialUpdate(noticeID,att,getDefaultCachePolicy(Tenancy.parseTenantID(noticeID)));
		}catch (Exception e){
			logger.error(String.format("notice.read.error :{%s}",e.getLocalizedMessage()),e.getCause());
			return 0;
		}
	}
	public int getTodoCountByAcctID(long acctID, CachePolicy p){
		SqlExpression conditon=sqlExpressionBuilder()
				.exp(_noticeToUserID).equal(acctID)
				.and(_todo).equal(true)
				.result();
		return countBy(conditon,p);
	}
	public int updateDoneByFlowTrailIDs(List<Long> flowTrailIDs){
//		SqlExpression att=sqlExpressionBuilder()
//				.exp(_status).equal(NotificationStatus.DONE)
//				.result();
//		SqlExpression conditon=sqlExpressionBuilder()
//				.exp(_status).notEqual(NotificationStatus.DONE)
//				.and(_flowTrailID).in(flowTrailIDs.stream().map(String::valueOf).toArray(String[]::new))
//				.result();
//		return repository.updateAll(att,conditon);
		return repository.updateDoneByIds(flowTrailIDs);
	}

	public int insertMany(int tenantID, List<Notice> notices){
		return Arrays.stream(repository.insertMany(tenantID,notices)).sum();
	}
}
