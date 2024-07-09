package scrips.datamigration.jpa.sss.coupon;

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
@Table(name = "floating_rate_temp")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaSssFloatingRatesTemp {
	@Id
	private String id;
	@Column(name="reference_rate")
	private String referenceRate;
	@Column(name="publication_date")
	private Integer publicationDate;
	@Column(name="value_date")
	private Integer valueDate;
	@Column(name="rate")
	private Double rate;
	@Column(name = "action")
	private String action;
	@Column(name = "status")
	private String status;
	@Column(name = "modified_by")
	private String modifiedBy;
	@Column(name = "modified_date")
	private Date modifiedDate;
	@Column(name = "approved_by")
	private String approvedBy;
	@Column(name = "approved_date")
	private Date approvedDate;
	@Column(name = "created_date")
	private Date createdDate;
	@Column(name = "effective_date")
	private Date effectiveDate;
	@Column(name = "approval_remark")
	private String approvalRemark;
	@Column(name = "workflow_status_id")
	private Integer workflowStatusId;
	@Column(name = "remarks")
	private String remarks;
}