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
            case HSQLDB:
                break;
            case IbmDB2:
                break;
            case Oracle:
                break;
            case SQLite:
                break;
            case MariaDB:
                break;
            case Firebird:
                break;
            case PostgreSQL:
                actuator = new PostgreSQLActuator(statement);
                break;
            case IbmInformix:
                break;
            default:
                break;
        }
        return actuator;
    }
}
