USE `authorize_demo`;

--
-- Table structure for table `product`
--

CREATE TABLE `product`
(
    `id`          varchar(35)  NOT NULL,
    `name`        varchar(256) NOT NULL,
    `description` text         NULL,
    `image`       text         NULL,
    `create_by`   varchar(35)  NULL,
    `create_at`   timestamp    NULL,
    `update_by`   varchar(35)  NULL,
    `update_at`   timestamp    NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;