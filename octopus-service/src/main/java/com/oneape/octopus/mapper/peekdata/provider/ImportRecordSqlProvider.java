package com.oneape.octopus.mapper.peekdata.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.peekdata.ImportRecordDO;

public class ImportRecordSqlProvider extends BaseSqlProvider<ImportRecordDO> {
    public static final String TABLE_NAME = "pd_import_record";

    /**
     * 获取表名
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
