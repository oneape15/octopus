
-- 数据字典字段信息表 dd_field_info
CREATE TABLE `dd_field_info`
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
  COMMENT '数据字典字段信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;