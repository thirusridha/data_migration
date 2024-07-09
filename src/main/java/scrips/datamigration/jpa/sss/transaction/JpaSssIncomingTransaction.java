package scrips.datamigration.jpa.sss.transaction;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "incoming_transaction")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaSssIncomingTransaction {
	
	@Id
//	private String id;
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
    @Column(name = "matched_sss_reference")
    private String matchedSssReference;
    @Column(name = "sender_member_code")
    private String senderMemberCode;
    @Column(name = "sender_reference")
    private String senderReference;
    @Column(name = "sender_type")
    private String senderType;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "modified_date")
    private Date modifiedDate;
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
    @Column(name = "pending_cancellation_indicator")
    private Short pendingCancellationIndicator;
    @Column(name = "deliverer_account_no")
    private String delivererAccountNo;
    @Column(name = "receiver_account_no")
    private String receiverAccountNo;
    @Column(name = "msg_received_timestamp")
    private Date msgReceivedTimestamp;
    @Column(name = "submitted_clo_settle_amount")
    private Double submittedCloSettleAmount;
    @Column(name = "deliverer_beneficiary_account")
    private String delivererBeneficiaryAccount;
    @Column(name = "accured_interest_amount")
    private Double accuredInterestAmount;
}
