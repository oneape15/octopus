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