package com.oneape.octopus.mapper.report.provider;

import com.google.common.base.Joiner;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.report.GroupRlReportDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public class GroupRlReportSqlProvider extends BaseSqlProvider<GroupRlReportDO> {
    public static final String TABLE_NAME = "r_group_rl_report";

    /**
     * 获取表名
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * 根据指定条件删除数据
     *
     * @param reportId Long
     * @param groupIds List
     * @return String
     */
    public String deleteBy(@Param("reportId") Long reportId, @Param("groupIds") List<Long> groupIds) {
        StringBuilder sb = new StringBuilder(" report_id = #{reportId}");
        sb.append(" AND group_id IN (").append(Joiner.on(",").join(groupIds)).append(")");
        return new SQL() {
            {
                DELETE_FROM(getTableName());
                WHERE(sb.toString());
            }
        }.toString();
    }
}
