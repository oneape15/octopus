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