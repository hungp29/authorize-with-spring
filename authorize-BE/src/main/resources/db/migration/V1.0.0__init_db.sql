CREATE DATABASE IF NOT EXISTS `authorize_demo` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `authorize_demo`;

--
-- Table structure for table `principal`
--
DROP TABLE IF EXISTS `principal`;
CREATE TABLE `principal`
(
    `id`          varchar(32) NOT NULL,
    `disabled`    boolean     NOT NULL DEFAULT '0',
    `deleted`     boolean     NOT NULL DEFAULT '0',
    `expire_date` timestamp   NULL,
    `locked`      boolean     NOT NULL DEFAULT '0',
    `create_by`   varchar(32) NULL,
    `create_at`   timestamp   NULL,
    `update_by`   varchar(32) NULL,
    `update_at`   timestamp   NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

--
-- Table structure for table `principal_attempt`
--
DROP TABLE IF EXISTS `principal_attempt`;
CREATE TABLE `principal_attempt`
(
    `id`            varchar(32) NOT NULL,
    `principal_id`  varchar(32) NOT NULL,
    `attempt_count` smallint    NULL,
    `create_by`     varchar(32) NULL,
    `create_at`     timestamp   NULL,
    `update_by`     varchar(32) NULL,
    `update_at`     timestamp   NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_principal_attempts_w_principal` FOREIGN KEY (`principal_id`) REFERENCES `principal` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

--
-- Table structure for table `role`
--
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`          varchar(32)  NOT NULL,
    `name`        varchar(255) NOT NULL,
    `system_role` boolean      NULL DEFAULT '0',
    `read_only`   boolean      NULL DEFAULT '0',
    `create_by`   varchar(32)  NULL,
    `create_at`   timestamp    NULL,
    `update_by`   varchar(32)  NULL,
    `update_at`   timestamp    NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

--
-- Table structure for table `principal_role`
--
DROP TABLE IF EXISTS `principal_role`;
CREATE TABLE `principal_role`
(
    `principal_id` varchar(32) NOT NULL,
    `role_id`      varchar(32) NOT NULL,
    PRIMARY KEY (`principal_id`, `role_id`),
    CONSTRAINT `fk_principal_role_w_principal` FOREIGN KEY (`principal_id`) REFERENCES `principal` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_principal_role_w_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

--
-- Table structure for table `policy`
--
DROP TABLE IF EXISTS `policy`;
CREATE TABLE `policy`
(
    `id`        varchar(32)  NOT NULL,
    `name`      varchar(255) NOT NULL,
    `read_only` boolean      NULL DEFAULT '0',
    `create_by` varchar(32)  NULL,
    `create_at` timestamp    NULL,
    `update_by` varchar(32)  NULL,
    `update_at` timestamp    NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

--
-- Table structure for table `policy_condition`
--
DROP TABLE IF EXISTS `policy_condition`;
CREATE TABLE `policy_condition`
(
    `id`              varchar(32) NOT NULL,
    `policy_id`       varchar(32) NOT NULL,
    `operator`        text        NOT NULL,
    `condition_value` text        NULL,
    `create_by`       varchar(32) NULL,
    `create_at`       timestamp   NULL,
    `update_by`       varchar(32) NULL,
    `update_at`       timestamp   NULL,
    PRIMARY KEY (`id`),
    KEY `policy_id` (`policy_id`),
    CONSTRAINT `fk_policy_condition_w_policy` FOREIGN KEY (`policy_id`) REFERENCES `policy` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

--
-- Table structure for table `policy_permission`
--
DROP TABLE IF EXISTS `policy_permission`;
CREATE TABLE `policy_permission`
(
    `id`         varchar(32) NOT NULL,
    `policy_id`  varchar(32) NOT NULL,
    `permission` text        NOT NULL,
    `create_by`  varchar(32) NULL,
    `create_at`  timestamp   NULL,
    `update_by`  varchar(32) NULL,
    `update_at`  timestamp   NULL,
    PRIMARY KEY (`id`),
    KEY `policy_id` (`policy_id`),
    CONSTRAINT `fk_policy_permission_w_policy` FOREIGN KEY (`policy_id`) REFERENCES `policy` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

--
-- Table structure for table `role_policy`
--
DROP TABLE IF EXISTS `role_policy`;
CREATE TABLE `role_policy`
(
    `role_id`   varchar(32) NOT NULL,
    `policy_id` varchar(32) NOT NULL,
    PRIMARY KEY (`role_id`, `policy_id`),
    CONSTRAINT `fk_role_policy_w_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_role_policy_w_policy` FOREIGN KEY (`policy_id`) REFERENCES `policy` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

--
-- Table structure for table `account`
--
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`
(
    `id`                 varchar(32)  NOT NULL,
    `principal_id`       varchar(32)  NOT NULL,
    `username`           varchar(255) NULL,
    `first_name`         varchar(255) NULL,
    `last_name`          varchar(255) NULL,
    `email`              varchar(255) NULL,
    `title`              varchar(255) NULL,
    `date_of_birth`      date         NULL,
    `avatar_url`         text         NULL,
    `house_address`      text         NULL,
    `work_address`       text         NULL,
    `phone_number`       varchar(20)  NULL,
    `notification_token` text         NULL,
    `language`           varchar(2)   NULL,
    `create_by`          varchar(32)  NULL,
    `create_at`          timestamp    NULL,
    `update_by`          varchar(32)  NULL,
    `update_at`          timestamp    NULL,
    PRIMARY KEY (id),
    KEY `principal_id` (`principal_id`),
    CONSTRAINT `fk_account_w_principal` FOREIGN KEY (`principal_id`) REFERENCES `principal` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

--
-- Table structure for table `auth_method_data`
--

CREATE TABLE `auth_method_data`
(
    `id`          varchar(32) NOT NULL,
    `auth_data_1` text        NULL,
    `auth_data_2` text        NULL,
    `auth_data_3` text        NULL,
    `expire_date` timestamp   NULL,
    `create_by`   varchar(32) NULL,
    `create_at`   timestamp   NULL,
    `update_by`   varchar(32) NULL,
    `update_at`   timestamp   NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

--
-- Table structure for table `auth_method`
--

CREATE TABLE `auth_method`
(
    `id`                  varchar(32)                                                  NOT NULL,
    `principal_id`        varchar(32)                                                  NOT NULL,
    `auth_type`           enum ('USERNAME_PASSWORD', 'EMAIL_PASSWORD', 'PHONE_NUMBER') NOT NULL,
    `determine_id`        text                                                         NULL,
    `auth_method_data_id` varchar(32)                                                  NOT NULL,
    `create_by`           varchar(32)                                                  NULL,
    `create_at`           timestamp                                                    NULL,
    `update_by`           varchar(32)                                                  NULL,
    `update_at`           timestamp                                                    NULL,
    PRIMARY KEY (`id`),
    KEY `principal_id` (`principal_id`),
    CONSTRAINT `fk_auth_method_w_principal` FOREIGN KEY (`principal_id`) REFERENCES `principal` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_auth_method_w_auth_method_data` FOREIGN KEY (`auth_method_data_id`) REFERENCES `auth_method_data` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;