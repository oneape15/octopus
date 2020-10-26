package com.oneape.octopus.mapper.peekdata;

import com.oneape.octopus.mapper.peekdata.provider.ModelTagSqlProvider;
import com.oneape.octopus.model.domain.peekdata.ModelTagDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ModelTagMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = ModelTagSqlProvider.class, method = "insert")
    int insert(ModelTagDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ModelTagSqlProvider.class, method = "updateById")
    int update(ModelTagDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ModelTagSqlProvider.class, method = "deleteById")
    int delete(ModelTagDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ModelTagSqlProvider.class, method = "findById")
    ModelTagDO findById(@Param("id") Long id);


    @SelectProvider(type = ModelTagSqlProvider.class, method = "listOrLink")
    List<ModelTagDO> listOrLink(@Param("model") ModelTagDO model);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ModelTagSqlProvider.class, method = "list")
    List<ModelTagDO> list(@Param("model") ModelTagDO model);
}
