package scrips.datamigration.jpa.sss.securities;

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
@Table(name = "securities_position_stats_temp")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaSssSecuritiesPositionStatsTemp {
	@Id
	private String id;
    @Column(name="securities_code")
    private String securitiesCode;
    @Column(name="value_date")
    private Integer valueDate;
    @Column(name="opening_nominal_amount")
    private Double openingNominalAmount;
    @Column(name="closing_nominal_amount")
    private Double closingNominalAmount;
    @Column(name="settled_count")
    private Integer settledCount;
    @Column(name="settled_amount")
    private Double settledAmount;
    @Column(name="receipt_count")
    private Integer receiptCount;
    @Column(name="receipt_amount")
    private Double receiptAmount;
    @Column(name="queued_count")
    private Integer queuedCount;
    @Column(name="queued_amount")
    private Double queuedAmount;
    @Column(name="rejected_count")
    private Integer rejectedCount;
    @Column(name="rejected_amount")
    private Double rejectedAmount;
    @Column(name="cancelled_count")
    private Integer cancelledCount;
    @Column(name="cancelled_amount")
    private Double cancelledAmount;
    @Column(name="account_id")
    private String accountId;
    @Column(name="remarks")
    private String remarks;
}
