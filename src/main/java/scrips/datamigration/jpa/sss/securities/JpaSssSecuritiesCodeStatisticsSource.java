package scrips.datamigration.jpa.sss.securities;

import javax.annotation.Generated;
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
@Table(name = "securities_code_statistics_source")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaSssSecuritiesCodeStatisticsSource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer seq;
	private String id;
	@Column(name = "securities_code")
	private String securitiesCode;
	@Column(name = "total_redeemed_amount")
	private String totalRedeemedAmount;
	@Column(name = "total_nominal_amount_issued_for_erf")
	private String totalNominalAmountIssuedForErf;
	@Column(name = "total_redeemed_amount_for_erf")
	private String totalRedeemedAmountForErf;
	@Column(name = "outstanding_nominal_amount")
	private String outstandingNominalAmount;
	@Column(name = "next_coupon_date")
	private String nextCouponDate;
	@Column(name = "last_coupon_date")
	private String lastCouponDate;
	@Column(name = "next_index_end_date")
	private String nextIndexEndDate;
	@Column(name = "amount_allotted_to_central_bank")
	private String amountAllotedToCentralBank;
	@Column(name = "amount_allotted_to_others")
	private String amountAllotedToOthers;
	private String remarks;
}
