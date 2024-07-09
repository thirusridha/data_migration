package scrips.datamigration.jpa.sss.transaction;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transaction_temp")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaSssTransactionTemp  {
	
	@Id
	private String id;
	@Column(name = "sss_reference")
	private String sssReference;
	@Column(name = "message_log_id")
	private String messageLogId;
	@Column(name = "securities_code")
	private String securitiesCode;
	@Column(name = "transaction_type")
	private String transactionType;
	@Column(name = "status")
	private String status;
	@Column(name = "settlement_date")
	private Integer settlementDate;
	@Column(name = "trade_date")
	private Integer tradeDate;
	@Column(name = "currency_code")
	private String currencyCode;
	@Column(name = "settlement_amount")
	private Double settlementAmount;
	@Column(name = "nominal_amount")
	private Double nominalAmount;
	@Column(name = "deal_price")
	private Double dealPrice;
	@Column(name = "deliverer_member_code")
	private String delivererMemberCode;
	@Column(name = "receiver_member_code")
	private String receiverMemberCode;
	@Column(name = "repo_closing_date")
	private Integer repoClosingDate;
	@Column(name = "repo_closing_settlement_amount")
	private Double repoClosingSettlementAmount;
	@Column(name = "reason_code")
	private String reasonCode;
	@Column(name = "transaction_reference")
	private String transactionReference;
	@Column(name = "processed_date")
	private Integer processedDate;
	@Column(name = "created_date")
	private Date createdDate;
	@Column(name = "modified_date")
	private Date modifiedDate;
	@Column(name = "sender_member_swift_code")
	private String senderMemberSwiftCode;
	@Column(name = "payer_member_swift_code")
	private String payerMemberSwiftCode;
	@Column(name = "payee_member_swift_code")
	private String payeeMemberSwiftCode;
	@Column(name = "receiver_member_swift_code")
	private String receiverMemberSwiftCode;
	@Column(name = "sender_member_code")
	private String senderMemberCode;
	@Column(name = "sender_type")
	private String senderType;
	@Column(name = "message_type")
	private String messageType;
	@Column(name = "receiver_rtgs_account")
	private String receiverRtgsAccount;
	@Column(name = "deliverer_rtgs_account")
	private String delivererRtgsAccount;
	@Column(name = "receiver_rtgs_member_code")
	private String receiverRtgsMemberCode;
	@Column(name = "deliverer_rtgs_member_code")
	private String delivererRtgsMemberCode;
	@Column(name = "hold_indicator")
	private Short holdIndicator;
	@Column(name = "payment_type")
	private String paymentType;
	@Column(name = "grid_lock_indicator")
	private Short gridLockIndicator;
	@Column(name = "rollover_count")
	private Integer rolloverCount;
	@Column(name = "pending_cancellation_indicator")
	private Short pendingCancellationIndicator;
	@Column(name = "deliverer_member_id")
	private String delivererMemberId;
	@Column(name = "receiver_member_id")
	private String receiverMemberId;
	@Column(name = "receiver_account_id")
	private String receiverAccountId;
	@Column(name = "deliverer_account_id")
	private String delivererAccountId;
	@Column(name = "sender_member_id")
	private String senderMemberId;
	@Column(name = "pdm_indicator")
	private Short pdmIndicator;
	@Column(name = "erf_reference")
	private String erfReference;
	@Column(name = "deliverer_account_no")
	private String delivererAccountNo;
	@Column(name = "receiver_account_no")
	private String receiverAccountNo;
	@Column(name = "msg_received_timestamp")
	private Date msgReceivedTimestamp;
	@Column(name = "receiver_sender_id")
	private String receiverSenderId;
	@Column(name = "deliverer_sender_id")
	private String delivererSenderId;
	@Column(name = "deliverer_onbehalf")
	private Short delivererOnbehalf;
	@Column(name = "receiver_onbehalf")
	private Short receiverOnbehalf;
	@Column(name = "process_flag")
	private String processFlag;
	@Column(name = "deliverer_beneficiary_account")
	private String delivererBeneficiaryAccount;
	@Column(name = "underlying_id")
	private String underlyingId;
	@Column(name = "balance_type")
	private String balanceType;
	@Column(name = "accured_interest_amount")
	private Double accuredInterestAmount;
	@Column(name = "deliverer_channel")
	private String delivererChannel;
	@Column(name = "receiver_channel")
	private String receiverChannel;
	@Column(name = "repo_rate")
	private Double repoRate;
	@Column(name = "haircut_rate")
	private Double haircutRate;
	@Column(name = "original_account_no")
	private String originalAccountNo;
	@Column(name = "remarks")
	private String remarks;
	
}