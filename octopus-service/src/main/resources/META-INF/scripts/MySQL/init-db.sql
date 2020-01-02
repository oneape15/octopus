-- 全局唯一生成器
CREATE TABLE `uid_worker_node`
(
    id          BIGINT(20)  NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    host_name   VARCHAR(64) NOT NULL COMMENT '主机名',
    port        VARCHAR(64) NOT NULL COMMENT '端口',
    type        INT         NOT NULL COMMENT '节点类型 ACTUAL 或 CONTAINER',
    launch_time BIGINT(20)  NOT NULL COMMENT '触发时间',
    created     BIGINT(20)  NOT NULL COMMENT '创建时间',
    modified    BIGINT(20)  NOT NULL COMMENT '修改时间',
    PRIMARY KEY (id)
)
    COMMENT 'UID生成器'
    ENGINE InnoDB;

-- sys_user 系统用户信息表
CREATE TABLE `sys_user`
(
    `id`         BIGINT(20)   NOT NULL,
    `nickname`   VARCHAR(128) NULL COMMENT '用户昵称',
    `username`   VARCHAR(256) NOT NULL COMMENT '登录用户名, 这里为登录邮箱地址',
    `password`   VARCHAR(128) NOT NULL COMMENT '登录密码',
    `avatar`     VARCHAR(512) NULL COMMENT '用户头像',
    `department` VARCHAR(128) NULL COMMENT '所在部门',
    `position`   VARCHAR(64)  NULL COMMENT '职位',
    `signature`  VARCHAR(512) NULL COMMENT '个性签名',
    `phone`      VARCHAR(16)  NULL COMMENT '手机号',
    `email`      VARCHAR(128) NULL COMMENT '邮箱号',
    `address`    VARCHAR(512) NULL COMMENT '地址',
    `status`     TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '账号状态 0 - 注册未激活; 1 - 正常; 2 - 锁定(不可用)',
    `archive`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '0 - 正常数据; 1 - 已归档(删除)',
    `created`    BIGINT(20)   NOT NULL COMMENT '创建时间',
    `creator`    BIGINT(20)   NOT NULL COMMENT '创建人',
    `modified`   BIGINT(20)   NULL     DEFAULT NULL COMMENT '最后一次更新时间',
    `modifier`   BIGINT(20)   NULL     DEFAULT NULL COMMENT '最后一次修改人',
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
    `user_id`    BIGINT(128)  NOT NULL COMMENT '用户Id',
    `token`      VARCHAR(128) NOT NULL COMMENT '用户登录token',
    `timeout`    INT(11)      NOT NULL COMMENT '有效时间',
    `login_time` BIGINT(20)   NULL COMMENT '登录时间',
    `archive`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '0 - 正常数据; 1 - 已归档(删除)',
    `created`    BIGINT(20)   NOT NULL COMMENT '创建时间',
    `creator`    BIGINT(20)   NOT NULL COMMENT '创建人',
    `modified`   BIGINT(20)   NULL     DEFAULT NULL COMMENT '最后一次更新时间',
    `modifier`   BIGINT(20)   NULL     DEFAULT NULL COMMENT '最后一次修改人',
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    COMMENT '用户会话信息表'
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_general_ci;