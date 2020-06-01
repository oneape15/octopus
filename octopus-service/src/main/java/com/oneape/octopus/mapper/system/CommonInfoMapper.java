package com.oneape.octopus.mapper.system;

import com.oneape.octopus.mapper.system.provider.CommonInfoSqlProvider;
import com.oneape.octopus.model.DO.system.CommonInfoDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommonInfoMapper {

    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = CommonInfoSqlProvider.class, method = "insert")
    int insert(CommonInfoDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = CommonInfoSqlProvider.class, method = "updateById")
    int update(CommonInfoDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = CommonInfoSqlProvider.class, method = "deleteById")
    int delete(CommonInfoDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = CommonInfoSqlProvider.class, method = "findById")
    CommonInfoDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = CommonInfoSqlProvider.class, method = "list")
    List<CommonInfoDO> list(@Param("model") CommonInfoDO model);

    /**
     * 获取数量
     *
     * @param model T
     * @return int
     */
    @SelectProvider(type = CommonInfoSqlProvider.class, method = "size")
    int size(@Param("model") CommonInfoDO model);

    @Select({
            "SELECT DISTINCT classify" +
                    " FROM " + CommonInfoSqlProvider.TABLE_NAME +
                    " WHERE " + CommonInfoSqlProvider.FIELD_ARCHIVE + " = 0 " +
                    " ORDER BY classify "
    })
    List<String> getAllClassify();

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = CommonInfoSqlProvider.class, method = "listWithOrder")
    List<CommonInfoDO> listWithOrder(@Param("model") CommonInfoDO model, @Param("orderFields") List<String> orderFields);
}
