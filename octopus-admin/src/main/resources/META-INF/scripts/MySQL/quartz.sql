CREATE TABLE qrtz_job_details (
  sched_name        VARCHAR(120) NOT NULL,
  job_name          VARCHAR(190) NOT NULL,
  job_group         VARCHAR(190) NOT NULL,
  description       VARCHAR(250) NULL,
  job_class_name    VARCHAR(250) NOT NULL,
  is_durable        VARCHAR(1)   NOT NULL,
  is_nonconcurrent  VARCHAR(1)   NOT NULL,
  is_update_data    VARCHAR(1)   NOT NULL,
  requests_recovery VARCHAR(1)   NOT NULL,
  job_data          BLOB         NULL,
  PRIMARY KEY (sched_name, job_name, job_group)
)
  ENGINE = InnoDB
  COMMENT '存放一个jobDetail信息'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

CREATE TABLE qrtz_triggers (
  sched_name     VARCHAR(120) NOT NULL,
  trigger_name   VARCHAR(190) NOT NULL,
  trigger_group  VARCHAR(190) NOT NULL,
  job_name       VARCHAR(190) NOT NULL,
  job_group      VARCHAR(190) NOT NULL,
  description    VARCHAR(250) NULL,
  next_fire_time BIGINT(13)   NULL,
  prev_fire_time BIGINT(13)   NULL,
  priority       INTEGER      NULL,
  trigger_state  VARCHAR(16)  NOT NULL,
  trigger_type   VARCHAR(8)   NOT NULL,
  start_time     BIGINT(13)   NOT NULL,
  end_time       BIGINT(13)   NULL,
  calendar_name  VARCHAR(190) NULL,
  misfire_instr  SMALLINT(2)  NULL,
  job_data       BLOB         NULL,
  PRIMARY KEY (sched_name, trigger_name, trigger_group),
  FOREIGN KEY (sched_name, job_name, job_group)
  REFERENCES qrtz_job_details (sched_name, job_name, job_group)
)
  ENGINE = InnoDB
  COMMENT '触发器的基本信息'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

CREATE TABLE qrtz_simple_triggers (
  sched_name      VARCHAR(120) NOT NULL,
  trigger_name    VARCHAR(190) NOT NULL,
  trigger_group   VARCHAR(190) NOT NULL,
  repeat_count    BIGINT(7)    NOT NULL,
  repeat_interval BIGINT(12)   NOT NULL,
  times_triggered BIGINT(10)   NOT NULL,
  PRIMARY KEY (sched_name, trigger_name, trigger_group),
  FOREIGN KEY (sched_name, trigger_name, trigger_group)
  REFERENCES qrtz_triggers (sched_name, trigger_name, trigger_group)
)
  ENGINE = InnoDB
  COMMENT '简单触发器的信息'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

CREATE TABLE qrtz_cron_triggers (
  sched_name      VARCHAR(120) NOT NULL,
  trigger_name    VARCHAR(190) NOT NULL,
  trigger_group   VARCHAR(190) NOT NULL,
  cron_expression VARCHAR(120) NOT NULL,
  time_zone_id    VARCHAR(80),
  PRIMARY KEY (sched_name, trigger_name, trigger_group),
  FOREIGN KEY (sched_name, trigger_name, trigger_group)
  REFERENCES qrtz_triggers (sched_name, trigger_name, trigger_group)
)
  ENGINE = InnoDB
  COMMENT '存放cron类型的触发器'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

CREATE TABLE qrtz_simprop_triggers
(
  sched_name    VARCHAR(120)   NOT NULL,
  trigger_name  VARCHAR(190)   NOT NULL,
  trigger_group VARCHAR(190)   NOT NULL,
  str_prop_1    VARCHAR(512)   NULL,
  str_prop_2    VARCHAR(512)   NULL,
  str_prop_3    VARCHAR(512)   NULL,
  int_prop_1    INT            NULL,
  int_prop_2    INT            NULL,
  long_prop_1   BIGINT         NULL,
  long_prop_2   BIGINT         NULL,
  dec_prop_1    NUMERIC(13, 4) NULL,
  dec_prop_2    NUMERIC(13, 4) NULL,
  bool_prop_1   VARCHAR(1)     NULL,
  bool_prop_2   VARCHAR(1)     NULL,
  PRIMARY KEY (sched_name, trigger_name, trigger_group),
  FOREIGN KEY (sched_name, trigger_name, trigger_group)
  REFERENCES qrtz_triggers (sched_name, trigger_name, trigger_group)
)
  ENGINE = InnoDB
  COMMENT '简单触发器的信息'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

CREATE TABLE qrtz_blob_triggers (
  sched_name    VARCHAR(120) NOT NULL,
  trigger_name  VARCHAR(190) NOT NULL,
  trigger_group VARCHAR(190) NOT NULL,
  blob_data     BLOB         NULL,
  PRIMARY KEY (sched_name, trigger_name, trigger_group),
  INDEX (sched_name, trigger_name, trigger_group),
  FOREIGN KEY (sched_name, trigger_name, trigger_group)
  REFERENCES qrtz_triggers (sched_name, trigger_name, trigger_group)
)
  ENGINE = InnoDB
  COMMENT '以Blob 类型存储的触发器'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

