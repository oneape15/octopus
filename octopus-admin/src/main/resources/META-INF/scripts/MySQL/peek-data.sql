-- 取数实例信息表 pd_peek
CREATE TABLE `pd_peek`
(
  `id`         BIGINT(20)  NOT NULL COMMENT '主键Id',
  `model_id`   BIGINT(20)  NOT NULL COMMENT '模型Id',
  `name`       VARCHAR(64) NOT NULL COMMENT '取数实例名称',
  `field_list` TEXT COMMENT '返回的数据字段名列表, 多个以","隔开',
  `peek_time`  INT(11)     NULL     DEFAULT 0 COMMENT '取数次数',
  `archive`    TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`    BIGINT(20)  NOT NULL COMMENT '创建时间',
  `creator`    BIGINT(20)  NOT NULL COMMENT '创建人',
  `modified`   BIGINT(20)  NULL     DEFAULT NULL COMMENT '最后一次更新时间',
  `modifier`   BIGINT(20)  NULL     DEFAULT NULL COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '取数实例信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- 取数字段信息表 pd_peek_field
CREATE TABLE `pd_peek_field`
(
  `id`             BIGINT(20)  NOT NULL COMMENT '主键Id',
  `peek_id`        BIGINT(20)  NOT NULL COMMENT '取数id',
  `meta_id`        BIGINT(20)  NULL COMMENT '字段id',
  `type`           TINYINT(1)  NULL     DEFAULT 0 COMMENT '类型; 0 -维度; 1-指标',
  `agg_expression` VARCHAR(50) NULL COMMENT '聚合函数',
  `data_type`      VARCHAR(50) NULL COMMENT '数据类型',
  `format`         VARCHAR(50) NULL COMMENT '格式',
  `archive`        TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`        BIGINT(20)  NOT NULL COMMENT '创建时间',
  `creator`        BIGINT(20)  NOT NULL COMMENT '创建人',
  `modified`       BIGINT(20)  NULL     DEFAULT NULL COMMENT '最后一次更新时间',
  `modifier`       BIGINT(20)  NULL     DEFAULT NULL COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '取数字段信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- 取数规则信息表 pd_peek_rule
CREATE TABLE `pd_peek_rule`
(
  `id`          BIGINT(20)   NOT NULL COMMENT '主键Id',
  `peek_id`     BIGINT(20)   NOT NULL COMMENT '取数id',
  `meta_id`     BIGINT(20)   NULL COMMENT '字段id',
  `field_name`  VARCHAR(128) COMMENT '字段名称',
  `rule`        VARCHAR(64)  NOT NULL COMMENT '规则名称',
  `input_value` VARCHAR(512) NOT NULL COMMENT '代入值',
  `archive`     TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`     BIGINT(20)   NOT NULL COMMENT '创建时间',
  `creator`     BIGINT(20)   NOT NULL COMMENT '创建人',
  `modified`    BIGINT(20)   NULL     DEFAULT NULL COMMENT '最后一次更新时间',
  `modifier`    BIGINT(20)   NULL     DEFAULT NULL COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '取数规则信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- 模型信息表 pd_model
CREATE TABLE `pd_model`
(
  `id`            BIGINT(20)   NOT NULL COMMENT '主键Id',
  `name`          VARCHAR(64)  NOT NULL COMMENT '模型名称',
  `datasource_id` BIGINT(20)   NOT NULL COMMENT '数据源Id',
  `table_name`    VARCHAR(512) NOT NULL COMMENT '具体表名',
  `status`        TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '模型状态 0 - 使用中; 1 - 已停用',
  `comment`       VARCHAR(512) NULL COMMENT '描述',
  `archive`       TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`       BIGINT(20)   NOT NULL COMMENT '创建时间',
  `creator`       BIGINT(20)   NOT NULL COMMENT '创建人',
  `modified`      BIGINT(20)   NULL     DEFAULT NULL COMMENT '最后一次更新时间',
  `modifier`      BIGINT(20)   NULL     DEFAULT NULL COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '模型信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- 模型元素信息表 pd_model_meta
CREATE TABLE `pd_model_meta`
(
  `id`               BIGINT(20)   NOT NULL COMMENT '主键Id',
  `model_id`         BIGINT(20)   NOT NULL COMMENT '模型Id',
  `name`             VARCHAR(64)  NOT NULL COMMENT '元素名称(表字段名称)',
  `show_name`        VARCHAR(64)  NOT NULL COMMENT '显示名称',
  `origin_data_type` VARCHAR(64)  NOT NULL COMMENT '原始数据类型',
  `data_type`        VARCHAR(64)  NOT NULL COMMENT '数据类型',
  `display`          TINYINT(1)   NULL     DEFAULT 1 COMMENT '是否显示, 1-显示; 0 - 不显示',
  `tag_id`           BIGINT(20)            DEFAULT NULL COMMENT '标签id',
  `comment`          VARCHAR(255) NULL COMMENT '字段详细描述',
  `archive`          TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`          BIGINT(20)   NOT NULL COMMENT '创建时间',
  `creator`          BIGINT(20)   NOT NULL COMMENT '创建人',
  `modified`         BIGINT(20)   NULL     DEFAULT NULL COMMENT '最后一次更新时间',
  `modifier`         BIGINT(20)   NULL     DEFAULT NULL COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '模型元素信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- 模型标签表 pd_model_tag
CREATE TABLE `pd_model_tag`
(
  `id`        BIGINT(20)  NOT NULL COMMENT '主键Id',
  `name`      VARCHAR(64) NOT NULL COMMENT '标签名',
  `rule`      VARCHAR(64) NOT NULL COMMENT '匹配规则',
  `defaulted` TINYINT(1)           DEFAULT '0' COMMENT '是否默认标签,0为非默认,1为默认',
  `archive`   TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`   BIGINT(20)  NOT NULL COMMENT '创建时间',
  `creator`   BIGINT(20)  NOT NULL COMMENT '创建人',
  `modified`  BIGINT(20)  NULL     DEFAULT NULL COMMENT '最后一次更新时间',
  `modifier`  BIGINT(20)  NULL     DEFAULT NULL COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '模型标签表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- 数据导入记录表 pd_import_record
CREATE TABLE `pd_import_record`
(
  `id`             BIGINT(20)   NOT NULL COMMENT '主键Id',
  `datasource_id`  BIGINT(20)   NOT NULL COMMENT '数据源Id',
  `table_name`     VARCHAR(128) NOT NULL COMMENT '具体表名',
  `file_name`      VARCHAR(512) NOT NULL COMMENT '导入文件名称',
  `cover`          TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否覆盖 0 - 不覆盖,追加; 1 - 覆盖',
  `partition_name` VARCHAR(200)          DEFAULT NULL COMMENT '分区名称',
  `status`         TINYINT(1)            DEFAULT NULL COMMENT '导入状态,0:导入中,1导入成功,2导入失败',
  `archive`        TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`        BIGINT(20)   NOT NULL COMMENT '创建时间',
  `creator`        BIGINT(20)   NOT NULL COMMENT '创建人',
  `modified`       BIGINT(20)   NULL     DEFAULT NULL COMMENT '最后一次更新时间',
  `modifier`       BIGINT(20)   NULL     DEFAULT NULL COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '数据导入记录表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;