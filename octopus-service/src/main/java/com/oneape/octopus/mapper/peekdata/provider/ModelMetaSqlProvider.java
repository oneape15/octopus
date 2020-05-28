package com.oneape.octopus.mapper.peekdata.provider;

import com.google.common.base.Joiner;
import com.oneape.octopus.common.enums.Archive;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.peekdata.ModelMetaDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public class ModelMetaSqlProvider extends BaseSqlProvider<ModelMetaDO> {
    public static final String TABLE_NAME = "pd_model_meta";

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
     * 根据模型id删除
     *
     * @param modelId Long
     * @return String
     */
    public String deleteByModelId(@Param("modelId") Long modelId) {
        return new SQL() {
            {
                UPDATE(getTableName());
                SET(FIELD_ARCHIVE + " = " + Archive.ARCHIVE.value(),
                        FIELD_MODIFIED + " = unix_timestamp(now())");
                WHERE("model_id = #{modelId}");
            }
        }.toString();
    }

    /**
     * 根据id列表进行删除
     *
     * @param ids List
     * @return String
     */
    public String delByIds(@Param("ids") List<Long> ids) {
        return new SQL() {
            {
                UPDATE(getTableName());
                SET(FIELD_ARCHIVE + " = " + Archive.ARCHIVE.value(),
                        FIELD_MODIFIED + " = unix_timestamp(now())");
                WHERE("id IN (" + Joiner.on(",").join(ids) + ")");
            }
        }.toString();
    }
}
