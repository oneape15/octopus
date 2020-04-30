package com.oneape.octopus.mapper.report.provider;

import com.google.common.base.Preconditions;
import com.oneape.octopus.commons.dto.BeanProperties;
import com.oneape.octopus.commons.value.BeanUtils;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.report.ReportGroupDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

public class ReportGroupSqlProvider extends BaseSqlProvider<ReportGroupDO> {
    public static final String TABLE_NAME = "r_report_group";

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
     * 根据条件查询, 并返回相应节点的一级子节点数量
     *
     * @param model ReportGroupDO
     * @return String
     */
    public String listWithChildrenSize(@Param("model") ReportGroupDO model) {
        Preconditions.checkNotNull(model, "查询数据集实体为空");
        List<BeanProperties> fields = BeanUtils.getFields(model);

        List<String> wheres = new ArrayList<>();

        fields.forEach(field -> {
            if (field.getValue() != null) {
                String columnName = field.getDbColumnName();
                wheres.add(columnName + " = #{model." + field.getName() + "}");
            }
        });

        return new SQL() {
            {
                SELECT("t.*",
                        "(SELECT COUNT(1) FROM r_report_group tmp where tmp.parent_id = t.id AND tmp.archive = 0) AS childrenSize");
                FROM(getTableName() + " t");
                WHERE(wheres.toArray(new String[wheres.size()]));
            }
        }.toString();
    }
}
