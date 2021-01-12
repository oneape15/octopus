package com.oneape.octopus.datasource;

import com.alibaba.fastjson.JSON;
import com.oneape.octopus.commons.dto.DataType;
import com.oneape.octopus.datasource.data.DatasourceInfo;
import com.oneape.octopus.datasource.data.ExecParam;
import com.oneape.octopus.datasource.data.Result;
import com.oneape.octopus.commons.dto.Value;
import com.oneape.octopus.datasource.schema.SchemaTableField;
import com.oneape.octopus.datasource.schema.SchemaTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class QueryFactoryTest {

    private static QueryFactory   queryFactory;
    private static DatasourceInfo dsi;
    private static String         schema;

    @BeforeAll
    static void init() {
        DatasourceFactory datasourceFactory = new DefaultDatasourceFactory();
        schema = "test";

        dsi = new DatasourceInfo();
        dsi.setUsername("helloword");
        dsi.setPassword("helloword");
        dsi.setUrl("jdbc:postgresql://127.0.0.1:5432/test");
        dsi.setDatasourceType(DatasourceTypeHelper.PostgreSQL);

        queryFactory = new DefaultQueryFactory(datasourceFactory);
    }



    @Test
    public void allDatabaseTest() {
        List<String> databaseNames = queryFactory.allDatabase(dsi);
        assertNotNull(databaseNames, "数据库列表为空");
        log.info("数据库如下： {}", JSON.toJSONString(databaseNames));
    }

    @Test
    public void allTablesTest() {
        List<SchemaTable> schemaTables = queryFactory.allTables(dsi, schema);

        assertNotNull(schemaTables);
        assertTrue(schemaTables.size() > 0);
        log.info("表信息如下： {}", JSON.toJSONString(schemaTables));
    }

    @Test
    public void tablesTest() {
        List<SchemaTable> schemaTables = queryFactory.allTables(dsi, schema);

        assertNotNull(schemaTables);
        assertTrue(schemaTables.size() > 0);
        log.info("表信息如下： {}", JSON.toJSONString(schemaTables));
    }

    @Test
    public void allFieldsTest() {
        List<SchemaTableField> fields = queryFactory.allFields(dsi, schema);
        assertNotNull(fields);
        log.info("数据库字段信息如下：{}", JSON.toJSONString(fields));
    }

    @Test
    public void tableFieldsTest() {
        String tableName = "r_datasource";
        List<SchemaTableField> fields = queryFactory.fieldOfTable(dsi, schema, tableName);
        assertNotNull(fields);
        log.info("表：{}字段信息如下：{}", tableName, JSON.toJSONString(fields));
    }

    @Test
    public void tableFieldsOfPgSqlTest() {
        String tableName = "agent_department";
        List<SchemaTableField> fields = queryFactory.fieldOfTable(dsi, schema, tableName);
        assertNotNull(fields);
        log.info("表：{}字段信息如下：{}", tableName, JSON.toJSONString(fields));
    }

    @Test
    public void queryTest() {
        String rawSql = "SELECT   " +
                " TABLE_SCHEMA schema_name,   " +
                " TABLE_NAME table_name,   " +
                " COLUMN_NAME column_name,   " +
                " COLUMN_DEFAULT default_val, " +
                " IS_NULLABLE,  " +
                " DATA_TYPE data_type,   " +
                " CHARACTER_MAXIMUM_LENGTH max_len,   " +
                " COLUMN_COMMENT " +
                "FROM   " +
                " information_schema.COLUMNS    " +
                "WHERE   " +
                " TABLE_SCHEMA = ? " +
                " AND TABLE_NAME = ?  " +
                " AND  1 = ? " +
                "ORDER BY   " +
                " TABLE_SCHEMA,   " +
                " TABLE_NAME,   " +
                " ORDINAL_POSITION   ";
        ExecParam execParam = new ExecParam();
        execParam.setNeedTotalSize(true);
        execParam.setRawSql(rawSql);
        List<Value> values = new ArrayList<>();
        values.add(new Value("octopus", DataType.STRING));
        values.add(new Value("r_datasource", DataType.STRING));
        values.add(new Value(1, DataType.INTEGER));
        execParam.setParams(values);
        execParam.setPageIndex(1);
        execParam.setPageSize(5);

        Result result = queryFactory.execSql(dsi, execParam);

        log.info("查询结果： {}", JSON.toJSONString(result));
    }

    @Test
    public void queryPgSqlTest() {
        String rawSql = "SELECT * FROM agent_department WHERE agent_id = ? and updator = ?";
        ExecParam execParam = new ExecParam();
        execParam.setNeedTotalSize(true);
        execParam.setRawSql(rawSql);
        List<Value> values = new ArrayList<>();
        values.add(new Value(1, DataType.INTEGER));
        values.add(new Value("秋葵", DataType.STRING));
        execParam.setParams(values);
        execParam.setPageIndex(1);
        execParam.setPageSize(5);

        Result result = queryFactory.execSql(dsi, execParam, cell -> {
            if (StringUtils.equals("updator", cell.getHead().getLabel())) {
                return String.valueOf(cell.getValue()) + "_大人";
            }
            return cell.getValue();
        });

        log.info("查询结果： {}", JSON.toJSONString(result));
    }
}
