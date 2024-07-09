package scrips.datamigration.jpa.member;

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
@Setter
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
@Builder
public class JpaMember {
	
	@Id       
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;
    @Column(name ="member_code")
    private String memberCode;
    @Column(name ="swift_bic")
    private String swiftBic;
    @Column(name ="swift_member")
    private Short swiftMember;
    @Column(name ="bank_code")
    private Short bankCode;
    @Column(name ="name")
    private String name;
    @Column(name ="short_name")
    private String shortName;
    @Column(name ="member_status")
    private String memberStatus;
    @Column(name ="member_type")
    private String memberType;
    @Column(name ="sector_id")
    private String sectorId;
    @Column(name ="mcb_id")
    private String mcbId;
    @Column(name ="uen")
    private String uen;
    @Column(name ="lei")
    private String lei;
    @Column(name ="member_classification")
    private String memberClassification;
    @Column(name ="exempted_from_billing")
    private Short exemptedFromBilling;
    @Column(name ="exempted_from_system_limit")
    private Short exemptedFromSystemLimit;
    @Column(name ="location")
    private String location;
    @Column(name ="activation_date")
    private Date activationDate;
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
    @Column(name ="fi_group")
    private String fiGroup;    
    @Column(name ="billing_profile_id")
    private String billingProfileId;
    @Column(name ="mcb_intraday")
    private Double mcbIntraday;
    @Column(name ="mcb_eod_minimum")
    private Double mcbEodMinimum;
    @Column(name ="mcb_eod_maximum")
    private Double mcbEodMaximum;
    @Column(name ="mcb_average")
    private Double mcbAverage;
    @Column(name ="tax_profile_id")
    private String taxProfileId;
    @Column(name ="verified_by")
    private String verifiedBy;
    @Column(name ="verified_date")
    private Date verifiedDate;
   
  
}
