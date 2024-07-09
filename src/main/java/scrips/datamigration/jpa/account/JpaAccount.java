package scrips.datamigration.jpa.account;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account")
@Data
//@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaAccount {

	 @Id
	@Column()
	private String id;
//	@Id
	@Column(name = "account_number",length = 34)
	private String accountNumber;
	@Column(name = "description")
	private String description;
	@Column(name = "member_id")
	private String memberId;
	// private String iban;
	@Column(name = "account_type")
	private String accountType;
	@Column(name = "main_account_indicator")
	private Integer mainAccountIndicator;
	@Column(name = "default_main_account")
	private Integer defaultMainAccount;
	@Column(name = "statement_delivery_bic")
	private String statementDeliveryBic;
	@Column(name = "account_status")
	private String accountStatus;
	@Column(name = "currency_code")
	private String currencyCode;
//	@Column(name = "minimum_balance")
//	private Long minimumBalance;
	@Column(name = "end_of_day_statement")
	private String endOfDayStatement;
	@Column(name = "payer_reference")
	private String payerReference;
	@Column(name = "related_reference")
	private String relatedReference;
	@Column(name = "counterparty_id")
	private String counterpartyId;
//	@Column(name = "minimum_balance")
//	private Long minimumBalance;
	
	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "DD-MM-YYYY")
	@Column(name = "value_date")
	private Integer valueDate;
	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "DD-MM-YYYY")
	@Column(name = "activation_date")
	private Date activationDate;
	@Column(name = "gl_category")
	private String glCategory;
	@Column(name = "cost_centre")
	private String costCentre;
	@Column(name = "gl_account_number")
	private String glAccountNumber;
	@Column(name = "statement_interval")
	private String statementInterval;
	@Column(name ="action" )
	private String action;
	@Column(name ="status" )
	private String status;
	@Column(name ="modified_by" )
	private String modifiedBy;
	@Column(name ="modified_date" )
	private Date modifiedDate;
	@Column(name ="approved_by" )
	private String approvedBy;
	@Column(name ="approved_date" )
	private Date approvedDate;
	@Column(name ="created_date" )
	private Date createdDate;
	@Column(name ="effective_date" )
	private Date effectiveDate;
	@Column(name ="approval_remark" )
	private String approvalRemark;
	@Column(name="workflow_status_id")
    private Integer workflowStatusId;
    @Column(name="compliance_calculation")
    private Short complianceCalculation;

//	@OneToOne(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//	private JpaAccountNameAddress accountNameAddress;
//	@OneToOne(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//	private JpaAccountSettlementPurpose accountSettlementPurpose;
}