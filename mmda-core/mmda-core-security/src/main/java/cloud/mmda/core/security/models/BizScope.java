package cloud.mmda.core.security.models;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Top level business scope in an enterprise.
 *
 * @author Roy Luo
 * @since 4.0
 */
public enum BizScope {
    GENERAL(0, "总","CRM,MOM,SRM,WMS,PM,HRM,FM", -1),

    BUSINESS(1, "商务", "CRM",0),
    MARKETING(2,"市场",  "CRM",1),
    SALES(4,"销售",  "CRM",2),
    DESIGN(8, "方案设计",  "CRM",3),

    OPERATION(16, "运营", "MOM,PM,SRM,WMS",4),
    RnD(32,"研发","MOM",5),
    PURCHASING(64, "采购", "SRM",6),
    PRODUCTION(128, "生产", "MOM",7),

    SERVICE(256, "服务", "PM,CRM,SRM,WMS",8),
    LOGISTICS(512, "物流", "WMS,WES",9),
    CONSTRUCTION(1024, "施工", "PM,SRM",10),
    RMA(2048, "售后", "PM,CRM",11),

    HR(4096, "人力资源", "HRM",12),
    FINANCIAL(8192,"财务", "FM",13),
    IT(16384, "信息技术", "BASE",14);

    private final int value;
    private final String text;
    private final String scopes;
    private final int bit;

    BizScope(int value, String text, String scopes, int bit) {
        this.value = value;
        this.text = text;
        this.scopes = scopes;
        this.bit = bit;
    }

    public int getValue() {
        return value;
    }
    public String getText() {
        return text;
    }
    public String getScopes() {
        return scopes;
    }
    public int getBit() {
        return this.bit;
    }

    public static final EnumSet<BizScope> allOf = EnumSet.range(GENERAL, GENERAL);
    public static final EnumSet<BizScope> setOf(int value) {
        EnumSet<BizScope> enumSet = EnumSet.noneOf(BizScope.class);
        if (value > 0) {
            Iterator var2 = allOf.iterator();

            while(var2.hasNext()) {
                BizScope e = (BizScope)var2.next();
                if (hasBit(value, e.bit)) {
                    enumSet.add(e);
                }
            }
        }

        return enumSet;
    }

    public static final int valueOf(EnumSet<BizScope> enumSet) {
        return (Integer)enumSet.stream().map((e) -> {
            return getBit(e.getValue(), e.getBit());
        }).reduce(0, (a, b) -> {
            return a | b;
        });
    }

    public static final String textOf(EnumSet<BizScope> enumSet) {
        return (String)enumSet.stream().map((e) -> {
            return e.getText();
        }).collect(Collectors.joining(" "));
    }

    public static final boolean hasBit(int value, int bit) {
        if (bit < 0) {
            return false;
        } else {
            return (value >> bit & 1) == 1;
        }
    }

    public static final int getBit(int value, int bit) {
        return bit < 0 ? value : value >> bit & 1;
    }

}
