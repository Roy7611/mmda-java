package cloud.mmda.core.enums;

import lombok.Getter;

public enum SqlParameterDirection implements EnumValue<Integer> {
    INPUT(1),
    OUTPUT(2),
    INPUT_OUTPUT(3),
    RETURN_VALUE(6);

    @Getter
    private Integer value;

    SqlParameterDirection(int value) {
        this.value = value;
    }
}
