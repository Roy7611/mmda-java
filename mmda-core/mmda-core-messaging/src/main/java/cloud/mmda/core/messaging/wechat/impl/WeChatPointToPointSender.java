package cloud.mmda.core.messaging.wechat.impl;

import cloud.mmda.core.messaging.wechat.WeChatMessage;
import cloud.mmda.core.messaging.wechat.WeChatSender;
import cloud.mmda.core.utils.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * @author LightHouse
 * @version 1.0.0
 * @date: 2025/04/14  13:08
 */
@Service
public class WeChatPointToPointSender extends WeChatSender {
    private final static Log logger = LogFactory.getLog(WeChatServiceAccountSender.class);

    private static final String API_PATH = "/qianxun/httpapi";

    private static URL buildUrl(String ip, int port, String senderWxid,Integer safekey) throws IOException {
        return new URL("http://" + ip + ":" + port + API_PATH + "?wxid=" + senderWxid+"&safekey="+safekey);
    }

    @Override
    public boolean send(WeChatMessage weChatMessage) {
        //参考文档 https://qxpro.apifox.cn/api-177838780
        try {
            String[] split = messageTemplateCode.split(";");
            // 构建请求URL
            URL url = buildUrl(split[0], Integer.parseInt(split[1]), split[2],Integer.parseInt(split[3]));

            // 将请求体转换为JSON字符串
            HashMap<Object, Object> map = new HashMap<>();
            String[] wxTokenSplit = wxToken.split(";");
            map.put("type", wxTokenSplit[weChatMessage.getType()]);
            if (weChatMessage.getType() == 0) {
                map.put("data", Map.of("wxid", weChatMessage.getTo(), "msg", weChatMessage.getText()));
            } else if (weChatMessage.getType() == 1) {
                /*
                {
                    "type": "sendShareUrl",
                    "data": {
                        "wxid": "wxid_awdys1jtcb6j22",
                        "title": "003 上海颂华 - 湿度PV(2.0)超过预设范围",
                        "content": "点我进入报警详情",
                        "jumpUrl": "https://lhyb24748355.vicp.fun/IOT/Alarms/52917295621639389",
                        // "app": "wx64f9cf5b17af074d",
                        "path": "C:\\Users\\86153\Pictures\\SavedPictures\\CSDN.png"
                    }
                }
                 */
                map.put("data", Map.of(
                        "wxid", weChatMessage.getTo(),
                        "title", weChatMessage.getText()
                        , "content", weChatMessage.getContent()
                        , "jumpUrl", weChatMessage.getJumpUrl()
                        , "path", "a"

                ));
            }
            String request = JsonUtil.toJson(map);

            // 发送HTTP请求
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                assert request != null;
                byte[] input = request.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 获取响应状态码
            int responseCode = conn.getResponseCode();

            // 如果状态码不是200，抛出异常或处理错误
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP request failed with response code: " + responseCode);
            }

            // 读取响应内容
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }

            // 将响应内容解析为 WeChatResponse 对象
            return true;
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
    }
}