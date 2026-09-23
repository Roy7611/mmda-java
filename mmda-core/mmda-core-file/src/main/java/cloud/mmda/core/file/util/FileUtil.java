package cloud.mmda.core.file.util;

import org.springframework.core.io.buffer.DataBuffer;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtil {
    public static List<File> unzipToFileList(File zipFile, File destDirectory) throws IOException {
        List<File> fileList = new ArrayList<>();

        // 创建目标目录（若不存在）
        if (!destDirectory.exists()) {
            destDirectory.mkdirs();
        }

        try (ZipFile zf = new ZipFile(zipFile)) {
            Enumeration<? extends ZipEntry> entries = zf.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();
                File targetFile = new File(destDirectory.getPath(), entryName);

                // 处理路径安全问题（防止 ZIP 炸弹或路径穿越攻击）
                if (!targetFile.toPath().normalize().startsWith(destDirectory.toPath())) {
                    throw new IOException("非法路径: " + entryName);
                }

                // 创建目录或文件
                if (entry.isDirectory()) {
                    targetFile.mkdirs();
                } else {
                    // 确保父目录存在
                    targetFile.getParentFile().mkdirs();

                    // 写入文件内容
                    try (InputStream is = zf.getInputStream(entry);
                         OutputStream os = new FileOutputStream(targetFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = is.read(buffer)) > 0) {
                            os.write(buffer, 0, len);
                        }
                    }

                    // 将文件加入列表
                    fileList.add(targetFile);
                }
            }
        }
        return fileList;
    }

    public static void zipFiles(List<File> files, File zipFile) throws IOException {
        byte[] buffer = new byte[1024];
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (File file : files) {
                try (InputStream is = new FileInputStream(file)) {
                    zout.putNextEntry(new ZipEntry(file.getName()));
                    int len;
                    while ((len = is.read(buffer)) > 0) {
                        zout.write(buffer, 0, len);
                    }
                }
            }
        }
    }

    public static void dataBufferToFile(DataBuffer dataBuffer,File file){
        try {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            new FileOutputStream(file).write(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
