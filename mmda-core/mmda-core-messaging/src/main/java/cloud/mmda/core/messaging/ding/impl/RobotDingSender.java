package cloud.mmda.core.messaging.ding.impl;

import cloud.mmda.core.utils.JsonUtil;
import com.aliyun.dingtalkrobot_1_0.Client;
import cloud.mmda.core.messaging.config.MessagingConfiguration;
import cloud.mmda.core.messaging.ding.DingMessage;
import cloud.mmda.core.messaging.ding.DingSender;
import cloud.mmda.core.utils.BaseUtil;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTOResponse;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTOResponseBody;
import com.aliyun.teaopenapi.models.Config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTOHeaders;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTORequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiV2UserGetbymobileRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiV2UserGetbymobileResponse;
import com.taobao.api.ApiException;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class RobotDingSender extends DingSender {
    private final static Log logger = LogFactory.getLog(RobotDingSender.class);
    // 企业凭证，两小时一更新
    private String enterpriseToken = "";
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private MessagingConfiguration configuration;
    public void getToken() throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(configuration.getDing().getGetTokenServiceUrl());
        OapiGettokenRequest req = new OapiGettokenRequest();
        req.setAppkey(configuration.getDing().getKey());
        req.setAppsecret(configuration.getDing().getSecret());
        req.setHttpMethod("GET");
        OapiGettokenResponse rsp = client.execute(req);
        logger.info("token:" + rsp.getBody());
        JSONObject json = JSON.parseObject(rsp.getBody());
        enterpriseToken = json.getString("access_token");
    }

    @Override
    public boolean send(DingMessage dingMessage) {
        String[] array = dingMessage.getTo().split(",");
        List<String> mobileList = Arrays.asList(array);
        String content = dingMessage.getText();
        String userID = getUserID(mobileList);
        logger.error(String.format("PHONE【%s】USERID:::::::::::【%s】", JsonUtil.toJson(mobileList),userID));
        try {
            if (BaseUtil.isNullOrEmpty(userID)) return false;

            return createDingNotify(userID, content,dingMessage.getOutID());
        } catch (Exception e) {
            return false;
        }
    }

    private boolean createDingNotify(String dingUserId, String content, String outID) throws ApiException {
        try {

            if (BaseUtil.isNullOrEmpty(enterpriseToken)) {
                getToken();
            }
            Client client = createClient();
            BatchSendOTOHeaders batchSendOTOHeaders = new BatchSendOTOHeaders();
            batchSendOTOHeaders.xAcsDingtalkAccessToken = enterpriseToken;
            BatchSendOTORequest batchSendOTORequest = new BatchSendOTORequest()
                    .setRobotCode(configuration.getDing().getKey())
                    .setUserIds(Collections.singletonList(dingUserId))
                    .setMsgKey("officialTextMsg")
                    .setMsgParam("{ \"content\": \"" + content + "\" }");
            BatchSendOTOResponse batchSendOTOResponse = client.batchSendOTOWithOptions(batchSendOTORequest, batchSendOTOHeaders, new RuntimeOptions());
            if (batchSendOTOResponse.statusCode.compareTo(200) == 0 && !BaseUtil.isNullOrEmpty(outID)) {
                logger.error(String.format("【BatchSendOTOResponse】:::::::::::::::::::::::::::【%s】", JsonUtil.toJson(batchSendOTOResponse)));
                BatchSendOTOResponseBody body = batchSendOTOResponse.getBody();
                String processQueryKey = body.getProcessQueryKey();
                String cacheKey = String.join(":",   RobotDingSender.class.getSimpleName(), processQueryKey);
                stringRedisTemplate.opsForValue().set(cacheKey, outID, Duration.ofMinutes(15));
            }
        } catch (TeaException err) {
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                logger.error(err.code + ":" + err.message);
                // 企业凭证enterpriseToken不合法导致出错时获取新企业凭证并重试
                if (err.code.equals("InvalidAuthentication")) {
                    getToken();
                    createDingNotify(dingUserId, content, outID);
                }
            }
        } catch (Exception e) {
            TeaException err = new TeaException(e.getMessage(), e);
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err中含有code和message 属性，可帮助开发定位问题
                logger.error(err.code + ":" + err.message);
            }

        }
        return true;
    }

    private Client createClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new Client(config);
    }

    private String getUserID(List<String> mobileList) {
        try {
            if (BaseUtil.isNullOrEmpty(enterpriseToken)) {
                getToken();
            }
            DingTalkClient client = new DefaultDingTalkClient(configuration.getDing().getGetByMobileServiceUrl());
            OapiV2UserGetbymobileRequest req = new OapiV2UserGetbymobileRequest();
            req.setMobile(mobileList.getFirst());
            OapiV2UserGetbymobileResponse rsp = client.execute(req, enterpriseToken);
            if (rsp != null) {
                OapiV2UserGetbymobileResponse.UserGetByMobileResponse result = rsp.getResult();
                return result == null ? null : result.getUserid();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
