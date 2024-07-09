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
@Table(name = "securities_code_temp")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaSssSecuritiesCodeTemp {
	
	@Id
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
	private Long denomination;
	@Column(name ="issuer_id" )
	private String issuerId;
	@Column(name ="ipa" )
	private String ipa;
	@Column(name ="facility_eligibility_id" )
	private String facilityEligibilityId;
	@Column(name ="haircut_id" )
	private String haircutId;
	@Column(name ="tradable" )
	private Short tradable;
	@Column(name ="first_auction_date" )
	private Date firstAuctionDate;
	@Column(name ="first_issuance_date" )
	private Integer firstIssuanceDate;
	@Column(name ="first_issuance_amount" )
	private Long firstIssuanceAmount;
	@Column(name ="redemption_date" )
	private Integer redemptionDate;
	@Column(name ="optional_redemption_date" )
	private Integer optionalRedemptionDate;
	@Column(name ="redemption_price" )
	private Double redemptionPrice;
	@Column(name ="optional_redemption_price" )
	private Double optionalRedemptionPrice;
	@Column(name ="central_bank_applied_amount" )
	private Long centralBankAppliedAmount;
	@Column(name ="coupon_structure" )
	private String couponStructure;
	@Column(name ="coupon_payment_frequency" )
	private String couponPaymentFrequency;
	@Column(name ="coupon_rate" )
	private Double couponRate;
	@Column(name ="average_yield" )
	private Double averageYield;
	@Column(name ="day_count_convention" )
	private String dayCountConvention;
	@Column(name ="rounding_option" )
	private String roundingOption;
	@Column(name ="record_date_period" )
	private Integer recordDatePeriod;
	@Column(name ="action" )
	private String action;
	@Column(name ="status" )
	private String status;
	@Column(name ="modified_by" )
	private String modifiedBy;
	@Column(name ="modified_date" )
	private Date modifiedDate;
	@Column(name ="approved_by" )
	private String approvedBy;
	@Column(name ="approved_date" )
	private Date approvedDate;
	@Column(name ="created_date" )
	private Date createdDate;
	@Column(name ="effective_date" )
	private Date effectiveDate;
	@Column(name ="approval_remark" )
	private String approvalRemark;
	@Column(name ="workflow_status_id" )
	private Integer workflowStatusId;
	@Column(name ="tenor_period" )
	private Integer tenorPeriod;
	@Column(name ="issue_price" )
	private Double issuePrice;
	@Column(name ="issue_yield" )
	private Double issueYield;
	@Column(name ="redemption_reimburse" )
	private String redemptionReimburse;
	@Column(name ="tax_scheme_id" )
	private String taxSchemeId;
	@Column(name ="first_coupon_date" )
	private Integer firstCouponDate;
	@Column(name ="benchmark_indicator" )
	private String benchmarkIndicator;
	@Column(name ="remarks" )
	private String remarks;
	
//	@OneToOne(targetEntity = JpaCouponShedules2.class)
	
//    private JpaCouponShedules2 coupon;
}