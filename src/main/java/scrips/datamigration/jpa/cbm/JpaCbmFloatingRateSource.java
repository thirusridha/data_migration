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
@Table(name="floating_rate_source")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaCbmFloatingRateSource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="seq" )
	private String seq;
	private String id;
	@Column(name="reference_rate")
	private String referenceRate;
	@Column(name="publication_date")
	private String publicationDate;
	@Column(name="value_date")
	private String valueDate;
	@Column(name="rate")
	private String rate;
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
