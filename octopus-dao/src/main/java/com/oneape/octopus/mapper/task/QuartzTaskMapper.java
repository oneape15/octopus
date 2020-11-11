package com.oneape.octopus.mapper.task;

import com.oneape.octopus.domain.task.QuartzTaskDO;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.mapper.task.provider.QuartzTaskSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-09-23 10:57.
 * Modify:
 */
public interface QuartzTaskMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = QuartzTaskSqlProvider.class, method = "insert")
    int insert(QuartzTaskDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = QuartzTaskSqlProvider.class, method = "updateById")
    int update(QuartzTaskDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = QuartzTaskSqlProvider.class, method = "deleteById")
    int delete(QuartzTaskDO model);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = QuartzTaskSqlProvider.class, method = "list")
    List<QuartzTaskDO> list(@Param("model") QuartzTaskDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = QuartzTaskSqlProvider.class, method = "findById")
    QuartzTaskDO findById(@Param("id") Long id);

    /**
     * Gets the invoke status task.
     *
     * @return List
     */
    @Select({
            "SELECT * FROM " + QuartzTaskSqlProvider.TABLE_NAME +
                    " WHERE " + BaseSqlProvider.FIELD_ARCHIVE + " = 0 AND status = 1"
    })
    List<QuartzTaskDO> getAllInvokeTask();

    /**
     * Check the name has exist.
     *
     * @param taskName String
     * @param filterId Long
     * @return int
     */
    @SelectProvider(type = QuartzTaskSqlProvider.class, method = "hasSameTaskName")
    int hasSameTaskName(@Param("taskName") String taskName, @Param("filterId") Long filterId);

}
