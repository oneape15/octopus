-- r_datasource 数据源信息表
CREATE TABLE r_datasource
(
  id          BIGINT        NOT NULL,
  name        VARCHAR(64)   NOT NULL,
  type        VARCHAR(64)   NOT NULL,
  status      SMALLINT      NOT NULL DEFAULT 0,
  jdbc_driver VARCHAR(256)  NOT NULL,
  jdbc_url    VARCHAR(512)  NOT NULL,
  username    VARCHAR(64)   NOT NULL,
  password    VARCHAR(64)   NULL     DEFAULT NULL,
  timeout     INT           NOT NULL,
  test_sql    VARCHAR(1024) NULL,
  comment     VARCHAR(256)  NULL,
  archive     SMALLINT      NOT NULL DEFAULT 0,
  created     BIGINT        NOT NULL,
  creator     BIGINT        NOT NULL,
  modified    BIGINT        NULL     DEFAULT NULL,
  modifier    BIGINT        NULL     DEFAULT NULL
);
COMMENT ON TABLE r_datasource IS '数据源信息表';
COMMENT ON COLUMN r_datasource.id IS '主键';
COMMENT ON COLUMN r_datasource.name IS '数据源名称';
COMMENT ON COLUMN r_datasource.type IS '数据源类型, MySQL, PgSQL';
COMMENT ON COLUMN r_datasource.status IS '状态, 0 - 可用; 1 - 不可用';
COMMENT ON COLUMN r_datasource.jdbc_driver IS 'jdbc驱动';
COMMENT ON COLUMN r_datasource.jdbc_url IS 'jdbc URL';
COMMENT ON COLUMN r_datasource.username IS '登录用户名';
COMMENT ON COLUMN r_datasource.password IS '登录密码';
COMMENT ON COLUMN r_datasource.timeout IS '连接池超时时间(ms)';
COMMENT ON COLUMN r_datasource.test_sql IS '检测SQL';
COMMENT ON COLUMN r_datasource.comment IS '描述';
COMMENT ON COLUMN r_datasource.archive IS '0 - 正常数据; 1 - 已归档(删除)';
COMMENT ON COLUMN r_datasource.created IS '创建时间';
COMMENT ON COLUMN r_datasource.creator IS '创建人';
COMMENT ON COLUMN r_datasource.modified IS '最后一次更新时间';
COMMENT ON COLUMN r_datasource.modifier IS '最后一次修改人';

-- r_report_group 报表分组表
CREATE TABLE r_report_group
(
  id        BIGINT       NOT NULL,
  parent_id BIGINT       NOT NULL DEFAULT 0,
  name      VARCHAR(128) NOT NULL,
  icon      VARCHAR(256) NULL,
  status    SMALLINT     NOT NULL DEFAULT 0,
  level     SMALLINT     NOT NULL DEFAULT 1,
  owner     BIGINT       NOT NULL,
  sort_id   BIGINT       NOT NULL,
  comment   VARCHAR(256) NULL,
  archive   SMALLINT     NOT NULL DEFAULT 0,
  created   BIGINT       NOT NULL,
  creator   BIGINT       NOT NULL,
  modified  BIGINT       NULL     DEFAULT NULL,
  modifier  BIGINT       NULL     DEFAULT NULL
);
COMMENT ON TABLE r_report_group IS '报表分组表';
COMMENT ON COLUMN r_report_group.id IS '主键';
COMMENT ON COLUMN r_report_group.parent_id IS '父级节点Id, 默认为0';
COMMENT ON COLUMN r_report_group.name IS '报表组名';
COMMENT ON COLUMN r_report_group.icon IS '报表组图标';
COMMENT ON COLUMN r_report_group.status IS '状态 0 - 编辑中; 1 - 上线;';
COMMENT ON COLUMN r_report_group.level IS '所在层级';
COMMENT ON COLUMN r_report_group.owner IS '报表组拥有者';
COMMENT ON COLUMN r_report_group.sort_id IS '排序Id';
COMMENT ON COLUMN r_report_group.comment IS '描述';
COMMENT ON COLUMN r_report_group.archive IS '0 - 正常数据; 1 - 已归档(删除)';
COMMENT ON COLUMN r_report_group.created IS '创建时间';
COMMENT ON COLUMN r_report_group.creator IS '创建人';
COMMENT ON COLUMN r_report_group.modified IS '最后一次更新时间';
COMMENT ON COLUMN r_report_group.modifier IS '最后一次修改人';

