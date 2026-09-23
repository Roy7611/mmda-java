package cloud.mmda.core.messaging.push;

import com.aliyuncs.AcsRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushRequest{
    private String target;
    private String title;
    private String content;
    private String url;
    private String icon;
    private String sound;
    private String badge;
    private String category;
    private String extra;
    private String extraData;
    private String targetValue;
    private String pushType;
    private String deviceType;
    private String body;
    private String pushTime;
    private String expireTime;
    private boolean storeOffline;
    private long appKey;
    private String androidNotifyType;
    private int androidNotificationBarType;
    private int androidNotificationBarPriority;
    private String androidOpenType;
    private String androidMusic;
    private String androidPopupTitle;
    private String androidPopupBody;
    private String androidNotificationChannel;
    private String androidExtParameters;
    private int iOSBadge;
    private String iOSMusic;
    private boolean iOSMutableContent;
    private String iOSApnsEnv;
    private boolean iOSRemind;
    private String iOSRemindBody;
    private String iOSExtParameters;
}
