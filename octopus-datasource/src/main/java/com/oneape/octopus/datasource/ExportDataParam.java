package com.oneape.octopus.datasource;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 取数参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExportDataParam extends ExecParam {
    private String title;
    private String exportFileType = "csv"; // 导出文件类型, 可选择文件类型: xls和csv
    private String from;     //发送方邮箱地址
    private String fromName; //发送方名称
    private String email;    //接收邮箱地址
    private String emailHtmlContent; //邮件html内容
    private List<String> tips;  //提示内容
}
