package cloud.mmda.core.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ImportResult {
    @Setter
    @Getter
    private String fileName;
    @Setter
    @Getter
    private int successCount;
    @Setter
    @Getter
    private int failedCount;
    @Setter
    @Getter
    private Object datas;
    @Setter
    @Getter
    private String failedUrl;
    @Setter
    @Getter
    private List<FileImportErrorInfo> errors;

    public void incrementSuccessCount(int count) {
        this.successCount += count;
    }

    public void incrementFailedCount() {
        this.failedCount++;
    }

    public ImportResult withErrors(List<FileImportErrorInfo> errors) {
        this.errors = errors;
        return this;
    }

    public List<FileImportErrorInfo> getErrors() {
        return errors;
    }


}

