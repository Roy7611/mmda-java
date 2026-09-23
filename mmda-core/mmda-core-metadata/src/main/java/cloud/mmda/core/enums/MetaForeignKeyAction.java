package cloud.mmda.core.enums;

import lombok.Getter;

public enum MetaForeignKeyAction implements EnumValue<Byte> {
    NO_ACTION(0, "NO ACTION"),
    RESTRICT(1, "RESTRICT"),
    CASCADE(2, "CASCADE"),
    SET_DEFAULT(3, "SET DEFAULT"),
    SET_NULL(4, "SET NULL"),
    ;

    @Getter
    private final Byte value;

    @Getter
    private final String text;

    MetaForeignKeyAction(int value, String text) {
        this.value = (byte) value;
        this.text = text;
    }

    public static MetaForeignKeyAction parse(String text) {
        for (MetaForeignKeyAction action : MetaForeignKeyAction.values()) {
            if (action.text.equalsIgnoreCase(text)) return action;
        }
        return NO_ACTION;
    }
}