-- r_report 报表信息表
CREATE TABLE r_report
(
  id              BIGINT       NOT NULL,
  code            VARCHAR(32)  NOT NULL,
  name            VARCHAR(32)  NOT NULL,
  icon            VARCHAR(256) NULL,
  report_type     VARCHAR(128) NOT NULL,
  x_axis          VARCHAR(64)  NULL,
  y_axis          VARCHAR(64)  NULL,
  report_sql_id   BIGINT       NULL,
  param_label_len SMALLINT     NULL,
  param_media_len SMALLINT     NULL,
  lov             SMALLINT     NOT NULL DEFAULT 0,
  flow_switch     SMALLINT     NOT NULL DEFAULT 0,
  owner           BIGINT       NOT NULL DEFAULT -1,
  sort_id         BIGINT       NOT NULL,
  comment         VARCHAR(512) NULL,
  archive         SMALLINT     NOT NULL DEFAULT 0,
  created         BIGINT       NOT NULL,
  creator         BIGINT       NOT NULL,
  modified        BIGINT       NULL     DEFAULT NULL,
  modifier        BIGINT       NULL     DEFAULT NULL
);
COMMENT ON TABLE r_report IS '报表信息表';
COMMENT ON COLUMN r_report.id IS '主键';
COMMENT ON COLUMN r_report.code IS '报表编码全局唯一';
COMMENT ON COLUMN r_report.name IS '报表名称';
COMMENT ON COLUMN r_report.icon IS '报表图标';
COMMENT ON COLUMN r_report.report_type IS '报表类型, 1 - 表格; 2 - 图表; 3 - xxx等; 多个以逗号分隔';
COMMENT ON COLUMN r_report.x_axis IS '图表显示时, x轴列名; 多个以";"隔开';
COMMENT ON COLUMN r_report.y_axis IS '图表显示时,y轴列名; 多个以";"隔开';
COMMENT ON COLUMN r_report.report_sql_id IS '报表详情查询Id';
COMMENT ON COLUMN r_report.param_label_len IS '查询字段标签显示长度';
COMMENT ON COLUMN r_report.param_media_len IS '查询字段控件显示长度';
COMMENT ON COLUMN r_report.lov IS '是否为lov； 0 - 普通报表; 1 - LOV报表';
COMMENT ON COLUMN r_report.flow_switch IS '流量开关';
COMMENT ON COLUMN r_report.owner IS '拥有者';
COMMENT ON COLUMN r_report.sort_id IS '排序Id';
COMMENT ON COLUMN r_report.comment IS '描述信息';
COMMENT ON COLUMN r_report.archive IS '0 - 正常数据; 1 - 已归档(删除)';
COMMENT ON COLUMN r_report.created IS '创建时间';
COMMENT ON COLUMN r_report.creator IS '创建人';
COMMENT ON COLUMN r_report.modified IS '最后一次更新时间';
COMMENT ON COLUMN r_report.modifier IS '最后一次修改人';

