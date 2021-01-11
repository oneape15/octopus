-- datasource Data source information table.
CREATE TABLE `datasource`
(
  `id`            BIGINT(20)    NOT NULL
  COMMENT 'primary key',
  `name`          VARCHAR(64)   NOT NULL
  COMMENT 'The data source name',
  `type`          VARCHAR(64)   NOT NULL
  COMMENT 'The data source type. eg: MySQL, Oracle',
  `status`        TINYINT(1)    NOT NULL DEFAULT 0
  COMMENT 'The data source statue , 0 - usable; 1 - disabled',
  `jdbc_driver`   VARCHAR(256)  NULL
  COMMENT 'The data source driver class.',
  `jdbc_url`      VARCHAR(512)  NOT NULL
  COMMENT 'The data source jdbc url.',
  `username`      VARCHAR(64)   NOT NULL
  COMMENT 'The data source login username.',
  `password`      VARCHAR(64)   NULL     DEFAULT NULL
  COMMENT 'The data source login password.',
  `sync`          TINYINT(1)    NULL     DEFAULT 0
  COMMENT 'Data source synchronization state. 0 - Out of sync; 1 - sync',
  `cron`          VARCHAR(128)  NULL
  COMMENT 'Synchronization period expression ''0 0 9 * * ?''',
  `max_pool_size` INT(11)       NOT NULL DEFAULT 5
  COMMENT 'The maximum number of connections in the pool',
  `min_idle`      INT(11)       NOT NULL DEFAULT 5
  COMMENT 'The minimum number of idle connections in the pool to maintain',
  `read_only`     TINYINT(1)    NOT NULL DEFAULT 0
  COMMENT 'Read-only data source tag',
  `can_ddl`       TINYINT(1)    NOT NULL DEFAULT 0
  COMMENT ' Can support DDL operations',
  `timeout`       INT(11)       NULL     DEFAULT 60
  COMMENT 'Connection pool timeout(ms)',
  `test_sql`      VARCHAR(1024) NULL
  COMMENT 'The check status SQL',
  `comment`       VARCHAR(256)  NULL
  COMMENT 'description',
  `archive`       TINYINT(1)    NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`       BIGINT(20)    NOT NULL
  COMMENT 'create time ',
  `creator`       BIGINT(20)    NOT NULL
  COMMENT 'Data record creator',
  `modified`      BIGINT(20)    NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`      BIGINT(20)    NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'Data source information table.'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- table_schema Table basic information table
CREATE TABLE `table_schema`
(
  `id`            BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `datasource_id` BIGINT(20)   NOT NULL
  COMMENT 'the data source id.',
  `schema_name`   VARCHAR(128) NOT NULL
  COMMENT 'the schema name.',
  `name`          VARCHAR(256) NOT NULL
  COMMENT 'the table name',
  `alias`         VARCHAR(256) NULL
  COMMENT 'data table alias',
  `view`          TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT 'the table is view table, 0 - no; 1 - yes',
  `sync`          TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT 'synchronization state. 0 - Out of sync; 1 - sync',
  `cron`          VARCHAR(128) NULL
  COMMENT 'Synchronous table structure expression. CRON is null, then is never sync.',
  `sync_time`     BIGINT(20)   NULL
  COMMENT 'Latest synchronization table structure time',
  `heat`          BIGINT(20)   NOT NULL DEFAULT 0
  COMMENT 'The table use time',
  `status`        TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT 'The table status. 0 - normal, 1 - has drop',
  `comment`       VARCHAR(512) NULL
  COMMENT 'description',
  `archive`       TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`       BIGINT(20)   NOT NULL
  COMMENT 'create time ',
  `creator`       BIGINT(20)   NOT NULL
  COMMENT 'Data record creator',
  `modified`      BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`      BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'Table basic information table'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- table_column Table field information table
CREATE TABLE `table_column`
(

  `id`            BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `datasource_id` BIGINT(20)   NOT NULL
  COMMENT 'the data source id.',
  `table_name`    VARCHAR(512) NOT NULL
  COMMENT 'the table name',
  `name`          VARCHAR(256) NOT NULL
  COMMENT 'the column name',
  `alias`         VARCHAR(256) NULL
  COMMENT 'the column alias, default is null',
  `data_type`     VARCHAR(32)  NULL
  COMMENT 'eg. INTEGER, FLOAT, STRING, DECIMAL...',
  `classify`      TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT 'column classify. 0 - normal column ; 1 - primary key; 2 - foreign key',
  `heat`          BIGINT(20)   NOT NULL DEFAULT 0
  COMMENT 'The column use time',
  `status`        TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT 'The column status. 0 - normal, 1 - has drop',
  `comment`       VARCHAR(256) NULL
  COMMENT 'description',
  `archive`       TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`       BIGINT(20)   NOT NULL
  COMMENT 'create time ',
  `creator`       BIGINT(20)   NOT NULL
  COMMENT 'Data record creator',
  `modified`      BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`      BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'Table field information table'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;