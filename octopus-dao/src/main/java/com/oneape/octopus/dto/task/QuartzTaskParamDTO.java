package com.oneape.octopus.dto.task;

import com.oneape.octopus.commons.enums.QuartzTaskType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-09-23 14:57.
 * Modify:
 */
@Data
public class QuartzTaskParamDTO implements Serializable {
    /**
     * execute type SERVE, RAW_SQL, SIMPLE_EMAIL
     *
     * @see QuartzTaskType
     */
    private String runType;

    // 报表id runType为REPORT时有效
    private Long serveId;
    // 查询人Id
    private Long userId;

    // 查询数据源 runType为RAW_SQL时有效
    private String dsName;
    // 执行sql   runType为RAW_SQL时有效
    private String rawSql;

    // 邮件标题
    private String       emailSubject;
    // 邮件发送人昵称
    private String       emailFromPersonal;
    // 邮件接收人
    private List<String> emailTos;
    // 邮件抄送人
    private List<String> emailCcs;
    // 邮件内容
    private String       emailBody;

}
