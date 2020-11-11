package com.oneape.octopus.commons.files;

import com.oneape.octopus.commons.cause.BizException;
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
            throw new BizException("The file path is empty");
        }
        fullFileName = StringUtils.trim(fullFileName);

        if (!StringUtils.endsWith(fullFileName, ".csv")) {
            fullFileName = fullFileName + ".csv";
        }

        // Determine if the file exists.
        Boolean isExist = FileUtils.isExist(fullFileName);
        if (isExist) {
            throw new BizException("File already exists! filePath: " + fullFileName);
        }

        fileWriter = new FileWriter(fullFileName, false);
        fileWriter.write(commonCsvHead);

        csvPrinter = new CSVPrinter(fileWriter, csvFormat);

        log.info("初始化文件: {} 成功~", fullFileName);
        isActive = true;
    }

    /**
     * Write one line at a time.
     *
     * @param row Collection
     * @throws Exception e
     */
    public void writeRow(Collection<Object> row) throws Exception {
        if (!isActive) {
            throw new BizException("The csv file stream is closed and cannot be operated on!");
        }
        csvPrinter.printRecord(row);
    }

    public void writeRow(Iterator<Object> row) throws Exception {
        if (!isActive) {
            throw new BizException("The csv file stream is closed and cannot be operated on!");
        }
        csvPrinter.printRecord(row);
    }

    /**
     * Multiple writes at a time.
     *
     * @param rows List
     * @throws Exception e
     */
    public void writeRow(List<List<Object>> rows) throws Exception {
        if (!isActive) {
            throw new BizException("The csv file stream is closed and cannot be operated on!");
        }
        csvPrinter.printRecords(rows);
    }

    public void flush() throws Exception {
        if (!isActive) {
            throw new BizException("The csv file stream is closed and cannot be operated on!");
        }
        csvPrinter.flush();
    }

    public void finish() {
        try {
            isActive = false;
            csvPrinter.flush();
            fileWriter.flush();
            fileWriter.close();
            csvPrinter.close();
        } catch (Exception e) {
            log.error("close file writer fail!", e);
        }
    }
}
