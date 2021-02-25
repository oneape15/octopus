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