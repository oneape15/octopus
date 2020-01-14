package com.oneape.octopus.commons.files;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipUtils {
    private static final int BUFFER_SIZE = 2 * 1024;

    /**
     * 压缩文件
     *
     * @param srcFile File 需要压缩的文件
     * @param out     OutputStream 压缩文件输出流
     * @throws RuntimeException e
     */
    public static void toZip(File srcFile, OutputStream out) throws RuntimeException {
        StopWatch watch = new StopWatch();
        watch.start();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);

            byte[] buf = new byte[BUFFER_SIZE];
            zos.putNextEntry(new ZipEntry(srcFile.getName()));
            int len;
            FileInputStream fis = new FileInputStream(srcFile);
            while ((len = fis.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            fis.close();

            watch.stop();
            log.info("压缩完成，耗时：{}ms", watch.getTime());
        } catch (Exception e) {
            throw new RuntimeException("压缩错误", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    log.error("关闭ZipOutputStream失败", e);
                }
            }
        }
    }
}
