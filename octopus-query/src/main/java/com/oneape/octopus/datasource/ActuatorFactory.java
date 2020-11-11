package com.oneape.octopus.datasource;

import com.google.common.base.Preconditions;
import com.oneape.octopus.datasource.dialect.*;

import java.sql.Connection;
import java.sql.Statement;

public class ActuatorFactory {

    public static Actuator build(Connection conn, DatasourceTypeHelper typeHelper) {
        Preconditions.checkNotNull(typeHelper, "The data source type is empty.");

        Actuator actuator;
        switch (typeHelper) {
            case H2:
                actuator = new H2Actuator(conn);
                break;
            case MySQL:
                actuator = new MySQLActuator(conn);
                break;
            case PostgreSQL:
                actuator = new PostgreSQLActuator(conn);
                break;
            case Odps:
                actuator = new OdpsActuator(conn);
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
