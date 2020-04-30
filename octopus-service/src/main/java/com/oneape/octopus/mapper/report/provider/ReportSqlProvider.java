package com.oneape.octopus.mapper.report.provider;

import com.google.common.base.Preconditions;
import com.oneape.octopus.commons.dto.BeanProperties;
import com.oneape.octopus.commons.value.BeanUtils;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.report.ReportDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

public class ReportSqlProvider extends BaseSqlProvider<ReportDO> {
    public static final String TABLE_NAME = "r_report";

    /**
     * 获取表名
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String findSimpleReport(@Param("model") ReportDO model) {
        Preconditions.checkNotNull(model, "查询数据集实体为空");
        List<BeanProperties> fields = BeanUtils.getFields(model);

        List<String> wheres = new ArrayList<>();

        fields.forEach(field -> {
            if (field.getValue() != null) {
                String columnName = field.getDbColumnName();
                wheres.add("r." + columnName + " = #{model." + field.getName() + "}");
            }
        });

        return new SQL() {
            {
                SELECT("id", "code", "name", "icon", "report_type as reportType", "lov", "owner",
                        "(select   group_concat(group_id) from " + GroupRlReportSqlProvider.TABLE_NAME + "   grr   where grr.report_id = r.id ) as groupIds");
                FROM(getTableName() + " r ");
                WHERE(wheres.toArray(new String[wheres.size()]));
            }
        }.toString();
    }
}
