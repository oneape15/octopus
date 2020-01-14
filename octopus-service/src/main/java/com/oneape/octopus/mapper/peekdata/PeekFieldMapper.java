package com.oneape.octopus.mapper.peekdata;

import com.oneape.octopus.mapper.peekdata.provider.PeekFieldSqlProvider;
import com.oneape.octopus.model.DO.peekdata.PeekFieldDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PeekFieldMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = PeekFieldSqlProvider.class, method = "insert")
    int insert(PeekFieldDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = PeekFieldSqlProvider.class, method = "updateById")
    int update(PeekFieldDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = PeekFieldSqlProvider.class, method = "deleteById")
    int deleteById(PeekFieldDO model);

    /**
     * 根据传入的对象值内容进行删除
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = PeekFieldSqlProvider.class, method = "delete")
    int delete(PeekFieldDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = PeekFieldSqlProvider.class, method = "findById")
    PeekFieldDO findById(@Param("id") Long id);


    @SelectProvider(type = PeekFieldSqlProvider.class, method = "listOrLink")
    List<PeekFieldDO> listOrLink(@Param("model") PeekFieldDO model);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = PeekFieldSqlProvider.class, method = "list")
    List<PeekFieldDO> list(@Param("model") PeekFieldDO model);
}