-- r_group_rl_report 报表与报表组关联关系表
CREATE TABLE r_group_rl_report
(
  id        BIGINT   NOT NULL,
  report_id BIGINT   NOT NULL,
  group_id  BIGINT   NOT NULL,
  archive   SMALLINT NOT NULL DEFAULT 0,
  created   BIGINT   NOT NULL,
  creator   BIGINT   NOT NULL,
  modified  BIGINT   NULL     DEFAULT NULL,
  modifier  BIGINT   NULL     DEFAULT NULL
);
COMMENT ON TABLE r_group_rl_report IS '报表与报表组关联关系表';
COMMENT ON COLUMN r_group_rl_report.id IS '主键';
COMMENT ON COLUMN r_group_rl_report.report_id IS '报表Id';
COMMENT ON COLUMN r_group_rl_report.group_id IS '报表组Id';
COMMENT ON COLUMN r_group_rl_report.archive IS '0 - 正常数据; 1 - 已归档(删除)';
COMMENT ON COLUMN r_group_rl_report.created IS '创建时间';
COMMENT ON COLUMN r_group_rl_report.creator IS '创建人';
COMMENT ON COLUMN r_group_rl_report.modified IS '最后一次更新时间';
COMMENT ON COLUMN r_group_rl_report.modifier IS '最后一次修改人';

-- r_report_column 报表字段信息表
CREATE TABLE r_report_column
(
  id              BIGINT       NOT NULL,
  report_id       BIGINT       NOT NULL,
  raw             SMALLINT     NOT NULL DEFAULT 0,
  name            VARCHAR(32)  NOT NULL,
  show_name       VARCHAR(32)  NOT NULL,
  data_type       VARCHAR(32)  NOT NULL,
  unit            VARCHAR(32)  NOT NULL,
  hidden          SMALLINT     NULL     DEFAULT 0,
  drill_report_id BIGINT       NULL,
  drill_params    VARCHAR(256) NULL,
  frozen          SMALLINT     NULL     DEFAULT 0,
  support_sort    SMALLINT     NOT NULL DEFAULT 0,
  split           SMALLINT     NULL     DEFAULT 0,
  split_char      VARCHAR(16)  NULL,
  split_kv_char   VARCHAR(16)  NULL,
  format_macro    VARCHAR(128) NULL,
  sort_id         BIGINT       NOT NULL,
  comment         VARCHAR(512) NULL,
  archive         SMALLINT     NOT NULL DEFAULT 0,
  created         BIGINT       NOT NULL,
  creator         BIGINT       NOT NULL,
  modified        BIGINT       NULL     DEFAULT NULL,
  modifier        BIGINT       NULL     DEFAULT NULL
);
COMMENT ON TABLE r_report_column IS '报表字段信息表';
COMMENT ON COLUMN r_report_column.id IS '主键';
COMMENT ON COLUMN r_report_column.report_id IS '报表Id';
COMMENT ON COLUMN r_report_column.raw IS '是否为原生的列; 0 - 原生的; 1 - 加工过(分裂新生成的)';
COMMENT ON COLUMN r_report_column.name IS '列名';
COMMENT ON COLUMN r_report_column.show_name IS '显示名';
COMMENT ON COLUMN r_report_column.data_type IS '数据类型';
COMMENT ON COLUMN r_report_column.unit IS '数据单位';
COMMENT ON COLUMN r_report_column.hidden IS '是否为隐藏列; 0 - 正常显示; 1 - 隐藏列';
COMMENT ON COLUMN r_report_column.drill_report_id IS '下钻查询详细报表Id';
COMMENT ON COLUMN r_report_column.drill_params IS '下钻列时,需要的参数; kv1= column_name1; kv2 = column_name2;';
COMMENT ON COLUMN r_report_column.frozen IS '是否为冻结列; 0 - 否; 1 - 冻结列';
COMMENT ON COLUMN r_report_column.support_sort IS '是否支持排序; 0 - 否; 1 - 是';
COMMENT ON COLUMN r_report_column.split IS '是否需要分裂; 0 - 否; 1 - 需要';
COMMENT ON COLUMN r_report_column.split_char IS '分裂分隔字符串';
COMMENT ON COLUMN r_report_column.split_kv_char IS '分裂后kv的分隔字符串';
COMMENT ON COLUMN r_report_column.format_macro IS '数据格式化宏';
COMMENT ON COLUMN r_report_column.archive IS '0 - 正常数据; 1 - 已归档(删除)';
COMMENT ON COLUMN r_report_column.created IS '创建时间';
COMMENT ON COLUMN r_report_column.creator IS '创建人';
COMMENT ON COLUMN r_report_column.modified IS '最后一次更新时间';
COMMENT ON COLUMN r_report_column.modifier IS '最后一次修改人';

