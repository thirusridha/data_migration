package scrips.datamigration.jpa.sss.account;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sss_account_temp")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class JpaSSSAccountTemp {
	@Id
    @Column(name="id")
    private String id;
    @Column(name = "member_id")
    private String memberId;
    @Column(name = "account_id")
    private String accountId;
    @Column(name = "description")
    private String description;
    @Column(name = "custody_account_type_id")
    private String custodyAccountTypeId;  
    @Column(name = "corporate_investor")
    private String corporateInvestor;
//    @Column(name = "account_category")
//    private String accountCategory;
//    @Column(name = "investor_individual_id")
//    private String investorIndividualId;
//    @Column(name = "investor_name")
//    private String investorName;
    @Column(name = "account_status")
    private String accountStatus;
    @Column(name = "tax_profile_id")
    private String taxProfileId;
    @Column(name = "statement_delivery_bic")
    private String statementDeliveryBic;
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
	@Column(name ="workflow_status_id" )
	private int workflowStatusId;
}
