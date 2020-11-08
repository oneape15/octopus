package com.oneape.octopus.mapper.peekdata.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.domain.peekdata.ImportRecordDO;

public class ImportRecordSqlProvider extends BaseSqlProvider<ImportRecordDO> {
    public static final String TABLE_NAME = "pd_import_record";

    /**
     * Gets the table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
