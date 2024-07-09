DROP TABLE IF EXISTS account;
CREATE TABLE `account` (
  `id` varchar(36) NOT NULL,
  `account_number` varchar(100) NOT NULL,
  `description` varchar(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `member_id` varchar(36) NOT NULL,
  `account_type` varchar(100) DEFAULT NULL,
  `main_account_indicator` varchar(100) DEFAULT NULL,
  `default_main_account` varchar(100) DEFAULT NULL,
  `statement_delivery_bic` varchar(100) DEFAULT NULL,
  `account_status` varchar(100) DEFAULT NULL,
  `currency_code` varchar(100) DEFAULT NULL,
  `minimum_balance` bigint DEFAULT NULL,
  `end_of_day_statement` varchar(36) DEFAULT NULL,
  `value_date` date DEFAULT NULL,
  `activation_date` date DEFAULT NULL,
  `gl_category` varchar(100) DEFAULT NULL,
  `cost_centre` varchar(100) DEFAULT NULL,
  `gl_account_number` varchar(100) DEFAULT NULL,
  `statement_interval` varchar(100) NULL,
  `migrated_date` datetime default NOW(),
  PRIMARY KEY (`account_number`)
);

DROP TABLE IF EXISTS account_settlement_purpose;
CREATE TABLE `account_settlement_purpose` (
  `account_id` varchar(36) DEFAULT NULL,
  `transaction_id` varchar(36) DEFAULT NULL,
  `currency_code` varchar(35) DEFAULT NULL,
   CONSTRAINT `account_id_FK` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_number`) ON DELETE SET NULL
);

DROP TABLE IF EXISTS account_name_address;
CREATE TABLE `account_name_address` (
  `account_id` varchar(36) DEFAULT NULL,
  `name_and_address` varchar(300) DEFAULT NULL,
  CONSTRAINT `account_id_FK_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_number`) ON DELETE SET NULL
);

DROP TABLE IF EXISTS account_temp;
CREATE TABLE `account_temp` (
  `id` varchar(46) NOT NULL,
  `account_number` varchar(100) NOT NULL,
  `description` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `member_id` varchar(46) NOT NULL,
  `account_type` varchar(100) DEFAULT NULL,
  `main_account_indicator` varchar(100) DEFAULT NULL,
  `default_main_account` varchar(100) DEFAULT NULL,
  `statement_delivery_bic` varchar(100) DEFAULT NULL,
  `account_status` varchar(100) DEFAULT NULL,
  `currency_code` varchar(100) DEFAULT NULL,
  `minimum_balance` bigint DEFAULT NULL,
  `end_of_day_statement` varchar(46) DEFAULT NULL,
  `value_date` date DEFAULT NULL,
  `activation_date` date DEFAULT NULL,
  `gl_category` varchar(100) DEFAULT NULL,
  `cost_centre` varchar(100) DEFAULT NULL,
  `gl_account_number` varchar(100) DEFAULT NULL,
  `statement_interval` varchar(100) NULL,
  `migrated_date` datetime default NOW(),
  PRIMARY KEY (`account_number`)
);

DROP TABLE IF EXISTS account_settlement_purpose_temp;
CREATE TABLE `account_settlement_purpose_temp` (
  `account_id` varchar(46) DEFAULT NULL,
  `transaction_id` varchar(46) DEFAULT NULL,
  `currency_code` varchar(45) DEFAULT NULL,
  CONSTRAINT `account_id_temp_FK` FOREIGN KEY (`account_id`) REFERENCES `account_temp` (`account_number`) ON DELETE SET NULL
);

DROP TABLE IF EXISTS account_name_address_temp;
CREATE TABLE `account_name_address_temp` (
  `account_id` varchar(46) DEFAULT NULL,
  `name_and_address` varchar(300) DEFAULT NULL,
   CONSTRAINT `account_id_temp_FK_1` FOREIGN KEY (`account_id`) REFERENCES `account_temp` (`account_number`) ON DELETE SET NULL
);

DROP TABLE IF EXISTS member;
CREATE TABLE member (
id varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
member_code varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
member_name varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Bank/FI name',
member_short_name varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Bank/FI short name',
member_type varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
member_status varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
activation_date date NOT NULL,
swift_member varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
swift_bic varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
location_code varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '3 digit location code',
fi_group char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
fi_code varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
sector_id varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
currency_code_subscription varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
tax_rate varchar(100) DEFAULT NULL,
fee_profile_id varchar(100) DEFAULT NULL,
mcb_id varchar(100) DEFAULT NULL,
intrday_mcb_requirement varchar(100) DEFAULT NULL,
minimum_mcb_requirement varchar(100) DEFAULT NULL,
maximum_mcb_requirement varchar(100) DEFAULT NULL,
average_mcb_requirement varchar(100) DEFAULT NULL,
uen varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Company registartion number',
lei varchar(20) DEFAULT NULL,
exempted_from_billing tinyint(1) DEFAULT NULL,
exempted_from_system_limit tinyint(1) DEFAULT NULL,
currency_code varchar(3) DEFAULT NULL,
currency_member_code varchar(36) DEFAULT NULL,
currency_settlement_agent varchar(100) DEFAULT NULL,
migrated_date datetime default NOW(),
PRIMARY KEY ( id )
);

DROP TABLE IF EXISTS member_temp;
CREATE TABLE member_temp (
id varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
member_code varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
member_name varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Bank/FI name',
member_short_name varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Bank/FI short name',
member_type varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
member_status varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
activation_date date NOT NULL,
swift_member varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
swift_bic varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
location_code varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '3 digit location code',
fi_group char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
fi_code varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
sector_id varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
currency_code_subscription varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
tax_rate varchar(100) DEFAULT NULL,
fee_profile_id varchar(100) DEFAULT NULL,
mcb_id varchar(100) DEFAULT NULL,
intrday_mcb_requirement varchar(100) DEFAULT NULL,
minimum_mcb_requirement varchar(100) DEFAULT NULL,
maximum_mcb_requirement varchar(100) DEFAULT NULL,
average_mcb_requirement varchar(100) DEFAULT NULL,
uen varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Company registartion number',
lei varchar(20) DEFAULT NULL,
exempted_from_billing tinyint(1) DEFAULT NULL,
exempted_from_system_limit tinyint(1) DEFAULT NULL,
currency_code varchar(3) DEFAULT NULL,
currency_member_code varchar(36) DEFAULT NULL,
currency_settlement_agent varchar(100) DEFAULT NULL,
migrated_date datetime default NOW(),
PRIMARY KEY ( id )
);

DROP TABLE IF EXISTS member_liaison;
CREATE TABLE member_liaison (
id varchar(100) NOT NULL,
member_code varchar(100) DEFAULT NULL,
liaison_officer_name varchar(100) DEFAULT NULL,
liaison_officer_contact_number varchar(100) DEFAULT NULL,
liaison_officer_email_address varchar(100) DEFAULT NULL,
registered_address varchar(100) DEFAULT NULL,
PRIMARY KEY ( id )
);

DROP TABLE IF EXISTS account_position;
CREATE TABLE `account_position` (
  `id` varchar(36) NOT NULL,
  `account_type` varchar(100) DEFAULT NULL,
  `account_id` varchar(100) DEFAULT NULL,
  `member_code` varchar(100) DEFAULT NULL,
  `member_name` varchar(100) DEFAULT NULL,
  `currency_code` varchar(100) DEFAULT NULL,
  `value_date` date DEFAULT NULL,
  `opening_balance` varchar(100) DEFAULT NULL,
  `debit` varchar(100) DEFAULT NULL,
  `credit` varchar(100) DEFAULT NULL,
  `total` varchar(100) DEFAULT NULL,
   `current_balance` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS account_position_temp;
CREATE TABLE `account_position_temp` (
  `id` varchar(36) NOT NULL,
  `account_type` varchar(100) DEFAULT NULL,
  `account_id` varchar(100) DEFAULT NULL,
  `member_code` varchar(100) DEFAULT NULL,
  `member_name` varchar(100) DEFAULT NULL,
  `currency_code` varchar(100) DEFAULT NULL,
  `value_date` date DEFAULT NULL,
  `opening_balance` varchar(100) DEFAULT NULL,
  `debit` varchar(100) DEFAULT NULL,
  `credit` varchar(100) DEFAULT NULL,
  `total` varchar(100) DEFAULT NULL,
   `current_balance` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);