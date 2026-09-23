package cloud.mmda.core.security.utils;

import cloud.mmda.core.security.models.OpenIdentity;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户名解析器，能解析国际手机号、邮箱和OpenID
 * <ul>
 *     <li>手机号格式：中国+86 13323238888, 美国+1 408 345 6789</li>
 *     <li>邮箱：roshion.luo@syclive.com</li>
 *     <li>OpenID：{wechat}12383333, {google}lostown@gmail.com</li>
 * </ul>
 */
public abstract class UsernameParser {

    private final static Pattern mobilePattern =  Pattern.compile("^(?:\\+(?:(?<countryAreaCode>\\d+)[\\s|-]))*(?<phoneNumber>[\\d|\\s|-]{7,25})$");
    public final static Optional<InternationalMobile> parseMobile(final String username) {
        Assert.notNull(username, "Username must not be null");
        Matcher m = mobilePattern.matcher(username);
        if (m.matches()) {
            return Optional.of(new InternationalMobile(m.group("countryAreaCode"), m.group("phoneNumber")));
        }
        return Optional.empty();
    }

    private final static Pattern emailPattern =  Pattern.compile("^([\\w|\\.]+)@\\w+(\\.\\w+)+$");
    public final static boolean isEmail(final String username) {
        if (StringUtils.isEmpty(username)) return false;
        Matcher m = emailPattern.matcher(username);
        return m.matches();
    }
    private final static Pattern openIdPattern =  Pattern.compile("^\\{(\\w+)\\}(\\w+)$");
    public final static boolean parseOpenId(final String username, @NonNull final OpenIdentity openId) {
        Assert.notNull(username, "Username must not be null");
        Matcher m = openIdPattern.matcher(username);
        boolean parsed = m.matches();
        if (parsed) {
            openId.setOpenIDType(m.group(1));
            openId.setOpenID(m.group(2));
        }
        return parsed;
    }
    public final static Optional<OpenIdentity> parseOpenId(final String username) {
        Assert.notNull(username, "Username must not be null");
        Matcher m = openIdPattern.matcher(username);
        if (m.matches()) {
            return Optional.of(new OpenIdentity(m.group(1),m.group(2)));
        }
        return Optional.empty();
    }
}
