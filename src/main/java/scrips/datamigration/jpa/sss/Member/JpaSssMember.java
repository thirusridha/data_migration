package scrips.datamigration.jpa.sss.Member;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaSssMember {
	@Id   //JpaSssMember
    private String id;
    @Column(name="member_code")
    private String memberCode;
    @Column(name="name")
    private String name;
    @Column(name="short_name")
    private String shortName;
    @Column(name="member_type")
    private String memberType;
    @Column(name="member_status")
    private String memberStatus;
    @Column(name="activation_date")
    private Date activationDate;
    @Column(name="swift_bic")
    private String swiftBic;
    @Column(name="location")
    private String location;
    @Column(name="fi_code")
    private String fiCode;
    @Column(name="trader_status")
    private String traderStatus;
    @Column(name="sector_id")
    private String sectorId;
    @Column(name="auto_debit_indicator")
    private Short autoDebitIndicator;
    @Column(name="facility_eligibility_id")
    private String facilityEligibilityId;
    @Column(name="uen")
    private String uen;
    @Column(name="lei")
    private String lei;
    @Column(name="statement_delivery_bic")
    private String statementDeliveryBic;
    @Column(name="end_of_day_statement")
    private String endOfDayStatement;
    @Column(name="exempted_from_billing")
    private Short exemptedFromBilling;
    @Column(name="exempted_from_system_limit")
    private Short exemptedFromSystemLimit;
    @Column(name="zero_holdings_statement")
    private String zeroHoldingsStatement;
    @Column(name="rtgs_account")
    private String rtgsAccount;
    @Column(name="tax_profile_id")
    private String taxProfileId;
    @Column(name="billing_profile_id")
    private String billingProfileId;
    @Column(name="action")
    private String action;
    @Column(name="status")
    private String status;
    @Column(name="modified_by")
    private String modifiedBy;
    @Column(name="modified_date")
    private Date modifiedDate;
    @Column(name="approved_by")
    private String approvedBy;
    @Column(name="approved_date")
    private Date approvedDate;
    @Column(name="created_date")
    private Date createdDate;
    @Column(name="effective_date")
    private Date effectiveDate;
    @Column(name="workflow_status_id")
    private Integer workflowStatusId;
    @Column(name="approval_remark")
    private String approvalRemark;
    @Column(name="fall_back_account")
    private String fallBackAccount;
    @Column(name="issuing_paying_agent")
    private String issuingPayingAgent;
    @Column(name="primary_dealer")
    private String primaryDealer;
    @Column(name="fi_group")
    private char fiGroup;
    @Column(name="account_operator")
    private String accountOperator;
    @Column(name="verified_by")
    private String verifiedBy;
    @Column(name="verified_date")
    private Date verifiedDate;
    @Column(name="rtgs_member_code")
    private String rtgsMemberCode;
}