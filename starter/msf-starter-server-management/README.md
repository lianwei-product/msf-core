# Connext MSF MicroService Management Server Starter

## 数据库初始化
```
-- 数据表删除脚本
DROP TABLE IF EXISTS `management_service_route`;

-- 数据表创建脚本。
CREATE TABLE `management_service_route` (
  `service_name`     VARCHAR(128)       NOT NULL,
  `gateway`          VARCHAR(2048)      NULL,
  `webapi_info`      LONGTEXT           NULL,
  `sys_created_sort` INT AUTO_INCREMENT NOT NULL,
  PRIMARY KEY (`sys_created_sort`),
  UNIQUE KEY `ix_service_route_service_name` (`service_name`)
);
```