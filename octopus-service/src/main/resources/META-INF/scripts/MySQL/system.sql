-- 全局唯一生成器
CREATE TABLE `uid_worker_node`
(
  id          BIGINT(20)  NOT NULL AUTO_INCREMENT
  COMMENT '自增主键',
  host_name   VARCHAR(64) NOT NULL
  COMMENT '主机名',
  port        VARCHAR(64) NOT NULL
  COMMENT '端口',
  type        INT         NOT NULL
  COMMENT '节点类型 ACTUAL 或 CONTAINER',
  launch_time BIGINT(20)  NOT NULL
  COMMENT '触发时间',
  created     BIGINT(20)  NOT NULL
  COMMENT '创建时间',
  modified    BIGINT(20)  NOT NULL
  COMMENT '修改时间',
  PRIMARY KEY (id)
)
  COMMENT 'UID生成器'
  ENGINE InnoDB;

-- 基础信息表 sys_common_info
CREATE TABLE `sys_common_info`
(
  `id`        BIGINT(20)  NOT NULL
  COMMENT '主键Id',
  `parent_id` BIGINT(20)  NOT NULL DEFAULT 0
  COMMENT '父类id',
  `classify`  VARCHAR(32) NOT NULL
  COMMENT '基础信息分类表',
  `code`      VARCHAR(64) NOT NULL
  COMMENT '编码信息',
  `name`      VARCHAR(64) NOT NULL
  COMMENT '数据源名称',
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
  COMMENT '基础信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- sys_user 系统用户信息表
CREATE TABLE `sys_user`
(
  `id`         BIGINT(20)   NOT NULL,
  `nickname`   VARCHAR(128) NULL
  COMMENT '用户昵称',
  `username`   VARCHAR(256) NOT NULL
  COMMENT '登录用户名, 这里为登录邮箱地址',
  `password`   VARCHAR(128) NOT NULL
  COMMENT '登录密码',
  `avatar`     VARCHAR(512) NULL
  COMMENT '用户头像',
  `department` VARCHAR(128) NULL
  COMMENT '所在部门',
  `position`   VARCHAR(64)  NULL
  COMMENT '职位',
  `signature`  VARCHAR(512) NULL
  COMMENT '个性签名',
  `phone`      VARCHAR(16)  NULL
  COMMENT '手机号',
  `email`      VARCHAR(128) NULL
  COMMENT '邮箱号',
  `address`    VARCHAR(512) NULL
  COMMENT '地址',
  `status`     TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '账号状态 0 - 注册未激活; 1 - 正常; 2 - 锁定(不可用)',
  `archive`    TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`    BIGINT(20)   NOT NULL
  COMMENT '创建时间',
  `creator`    BIGINT(20)   NOT NULL
  COMMENT '创建人',
  `modified`   BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`   BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '系统用户信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- sys_user_session 用户会话信息表
CREATE TABLE `sys_user_session`
(
  `id`         BIGINT(20)   NOT NULL,
  `user_id`    BIGINT(128)  NOT NULL
  COMMENT '用户Id',
  `token`      VARCHAR(128) NOT NULL
  COMMENT '用户登录token',
  `timeout`    INT(11)      NOT NULL
  COMMENT '有效时间',
  `login_time` BIGINT(20)   NULL
  COMMENT '登录时间',
  `archive`    TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`    BIGINT(20)   NOT NULL
  COMMENT '创建时间',
  `creator`    BIGINT(20)   NOT NULL
  COMMENT '创建人',
  `modified`   BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`   BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '用户会话信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- sys_role 角色信息表
CREATE TABLE `sys_role`
(
  `id`         BIGINT(20)   NOT NULL,
  `name`       VARCHAR(128) NOT NULL
  COMMENT '角色名称',
  `code`       VARCHAR(128) NOT NULL
  COMMENT '角色编码',
  `type`       TINYINT(1)   NOT NULL
  COMMENT '类型, 角色类型: 0 - 普通; 1 - 默认角色',
  `department` VARCHAR(128) NULL
  COMMENT '部门',
  `comment`    VARCHAR(256) NULL
  COMMENT '描述',
  `archive`    TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`    BIGINT(20)   NOT NULL
  COMMENT '创建时间',
  `creator`    BIGINT(20)   NOT NULL
  COMMENT '创建人',
  `modified`   BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`   BIGINT(20)   NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '角色信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- sys_user_rl_role 用户与角色关联关系表
CREATE TABLE `sys_user_rl_role`
(
  `id`       BIGINT(20) NOT NULL,
  `user_id`  BIGINT(20) NOT NULL
  COMMENT '用户Id',
  `role_id`  BIGINT(20) NOT NULL
  COMMENT '角色Id',
  `archive`  TINYINT(1) NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`  BIGINT(20) NOT NULL
  COMMENT '创建时间',
  `creator`  BIGINT(20) NOT NULL
  COMMENT '创建人',
  `modified` BIGINT(20) NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier` BIGINT(20) NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '用户与角色关联关系表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- sys_resource 资源信息表
CREATE TABLE `sys_resource`
(
  `id`        BIGINT(20)   NOT NULL,
  `parent_id` BIGINT(20)   NOT NULL
  COMMENT '父节点',
  `level`     INT(11)      NOT NULL
  COMMENT '层级，开始为1',
  `name`      VARCHAR(128) NOT NULL
  COMMENT '资源名称',
  `icon`      VARCHAR(512) NULL
  COMMENT '资源图标',
  `type`      TINYINT(1)   NOT NULL
  COMMENT '类型, 0 - 菜单; 1 - 资源项',
  `path`      VARCHAR(512) NULL
  COMMENT '资源路径',
  `auth_code` VARCHAR(128) NULL
  COMMENT '权限编码',
  `sort_id`   BIGINT(20)   NOT NULL DEFAULT 0
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
  COMMENT '资源信息表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

-- sys_role_rl_resource 角色与资源关联关系表
CREATE TABLE `sys_role_rl_resource`
(
  `id`          BIGINT(20) NOT NULL,
  `role_id`     BIGINT(20) NOT NULL
  COMMENT '角色Id',
  `resource_id` BIGINT(20) NOT NULL
  COMMENT '资源Id',
  `mask`        INT(11)    NOT NULL DEFAULT 0
  COMMENT '权限掩码 1 - 查看; 2 - 新增; 4 - 修改; 8 - 删除',
  `archive`     TINYINT(1) NOT NULL DEFAULT 0
  COMMENT '0 - 正常数据; 1 - 已归档(删除)',
  `created`     BIGINT(20) NOT NULL
  COMMENT '创建时间',
  `creator`     BIGINT(20) NOT NULL
  COMMENT '创建人',
  `modified`    BIGINT(20) NULL     DEFAULT NULL
  COMMENT '最后一次更新时间',
  `modifier`    BIGINT(20) NULL     DEFAULT NULL
  COMMENT '最后一次修改人',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  COMMENT '角色与资源关联关系表'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

