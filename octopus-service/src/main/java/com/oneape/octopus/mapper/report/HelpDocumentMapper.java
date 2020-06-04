package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.HelpDocumentSqlProvider;
import com.oneape.octopus.mapper.report.provider.ReportSqlProvider;
import com.oneape.octopus.model.DO.report.HelpDocumentDO;
import com.oneape.octopus.model.DO.report.ReportDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * The help document Mapper.
 */
@Mapper
public interface HelpDocumentMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = HelpDocumentSqlProvider.class, method = "insert")
    int insert(HelpDocumentDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = HelpDocumentSqlProvider.class, method = "updateById")
    int update(HelpDocumentDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = HelpDocumentSqlProvider.class, method = "deleteById")
    int delete(HelpDocumentDO model);

    @UpdateProvider(type = HelpDocumentSqlProvider.class, method = "deleteByBizInfo")
    int deleteByBizInfo(@Param("bizType") Integer bizType, @Param("bizId") Long bizId);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ReportSqlProvider.class, method = "findById")
    HelpDocumentDO findById(@Param("id") Long id);

    @SelectProvider(type = HelpDocumentSqlProvider.class, method = "findByBizInfo")
    HelpDocumentDO findByBizInfo(@Param("bizType") Integer bizType, @Param("bizId") Long bizId);

}
