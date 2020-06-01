package com.oneape.octopus.mapper.peekdata;

import com.oneape.octopus.mapper.peekdata.provider.PeekFieldSqlProvider;
import com.oneape.octopus.model.DO.peekdata.PeekFieldDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PeekFieldMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = PeekFieldSqlProvider.class, method = "insert")
    int insert(PeekFieldDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = PeekFieldSqlProvider.class, method = "updateById")
    int update(PeekFieldDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = PeekFieldSqlProvider.class, method = "deleteById")
    int deleteById(PeekFieldDO model);

    /**
     * 根据传入的对象值内容进行删除
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = PeekFieldSqlProvider.class, method = "delete")
    int delete(PeekFieldDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = PeekFieldSqlProvider.class, method = "findById")
    PeekFieldDO findById(@Param("id") Long id);


    @SelectProvider(type = PeekFieldSqlProvider.class, method = "listOrLink")
    List<PeekFieldDO> listOrLink(@Param("model") PeekFieldDO model);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = PeekFieldSqlProvider.class, method = "list")
    List<PeekFieldDO> list(@Param("model") PeekFieldDO model);
}
