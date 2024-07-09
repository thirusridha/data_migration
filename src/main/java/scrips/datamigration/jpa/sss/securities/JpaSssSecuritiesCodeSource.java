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
@Table(name = "securities_code_source")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaSssSecuritiesCodeSource {
	@Id
	private int seq;
	private String id;
	@Column(name ="securities_code" )
	private String securitiesCode;
	@Column(name ="issue_code" )
	private String issueCode;
	@Column(name ="description" )
	private String description;
	@Column(name ="securities_type_id" )
	private String securitiesTypeId;
	@Column(name ="securities_status" )
	private String securitiesStatus;
	@Column(name ="securities_tenor_period_unit" )
	private String securitiesTenorPeriodUnit;
//	@Column(name ="credit_rating" )
//	private String creditRating;
	@Column(name ="currency_code" )
	private String currencyCode;
	@Column(name ="denomination" )
	private String denomination;
	@Column(name ="issuer_id" )
	private String issuerId;
	@Column(name ="ipa" )
	private String ipa;
	@Column(name ="facility_eligibility_id" )
	private String facilityEligibilityId;
	@Column(name ="haircut_id" )
	private String haircutId;
	@Column(name ="tradable" )
	private String tradable;
	@Column(name ="first_auction_date" )
	private String firstAuctionDate;
	@Column(name ="first_issuance_date" )
	private String firstIssuanceDate;
	@Column(name ="first_issuance_amount" )
	private String firstIssuanceAmount;
	@Column(name ="redemption_date" )
	private String redemptionDate;
	@Column(name ="optional_redemption_date" )
	private String optionalRedemptionDate;
	@Column(name ="redemption_price" )
	private String redemptionPrice;
	@Column(name ="optional_redemption_price" )
	private String optionalRedemptionPrice;
	@Column(name ="central_bank_applied_amount" )
	private String centralBankAppliedAmount;
	@Column(name ="coupon_structure" )
	private String couponStructure;
	@Column(name ="coupon_payment_frequency" )
	private String couponPaymentFrequency;
	@Column(name ="coupon_rate" )
	private String couponRate;
	@Column(name ="average_yield" )
	private String averageYield;
	@Column(name ="day_count_convention" )
	private String dayCountConvention;
	@Column(name ="rounding_option" )
	private String roundingOption;

	@Column(name ="record_date_period" )
	private String recordDatePeriod;
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
	@Column(name ="tenor_period" )
	private String tenorPeriod;
	@Column(name ="issue_price" )
	private String issuePrice;
	@Column(name ="issue_yield" )
	private String issueYield;
	@Column(name ="redemption_reimburse" )
	private String redemptionReimburse;
	@Column(name ="tax_scheme_id" )
	private String taxSchemeId;
	@Column(name ="first_coupon_date" )
	private String firstCouponDate;
	@Column(name ="benchmark_indicator" )
	private String benchmarkIndicator;
	@Column(name ="remarks" )
	private String remarks;
}