package cloud.mmda.core.file.excel;

@FunctionalInterface
public interface ExcelExportProgressListener {
    /**
     * @param totalRowsWritten 已写入总行数
     * @param lastBatchSize 本次写入行数
     */
    void onProgress(int totalRowsWritten, int lastBatchSize);
}

