package cloud.mmda.core.file.excel.bindings;

import lombok.Getter;

public class BindingResult {

    @Getter
    private int status;
    @Getter
    private String message;

    public BindingResult(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

    public static final BindingResult OK = new BindingResult(0, "OK");
    public static final BindingResult error(final String message) {
        return new BindingResult(1, message);
    }
}
