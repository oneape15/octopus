package com.oneape.octopus.mapper.peekdata;

import com.oneape.octopus.mapper.peekdata.provider.ModelSqlProvider;
import com.oneape.octopus.model.domain.peekdata.ModelDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ModelMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = ModelSqlProvider.class, method = "insert")
    int insert(ModelDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ModelSqlProvider.class, method = "updateById")
    int update(ModelDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ModelSqlProvider.class, method = "deleteById")
    int delete(ModelDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ModelSqlProvider.class, method = "findById")
    ModelDO findById(@Param("id") Long id);


    @SelectProvider(type = ModelSqlProvider.class, method = "listOrLink")
    List<ModelDO> listOrLink(@Param("model") ModelDO model);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ModelSqlProvider.class, method = "list")
    List<ModelDO> list(@Param("model") ModelDO model);
}
