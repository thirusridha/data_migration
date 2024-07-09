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
@Table(name = "allotment_source")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaSssAllotmentSource {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private String seq;
	private String id;
	@Column(name = "securities_code")
    private String securitiesCode;
    @Column(name = "auction_date")
    private String auctionDate;
    @Column(name = "issuance_date")
    private String issuanceDate;
    @Column(name = "allotment_price")
    private String allotmentPrice;
    @Column(name = "total_nominal_amount_alloted")
    private String totalNominalAmountAlloted;
    @Column(name = "total_nominal_amount_to_be_alloted")
    private String totalNominalAmountToBeAlloted;
    @Column(name = "first_allotment")
    private String firstAllotment;
    @Column(name = "total_settlement_amount")
    private String totalSettlementAmount;
    @Column(name = "processed")
    private String processed;
    @Column(name = "action")
    private String action;
    @Column(name = "status")
    private String status;
    @Column(name = "modified_by")
    private String modifiedBy;
    @Column(name = "modified_date")
    private String modifiedDate;
    @Column(name = "approved_by")
    private String approvedBy;
    @Column(name = "approved_date")
    private String approvedDate;
    @Column(name = "effective_date")
    private String effectiveDate;
    @Column(name = "created_date")
    private String createdDate;
    @Column(name = "approval_remark")
    private String approvalRemark;
    @Column(name = "workflow_status_id")
    private String workflowStatusId;
    @Column(name = "planned_total_nominal_amount_allotted")
    private String plannedTotalNominalAmountAllotted;
    private String remarks;
}
