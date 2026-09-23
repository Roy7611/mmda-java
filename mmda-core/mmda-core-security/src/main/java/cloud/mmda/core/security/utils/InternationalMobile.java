package cloud.mmda.core.security.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * International mobile phone number includes country and area code prefix followed by phone number
 *
 * @Remark
 * <ul>
 *     <li>国内手机号码时，所填号码可以不带+86，系统默认添加86</li>
 *     <li>其他号码：+{国家码}{地区码}{终端号码}（华为）</li>
 *     <li>国内短信：+/+86/0086/86 或无任何前缀的 11 位手机号码，例如 1390000****（阿里）</li>
 *     <li>国际/港澳台消息：国际区号+号码，例如 852000012****（阿里）</li>
 * </ul>
 *
 * @author Roy Luo
 * @since 4.0
 */
@Data
@AllArgsConstructor
public class InternationalMobile {
    private String countryAreaCode;
    private String phoneNumber;

    /**
     * Returns standard formatted phone number that can be called or send a message to
     * @return
     */
    public String getCallablePhoneNumber() {
        if(StringUtils.hasText(countryAreaCode)) {
            return countryAreaCode + StringUtils.deleteAny(phoneNumber,"- ");
        }
        return phoneNumber;
    }
}
