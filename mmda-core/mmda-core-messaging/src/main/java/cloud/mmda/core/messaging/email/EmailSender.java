package cloud.mmda.core.messaging.email;

import cloud.mmda.core.messaging.Sender;
import cloud.mmda.core.messaging.config.MessagingConfiguration;
import cloud.mmda.core.utils.BaseUtil;
import jakarta.activation.URLDataSource;
import jakarta.annotation.Resource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;

import jakarta.mail.internet.MimeMultipart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import jakarta.activation.DataHandler;
import java.net.URL;



/**
 * 阿里云短信发送器
 */
@Service
public class EmailSender implements Sender<EmailMessage> {
    private final static Log logger = LogFactory.getLog(EmailSender.class);
    //@Value("${spring.mail.username}")
    //public String USER_NAME;//发送者
    //@Resource
    //private JavaMailSender mailSender;//执行者
    @Resource
    private MessagingConfiguration configuration;
    @Override
    public boolean send(EmailMessage email) {
        try {
            logger.info("发送邮件：" + email.getText());
            //SimpleMailMessage message = new SimpleMailMessage();
            //message.setFrom(USER_NAME);
            //message.setTo(email.getTo()); //收件方
            //message.setSubject(email.getTitle()); //主题
            //message.setText(email.getText());// 邮件内容
            //mailSender.send(message);
            // smtp配置，可保存到properties文件，读取
            Properties props = new Properties();
            props.put("mail.smtp.host", configuration.getMail().getHost());
            props.put("mail.debug", true);
            props.put("mail.smtp.port", configuration.getMail().getPort());
            props.put("mail.smtp.ssl", true);
            //// 需要认证
            props.put("mail.smtp.auth", configuration.getMail().getSmtpAuth());
            props.put("mail.smtp.user", configuration.getMail().getUsername());
            props.put("mail.smtp.pass", configuration.getMail().getPassword());
            // 使用ssl
            props.put("mail.smtp.socketFactory.class", configuration.getMail().getSmtpSocketFactoryClass());
            props.put("mail.smtp.socketFactory.fallback", configuration.getMail().getSmtpSocketFactoryFallback());
            props.put("mail.smtp.socketFactory.port", configuration.getMail().getSmtpSocketFactoryPort());
            props.put("mail.smtp.starttls.required", configuration.getMail().getSmtpStarttlsRequired());
            props.put("mail.smtp.starttls.enable", configuration.getMail().getSmtpStarttlsEnable());
            // 创建会话
            Boolean auth = configuration.getMail().getSmtpAuth();
            String username = configuration.getMail().getUsername();
            String password = configuration.getMail().getPassword();
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    if (auth) {
                        // 需要认证
                        return new PasswordAuthentication(username, password);
                    }
                    return super.getPasswordAuthentication();
                }
            });
            // 构建邮件消息
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            String[] to = {email.getTo()};
            InternetAddress[] address = new InternetAddress[to.length];
            for (int i = 0, j = to.length; i < j; i++) {
                address[i] = new InternetAddress(to[i]);
            }
            // 可以用msg.setRecipients方法增加多个接收人，指定接收人类型
            // Message.RecipientType.CC 抄送
            // Message.RecipientType.BCC 密送
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(email.getTitle());
            msg.setText(email.getText());
            msg.setSentDate(new Date());

            // 创建消息和附件部分
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(email.getText());
            // 创建附件部分
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // 添加附件
            boolean addAttachment=addAttachment(multipart,email.getAttachments());
            if (!addAttachment) return false;

            // 设置邮件内容
            msg.setContent(multipart);


            // 发送邮件
            Transport.send(msg);
            logger.info("发送邮件成功");
            return true;
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
    }

    private boolean  addAttachment( Multipart multipart,List<String> attachments){
        try {
            // 添加附件
            if (BaseUtil.hasAny(attachments)) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                String fileName;
                for (String attachment : attachments) {
                    if (attachment.startsWith("http://") || attachment.startsWith("https://")) {
                        String encodedUrl = encodeURL(attachment); // 使用编码后的 URL
                        URL url = new URL(encodedUrl);
                        URLDataSource dataSource = new URLDataSource(url);
                        attachmentPart.setDataHandler(new DataHandler(dataSource));
                        fileName = attachment.substring(attachment.lastIndexOf("/") + 1);
                    }else{
                        // 处理文件路径类型的附件
                        File file = new File(attachment);
                        if (!file.exists() || !file.isFile()) {
                            logger.warn("附件路径无效: " + attachment);
                            continue;
                        }
                        attachmentPart.attachFile(file);
                        fileName = file.getName();
                    }
                    logger.info("文件地址" + fileName);
                    attachmentPart.setFileName(fileName);
                    multipart.addBodyPart(attachmentPart);
                }

            }
            return true;
        }catch (Exception e){
            logger.error("发送邮件【添加附件】失败"+e.getMessage(),e);
            return false;
        }

    }

    public static String encodeURL(String url) {
        try {
            // 对 URL 中的路径部分进行编码（防止中文或特殊字符问题）
            URL originalUrl = new URL(url);
            String protocol = originalUrl.getProtocol();
            String host = originalUrl.getHost();
            int port = originalUrl.getPort();
            String path = originalUrl.getPath();
            String query = originalUrl.getQuery();

            // 编码路径和查询参数
            String encodedPath = URLEncoder.encode(path, "UTF-8").replace("%2F", "/");
            String encodedQuery = query != null ? URLEncoder.encode(query, "UTF-8") : "";

            // 构造编码后的 URL
            return protocol + "://" + host + (port != -1 ? ":" + port : "") + encodedPath + (query != null ? "?" + encodedQuery : "");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return url;
        }
    }

}
