package com.oneape.octopus.mapper.peekdata;

import com.oneape.octopus.mapper.peekdata.provider.PeekRuleSqlProvider;
import com.oneape.octopus.model.DO.peekdata.PeekRuleDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PeekRuleMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = PeekRuleSqlProvider.class, method = "insert")
    int insert(PeekRuleDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = PeekRuleSqlProvider.class, method = "updateById")
    int update(PeekRuleDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = PeekRuleSqlProvider.class, method = "deleteById")
    int deleteById(PeekRuleDO model);

    /**
     * 根据传入的对象值内容进行删除
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = PeekRuleSqlProvider.class, method = "delete")
    int delete(PeekRuleDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = PeekRuleSqlProvider.class, method = "findById")
    PeekRuleDO findById(@Param("id") Long id);


    @SelectProvider(type = PeekRuleSqlProvider.class, method = "listOrLink")
    List<PeekRuleDO> listOrLink(@Param("model") PeekRuleDO model);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = PeekRuleSqlProvider.class, method = "list")
    List<PeekRuleDO> list(@Param("model") PeekRuleDO model);
}
