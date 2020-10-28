package com.oneape.octopus.commons.files;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-10-22 15:59.
 * Modify:
 */
@Slf4j
public class EasyCsv {
    private final static Charset   charset       = Charset.forName("utf-8");
    private final static CSVFormat csvFormat     = CSVFormat.DEFAULT.withRecordSeparator("\n");
    private final static String    commonCsvHead = new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}, charset);

    private final FileWriter fileWriter;
    private final CSVPrinter csvPrinter;
    private boolean isActive = false;

    public EasyCsv(String fullFileName) throws Exception {
        if (StringUtils.isBlank(fullFileName)) {
            throw new RuntimeException("文件路径为空");
        }
        fullFileName = StringUtils.trim(fullFileName);

        if (!StringUtils.endsWith(fullFileName, ".csv")) {
            fullFileName = fullFileName + ".csv";
        }

        // 判断文件是否存在
        Boolean isExist = FileUtils.isExist(fullFileName);
        if (isExist) {
            throw new RuntimeException("文件:【" + fullFileName + "】已存在");
        }

        fileWriter = new FileWriter(fullFileName, false);
        fileWriter.write(commonCsvHead);

        csvPrinter = new CSVPrinter(fileWriter, csvFormat);

        log.info("初始化文件: {} 成功~", fullFileName);
        isActive = true;
    }

    /**
     * 写入一行数据
     *
     * @param row Collection
     * @throws Exception e
     */
    public void writeRow(Collection<Object> row) throws Exception {
        if (!isActive) {
            throw new RuntimeException("文件流已经关闭,无法进行操作");
        }
        csvPrinter.printRecord(row);
    }

    public void writeRow(Iterator<Object> row) throws Exception {
        if (!isActive) {
            throw new RuntimeException("文件流已经关闭,无法进行操作");
        }
        csvPrinter.printRecord(row);
    }

    /**
     * 一次写入多行
     *
     * @param rows List
     * @throws Exception e
     */
    public void writeRow(List<List<Object>> rows) throws Exception {
        if (!isActive) {
            throw new RuntimeException("文件流已经关闭,无法进行操作");
        }
        csvPrinter.printRecords(rows);
    }

    public void flush() throws Exception {
        if (!isActive) {
            throw new RuntimeException("文件流已经关闭,无法进行操作");
        }
        csvPrinter.flush();
    }

    /**
     * 结束操作
     */
    public void finish() {
        try {
            csvPrinter.flush();
            fileWriter.flush();
            fileWriter.close();
            csvPrinter.close();
            isActive = false;
        } catch (Exception e) {
            log.error("结束csvPrinter, fileWriter流失败", e);
        }
    }
}
