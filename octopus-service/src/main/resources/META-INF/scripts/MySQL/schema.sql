-- r_datasource 数据源信息表
CREATE TABLE `r_datasource`
(
  `id`          BIGINT(20)    NOT NULL
  COMMENT '主键Id',
  `name`        VARCHAR(64)   NOT NULL
  COMMENT '数据源名称',
  `type`        VARCHAR(64)   NOT NULL
  COMMENT '数据源类型, MySQL, Oracle',
  `status`      TINYINT(1)    NOT NULL DEFAULT 0
  COMMENT '状态, 0 - 可用; 1 - 不可用',
  `jdbc_driver` VARCHAR(256)  NOT NULL
  COMMENT 'jdbc驱动',
  `jdbc_url`    VARCHAR(512)  NOT NULL
  COMMENT 'jdbc URL',
  `username`    VARCHAR(64)   NOT NULL
  COMMENT '登录用户名',
  `password`    VARCHAR(64)   NULL     DEFAULT NULL
  COMMENT '登录密码',
  `timeout`     INT(11)       NULL     DEFAULT 60
  COMMENT '连接池超时时间(ms)',
  `test_sql`    VARCHAR(1024) NULL
  COMMENT '检测SQL',
  `comment`     VARCHAR(256)  NULL
  COMMENT '描述',
  `archive`     TINYINT(1)    NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`     BIGINT(20)    NOT NULL
  COMMENT '创建时间',
  `creator`     BIGINT(20)    NOT NULL
  COMMENT '创建人',
  `modified`    BIGINT(20)    NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`    BIGINT(20)    NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '数据源信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- r_table_schema Table basic information table
CREATE TABLE `r_table_schema`
(
  `id`         BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `name`       VARCHAR(512) NOT NULL
  COMMENT 'the table name',
  `view_table` TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT 'the table is view table, 0 - no; 1 - yes',
  `sync_time`  BIGINT(20)   NULL
  COMMENT 'Latest synchronization table structure time',
  `sync_cron`  VARCHAR(128) NULL
  COMMENT 'Synchronous table structure expression. syncCron is null, then is never sync.',
  `heat`       BIGINT(20)   NOT NULL DEFAULT 0
  COMMENT 'The table use time',
  `status`     TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT 'The table status. 0 - normal, 1 - has drop',
  `comment`    VARCHAR(256) NULL
  COMMENT 'description',
  `archive`    TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`    BIGINT(20)   NOT NULL
  COMMENT 'create time ',
  `creator`    BIGINT(20)   NOT NULL
  COMMENT 'Data record creator',
  `modified`   BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`   BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'Table basic information table'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- r_table_column Table field information table
CREATE TABLE `r_table_column`
(

  `id`         BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `table_name` VARCHAR(512) NOT NULL
  COMMENT 'the table name',
  `name`       VARCHAR(256) NOT NULL
  COMMENT 'the column name',
  `alias`      VARCHAR(256) NULL
  COMMENT 'the column alias, default is null',
  `data_type`  VARCHAR(32)  NULL
  COMMENT 'eg. INTEGER, FLOAT, STRING, DECIMAL...',
  `classify`   TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT 'column classify. 0 - normal column ; 1 - primary key; 2 - foreign key',
  `heat`       BIGINT(20)   NOT NULL DEFAULT 0
  COMMENT 'The column use time',
  `status`     TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT 'The column status. 0 - normal, 1 - has drop',
  `comment`    VARCHAR(256) NULL
  COMMENT 'description',
  `archive`    TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`    BIGINT(20)   NOT NULL
  COMMENT 'create time ',
  `creator`    BIGINT(20)   NOT NULL
  COMMENT 'Data record creator',
  `modified`   BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`   BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'Table field information table'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;