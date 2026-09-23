package cloud.mmda.core.messaging.wechat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//{"action_name": "QR_LIMIT_SCENE", "action_info": {"scene": {"scene_id": 123}}}
//{"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene": {"scene_str": "123"}}}
//临时二维码
//{"expire_seconds": 604800, "action_name": "QR_STR_SCENE", "action_info": {"scene": {"scene_str": "test"}}}
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrCode {
    private long expire_seconds;
    private String action_name;
    private Scene action_info;
}
