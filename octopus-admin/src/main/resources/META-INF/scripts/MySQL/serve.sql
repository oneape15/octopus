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
  `serve_type`  INT(11)      NOT NULL
  COMMENT 'serve type , 1 - table; 2 - interface; eg.',
  `visual_type` INT(11)      NOT NULL
  COMMENT 'the serve visual type , 1 - table; 2 - line; 4 - bar; eg.',
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

