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

    // 删除文件夹
    public static void delDir(String dirPath) {
        File file = new File(dirPath);
        boolean status;
        if (file.isFile()) {
            status = file.delete();
        } else {
            File[] files = file.listFiles();
            if (files == null) {
                status = file.delete();
            } else {
                for (File f : files) {
                    delDir(f.getAbsolutePath());
                }
                status = file.delete();
            }
        }
        log.info("删除文件: {} {}", dirPath, status ? "成功" : "失败");
    }

    public static void makeDir(String dirPath) {
        if (StringUtils.isBlank(dirPath)) {
            throw new RuntimeException("目录路径为空");
        }
        File file = new File(dirPath);
        if (!file.exists()) {
            boolean status = file.mkdirs();

            log.info("目录: {} 创建: {}", dirPath, status ? "成功" : "失败");
        }

    }

    // 删除文件
    public static boolean delFile(File file) {
        if (file != null) {
            boolean status = file.delete();
            log.info("删除文件: {} {}", file.getName(), status ? "成功" : "失败");
            return status;
        }
        return false;
    }

    /**
     * 获取文件名后缀
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
     * 获取文件名
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
     * 文件是否存在
     *
     * @param filePath String
     * @return true - 存在; false - 不存在;
     */
    public static boolean isExist(String filePath) {
        if (StringUtils.isBlank(filePath)) return false;

        File file = new File(filePath);

        return (!file.isDirectory()) && file.exists();
    }

    public static boolean delFileByPath(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件路径为空");
        }
        return delFile(new File(filePath));
    }

    // 写一个txt文件
    public static File writeTxtFile(String filePath, String fileName, String content) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            boolean status = file.mkdir();
            log.debug("文件夹:{} 创建{}", filePath, status ? "成功" : "失败");
        }
        File txtFile = File.createTempFile(fileName, ".txt", new File(filePath));

        FileWriter writer = new FileWriter(txtFile, false);
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write(content);
        bw.close();

        return txtFile;
    }

    /**
     * 读取txt文本内容
     *
     * @param file File
     * @return String 文本内容
     */
    public static String readTxtFile(File file) {
        StringBuilder result = new StringBuilder();
        try {

            //构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            //使用readLine方法，一次读一行
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator()).append(s);
            }
            br.close();
        } catch (Exception e) {
            log.error("读取文件失败.", e);
            throw new RuntimeException("读取文件内容失败", e);
        }
        return result.toString();
    }
}
