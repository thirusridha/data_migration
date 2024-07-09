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
@Table(name = "securities_position_stats_source")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaSssSecuritiesPositionStatsSource {
	@Id
	private int seq;
	private String id;
	@Column(name = "securities_code")
	private String securitiesCode;
	@Column(name = "value_date")
	private String valueDate;
	@Column(name = "opening_nominal_amount")
	private String openingNominalAmount;
	@Column(name = "closing_nominal_amount")
	private String closingNominalAmount;
	@Column(name = "settled_count")
	private String settledCount;
	@Column(name = "settled_amount")
	private String settledAmount;
	@Column(name = "receipt_count")
	private String receiptCount;
	@Column(name = "receipt_amount")
	private String receiptAmount;
	@Column(name = "queued_count")
	private String queuedCount;
	@Column(name = "queued_amount")
	private String queuedAmount;
	@Column(name = "rejected_count")
	private String rejectedCount;
	@Column(name = "rejected_amount")
	private String rejectedAmount;
	@Column(name = "cancelled_count")
	private String cancelledCount;
	@Column(name = "cancelled_amount")
	private String cancelledAmount;
	@Column(name = "account_id")
	private String accountId;
	private String remarks;
}
