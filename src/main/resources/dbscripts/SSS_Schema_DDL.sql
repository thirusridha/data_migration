CREATE TABLE `sss_member` (
  `id` varchar(36) NOT NULL,
  `member_code` varchar(100) DEFAULT NULL,
  `member_name` varchar(100) DEFAULT NULL,
  `activation_date` varchar(100) DEFAULT NULL,
  `member_short_name` varchar(100) DEFAULT NULL,
  `member_status` varchar(100) DEFAULT NULL,
  `member_type` int DEFAULT NULL,
  `sector_id` varchar(100) DEFAULT NULL,
  `bank_code` varchar(100) DEFAULT NULL,
  `auto_debit_indicator` varchar(100) DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `facility_eligibility_id` varchar(100) DEFAULT NULL,
  `lei` varchar(100) DEFAULT NULL,
  `uen` varchar(100) DEFAULT NULL,
  `zero_holdings_statement` varchar(100) DEFAULT NULL,
  `exempted_from_billing` varchar(100) DEFAULT NULL,
  `exempted_from_system_limit` varchar(100) DEFAULT NULL,
  `statement_delivery_bic` varchar(100) DEFAULT NULL,
  `tax_profile_id` varchar(100) DEFAULT NULL,
  `fee_profile_id` varchar(100) DEFAULT NULL,
  `end_of_day_statement` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `sss_account` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(100) DEFAULT NULL,
  `member_code` varchar(100) DEFAULT NULL,
  `custody_account_type_id` varchar(100) DEFAULT NULL,
  `account_category` varchar(100) DEFAULT NULL,
  `investor_individual_id` varchar(100) DEFAULT NULL,
  `investor_name` varchar(100) DEFAULT NULL,
  `account_status` varchar(100) DEFAULT NULL,
  `tax_profile_id` varchar(100) DEFAULT NULL,
  `statement_delivery_bic` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE `sss_account_securites_position_stats` (
  `member_code` varchar(100) NOT NULL,
  `account_id` varchar(100) DEFAULT NULL,
  `securities_code` varchar(100) DEFAULT NULL,
  `value_date` varchar(100) DEFAULT NULL,
  `opening_nominal_amount` int DEFAULT NULL,
  `closing_nominal_amount` int DEFAULT NULL,
  `settled_count` int DEFAULT NULL,
  `settled_amount` int DEFAULT NULL,
  `receipt_count` int DEFAULT NULL,
  `receipt_amount` int DEFAULT NULL,
  `queued_count` int DEFAULT NULL,
  `queued_amount` int DEFAULT NULL,
  `rejected_count` int DEFAULT NULL,
  `rejected_amount` int DEFAULT NULL,
  `collected_count` int DEFAULT NULL,
  PRIMARY KEY (`member_code`)
);



CREATE TABLE `sss_securities_code` (
  `id` varchar(36) NOT NULL,
  `securities_code` varchar(36) NOT NULL,
  `issuer_code` varchar(100) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `securities_type` varchar(100) DEFAULT NULL,
  `securities_status` varchar(100) DEFAULT NULL,
  `tenor_period_unit` varchar(100) DEFAULT NULL,
  `tenor_period` varchar(100) DEFAULT NULL,
  `currency_code` varchar(100) DEFAULT NULL,
  `denomination` varchar(100) DEFAULT NULL,
  `issuer` varchar(100) DEFAULT NULL,
  `ipa` varchar(100) DEFAULT NULL,
  `primary_registry_id` varchar(100) DEFAULT NULL,
  `facility_eligibility_id` varchar(100) DEFAULT NULL,
  `haircut_id` varchar(100) DEFAULT NULL,
  `tradable` varchar(100) DEFAULT NULL,
  `automatic_event_generation` varchar(100) DEFAULT NULL,
  `tax_scheme_id` varchar(100) DEFAULT NULL,
  `first_auction_date` varchar(100) DEFAULT NULL,
  `first_issuance_amount` varchar(100) DEFAULT NULL,
  `redemption_date` varchar(100) DEFAULT NULL,
  `redemption_price` varchar(100) DEFAULT NULL,
  `central_bank_applied_amount` varchar(100) DEFAULT NULL,
  `coupon_structure` varchar(100) DEFAULT NULL,
  `coupon_payment_frequency` varchar(100) DEFAULT NULL,
  `rate` varchar(100) DEFAULT NULL,
  `issue_price` varchar(100) DEFAULT NULL,
  `issue_yield` varchar(100) DEFAULT NULL,
  `day_count_convention` varchar(100) DEFAULT NULL,
  `rounding_option` varchar(100) DEFAULT NULL,
  `voluntary_redemption_frequency` varchar(100) DEFAULT NULL,
  `redemption_end_date_offset` varchar(100) DEFAULT NULL,
  `record_date_period` varchar(100) DEFAULT NULL,  
  PRIMARY KEY (`id`)
);


CREATE TABLE `sss_securities_code_statistics` (
  `securities_code` varchar(36) NOT NULL,
  `total_nominal_amount_issued` int DEFAULT NULL,
  `total_redeemed_amount` int DEFAULT NULL,
  `total_nominal_amount_issued_for_erf` int DEFAULT NULL,
  `outstanding_nominal_amount` int DEFAULT NULL,
  `next_coupon_date` varchar(100) DEFAULT NULL,
  `last_coupon_date` varchar(100) DEFAULT NULL,
  `next_index_end_date` varchar(100) DEFAULT NULL,
  `amount_alloted_to_central_bank` varchar(100) DEFAULT NULL,
  `amount_alloted_to_others` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`securities_code`)
);


