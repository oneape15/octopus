package com.oneape.octopus.mapper.peekdata;

import com.oneape.octopus.mapper.peekdata.provider.PeekSqlProvider;
import com.oneape.octopus.model.DO.peekdata.PeekDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PeekMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = PeekSqlProvider.class, method = "insert")
    int insert(PeekDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = PeekSqlProvider.class, method = "updateById")
    int update(PeekDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = PeekSqlProvider.class, method = "deleteById")
    int delete(PeekDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = PeekSqlProvider.class, method = "findById")
    PeekDO findById(@Param("id") Long id);


    @SelectProvider(type = PeekSqlProvider.class, method = "listOrLink")
    List<PeekDO> listOrLink(@Param("model") PeekDO model);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = PeekSqlProvider.class, method = "list")
    List<PeekDO> list(@Param("model") PeekDO model);

    /**
     * 取数增加一次
     *
     * @param peekId Long
     * @return int
     */
    @UpdateProvider(type = PeekSqlProvider.class, method = "incPeekTime")
    int incPeekTime(@Param("peekId") Long peekId);
}