CREATE TABLE qrtz_calendars (
  sched_name    VARCHAR(120) NOT NULL,
  calendar_name VARCHAR(190) NOT NULL,
  calendar      BLOB         NOT NULL,
  PRIMARY KEY (sched_name, calendar_name)
)
  ENGINE = InnoDB
  COMMENT '存放日历信息，quartz可配置一个日历来指定一个时间范围'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

CREATE TABLE qrtz_paused_trigger_grps (
  sched_name    VARCHAR(120) NOT NULL,
  trigger_group VARCHAR(190) NOT NULL,
  PRIMARY KEY (sched_name, trigger_group)
)
  ENGINE = InnoDB
  COMMENT '存放暂停掉的触发器'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

CREATE TABLE qrtz_fired_triggers (
  sched_name        VARCHAR(120) NOT NULL,
  entry_id          VARCHAR(95)  NOT NULL,
  trigger_name      VARCHAR(190) NOT NULL,
  trigger_group     VARCHAR(190) NOT NULL,
  instance_name     VARCHAR(190) NOT NULL,
  fired_time        BIGINT(13)   NOT NULL,
  sched_time        BIGINT(13)   NOT NULL,
  priority          INTEGER      NOT NULL,
  state             VARCHAR(16)  NOT NULL,
  job_name          VARCHAR(190) NULL,
  job_group         VARCHAR(190) NULL,
  is_nonconcurrent  VARCHAR(1)   NULL,
  requests_recovery VARCHAR(1)   NULL,
  PRIMARY KEY (sched_name, entry_id)
)
  ENGINE = InnoDB
  COMMENT '存放已触发的触发器'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

CREATE TABLE qrtz_scheduler_state (
  sched_name        VARCHAR(120) NOT NULL,
  instance_name     VARCHAR(190) NOT NULL,
  last_checkin_time BIGINT(13)   NOT NULL,
  checkin_interval  BIGINT(13)   NOT NULL,
  PRIMARY KEY (sched_name, instance_name)
)
  ENGINE = InnoDB
  COMMENT '调度器状态'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

CREATE TABLE qrtz_locks (
  sched_name VARCHAR(120) NOT NULL,
  lock_name  VARCHAR(40)  NOT NULL,
  PRIMARY KEY (sched_name, lock_name)
)
  ENGINE = InnoDB
  COMMENT '存储程序的悲观锁的信息(假如使用了悲观锁)'
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

CREATE INDEX idx_qrtz_j_req_recovery
  ON qrtz_job_details (sched_name, requests_recovery);
CREATE INDEX idx_qrtz_j_grp
  ON qrtz_job_details (sched_name, job_group);

CREATE INDEX idx_qrtz_t_j
  ON qrtz_triggers (sched_name, job_name, job_group);
CREATE INDEX idx_qrtz_t_jg
  ON qrtz_triggers (sched_name, job_group);
CREATE INDEX idx_qrtz_t_c
  ON qrtz_triggers (sched_name, calendar_name);
CREATE INDEX idx_qrtz_t_g
  ON qrtz_triggers (sched_name, trigger_group);
CREATE INDEX idx_qrtz_t_state
  ON qrtz_triggers (sched_name, trigger_state);
CREATE INDEX idx_qrtz_t_n_state
  ON qrtz_triggers (sched_name, trigger_name, trigger_group, trigger_state);
CREATE INDEX idx_qrtz_t_n_g_state
  ON qrtz_triggers (sched_name, trigger_group, trigger_state);
CREATE INDEX idx_qrtz_t_next_fire_time
  ON qrtz_triggers (sched_name, next_fire_time);
CREATE INDEX idx_qrtz_t_nft_st
  ON qrtz_triggers (sched_name, trigger_state, next_fire_time);
CREATE INDEX idx_qrtz_t_nft_misfire
  ON qrtz_triggers (sched_name, misfire_instr, next_fire_time);
CREATE INDEX idx_qrtz_t_nft_st_misfire
  ON qrtz_triggers (sched_name, misfire_instr, next_fire_time, trigger_state);
CREATE INDEX idx_qrtz_t_nft_st_misfire_grp
  ON qrtz_triggers (sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state);

CREATE INDEX idx_qrtz_ft_trig_inst_name
  ON qrtz_fired_triggers (sched_name, instance_name);
CREATE INDEX idx_qrtz_ft_inst_job_req_rcvry
  ON qrtz_fired_triggers (sched_name, instance_name, requests_recovery);
CREATE INDEX idx_qrtz_ft_j_g
  ON qrtz_fired_triggers (sched_name, job_name, job_group);
CREATE INDEX idx_qrtz_ft_jg
  ON qrtz_fired_triggers (sched_name, job_group);
CREATE INDEX idx_qrtz_ft_t_g
  ON qrtz_fired_triggers (sched_name, trigger_name, trigger_group);
CREATE INDEX idx_qrtz_ft_tg
  ON qrtz_fired_triggers (sched_name, trigger_group);

COMMIT;