package com.oneape.octopus.commons.files;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-05-06 17:20.
 * Modify:
 */
@Slf4j
public class FileUtils {

    // delete the dir.
    public static void delDir(String dirPath) {
        File file = new File(dirPath);
        boolean status;
        if (!file.isFile()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    delDir(f.getAbsolutePath());
                }
            }
        }
        status = file.delete();
        log.info("Delete dir: {} {}", dirPath, status ? "success" : "fail");
    }

    /**
     * create directory.
     *
     * @param dirPath String
     */
    public static void makeDir(String dirPath) {
        if (StringUtils.isBlank(dirPath)) {
            throw new RuntimeException("The directory path is empty.");
        }
        File file = new File(dirPath);
        if (!file.exists()) {
            boolean status = file.mkdirs();

            log.info("Directory: {} create: {}", dirPath, status ? "success" : "fail");
        }

    }

    /**
     * delete file
     *
     * @param file File
     * @return boolean
     */
    public static boolean delFile(File file) {
        if (file != null) {
            boolean status = file.delete();
            log.info("Delete file: {} {}", file.getName(), status ? "success" : "fail");
            return status;
        }
        return false;
    }

    /**
     * Get filename suffix.
     *
     * @param fileName String
     * @return String
     */
    public static String getFileSuffix(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        int indexOf = StringUtils.lastIndexOf(fileName, ".");
        if (indexOf > -1) {
            return StringUtils.substring(fileName, indexOf + 1).trim();
        }
        return null;
    }

    /**
     * Get file name.
     *
     * @param filePath String
     * @return String
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        int indexOf = StringUtils.lastIndexOf(filePath, "/");
        if (indexOf > -1) {
            return StringUtils.substring(filePath, indexOf + 1).trim();
        }
        return null;
    }

    /**
     * Determine whether the file exists.
     *
     * @param filePath String
     * @return true - exist; false - not exist;
     */
    public static boolean isExist(String filePath) {
        if (StringUtils.isBlank(filePath)) return false;

        File file = new File(filePath);

        return (!file.isDirectory()) && file.exists();
    }

    /**
     * delete the file.
     *
     * @param filePath String
     * @return boolean
     */
    public static boolean delFileByPath(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("The file path is empty.");
        }
        return delFile(new File(filePath));
    }

    /**
     * Write a TXT file
     *
     * @param filePath String
     * @param fileName String
     * @param content  String
     * @return File
     * @throws IOException e
     */
    public static File writeTxtFile(String filePath, String fileName, String content) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            boolean status = file.mkdir();
            log.debug("Directory :{} create {}", filePath, status ? "success" : "fail");
        }
        File txtFile = File.createTempFile(fileName, ".txt", new File(filePath));

        FileWriter writer = new FileWriter(txtFile, false);
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write(content);
        bw.close();

        return txtFile;
    }

    /**
     * Read a TXT file
     *
     * @param file File
     * @return String
     */
    public static String readTxtFile(File file) {
        StringBuilder result = new StringBuilder();
        try {

            // Construct a BufferedReader class to read the file.
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            // Using the readLine method, read one line at a time.
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator()).append(s);
            }
            br.close();
        } catch (Exception e) {
            log.error("Read file: {} fail.", file.getName(), e);
            throw new RuntimeException("Read file fail.", e);
        }
        return result.toString();
    }
}
