DROP TABLE IF EXISTS `t_mock_node`;
CREATE TABLE `t_mock_node` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `node_ip` int(10) unsigned NOT NULL,
  `is_online` tinyint(3) unsigned NOT NULL DEFAULT 0,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_mock_rule`;
CREATE TABLE `t_mock_rule` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `node_id` int(10) unsigned NOT NULL,
  `class_pattern` varchar(100) NOT NULL,
  `method_pattern` varchar(50) NOT NULL,
  `script` varchar(255) NOT NULL,
  `watcher_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
