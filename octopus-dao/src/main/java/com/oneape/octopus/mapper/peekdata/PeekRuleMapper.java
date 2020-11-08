package com.oneape.octopus.mapper.peekdata;

import com.oneape.octopus.mapper.peekdata.provider.PeekRuleSqlProvider;
import com.oneape.octopus.domain.peekdata.PeekRuleDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PeekRuleMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = PeekRuleSqlProvider.class, method = "insert")
    int insert(PeekRuleDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = PeekRuleSqlProvider.class, method = "updateById")
    int update(PeekRuleDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = PeekRuleSqlProvider.class, method = "deleteById")
    int deleteById(PeekRuleDO model);

    /**
     * 根据传入的对象值内容进行删除
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = PeekRuleSqlProvider.class, method = "delete")
    int delete(PeekRuleDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = PeekRuleSqlProvider.class, method = "findById")
    PeekRuleDO findById(@Param("id") Long id);


    @SelectProvider(type = PeekRuleSqlProvider.class, method = "listOrLink")
    List<PeekRuleDO> listOrLink(@Param("model") PeekRuleDO model);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = PeekRuleSqlProvider.class, method = "list")
    List<PeekRuleDO> list(@Param("model") PeekRuleDO model);
}