-- r_report_param 报表查询字段信息表
CREATE TABLE r_report_param
(
  id            BIGINT       NOT NULL,
  report_id     BIGINT       NOT NULL,
  show_name     VARCHAR(32)  NOT NULL,
  param_name    VARCHAR(32)  NOT NULL,
  data_type     VARCHAR(32)  NOT NULL,
  val_default   VARCHAR(64)  NULL,
  val_max       VARCHAR(64)  NULL,
  val_min       VARCHAR(64)  NULL,
  val_forbidden VARCHAR(256) NULL,
  must_fill_in  SMALLINT     NULL     DEFAULT 0,
  order_by_type VARCHAR(16)  NULL,
  placeholder   VARCHAR(64)  NULL,
  err_message   VARCHAR(128) NULL,
  depend_on     VARCHAR(512) NULL,
  type          INT          NOT NULL DEFAULT 0,
  lov_report_id BIGINT       NOT NULL DEFAULT 0,
  sort_id       BIGINT       NOT NULL,
  comment       VARCHAR(512) NULL,
  archive       SMALLINT     NOT NULL DEFAULT 0,
  created       BIGINT       NOT NULL,
  creator       BIGINT       NOT NULL,
  modified      BIGINT       NULL     DEFAULT NULL,
  modifier      BIGINT       NULL     DEFAULT NULL
);
COMMENT ON TABLE r_report_param IS '报表查询字段信息表';
COMMENT ON COLUMN r_report_param.id IS '主键';
COMMENT ON COLUMN r_report_param.report_id IS '报表Id';
COMMENT ON COLUMN r_report_param.show_name IS '字段显示名';
COMMENT ON COLUMN r_report_param.param_name IS '查询sql代入名';
COMMENT ON COLUMN r_report_param.data_type IS '字段数据类型';
COMMENT ON COLUMN r_report_param.val_default IS '默认值';
COMMENT ON COLUMN r_report_param.val_max IS '最大值';
COMMENT ON COLUMN r_report_param.val_min IS '最小值';
COMMENT ON COLUMN r_report_param.val_forbidden IS '禁止使用值';
COMMENT ON COLUMN r_report_param.must_fill_in IS '是否为必填字段 0 - 否; 1 - 必填';
COMMENT ON COLUMN r_report_param.order_by_type IS '排序方式 ASC, DESC';
COMMENT ON COLUMN r_report_param.placeholder IS '提示信息';
COMMENT ON COLUMN r_report_param.err_message IS '错误提示信息';
COMMENT ON COLUMN r_report_param.depend_on IS '依赖关系, 取query_name值,多个以逗号分隔';
COMMENT ON COLUMN r_report_param.type IS '字段类型; 0 - 普通字段; 1 - 内部字段; 2 - 多选字段; 4 - lov选择; ';
COMMENT ON COLUMN r_report_param.lov_report_id IS '当字段值内容依赖别一个sql查询结果时(LOV), 填入';
COMMENT ON COLUMN r_report_param.archive IS '0 - 正常数据; 1 - 已归档(删除)';
COMMENT ON COLUMN r_report_param.created IS '创建时间';
COMMENT ON COLUMN r_report_param.creator IS '创建人';
COMMENT ON COLUMN r_report_param.modified IS '最后一次更新时间';
COMMENT ON COLUMN r_report_param.modifier IS '最后一次修改人';

