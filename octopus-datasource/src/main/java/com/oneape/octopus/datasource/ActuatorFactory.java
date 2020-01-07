package com.oneape.octopus.datasource;

import com.oneape.octopus.datasource.dialect.Actuator;
import com.oneape.octopus.datasource.dialect.H2Actuator;
import com.oneape.octopus.datasource.dialect.MySQLActuator;
import com.oneape.octopus.datasource.dialect.PostgreSQLActuator;

import java.sql.Statement;

public class ActuatorFactory {

    public static Actuator build(Statement statement, DatasourceTypeHelper typeHelper) {
        Actuator actuator = null;

        if (typeHelper == null) {
            throw new RuntimeException("数据源类型为空~");
        }

        switch (typeHelper) {
            case H2:
                actuator = new H2Actuator(statement);
                break;
            case MySQL:
                actuator = new MySQLActuator(statement);
                break;
            case PostgreSQL:
                actuator = new PostgreSQLActuator(statement);
                break;
            case HSQLDB:
            case IbmDB2:
            case Oracle:
            case SQLite:
            case MariaDB:
            case Firebird:
            case IbmInformix:
            default:
                throw new RuntimeException("该数据源暂未支持");
        }
        return actuator;
    }
}
