package com.oneape.octopus.commons.files;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.Collection;

@Slf4j
public class CSVHelper {
    private final static CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
    private final static String commonCsvHead = new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
    private final static String div = "\t";

    private static ThreadLocal<FileWriter> fileWriter = new ThreadLocal<>();
    private static ThreadLocal<CSVPrinter> csvPrinter = new ThreadLocal<>();

    private CSVHelper() {
        log.info("CSVHelper init...");
    }

    /**
     * 初始化csv文件
     *
     * @param fullFileName String
     * @return boolean
     */
    public static boolean initCSVFile(String fullFileName) {
        if (!StringUtils.isNotBlank(fullFileName) || !StringUtils.endsWith(fullFileName, ".csv")) {
            return false;
        }
        try {
            File file = new File(fullFileName);
            if (file.exists()) {
                boolean status = file.delete();
                if (!status) {
                    throw new RuntimeException("删除已存在文件失败~");
                }
            }

            fileWriter.set(new FileWriter(fullFileName, true));
            fileWriter.get().write(commonCsvHead);
            csvPrinter.set(new CSVPrinter(fileWriter.get(), csvFormat));

            return true;
        } catch (Exception e) {
            log.error("初始化csv文件失败", e);
        }
        return false;
    }

    /**
     * 写入一行
     *
     * @param row Collection<Object>
     * @throws Exception e
     */
    public static void writeRow(Collection<Object> row) throws Exception {
        csvPrinter.get().printRecord(row);
    }

    public static void flush() throws Exception {
        csvPrinter.get().flush();
    }

    /**
     * 结束操作
     */
    public static void finish() {
        try {
            csvPrinter.get().flush();
            fileWriter.get().flush();
            fileWriter.get().close();
            csvPrinter.get().close();
        } catch (Exception e) {
            log.error("结束csvPrinter, fileWriter流失败", e);
        } finally {
            fileWriter.remove();
            csvPrinter.remove();
        }
    }
}
