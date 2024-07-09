package scrips.datamigration.jpa.account;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account_source")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaAccountSource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String seq;
	private String id;
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
		private String mainAccountIndicator;
		@Column(name = "default_main_account")
		private String defaultMainAccount;
		@Column(name = "statement_delivery_bic")
		private String statementDeliveryBic;
		@Column(name = "account_status")
		private String accountStatus;
		@Column(name = "currency_code")
		private String currencyCode;
//		@Column(name = "minimum_balance")
//		private Long minimumBalance;
		@Column(name = "end_of_day_statement")
		private String endOfDayStatement;
		@Column(name = "payer_reference")
		private String payerReference;
		@Column(name = "related_reference")
		private String relatedReference;
		@Column(name = "counterparty_id")
		private String counterpartyId;
		// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "DD-MM-YYYY")
		@Column(name = "value_date")
		private String valueDate;
		// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "DD-MM-YYYY")
		@Column(name = "activation_date")
		private String activationDate;
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
		private String modifiedDate;
		@Column(name ="approved_by" )
		private String approvedBy;
		@Column(name ="approved_date" )
		private String approvedDate;
		@Column(name ="created_date" )
		private String createdDate;
		@Column(name ="effective_date" )
		private String effectiveDate;
		@Column(name ="approval_remark" )
		private String approvalRemark;
		@Column(name="workflow_status_id")
	    private String workflowStatusId;
	    @Column(name="compliance_calculation")
	    private String complianceCalculation;
	    @Column(name="remarks")
	    private String remarks;
	    
	
//	@OneToOne(mappedBy = "account_temp",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//	private JpaAccountNameAddressTemp accountNameAddress;
//	
//	@OneToOne(mappedBy = "account_temp", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
//	private JpaAccountSettlementPurposeTemp accountSettlementPurpose;
}