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

-- r_report_group 报表分组表
CREATE TABLE `r_report_group`
(
  `id`        BIGINT(20)   NOT NULL,
  `parent_id` BIGINT(20)   NOT NULL DEFAULT 0
  COMMENT '父级节点Id, 默认为0',
  `name`      VARCHAR(128) NOT NULL
  COMMENT '报表组名',
  `icon`      VARCHAR(256) NULL
  COMMENT '报表组图标',
  `status`    TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '状态 0 - 编辑中; 1 - 上线;',
  `level`     TINYINT(1)   NOT NULL DEFAULT 1
  COMMENT '所在层级',
  `owner`     BIGINT(20)   NOT NULL
  COMMENT '报表组拥有者',
  `sort_id`   BIGINT(20)   NOT NULL
  COMMENT '排序Id',
  `comment`   VARCHAR(256) NULL
  COMMENT '描述',
  `archive`   TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`   BIGINT(20)   NOT NULL
  COMMENT '创建时间',
  `creator`   BIGINT(20)   NOT NULL
  COMMENT '创建人',
  `modified`  BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`  BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '报表分组表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- r_report 报表信息表
CREATE TABLE `r_report`
(
  `id`              BIGINT(20)   NOT NULL,
  `code`            VARCHAR(32)  NOT NULL
  COMMENT '报表编码全局唯一',
  `name`            VARCHAR(32)  NOT NULL
  COMMENT '报表名称',
  `icon`            VARCHAR(256) NULL
  COMMENT '报表图标',
  `report_type`     VARCHAR(128) NOT NULL
  COMMENT '报表类型, 1 - 表格; 2 - 图表; 3 - xxx等; 多个以逗号分隔',
  `x_axis`          VARCHAR(64)  NULL
  COMMENT '图表显示时, x轴列名; 多个以";"隔开',
  `y_axis`          VARCHAR(64)  NULL
  COMMENT '图表显示时,y轴列名; 多个以";"隔开',
  `report_sql_id`   BIGINT(20)   NULL
  COMMENT '报表详情查询Id',
  `param_label_len` SMALLINT     NULL
  COMMENT '查询字段标签显示长度',
  `param_media_len` SMALLINT     NULL
  COMMENT '查询字段控件显示长度',
  `lov`             TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '是否为lov； 0 - 普通报表; 1 - LOV报表',
  `flow_switch`     TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '流量开关',
  `owner`           BIGINT(20)   NOT NULL DEFAULT -1
  COMMENT '拥有者',
  `sort_id`         BIGINT(20)   NOT NULL
  COMMENT '排序Id',
  `comment`         VARCHAR(512) NULL
  COMMENT '描述信息',
  `archive`         TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`         BIGINT(20)   NOT NULL
  COMMENT '创建时间',
  `creator`         BIGINT(20)   NOT NULL
  COMMENT '创建人',
  `modified`        BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`        BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '报表信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- r_group_rl_report 报表与报表组关联关系表
CREATE TABLE `r_group_rl_report`
(
  `id`        BIGINT(20) NOT NULL,
  `report_id` BIGINT(20) NOT NULL
  COMMENT '报表Id',
  `group_id`  BIGINT(20) NOT NULL
  COMMENT '报表组Id',
  `archive`   TINYINT(1) NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`   BIGINT(20) NOT NULL
  COMMENT '创建时间',
  `creator`   BIGINT(20) NOT NULL
  COMMENT '创建人',
  `modified`  BIGINT(20) NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`  BIGINT(20) NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '报表与报表组关联关系表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- r_report_column 报表字段信息表
CREATE TABLE `r_report_column`
(
  `id`              BIGINT(20)   NOT NULL,
  `report_id`       BIGINT(20)   NOT NULL
  COMMENT '报表Id',
  `raw`             TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '是否为原生的列; 0 - 原生的; 1 - 加工过(分裂新生成的)',
  `name`            VARCHAR(32)  NOT NULL
  COMMENT '列名',
  `show_name`       VARCHAR(32)  NOT NULL
  COMMENT '显示名',
  `data_type`       VARCHAR(32)  NOT NULL
  COMMENT '数据类型',
  `unit`            VARCHAR(32)  NULL
  COMMENT '数据单位',
  `hidden`          TINYINT(1)   NULL     DEFAULT 0
  COMMENT '是否为隐藏列; 0 - 正常显示; 1 - 隐藏列',
  `drill_report_id` BIGINT(20)   NULL
  COMMENT '下钻查询详细报表Id',
  `drill_params`    VARCHAR(256) NULL
  COMMENT '下钻列时,需要的参数; kv1= column_name1; kv2 = column_name2;',
  `frozen`          TINYINT(1)   NULL     DEFAULT 0
  COMMENT '是否为冻结列; 0 - 否; 1 - 冻结列',
  `support_sort`    TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '是否支持排序; 0 - 否; 1 - 是',
  `split`           TINYINT(1)   NULL     DEFAULT 0
  COMMENT '是否需要分裂; 0 - 否; 1 - 需要',
  `split_char`      VARCHAR(16)  NULL
  COMMENT '分裂分隔字符串',
  `split_kv_char`   VARCHAR(16)  NULL
  COMMENT '分裂后kv的分隔字符串',
  `format_macro`    VARCHAR(128) NULL
  COMMENT '数据格式化宏',
  `sort_id`         BIGINT(20)   NOT NULL
  COMMENT '排序Id',
  `comment`         VARCHAR(512) NULL
  COMMENT '描述信息',
  `archive`         TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`         BIGINT(20)   NOT NULL
  COMMENT '创建时间',
  `creator`         BIGINT(20)   NOT NULL
  COMMENT '创建人',
  `modified`        BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`        BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '报表字段信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- r_report_param 报表查询字段信息表
CREATE TABLE `r_report_param`
(
  `id`            BIGINT(20)   NOT NULL,
  `report_id`     BIGINT(20)   NOT NULL
  COMMENT '报表Id',
  `show_name`     VARCHAR(32)  NOT NULL
  COMMENT '字段显示名',
  `param_name`    VARCHAR(32)  NOT NULL
  COMMENT '查询sql代入名',
  `data_type`     VARCHAR(32)  NOT NULL
  COMMENT '字段数据类型',
  `val_default`   VARCHAR(64)  NULL
  COMMENT '默认值',
  `val_max`       VARCHAR(64)  NULL
  COMMENT '最大值',
  `val_min`       VARCHAR(64)  NULL
  COMMENT '最小值',
  `val_forbidden` VARCHAR(256) NULL
  COMMENT '禁止使用值',
  `must_fill_in`  TINYINT(1)   NULL     DEFAULT 0
  COMMENT '是否为必填字段 0 - 否; 1 - 必填',
  `order_by_type` VARCHAR(16)  NULL
  COMMENT '排序方式 ASC, DESC',
  `placeholder`   VARCHAR(64)  NULL
  COMMENT '提示信息',
  `err_message`   VARCHAR(128) NULL
  COMMENT '错误提示信息',
  `depend_on`     VARCHAR(512) NULL
  COMMENT '依赖关系, 取query_name值,多个以逗号分隔',
  `type`          INT(11)      NOT NULL DEFAULT 0
  COMMENT '字段类型; 0 - 普通字段; 1 - 内部字段; 2 - 多选字段; 4 - lov选择; ',
  `lov_report_id` BIGINT(20)   NOT NULL DEFAULT 0
  COMMENT '当字段值内容依赖别一个sql查询结果时(LOV), 填入',
  `sort_id`       BIGINT(20)   NOT NULL
  COMMENT '排序Id',
  `comment`       VARCHAR(512) NULL
  COMMENT '描述信息',
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
  COMMENT '报表查询字段信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- r_report_sql 报表执行sql表
CREATE TABLE `r_report_sql`
(
  `id`              BIGINT(20)   NOT NULL
  COMMENT '主键Id',
  `ds_id`           BIGINT(20)   NOT NULL
  COMMENT '依赖的数据源Id',
  `cached`          TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '是否需要缓存; 0 - 不需要; 1 - 需要',
  `cached_time`     INT(11)      NULL
  COMMENT '缓存时间(秒)',
  `timeout`         INT(11)      NULL
  COMMENT '超时时间(秒)',
  `paging`          TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '是否分页； 0 - 不分页； 1 - 分页',
  `text`            TEXT         NOT NULL
  COMMENT 'sql内容',
  `need_detail_log` TINYINT(1)   NULL     DEFAULT 0
  COMMENT '是否需要详细运行日志, 0 - 不需要(只记录运行耗时、运行错误信息), 1 - 记录运行sql及所有默认信息',
  `comment`         VARCHAR(512) NULL
  COMMENT '描述信息',
  `archive`         TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`         BIGINT(20)   NOT NULL
  COMMENT '创建时间',
  `creator`         BIGINT(20)   NOT NULL
  COMMENT '创建人',
  `modified`        BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`        BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '报表执行sql表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- r_sql_log sql执行日志表
CREATE TABLE `r_sql_log`
(
  `id`           BIGINT(20)  NOT NULL
  COMMENT '主键Id',
  `ds_id`        BIGINT(20)  NULL
  COMMENT '数据源Id',
  `report_code`  VARCHAR(32) NOT NULL
  COMMENT '所在报表编码',
  `elapsed_time` INT(11)     NOT NULL
  COMMENT '运行耗时',
  `complete`     TINYINT(1)  NOT NULL DEFAULT 0
  COMMENT 'sql运行状态; 0 - 运行出错; 1 - 成功',
  `raw_sql`      TEXT        NULL
  COMMENT 'sql内容',
  `err_info`     TEXT        NULL
  COMMENT '错误信息',
  `archive`      TINYINT(1)  NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`      BIGINT(20)  NOT NULL
  COMMENT '创建时间',
  `creator`      BIGINT(20)  NOT NULL
  COMMENT '创建人',
  `modified`     BIGINT(20)  NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`     BIGINT(20)  NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT 'sql执行日志表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;