-- r_report_sql 报表执行sql表
CREATE TABLE r_report_sql
(
  id              BIGINT       NOT NULL,
  ds_id           BIGINT       NOT NULL,
  cached          SMALLINT     NOT NULL DEFAULT 0,
  cached_time     INT          NULL,
  timeout         INT          NULL,
  paging          SMALLINT     NOT NULL DEFAULT 0,
  text            TEXT         NOT NULL,
  need_detail_log SMALLINT     NULL     DEFAULT 0,
  comment         VARCHAR(512) NULL,
  archive         SMALLINT     NOT NULL DEFAULT 0,
  created         BIGINT       NOT NULL,
  creator         BIGINT       NOT NULL,
  modified        BIGINT       NULL     DEFAULT NULL,
  modifier        BIGINT       NULL     DEFAULT NULL
);
COMMENT ON TABLE r_report_sql IS '报表执行sql表';
COMMENT ON COLUMN r_report_sql.id IS '主键';
COMMENT ON COLUMN r_report_sql.ds_id IS '依赖的数据源Id';
COMMENT ON COLUMN r_report_sql.cached IS '是否需要缓存; 0 - 不需要; 1 - 需要';
COMMENT ON COLUMN r_report_sql.cached_time IS '缓存时间(秒)';
COMMENT ON COLUMN r_report_sql.timeout IS '超时时间(秒)';
COMMENT ON COLUMN r_report_sql.paging IS '是否分页； 0 - 不分页； 1 - 分页';
COMMENT ON COLUMN r_report_sql.text IS 'sql内容';
COMMENT ON COLUMN r_report_sql.need_detail_log IS '是否需要详细运行日志, 0 - 不需要(只记录运行耗时、运行错误信息), 1 - 记录运行sql及所有默认信息';
COMMENT ON COLUMN r_report_sql.archive IS '0 - 正常数据; 1 - 已归档(删除)';
COMMENT ON COLUMN r_report_sql.created IS '创建时间';
COMMENT ON COLUMN r_report_sql.creator IS '创建人';
COMMENT ON COLUMN r_report_sql.modified IS '最后一次更新时间';
COMMENT ON COLUMN r_report_sql.modifier IS '最后一次修改人';

-- r_sql_log sql执行日志表
CREATE TABLE r_sql_log
(
  id            BIGINT   NOT NULL,
  ds_id         BIGINT   NOT NULL,
  report_id     BIGINT   NOT NULL,
  report_sql_id BIGINT   NOT NULL,
  elapsed_time  INT      NOT NULL,
  complete      SMALLINT NOT NULL DEFAULT 0,
  raw_sql       TEXT     NULL,
  err_info      TEXT     NULL,
  archive       SMALLINT NOT NULL DEFAULT 0,
  created       BIGINT   NOT NULL,
  creator       BIGINT   NOT NULL,
  modified      BIGINT   NULL     DEFAULT NULL,
  modifier      BIGINT   NULL     DEFAULT NULL
);
COMMENT ON TABLE r_sql_log IS 'sql执行日志表';
COMMENT ON COLUMN r_sql_log.id IS '主键';
COMMENT ON COLUMN r_sql_log.ds_id IS '依赖的数据源Id';
COMMENT ON COLUMN r_sql_log.report_id IS '所在报表Id';
COMMENT ON COLUMN r_sql_log.report_sql_id IS '原sqlId';
COMMENT ON COLUMN r_sql_log.elapsed_time IS '运行耗时';
COMMENT ON COLUMN r_sql_log.complete IS 'sql运行状态; 0 - 运行出错; 1 - 成功';
COMMENT ON COLUMN r_sql_log.raw_sql IS 'sql内容';
COMMENT ON COLUMN r_sql_log.err_info IS '错误信息';
COMMENT ON COLUMN r_sql_log.archive IS '0 - 正常数据; 1 - 已归档(删除)';
COMMENT ON COLUMN r_sql_log.created IS '创建时间';
COMMENT ON COLUMN r_sql_log.creator IS '创建人';
COMMENT ON COLUMN r_sql_log.modified IS '最后一次更新时间';
COMMENT ON COLUMN r_sql_log.modifier IS '最后一次修改人';


