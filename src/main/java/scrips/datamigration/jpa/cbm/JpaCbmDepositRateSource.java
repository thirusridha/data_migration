package scrips.datamigration.jpa.cbm;

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
@Table(name="cbm_deposit_rate_source")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaCbmDepositRateSource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="seq" )
	private String seq;
	private String id;
	@Column(name = "tenor_period")
	private String tenorPeriod;
	@Column(name = "deposit_rate")
	private String depositRate;
	@Column(name = "facility_id")
	private String facilityId;
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
	@Column(name ="remarks" )
	private String remarks;
	
}