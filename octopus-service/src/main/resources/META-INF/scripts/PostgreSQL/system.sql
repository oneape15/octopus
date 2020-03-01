-- UID生成器
CREATE TABLE uid_worker_node
(
  id          SERIAL NOT NULL PRIMARY KEY,
  host_name   VARCHAR(64),
  port        VARCHAR(64),
  type        INTEGER,
  launch_time BIGINT,
  created     BIGINT,
  modified    BIGINT
);
COMMENT ON TABLE uid_worker_node IS 'UID生成器';
COMMENT ON COLUMN uid_worker_node.id IS '自增主键';
COMMENT ON COLUMN uid_worker_node.host_name IS '主机名';
COMMENT ON COLUMN uid_worker_node.port IS '端口';
COMMENT ON COLUMN uid_worker_node.type IS '节点类型 ACTUAL 或 CONTAINER';
COMMENT ON COLUMN uid_worker_node.launch_time IS '触发时间';
COMMENT ON COLUMN uid_worker_node.created IS '创建时间';
COMMENT ON COLUMN uid_worker_node.modified IS '修改时间';
