package com.oneape.octopus.model.VO.report;

import com.oneape.octopus.model.DTO.serve.ServeColumnDTO;
import com.oneape.octopus.model.DO.serve.ServeInfoDO;
import com.oneape.octopus.model.VO.report.args.QueryArg;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-05 17:02.
 * Modify:
 */
@Data
public class ReportConfigVO {
    private Long    id;
    // report name.
    private String  name;
    // Timeliness of report data. 0 - real time, 1 - minutes, 2 - hours, 3 - days, 4 - months;
    private Integer timeBased;
    // report type , 1 - table; 2 - pie; 3 - bar , Multiple are separated by commas.
    private String  reportType;
    // When the chart is displayed, the X-axis column name; Multiple with ";" separated.
    private String  xAxis;
    // When the chart is displayed, the Y-axis column name; Multiple with ";" separated'
    private String  yAxis;
    // Query the field label display length
    private Integer paramLabelLen;
    // Query the field control display length
    private Integer paramMediaLen;
    // the report description.
    private String  comment;

    // Query the parameter form information.
    private List<QueryArg>       args;
    // the report show column information.
    private List<ServeColumnDTO> columns;
    // the rich text about report.
    private String               helpDoc;


    public static ReportConfigVO from(ServeInfoDO serveInfoDO) {
        ReportConfigVO vo = new ReportConfigVO();
        BeanUtils.copyProperties(serveInfoDO, vo);

        return vo;
    }

}
