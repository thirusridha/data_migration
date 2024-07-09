package scrips.datamigration.jpa.sss.securities;
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
@Table(name = "allotment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaSssAllotment {
	
	@Id
	private String id;
	@Column(name = "securities_code")//allotment
    private String securitiesCode;
    @Column(name = "auction_date")
    private Date auctionDate;
    @Column(name = "issuance_date")
    private Integer issuanceDate;
    @Column(name = "allotment_price")
    private Double allotmentPrice;
    @Column(name = "total_nominal_amount_alloted")
    private Long totalNominalAmountAlloted;
    @Column(name = "total_nominal_amount_to_be_alloted")
    private Long totalNominalAmountToBeAlloted;
    @Column(name = "first_allotment")
    private Short firstAllotment;
    @Column(name = "total_settlement_amount")
    private Double totalSettlementAmount;
    @Column(name = "processed")
    private Short processed;
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
    @Column(name = "effective_date")
    private Date effectiveDate;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "approval_remark")
    private String approvalRemark;
    @Column(name = "workflow_status_id")
    private Integer workflowStatusId;
    @Column(name = "planned_total_nominal_amount_allotted")
    private Long plannedTotalNominalAmountAllotted;
}