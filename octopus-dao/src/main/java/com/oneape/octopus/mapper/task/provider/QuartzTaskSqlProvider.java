package com.oneape.octopus.mapper.task.provider;

import com.oneape.octopus.domain.task.QuartzTaskDO;
import com.oneape.octopus.mapper.BaseSqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-09-23 11:03.
 * Modify:
 */
public class QuartzTaskSqlProvider extends BaseSqlProvider<QuartzTaskDO> {
    public static final String TABLE_NAME = "quartz_task";

    /**
     * get table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String hasSameTaskName(@Param("taskName") String taskName, @Param("filterId") Long filterId) {
        List<String> wheres = new ArrayList<>();
        wheres.add("task_name = #{taskName}");
        if (filterId != null) {
            wheres.add("id != #{filterId}");
        }
        return new SQL()
                .SELECT("COUNT(0)")
                .FROM(TABLE_NAME)
                .WHERE(wheres.toArray(new String[wheres.size()]))
                .toString();
    }
}
