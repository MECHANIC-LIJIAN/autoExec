
DROP TABLE IF EXISTS `ssh_action`;
CREATE TABLE `ssh_action` (
  `action_id`  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  `action_name` varchar(255) NOT NULL,
  `host_id` varchar(36)  NOT NULL,
  `cmd` varchar(512) NOT NULL,
  `max_size` int(3) DEFAULT NULL,
  `max_second` int(4) DEFAULT NULL,
  `stop_words` varchar(255)  DEFAULT NULL,
  `check_process` varchar(20) DEFAULT NULL
);

DROP TABLE IF EXISTS `ssh_action_log`;
CREATE TABLE `ssh_action_log` (
  `action_log_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  `action_id` int(10) NOT NULL,
  `exec_res` text  NOT NULL,
  `start_time` bigint(15) DEFAULT NULL,
  `end_time` bigint(15) DEFAULT NULL,
  `exit_status` smallint(5) DEFAULT NULL,
  `status` tinyint(1) NOT NULL,
  `reason` varchar(20) DEFAULT NULL,
  `check_res` tinyint(1) DEFAULT NULL
) ;

DROP TABLE IF EXISTS `ssh_host`;
CREATE TABLE `ssh_host` (
  `host_id` varchar(32)  PRIMARY KEY NOT NULL,
  `ip_address` varchar(15)  NOT NULL,
  `hostname` varchar(20) NOT NULL,
  `port` varchar(5) NOT NULL DEFAULT '22',
  `username` varchar(16) NOT NULL,
  `password` varchar(64)  NOT NULL,
  `identity` varchar(64)  DEFAULT NULL,
  `passphrase` varchar(64)  DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `remark` varchar(64)  DEFAULT NULL
);

DROP TABLE IF EXISTS `ssh_step_action`;
CREATE TABLE `ssh_step_action` (
  `task_id` int(11) NOT NULL,
  `action_id` int(11) NOT NULL,
  `step` tinyint(4) NOT NULL,
  PRIMARY KEY (`task_id`,`action_id`,`step`)
) ;

DROP TABLE IF EXISTS `ssh_step_log`;
CREATE TABLE `ssh_step_log` (
  `task_log_id` int(11) NOT NULL,
  `action_log_id` int(11) NOT NULL,
  `step` tinyint(4) NOT NULL,
  PRIMARY KEY (`task_log_id`,`action_log_id`)
) ;


DROP TABLE IF EXISTS `ssh_task`;
CREATE TABLE `ssh_task` (
  `task_id`  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  `task_name` varchar(255) NOT NULL,
  `user_id` int(5) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL
);

DROP TABLE IF EXISTS `ssh_task_log`;
CREATE TABLE `ssh_task_log` (
  `task_log_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  `task_id` int(5) NOT NULL,
  `start_time` bigint(13) NOT NULL,
  `end_time` bigint(13) DEFAULT NULL,
  `status` tinyint(1) NOT NULL,
  `cur_step` int(10) DEFAULT NULL,
  `reason` varchar(50) DEFAULT NULL
) ;
