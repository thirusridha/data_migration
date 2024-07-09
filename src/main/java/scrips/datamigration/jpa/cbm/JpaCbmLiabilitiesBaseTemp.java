package scrips.datamigration.jpa.cbm;

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
@Table(name = "cbm_liabilities_base_temp")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaCbmLiabilitiesBaseTemp {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name="member_id")
	private String memberId;
	@Column(name="sector_id")
	private String sectorId;
	@Column(name="currency_code")
	private String currencyCode;
	@Column(name="mcb_id")
	private String mcbId;
	@Column(name="ql_lb_type")
	private String qlLbType;
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
	private Integer workflowStatusId;
	@Column(name="remarks")
	private String remarks;
	}