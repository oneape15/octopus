-- Report information table.
CREATE TABLE `report`
(
  `id`              BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `name`            VARCHAR(64)  NOT NULL
  COMMENT 'report name.',
  `time_based`      TINYINT(1)   NULL
  COMMENT 'Timeliness of report data. 0 - real time, 1 - minutes, 2 - hours, 3 - days',
  `report_type`     VARCHAR(128) NOT NULL
  COMMENT 'report type , 1 - table; 2 - pie; 3 - bar , Multiple are separated by commas',
  `x_axis`          VARCHAR(64)  NULL
  COMMENT 'When the chart is displayed, the X-axis column name; Multiple with ";" separated',
  `y_axis`          VARCHAR(64)  NULL
  COMMENT 'When the chart is displayed, the Y-axis column name; Multiple with ";" separated',
  `param_label_len` SMALLINT     NULL
  COMMENT 'Query the field label display length',
  `param_media_len` SMALLINT     NULL
  COMMENT 'Query the field control display length',
  `sort_id`         BIGINT(20)   NOT NULL
  COMMENT 'Sort field',
  `comment`         VARCHAR(256) NULL
  COMMENT 'description',
  `archive`         TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`         BIGINT(20)   NOT NULL
  COMMENT 'create time ',
  `creator`         BIGINT(20)   NOT NULL
  COMMENT 'Data record creator',
  `modified`        BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`        BIGINT(20)   NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'Report information table.'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

--  Report field information table.
CREATE TABLE `report_column`
(
  `id`                BIGINT(20)    NOT NULL
  COMMENT 'primary key',
  `report_id`         BIGINT(20)    NOT NULL
  COMMENT 'The report Id',
  `name`              VARCHAR(128)  NOT NULL
  COMMENT 'The report column name',
  `alias`             VARCHAR(128)  NOT NULL
  COMMENT 'The report column alias name',
  `data_type`         VARCHAR(32)   NOT NULL
  COMMENT 'The report column data type',
  `unit`              VARCHAR(32)   NULL
  COMMENT 'data unit',
  `unit_use_where`    TINYINT(1)    NULL     DEFAULT 0
  COMMENT 'Where does the unit symbol apply. 0 - the table head; 1 - each row item.',
  `default_value`     VARCHAR(32)   NULL
  COMMENT 'The default value when the data item is empty.',
  `hidden`            TINYINT(1)    NULL     DEFAULT 0
  COMMENT 'Is a hidden column; 0 - no; 1 - yes',
  `drill_source_type` TINYINT(1)    NULL
  COMMENT 'Type of resource to jump when drilling down a column. eg: 1 - report; 2 - dashboard; 3 - External links.',
  `drill_uri`         VARCHAR(1024) NULL
  COMMENT 'Drill down to the resource URI.',
  `drill_params`      VARCHAR(256)  NULL
  COMMENT 'The parameters required when drilling down the column; kv1=column_name1; kv2=column_name2;',
  `drill_open_type`   TINYINT(1)    NULL
  COMMENT 'When drilling down the column, open mode 1 - new window; 2 - Current window; 3 - Popover page.',
  `frozen`            TINYINT(1)    NULL     DEFAULT 0
  COMMENT 'Is a frozen column; 0 - no; 1 - yes',
  `support_sort`      TINYINT(1)    NOT NULL DEFAULT 0
  COMMENT 'Whether sorting is supported; 0 - no; 1 - yes',
  `format_macro`      VARCHAR(128)  NULL
  COMMENT 'Data formatting macro',
  `sort_id`           BIGINT(20)    NOT NULL
  COMMENT 'Sort field',
  `comment`           VARCHAR(256)  NULL
  COMMENT 'description',
  `archive`           TINYINT(1)    NOT NULL DEFAULT 0
  COMMENT '0 - normal data; 1 - have archive (soft delete)',
  `created`           BIGINT(20)    NOT NULL
  COMMENT 'create time ',
  `creator`           BIGINT(20)    NOT NULL
  COMMENT 'Data record creator',
  `modified`          BIGINT(20)    NULL     DEFAULT NULL
  COMMENT 'Last updated time',
  `modifier`          BIGINT(20)    NULL     DEFAULT NULL
  COMMENT 'Data record  modifier',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'Report field information table.'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Report query parameter information table.
CREATE TABLE `report_param`
(
  `id`            BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `report_id`     BIGINT(20)   NOT NULL
  COMMENT 'The report Id',
  `name`          VARCHAR(128) NOT NULL
  COMMENT 'The report param name',
  `alias`         VARCHAR(128) NOT NULL
  COMMENT 'The report param alias name',
  `data_type`     VARCHAR(32)  NOT NULL
  COMMENT 'The report param data type',
  `val_default`   VARCHAR(64)  NULL
  COMMENT 'default value',
  `val_max`       VARCHAR(64)  NULL
  COMMENT 'The maximum value',
  `val_min`       VARCHAR(64)  NULL
  COMMENT 'The minimum value',
  `val_forbidden` VARCHAR(256) NULL
  COMMENT 'Prohibited value',
  `must_fill_in`  TINYINT(1)   NULL     DEFAULT 0
  COMMENT 'Is a required field 0 - no; 1 - yes',
  `placeholder`   VARCHAR(64)  NULL
  COMMENT 'Parameters briefly describe information',
  `err_message`   VARCHAR(128) NULL
  COMMENT 'Prompt message when error occurs',
  `depend_on`     VARCHAR(512) NULL
  COMMENT 'Dependency, taking the name value, multiple separated by commas.',
  `type`          INT(11)      NOT NULL DEFAULT 0
  COMMENT 'The parameter types; 0 - normal; 1 - inline; 2 - between; 4 - multi;',
  `lov_report_id` BIGINT(20)   NULL
  COMMENT 'When the field value content depends on another SQL query result (LOV), fill in',
  `lov_kv_name`   VARCHAR(128) NULL
  COMMENT 'The query parameter LOV is the specified KV name',
  `sort_id`       BIGINT(20)   NOT NULL
  COMMENT 'Sort field',
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
  COMMENT 'Report query parameter information table'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- A report DSL describes information tables
CREATE TABLE `report_dsl`
(
  `id`            BIGINT(20)   NOT NULL
  COMMENT 'primary key',
  `datasource_id` BIGINT(20)   NOT NULL
  COMMENT 'dependency on the data source id',
  `report_id`     BIGINT(20)   NOT NULL
  COMMENT 'the owner id of report',
  `cached_time`   INT(11)      NULL
  COMMENT 'Cache time (seconds)',
  `timeout`       INT(11)      NULL
  COMMENT 'timeout time (seconds)',
  `text`          TEXT         NOT NULL
  COMMENT 'dsl sql content',
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
  COMMENT 'A report DSL describes information tables.'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- Rich text information table
CREATE TABLE `help_document`
(
  `id`       BIGINT(20) NOT NULL
  COMMENT 'primary key',
  `biz_type` TINYINT(1) NOT NULL
  COMMENT 'Business types. 0 - report; 1 - dashboard; 2 - interface;',
  `biz_id`   BIGINT(20) NOT NULL
  COMMENT 'the business primary key',
  `text`     TEXT       NULL
  COMMENT 'the rich text content',
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
  COMMENT 'A report DSL describes information tables.'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

