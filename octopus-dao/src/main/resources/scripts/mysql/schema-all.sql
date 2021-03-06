-- Gateway routing configuration information table.
CREATE TABLE `gateway_route`
(
  `id`         BIGINT(20)    NOT NULL
  COMMENT 'primary key',
  `route_id`   VARCHAR(64)   NOT NULL
  COMMENT 'The route unique id',
  `uri`        VARCHAR(512)  NOT NULL
  COMMENT 'The group id',
  `predicates` VARCHAR(1024) NULL
  COMMENT 'Predicate configuration information.',
  `filters`    VARCHAR(1024) NULL
  COMMENT 'Filter configuration information.',
  `state`      TINYINT(1)    NOT NULL DEFAULT 0
  COMMENT 'route state 0 - invalid; 1 - valid.',
  `metadata`   VARCHAR(512)  NULL
  COMMENT 'Metadata configuration information.',
  `order`      INT(11)       NOT NULL
  COMMENT 'The order value.',
  `archive`    TINYINT(1)    NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`    BIGINT(20)    NOT NULL
  COMMENT 'create time ',
  `creator`    BIGINT(20)    NOT NULL
  COMMENT 'Data record creator',
  `modified`   BIGINT(20)    NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`   BIGINT(20)    NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'Gateway routing configuration information table.'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- UID generator
CREATE TABLE `sys_worker_node`
(
  id          BIGINT(20)  NOT NULL AUTO_INCREMENT
  COMMENT 'Since the primary key',
  host_name   VARCHAR(64) NOT NULL
  COMMENT 'host name',
  port        VARCHAR(64) NOT NULL
  COMMENT 'the host port',
  type        INT         NOT NULL
  COMMENT 'node type ACTUAL or CONTAINER',
  launch_time BIGINT(20)  NOT NULL
  COMMENT 'Node trigger time, default to current time',
  created     BIGINT(20)  NOT NULL
  COMMENT 'create time',
  modified    BIGINT(20)  NOT NULL
  COMMENT 'Last updated time',
  PRIMARY KEY (id)
)
  COMMENT 'UID generator'
  ENGINE InnoDB;

-- Basic information table
CREATE TABLE `sys_common_info`
(
  `id`        BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `parent_id` BIGINT(20)   NOT NULL DEFAULT 0
  COMMENT 'The parent id',
  `classify`  VARCHAR(32)  NOT NULL
  COMMENT 'Basic information classification',
  `key`       VARCHAR(128) NOT NULL
  COMMENT 'The common information key',
  `value`     VARCHAR(512) NOT NULL
  COMMENT 'The common information value',
  `archive`   TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`   BIGINT(20)   NOT NULL
  COMMENT 'create time ',
  `creator`   BIGINT(20)   NOT NULL
  COMMENT 'Data record creator',
  `modified`  BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`  BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'Basic information table'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Organizational structure information table.
CREATE TABLE `sys_org`
(
  `id`                BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `parent_id`         BIGINT(20)   NOT NULL DEFAULT 0
  COMMENT 'The parent node id',
  `code`              VARCHAR(128) NULL
  COMMENT 'Organizational architecture is uniquely coded.',
  `name`              VARCHAR(256) NOT NULL
  COMMENT 'Organizational architecture is uniquely name.',
  `dept_head_user_id` BIGINT(20)   NULL
  COMMENT 'ID of department head',
  `comment`           VARCHAR(256) NULL
  COMMENT 'description',
  `archive`           TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`           BIGINT(20)   NOT NULL
  COMMENT 'create time ',
  `creator`           BIGINT(20)   NOT NULL
  COMMENT 'Data record creator',
  `modified`          BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`          BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'Organizational structure information table.'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

--  System user information table.
CREATE TABLE `sys_user`
(
  `id`       BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `nickname` VARCHAR(128) NULL
  COMMENT 'user nickname.',
  `username` VARCHAR(256) NOT NULL
  COMMENT 'The user login name.',
  `password` VARCHAR(128) NOT NULL
  COMMENT 'The user login password.',
  `avatar`   VARCHAR(512) NULL
  COMMENT 'avatar',
  `phone`    VARCHAR(16)  NULL
  COMMENT 'cell-phone number',
  `email`    VARCHAR(128) NULL
  COMMENT 'email address',
  `status`   VARCHAR(32)  NOT NULL DEFAULT 'INACTIVE'
  COMMENT 'Account status INACTIVE, NORMAL, LOCK.',
  `archive`  TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`  BIGINT(20)   NOT NULL
  COMMENT 'create time ',
  `creator`  BIGINT(20)   NOT NULL
  COMMENT 'Data record creator',
  `modified` BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier` BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'System user information table'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- User - Org association table
CREATE TABLE `sys_user_rl_org`
(
  `id`       BIGINT(20) NOT NULL
  COMMENT 'primary key',
  `user_id`  BIGINT(20) NOT NULL
  COMMENT 'The user id',
  `org_id`   BIGINT(20) NOT NULL
  COMMENT 'The org id',
  `archive`  TINYINT(1) NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`  BIGINT(20) NOT NULL
  COMMENT 'create time ',
  `creator`  BIGINT(20) NOT NULL
  COMMENT 'Data record creator',
  `modified` BIGINT(20) NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier` BIGINT(20) NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'User - Org association table.'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

--  User session information table
CREATE TABLE `sys_user_session`
(
  `id`          BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `app_type`    TINYINT(1)   NOT NULL
  COMMENT 'The application type.',
  `user_id`     BIGINT(128)  NOT NULL
  COMMENT 'user id',
  `token`       VARCHAR(128) NOT NULL
  COMMENT 'User login token',
  `login_time`  BIGINT(20)   NULL
  COMMENT 'User login time',
  `logout_time` BIGINT(20)   NULL
  COMMENT 'User logout time',
  `expire_at`   BIGINT(20)   NOT NULL
  COMMENT 'Token expiration time',
  `archive`     TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`     BIGINT(20)   NOT NULL
  COMMENT 'create time ',
  `creator`     BIGINT(20)   NOT NULL
  COMMENT 'Data record creator',
  `modified`    BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`    BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'User session information table.'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- User role information table.
CREATE TABLE `sys_role`
(
  `id`       BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `name`     VARCHAR(128) NOT NULL
  COMMENT 'The role name',
  `code`     VARCHAR(128) NOT NULL
  COMMENT 'The role code',
  `type`     TINYINT(1)   NOT NULL
  COMMENT 'Role type: 0-normal; 1 - Default role; 3 - System role.',
  `status`   TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT 'Role state 0- normal, 1- locked',
  `comment`  VARCHAR(256) NULL
  COMMENT 'description',
  `archive`  TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`  BIGINT(20)   NOT NULL
  COMMENT 'create time ',
  `creator`  BIGINT(20)   NOT NULL
  COMMENT 'Data record creator',
  `modified` BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier` BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'User role information table.'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- User - role association table
CREATE TABLE `sys_user_rl_role`
(
  `id`       BIGINT(20) NOT NULL
  COMMENT 'primary key',
  `user_id`  BIGINT(20) NOT NULL
  COMMENT 'The user id',
  `role_id`  BIGINT(20) NOT NULL
  COMMENT 'The role id',
  `archive`  TINYINT(1) NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`  BIGINT(20) NOT NULL
  COMMENT 'create time ',
  `creator`  BIGINT(20) NOT NULL
  COMMENT 'Data record creator',
  `modified` BIGINT(20) NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier` BIGINT(20) NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'User - role association table.'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Resource information table
CREATE TABLE `sys_resource`
(
  `id`        BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `parent_id` BIGINT(20)   NOT NULL
  COMMENT 'The parent node id',
  `name`      VARCHAR(128) NOT NULL
  COMMENT 'The resource name',
  `code`      VARCHAR(128) NOT NULL
  COMMENT 'The resource code.',
  `icon`      VARCHAR(512) NULL
  COMMENT 'The resource icon',
  `type`      TINYINT(1)   NOT NULL
  COMMENT 'The resource type. 0 - menu; 1 - button.',
  `path`      VARCHAR(512) NULL
  COMMENT 'The resource path',
  `sort_id`   BIGINT(20)   NOT NULL DEFAULT 0
  COMMENT 'The resource sort field.',
  `comment`   VARCHAR(256) NULL
  COMMENT 'description',
  `archive`   TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`   BIGINT(20)   NOT NULL
  COMMENT 'create time ',
  `creator`   BIGINT(20)   NOT NULL
  COMMENT 'Data record creator',
  `modified`  BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`  BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'Resource information table'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Role - resource association table
CREATE TABLE `sys_role_rl_resource`
(
  `id`          BIGINT(20) NOT NULL
  COMMENT 'primary key',
  `role_id`     BIGINT(20) NOT NULL
  COMMENT 'The role id',
  `resource_id` BIGINT(20) NOT NULL
  COMMENT 'The resource id',
  `mask`        INT(11)    NOT NULL DEFAULT 0
  COMMENT 'Permissions mask 0 - blank; 1 - View; 2 - Add; 4 - Modification; 8 - Delete;',
  `archive`     TINYINT(1) NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`     BIGINT(20) NOT NULL
  COMMENT 'create time ',
  `creator`     BIGINT(20) NOT NULL
  COMMENT 'Data record creator',
  `modified`    BIGINT(20) NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`    BIGINT(20) NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'Role - resource association table.'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Role - datasource schema association table
CREATE TABLE `sys_role_rl_schema`
(
  `id`          BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `role_id`     BIGINT(20)   NOT NULL
  COMMENT 'The role id',
  `ds_id`       BIGINT(20)   NOT NULL
  COMMENT 'The datasource id',
  `table_name`  VARCHAR(512) NOT NULL
  COMMENT 'The table name',
  `expire_time` BIGINT(20)   NOT NULL
  COMMENT 'View expiration time.',
  `archive`     TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`     BIGINT(20)   NOT NULL
  COMMENT 'create time ',
  `creator`     BIGINT(20)   NOT NULL
  COMMENT 'Data record creator',
  `modified`    BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`    BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'Role - datasource schema association table.'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Serve information table.
CREATE TABLE `serve_info`
(
  `id`          BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `name`        VARCHAR(64)  NOT NULL
  COMMENT 'serve name.',
  `icon`        VARCHAR(256) NULL
  COMMENT 'The serve icon url',
  `time_based`  TINYINT(1)   NULL
  COMMENT 'Timeliness of serve data. 0 - real time, 1 - minutes, 2 - hours, 3 - days',
  `serve_type`  VARCHAR(32)  NOT NULL
  COMMENT 'serve type , 1 - table; 2 - interface; eg.',
  `visual_type` INT(11)      NOT NULL
  COMMENT 'the serve visual type , 1 - table; 2 - line; 4 - bar; eg.',
  `status`      VARCHAR(32)  NOT NULL
  COMMENT 'the serve status',
  `sort_id`     BIGINT(20)   NOT NULL
  COMMENT 'Sort field',
  `comment`     VARCHAR(256) NULL
  COMMENT 'description',
  `config_text` TEXT         NULL
  COMMENT 'The serve config text information.',
  `archive`     TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`     BIGINT(20)   NOT NULL
  COMMENT 'create time ',
  `creator`     BIGINT(20)   NOT NULL
  COMMENT 'Data record creator',
  `modified`    BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`    BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'Serve information table.'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Serve information table.
CREATE TABLE `serve_group`
(
  `id`         BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `parent_id`  BIGINT(20)   NOT NULL DEFAULT 0
  COMMENT 'The parent node id',
  `name`       VARCHAR(64)  NOT NULL
  COMMENT 'group name.',
  `icon`       VARCHAR(256) NULL
  COMMENT 'The serve icon url',
  `serve_type` VARCHAR(32)  NOT NULL
  COMMENT 'serve type , 1 - table; 2 - interface; eg.',
  `sort_id`    BIGINT(20)   NOT NULL
  COMMENT 'Sort field',
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
  COMMENT 'Serve group table.'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Service and grouping association relational table.
CREATE TABLE `serve_rl_group`
(
  `id`       BIGINT(20) NOT NULL
  COMMENT 'primary key',
  `serve_id` BIGINT(20) NOT NULL
  COMMENT 'The serve id',
  `group_id` BIGINT(20) NOT NULL
  COMMENT 'The group id',
  `archive`  TINYINT(1) NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`  BIGINT(20) NOT NULL
  COMMENT 'create time ',
  `creator`  BIGINT(20) NOT NULL
  COMMENT 'Data record creator',
  `modified` BIGINT(20) NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier` BIGINT(20) NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'Service and grouping association relational tables.'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- datasource Data source information table.
CREATE TABLE `datasource`
(
  `id`             BIGINT(20)    NOT NULL
  COMMENT 'primary key',
  `name`           VARCHAR(64)   NOT NULL
  COMMENT 'The data source name',
  `type`           VARCHAR(64)   NOT NULL
  COMMENT 'The data source type. eg: MySQL, Oracle',
  `status`         TINYINT(1)    NOT NULL DEFAULT 0
  COMMENT 'The data source statue , 0 - usable; 1 - disabled',
  `jdbc_driver`    VARCHAR(256)  NULL
  COMMENT 'The data source driver class.',
  `jdbc_url`       VARCHAR(512)  NOT NULL
  COMMENT 'The data source jdbc url.',
  `username`       VARCHAR(64)   NOT NULL
  COMMENT 'The data source login username.',
  `password`       VARCHAR(64)   NULL     DEFAULT NULL
  COMMENT 'The data source login password.',
  `sync`           TINYINT(1)    NULL     DEFAULT 0
  COMMENT 'Data source synchronization state. 0 - Out of sync; 1 - sync',
  `last_sync_time` BIGINT(20)    NULL
  COMMENT 'The last sync time.',
  `cron`           VARCHAR(128)  NULL
  COMMENT 'Synchronization period expression ''0 0 9 * * ?''',
  `max_pool_size`  INT(11)       NOT NULL DEFAULT 5
  COMMENT 'The maximum number of connections in the pool',
  `min_idle`       INT(11)       NOT NULL DEFAULT 5
  COMMENT 'The minimum number of idle connections in the pool to maintain',
  `read_only`      TINYINT(1)    NOT NULL DEFAULT 0
  COMMENT 'Read-only data source tag',
  `can_ddl`        TINYINT(1)    NOT NULL DEFAULT 0
  COMMENT ' Can support DDL operations',
  `timeout`        INT(11)       NULL     DEFAULT 60
  COMMENT 'Connection pool timeout(ms)',
  `test_sql`       VARCHAR(1024) NULL
  COMMENT 'The check status SQL',
  `comment`        VARCHAR(256)  NULL
  COMMENT 'description',
  `archive`        TINYINT(1)    NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`        BIGINT(20)    NOT NULL
  COMMENT 'create time ',
  `creator`        BIGINT(20)    NOT NULL
  COMMENT 'Data record creator',
  `modified`       BIGINT(20)    NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`       BIGINT(20)    NULL     DEFAULT NULL
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

-- DDL audit information table
CREATE TABLE `ddl_audit_info`
(
  `id`            BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `name`          VARCHAR(128) NOT NULL
  COMMENT 'ddl audit name.',
  `datasource_id` BIGINT(20)   NOT NULL
  COMMENT 'the data source id.',
  `ddl_text`      TEXT         NULL
  COMMENT 'The ddl information',
  `status`        VARCHAR(32)  NOT NULL
  COMMENT 'audit status',
  `auditor`       BIGINT(20)   NULL
  COMMENT 'auditor',
  `opinion`       VARCHAR(512) NULL
  COMMENT 'The audit opinion',
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
  COMMENT 'DDL audit information table'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- 取数实例信息表 pd_peek
CREATE TABLE `pd_peek`
(
  `id`         BIGINT(20)  NOT NULL
  COMMENT '主键Id',
  `model_id`   BIGINT(20)  NOT NULL
  COMMENT '模型Id',
  `name`       VARCHAR(64) NOT NULL
  COMMENT '取数实例名称',
  `field_list` TEXT COMMENT '返回的数据字段名列表, 多个以","隔开',
  `peek_time`  INT(11)     NULL     DEFAULT 0
  COMMENT '取数次数',
  `archive`    TINYINT(1)  NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`    BIGINT(20)  NOT NULL
  COMMENT '创建时间',
  `creator`    BIGINT(20)  NOT NULL
  COMMENT '创建人',
  `modified`   BIGINT(20)  NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`   BIGINT(20)  NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '取数实例信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- 取数字段信息表 pd_peek_field
CREATE TABLE `pd_peek_field`
(
  `id`             BIGINT(20)  NOT NULL
  COMMENT '主键Id',
  `peek_id`        BIGINT(20)  NOT NULL
  COMMENT '取数id',
  `meta_id`        BIGINT(20)  NULL
  COMMENT '字段id',
  `type`           TINYINT(1)  NULL     DEFAULT 0
  COMMENT '类型; 0 -维度; 1-指标',
  `agg_expression` VARCHAR(50) NULL
  COMMENT '聚合函数',
  `data_type`      VARCHAR(50) NULL
  COMMENT '数据类型',
  `format`         VARCHAR(50) NULL
  COMMENT '格式',
  `archive`        TINYINT(1)  NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`        BIGINT(20)  NOT NULL
  COMMENT '创建时间',
  `creator`        BIGINT(20)  NOT NULL
  COMMENT '创建人',
  `modified`       BIGINT(20)  NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`       BIGINT(20)  NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '取数字段信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- 取数规则信息表 pd_peek_rule
CREATE TABLE `pd_peek_rule`
(
  `id`          BIGINT(20)   NOT NULL
  COMMENT '主键Id',
  `peek_id`     BIGINT(20)   NOT NULL
  COMMENT '取数id',
  `meta_id`     BIGINT(20)   NULL
  COMMENT '字段id',
  `field_name`  VARCHAR(128) COMMENT '字段名称',
  `rule`        VARCHAR(64)  NOT NULL
  COMMENT '规则名称',
  `input_value` VARCHAR(512) NOT NULL
  COMMENT '代入值',
  `archive`     TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`     BIGINT(20)   NOT NULL
  COMMENT '创建时间',
  `creator`     BIGINT(20)   NOT NULL
  COMMENT '创建人',
  `modified`    BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`    BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '取数规则信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- 模型信息表 pd_model
CREATE TABLE `pd_model`
(
  `id`            BIGINT(20)   NOT NULL
  COMMENT '主键Id',
  `name`          VARCHAR(64)  NOT NULL
  COMMENT '模型名称',
  `datasource_id` BIGINT(20)   NOT NULL
  COMMENT '数据源Id',
  `table_name`    VARCHAR(512) NOT NULL
  COMMENT '具体表名',
  `status`        TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '模型状态 0 - 使用中; 1 - 已停用',
  `comment`       VARCHAR(512) NULL
  COMMENT '描述',
  `archive`       TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`       BIGINT(20)   NOT NULL
  COMMENT '创建时间',
  `creator`       BIGINT(20)   NOT NULL
  COMMENT '创建人',
  `modified`      BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`      BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '模型信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- 模型元素信息表 pd_model_meta
CREATE TABLE `pd_model_meta`
(
  `id`               BIGINT(20)   NOT NULL
  COMMENT '主键Id',
  `model_id`         BIGINT(20)   NOT NULL
  COMMENT '模型Id',
  `name`             VARCHAR(64)  NOT NULL
  COMMENT '元素名称(表字段名称)',
  `show_name`        VARCHAR(64)  NOT NULL
  COMMENT '显示名称',
  `origin_data_type` VARCHAR(64)  NOT NULL
  COMMENT '原始数据类型',
  `data_type`        VARCHAR(64)  NOT NULL
  COMMENT '数据类型',
  `display`          TINYINT(1)   NULL     DEFAULT 1
  COMMENT '是否显示, 1-显示; 0 - 不显示',
  `tag_id`           BIGINT(20)            DEFAULT NULL
  COMMENT '标签id',
  `comment`          VARCHAR(255) NULL
  COMMENT '字段详细描述',
  `archive`          TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`          BIGINT(20)   NOT NULL
  COMMENT '创建时间',
  `creator`          BIGINT(20)   NOT NULL
  COMMENT '创建人',
  `modified`         BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`         BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '模型元素信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- 模型标签表 pd_model_tag
CREATE TABLE `pd_model_tag`
(
  `id`        BIGINT(20)  NOT NULL
  COMMENT '主键Id',
  `name`      VARCHAR(64) NOT NULL
  COMMENT '标签名',
  `rule`      VARCHAR(64) NOT NULL
  COMMENT '匹配规则',
  `defaulted` TINYINT(1)           DEFAULT '0'
  COMMENT '是否默认标签,0为非默认,1为默认',
  `archive`   TINYINT(1)  NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`   BIGINT(20)  NOT NULL
  COMMENT '创建时间',
  `creator`   BIGINT(20)  NOT NULL
  COMMENT '创建人',
  `modified`  BIGINT(20)  NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`  BIGINT(20)  NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '模型标签表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- 数据导入记录表 pd_import_record
CREATE TABLE `pd_import_record`
(
  `id`             BIGINT(20)   NOT NULL
  COMMENT '主键Id',
  `datasource_id`  BIGINT(20)   NOT NULL
  COMMENT '数据源Id',
  `table_name`     VARCHAR(128) NOT NULL
  COMMENT '具体表名',
  `file_name`      VARCHAR(512) NOT NULL
  COMMENT '导入文件名称',
  `cover`          TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '是否覆盖 0 - 不覆盖,追加; 1 - 覆盖',
  `partition_name` VARCHAR(200)          DEFAULT NULL
  COMMENT '分区名称',
  `status`         TINYINT(1)            DEFAULT NULL
  COMMENT '导入状态,0:导入中,1导入成功,2导入失败',
  `archive`        TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`        BIGINT(20)   NOT NULL
  COMMENT '创建时间',
  `creator`        BIGINT(20)   NOT NULL
  COMMENT '创建人',
  `modified`       BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`       BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '数据导入记录表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- quartz_task Custom timer task information table
CREATE TABLE `quartz_task`
(
  `id`            BIGINT(20) UNSIGNED NOT NULL
  COMMENT 'primary key',
  `task_name`     VARCHAR(512)        NOT NULL
  COMMENT 'The task name',
  `group_name`    VARCHAR(128)        NOT NULL
  COMMENT 'task group',
  `cron`          VARCHAR(128)        NOT NULL
  COMMENT 'Cron expression',
  `status`        TINYINT(1)          NOT NULL  DEFAULT 0
  COMMENT 'status 0 - block up; 1 - invoke',
  `job_class`     VARCHAR(512)        NULL
  COMMENT 'The class in which the job resides',
  `params`        TEXT                NULL
  COMMENT 'Run task parameters (JSON format)',
  `last_run_time` BIGINT(20)          NULL
  COMMENT 'Last run time',
  `archive`       TINYINT(1)          NOT NULL  DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`       BIGINT(20)          NOT NULL
  COMMENT 'create time ',
  `creator`       BIGINT(20)          NOT NULL
  COMMENT 'Data record creator',
  `modified`      BIGINT(20)          NULL      DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`      BIGINT(20)          NULL      DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'Custom timer task information table'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Store a jobDetail
CREATE TABLE qrtz_job_details (
  sched_name        VARCHAR(120) NOT NULL,
  job_name          VARCHAR(190) NOT NULL,
  job_group         VARCHAR(190) NOT NULL,
  description       VARCHAR(250) NULL,
  job_class_name    VARCHAR(250) NOT NULL,
  is_durable        VARCHAR(1)   NOT NULL,
  is_nonconcurrent  VARCHAR(1)   NOT NULL,
  is_update_data    VARCHAR(1)   NOT NULL,
  requests_recovery VARCHAR(1)   NOT NULL,
  job_data          BLOB         NULL,
  KEY `idx_qrtz_j_req_recovery` (sched_name, requests_recovery) USING BTREE,
  KEY `idx_qrtz_j_grp` (sched_name, job_group) USING BTREE,
  PRIMARY KEY (sched_name, job_name, job_group)
)
  ENGINE = InnoDB
  COMMENT 'Store a jobDetail'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Basic information about triggers
CREATE TABLE qrtz_triggers (
  sched_name     VARCHAR(120) NOT NULL,
  trigger_name   VARCHAR(190) NOT NULL,
  trigger_group  VARCHAR(190) NOT NULL,
  job_name       VARCHAR(190) NOT NULL,
  job_group      VARCHAR(190) NOT NULL,
  description    VARCHAR(250) NULL,
  next_fire_time BIGINT(13)   NULL,
  prev_fire_time BIGINT(13)   NULL,
  priority       INTEGER      NULL,
  trigger_state  VARCHAR(16)  NOT NULL,
  trigger_type   VARCHAR(8)   NOT NULL,
  start_time     BIGINT(13)   NOT NULL,
  end_time       BIGINT(13)   NULL,
  calendar_name  VARCHAR(190) NULL,
  misfire_instr  SMALLINT(2)  NULL,
  job_data       BLOB         NULL,
  PRIMARY KEY (sched_name, trigger_name, trigger_group),
  KEY `idx_qrtz_t_j` (sched_name, job_name, job_group),
  KEY `idx_qrtz_t_jg` (sched_name, job_group),
  KEY `idx_qrtz_t_c` (sched_name, calendar_name),
  KEY `idx_qrtz_t_g` (sched_name, trigger_group),
  KEY `idx_qrtz_t_state` (sched_name, trigger_state),
  KEY `idx_qrtz_t_n_state` (sched_name, trigger_name, trigger_group, trigger_state),
  KEY `idx_qrtz_t_n_g_state` (sched_name, trigger_group, trigger_state),
  KEY `idx_qrtz_t_next_fire_time` (sched_name, next_fire_time),
  KEY `idx_qrtz_t_nft_st` (sched_name, trigger_state, next_fire_time),
  KEY `idx_qrtz_t_nft_misfire` (sched_name, misfire_instr, next_fire_time),
  KEY `idx_qrtz_t_nft_st_misfire` (sched_name, misfire_instr, next_fire_time, trigger_state),
  KEY `idx_qrtz_t_nft_st_misfire_grp` (sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state),
  FOREIGN KEY (sched_name, job_name, job_group)
  REFERENCES qrtz_job_details (sched_name, job_name, job_group)
)
  ENGINE = InnoDB
  COMMENT 'Basic information about triggers'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Information for a simple trigger
CREATE TABLE qrtz_simple_triggers (
  sched_name      VARCHAR(120) NOT NULL,
  trigger_name    VARCHAR(190) NOT NULL,
  trigger_group   VARCHAR(190) NOT NULL,
  repeat_count    BIGINT(7)    NOT NULL,
  repeat_interval BIGINT(12)   NOT NULL,
  times_triggered BIGINT(10)   NOT NULL,
  PRIMARY KEY (sched_name, trigger_name, trigger_group),
  FOREIGN KEY (sched_name, trigger_name, trigger_group)
  REFERENCES qrtz_triggers (sched_name, trigger_name, trigger_group)
)
  ENGINE = InnoDB
  COMMENT 'Information for a simple trigger'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Store a Cron trigger
CREATE TABLE qrtz_cron_triggers (
  sched_name      VARCHAR(120) NOT NULL,
  trigger_name    VARCHAR(190) NOT NULL,
  trigger_group   VARCHAR(190) NOT NULL,
  cron_expression VARCHAR(120) NOT NULL,
  time_zone_id    VARCHAR(80),
  PRIMARY KEY (sched_name, trigger_name, trigger_group),
  FOREIGN KEY (sched_name, trigger_name, trigger_group)
  REFERENCES qrtz_triggers (sched_name, trigger_name, trigger_group)
)
  ENGINE = InnoDB
  COMMENT 'Store a Cron trigger'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;
-- Details of simple triggers
CREATE TABLE qrtz_simprop_triggers
(
  sched_name    VARCHAR(120)   NOT NULL,
  trigger_name  VARCHAR(190)   NOT NULL,
  trigger_group VARCHAR(190)   NOT NULL,
  str_prop_1    VARCHAR(512)   NULL,
  str_prop_2    VARCHAR(512)   NULL,
  str_prop_3    VARCHAR(512)   NULL,
  int_prop_1    INT            NULL,
  int_prop_2    INT            NULL,
  long_prop_1   BIGINT         NULL,
  long_prop_2   BIGINT         NULL,
  dec_prop_1    NUMERIC(13, 4) NULL,
  dec_prop_2    NUMERIC(13, 4) NULL,
  bool_prop_1   VARCHAR(1)     NULL,
  bool_prop_2   VARCHAR(1)     NULL,
  PRIMARY KEY (sched_name, trigger_name, trigger_group),
  FOREIGN KEY (sched_name, trigger_name, trigger_group)
  REFERENCES qrtz_triggers (sched_name, trigger_name, trigger_group)
)
  ENGINE = InnoDB
  COMMENT 'Details of simple triggers'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Store a Blob type triggers
CREATE TABLE qrtz_blob_triggers (
  sched_name    VARCHAR(120) NOT NULL,
  trigger_name  VARCHAR(190) NOT NULL,
  trigger_group VARCHAR(190) NOT NULL,
  blob_data     BLOB         NULL,
  PRIMARY KEY (sched_name, trigger_name, trigger_group),
  INDEX (sched_name, trigger_name, trigger_group),
  FOREIGN KEY (sched_name, trigger_name, trigger_group)
  REFERENCES qrtz_triggers (sched_name, trigger_name, trigger_group)
)
  ENGINE = InnoDB
  COMMENT 'Store a Blob type triggers'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Store calendar information, Quartz can configure a calendar to specify a time range
CREATE TABLE qrtz_calendars (
  sched_name    VARCHAR(120) NOT NULL,
  calendar_name VARCHAR(190) NOT NULL,
  calendar      BLOB         NOT NULL,
  PRIMARY KEY (sched_name, calendar_name)
)
  ENGINE = InnoDB
  COMMENT 'Store calendar information, Quartz can configure a calendar to specify a time range'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Store a suspended trigger
CREATE TABLE qrtz_paused_trigger_grps (
  sched_name    VARCHAR(120) NOT NULL,
  trigger_group VARCHAR(190) NOT NULL,
  PRIMARY KEY (sched_name, trigger_group)
)
  ENGINE = InnoDB
  COMMENT 'Store a suspended trigger'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Stores the triggered trigger
CREATE TABLE qrtz_fired_triggers (
  sched_name        VARCHAR(120) NOT NULL,
  entry_id          VARCHAR(95)  NOT NULL,
  trigger_name      VARCHAR(190) NOT NULL,
  trigger_group     VARCHAR(190) NOT NULL,
  instance_name     VARCHAR(190) NOT NULL,
  fired_time        BIGINT(13)   NOT NULL,
  sched_time        BIGINT(13)   NOT NULL,
  priority          INTEGER      NOT NULL,
  state             VARCHAR(16)  NOT NULL,
  job_name          VARCHAR(190) NULL,
  job_group         VARCHAR(190) NULL,
  is_nonconcurrent  VARCHAR(1)   NULL,
  requests_recovery VARCHAR(1)   NULL,
  PRIMARY KEY (sched_name, entry_id),
  KEY `idx_qrtz_ft_trig_inst_name`  (sched_name, instance_name) USING BTREE,
  KEY `idx_qrtz_ft_inst_job_req_rcvry` (sched_name, instance_name, requests_recovery) USING BTREE,
  KEY `idx_qrtz_ft_j_g` (sched_name, job_name, job_group) USING BTREE,
  KEY `idx_qrtz_ft_jg` (sched_name, job_group) USING BTREE,
  KEY `idx_qrtz_ft_t_g` (sched_name, trigger_name, trigger_group) USING BTREE,
  KEY `idx_qrtz_ft_tg` (sched_name, trigger_group) USING BTREE
)
  ENGINE = InnoDB
  COMMENT 'Stores the triggered trigger'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Scheduler state
CREATE TABLE qrtz_scheduler_state (
  sched_name        VARCHAR(120) NOT NULL,
  instance_name     VARCHAR(190) NOT NULL,
  last_checkin_time BIGINT(13)   NOT NULL,
  checkin_interval  BIGINT(13)   NOT NULL,
  PRIMARY KEY (sched_name, instance_name)
)
  ENGINE = InnoDB
  COMMENT 'Scheduler state'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Stores information about the pessimistic lock of the program
CREATE TABLE qrtz_locks (
  sched_name VARCHAR(120) NOT NULL,
  lock_name  VARCHAR(40)  NOT NULL,
  PRIMARY KEY (sched_name, lock_name)
)
  ENGINE = InnoDB
  COMMENT 'Stores information about the pessimistic lock of the program'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;
