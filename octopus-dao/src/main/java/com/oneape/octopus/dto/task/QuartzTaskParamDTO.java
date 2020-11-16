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

    // Serve id, This is valid when the value of runType is SERVE
    private Long   serveId;
    // Query person Id
    private Long   userId;
    // The datasource name, This is valid when the value of runType is RAW_SQL.
    private String dsName;
    // raw sql, The datasource name, This is valid when the value of runType is RAW_SQL.
    private String rawSql;

    // Email title
    private String       emailSubject;
    // Nickname of the sender
    private String       emailFromPersonal;
    // Mail receiver
    private List<String> emailTos;
    // Cc the person
    private List<String> emailCcs;
    // Email content
    private String       emailBody;

}
