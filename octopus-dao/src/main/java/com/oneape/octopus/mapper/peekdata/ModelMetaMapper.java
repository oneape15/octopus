package com.oneape.octopus.mapper.peekdata;

import com.oneape.octopus.mapper.peekdata.provider.ModelMetaSqlProvider;
import com.oneape.octopus.domain.peekdata.ModelMetaDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ModelMetaMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = ModelMetaSqlProvider.class, method = "insert")
    int insert(ModelMetaDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ModelMetaSqlProvider.class, method = "updateById")
    int update(ModelMetaDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ModelMetaSqlProvider.class, method = "deleteById")
    int delete(ModelMetaDO model);

    /**
     * 根据模型Id删除
     *
     * @param modelId Long
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ModelMetaSqlProvider.class, method = "deleteByModelId")
    int delByModelId(@Param("modelId") Long modelId);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ModelMetaSqlProvider.class, method = "findById")
    ModelMetaDO findById(@Param("id") Long id);


    @SelectProvider(type = ModelMetaSqlProvider.class, method = "listOrLink")
    List<ModelMetaDO> listOrLink(@Param("model") ModelMetaDO model);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ModelMetaSqlProvider.class, method = "list")
    List<ModelMetaDO> list(@Param("model") ModelMetaDO model);

    /**
     * 根据Id列表进行删除
     *
     * @param ids List
     * @return int
     */
    @UpdateProvider(type = ModelMetaSqlProvider.class, method = "delByIds")
    int delByIds(@Param("ids") List<Long> ids);
}
