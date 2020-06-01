package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.ReportGroupSqlProvider;
import com.oneape.octopus.model.DO.report.ReportGroupDO;
import com.oneape.octopus.model.VO.ReportGroupVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 报表组Mapper
 */
@Mapper
public interface ReportGroupMapper {

    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = ReportGroupSqlProvider.class, method = "insert")
    int insert(ReportGroupDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportGroupSqlProvider.class, method = "updateById")
    int update(ReportGroupDO model);

    /**
     * 计算数量
     *
     * @param model T
     * @return int 数量值
     */
    @SelectProvider(type = ReportGroupSqlProvider.class, method = "size")
    int size(ReportGroupDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportGroupSqlProvider.class, method = "deleteById")
    int delete(ReportGroupDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ReportGroupSqlProvider.class, method = "findById")
    ReportGroupDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ReportGroupSqlProvider.class, method = "list")
    List<ReportGroupDO> list(@Param("model") ReportGroupDO model);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ReportGroupSqlProvider.class, method = "listWithChildrenSize")
    List<ReportGroupVO> listWithChildrenSize(@Param("model") ReportGroupDO model);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ReportGroupSqlProvider.class, method = "listWithOrder")
    List<ReportGroupDO> listWithOrder(@Param("model") ReportGroupDO model,
                                      @Param("orderFields") List<String> orderFields);
}
