package com.oneape.octopus.mapper.peekdata;

import com.oneape.octopus.mapper.peekdata.provider.ModelMetaSqlProvider;
import com.oneape.octopus.model.DO.peekdata.ModelMetaDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ModelMetaMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = ModelMetaSqlProvider.class, method = "insert")
    int insert(ModelMetaDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ModelMetaSqlProvider.class, method = "updateById")
    int update(ModelMetaDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ModelMetaSqlProvider.class, method = "deleteById")
    int delete(ModelMetaDO model);

    /**
     * 根据模型Id删除
     *
     * @param modelId Long
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ModelMetaSqlProvider.class, method = "deleteByModelId")
    int delByModelId(@Param("modelId") Long modelId);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ModelMetaSqlProvider.class, method = "findById")
    ModelMetaDO findById(@Param("id") Long id);


    @SelectProvider(type = ModelMetaSqlProvider.class, method = "listOrLink")
    List<ModelMetaDO> listOrLink(@Param("model") ModelMetaDO model);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ModelMetaSqlProvider.class, method = "list")
    List<ModelMetaDO> list(@Param("model") ModelMetaDO model);
}
