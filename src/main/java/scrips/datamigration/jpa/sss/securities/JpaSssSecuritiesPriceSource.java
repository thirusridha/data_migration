package scrips.datamigration.jpa.sss.securities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "securities_price_source")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaSssSecuritiesPriceSource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int seq;
	private String id;      
    @Column(name ="securities_code" )
    private String securitiesCode;
    @Column(name ="pricing_date" )
    private String pricingDate;
    @Column(name ="description" )
    private String description;
    @Column(name ="price" )
    private String price;
    @Column(name ="issue_code" )
    private String issueCode;
    @Column(name ="pricing_type" )
    private String pricingType;
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
	@Column(name ="workflow_status_id" )
	private String workflowStatusId;
	private String remarks;
}