CREATE TABLE `sss_coupon_schedules` (
  `id` varchar(36) NOT NULL,
  `securities_code` varchar(36) NOT NULL,
  `securiies_type` varchar(100) DEFAULT NULL,
  `issuer` varchar(100) DEFAULT NULL,
  `coupon_structure` varchar(100) DEFAULT NULL,
  `coupon_payment_frequency` varchar(100) DEFAULT NULL,
  `coupon_date` varchar(100) DEFAULT NULL,
  `coupon_rate` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE `sss_allotment` (
  `id` varchar(36) NOT NULL,
  `securities_code` varchar(100) DEFAULT NULL,
  `issue_code` varchar(100) DEFAULT NULL,
  `allotment_date` varchar(100) DEFAULT NULL,
  `issuance_date` varchar(100) DEFAULT NULL,
  `total_nominal_amount` varchar(100) DEFAULT NULL,
  `total_settlement_amount` varchar(100) DEFAULT NULL,
  `total_allotment_from_auction_result` varchar(100) DEFAULT NULL,
  `first_allotment` varchar(100) DEFAULT NULL,
  `account_id` varchar(100) DEFAULT NULL,
  `allotment_price` varchar(100) DEFAULT NULL,
  `nominal_amount` varchar(100) DEFAULT NULL,
  `alloment_status` varchar(100) DEFAULT NULL,
  `securities_opening_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE `sss_securities_price` (
  `id` varchar(36) NOT NULL,
  `securities_code` varchar(100) NOT NULL,
  `pricing_date` varchar(100) DEFAULT NULL,
  `pricing_type` varchar(100) DEFAULT NULL,
  `securities_description` varchar(100) DEFAULT NULL,
  `price` varchar(100) DEFAULT NULL,
  `issue_code` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE `sss_floating_rates` (
  `id` varchar(36) NOT NULL,
  `rate_index` varchar(100) DEFAULT NULL,
  `publication_date` varchar(100) DEFAULT NULL,
  `value_date` varchar(100) DEFAULT NULL,
  `rate` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);



CREATE TABLE `sss_transaction` (
  `id` varchar(36) NOT NULL,
  `sss_reference` varchar(100) DEFAULT NULL,
  `message_log_id` varchar(100) DEFAULT NULL,
  `securities_type` varchar(100) DEFAULT NULL,
  `transaction_ype` varchar(100) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `settlement_date` varchar(100) DEFAULT NULL,
  `trade_date` varchar(100) DEFAULT NULL,
  `currency_code` varchar(100) DEFAULT NULL,
  `settlement_amount` varchar(100) DEFAULT NULL,
  `nominal_amount` varchar(100) DEFAULT NULL,
  `deal_price` varchar(100) DEFAULT NULL,
  `deliverer_member_id` varchar(100) DEFAULT NULL,
  `deliverer_account_id` varchar(100) DEFAULT NULL,
  `receiver_member_id` varchar(100) DEFAULT NULL,
  `receiver_account_id` varchar(100) DEFAULT NULL,
  `repo_closing_date` varchar(100) DEFAULT NULL,
  `repo_closing_settlement_amount` varchar(100) DEFAULT NULL,
  `reason_code` varchar(100) DEFAULT NULL,
  `link_transaction_reference` varchar(100) DEFAULT NULL,
  `processed_date` varchar(100) DEFAULT NULL,
  `created_date` varchar(100) DEFAULT NULL,
  `modified_date` varchar(100) DEFAULT NULL,
  `transaction_received_date` varchar(100) DEFAULT NULL,
  `sender_swift_member_code` varchar(100) DEFAULT NULL,
  `payer_swift_member_code` varchar(100) DEFAULT NULL,
  `payee_swift_member_code` varchar(100) DEFAULT NULL,
  `receiver_swift_member_code` varchar(100) DEFAULT NULL,
  `sender_member_code` varchar(100) DEFAULT NULL,
  `sender_type` varchar(100) DEFAULT NULL,
  `accrued_interest_amount` varchar(100) DEFAULT NULL,
  `match_timestamp` varchar(100) DEFAULT NULL,
  `deliverer_rtgs_account` varchar(100) DEFAULT NULL,
  `receiver_rtgs_account` varchar(100) DEFAULT NULL,
  `grid_lock_indicator` varchar(100) DEFAULT NULL,
  `rollover_count` varchar(100) DEFAULT NULL,
  `pending_cancellation_indicator` varchar(100) DEFAULT NULL,
  `receiver_rtgs_member_code` varchar(100) DEFAULT NULL,
  `deliverer_rtgs-member_code` varchar(100) DEFAULT NULL,
  `payment_type` varchar(100) DEFAULT NULL,
  `hold_indicator` varchar(100) DEFAULT NULL,
  `channel` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
