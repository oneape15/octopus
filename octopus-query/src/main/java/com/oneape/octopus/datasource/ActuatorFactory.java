package com.oneape.octopus.datasource;

import com.google.common.base.Preconditions;
import com.oneape.octopus.datasource.dialect.Actuator;
import com.oneape.octopus.datasource.dialect.H2Actuator;
import com.oneape.octopus.datasource.dialect.MySQLActuator;
import com.oneape.octopus.datasource.dialect.PostgreSQLActuator;

import java.sql.Statement;

public class ActuatorFactory {

    public static Actuator build(Statement statement, DatasourceTypeHelper typeHelper) {
        Preconditions.checkNotNull(typeHelper, "The data source type is empty.");

        Actuator actuator;
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
                throw new RuntimeException("This data source is not supported yet.");
        }
        return actuator;
    }
}
