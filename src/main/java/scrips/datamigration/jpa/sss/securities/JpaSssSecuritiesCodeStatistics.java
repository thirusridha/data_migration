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
@Table(name = "securities_code_statistics")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaSssSecuritiesCodeStatistics {
	@Id
	@Column(name = "securities_code")
	private String securitiesCode;
	@Column(name = "total_redeemed_amount")
	private Long totalRedeemedAmount;
	@Column(name = "total_nominal_amount_issued_for_erf")
	private Long totalNominalAmountIssuedForErf;
	@Column(name = "total_redeemed_amount_for_erf")
	private Long totalRedeemedAmountForErf;
	@Column(name = "outstanding_nominal_amount")
	private Long outstandingNominalAmount;
	@Column(name = "next_coupon_date")
	private Integer nextCouponDate;
	@Column(name = "last_coupon_date")
	private Integer lastCouponDate;
	@Column(name = "next_index_end_date")
	private Integer nextIndexEndDate;
	@Column(name = "amount_allotted_to_central_bank")
	private Long amountAllotedToCentralBank;
	@Column(name = "amount_allotted_to_others")
	private Long amountAllotedToOthers;

}
