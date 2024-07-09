package scrips.datamigration.business;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import scrips.datamigration.common.ValidationRegex;
import scrips.datamigration.exception.FailedValidationException;
import scrips.datamigration.jpa.account.JpaAccount;
import scrips.datamigration.jpa.account.JpaAccountTemp;
import scrips.datamigration.jpa.accountposition.JpaAccountPosition;
import scrips.datamigration.jpa.accountposition.JpaAccountPositionTemp;
import scrips.datamigration.jpa.cbm.JpaCbmDepositRate;
import scrips.datamigration.jpa.cbm.JpaCbmDepositRateTemp;
import scrips.datamigration.jpa.cbm.JpaCbmFloatingRateTemp;
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBase;
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBaseDetail;
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBaseDetailTemp;
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBaseTemp;
import scrips.datamigration.jpa.cbm.JpaCbmCostCentre;
import scrips.datamigration.jpa.cbm.JpaCbmCostCentreTemp;
import scrips.datamigration.jpa.cbm.JpaCbmGlAccount;
import scrips.datamigration.jpa.cbm.JpaCbmGlAccountTemp;
import scrips.datamigration.jpa.member.JpaMember;
import scrips.datamigration.jpa.member.JpaMemberTemp;
import scrips.datamigration.jpa.sss.Member.JpaSssMember;
import scrips.datamigration.jpa.sss.account.JpaSSSAccount;
import scrips.datamigration.jpa.sss.coupon.JpaSssFloatingRates;
import scrips.datamigration.jpa.sss.coupon.JpaSssFloatingRatesTemp;
import scrips.datamigration.jpa.sss.transaction.JpaSssTransaction;
import scrips.datamigration.jpa.sss.transaction.JpaSssTransactionTemp;
import scrips.datamigration.jpa.sss.securities.JpaSssAllotment;
import scrips.datamigration.jpa.sss.securities.JpaSssAllotmentTemp;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCode;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeStatistics;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeStatisticsTemp;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeTemp;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesPositionStatsTemp;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesPrice;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesPriceTemp;

@Service
@Slf4j
public class ValidationService {

	private String regexValidator(String validatedLine, String regXPattern, String configKey) {
		if(validatedLine!=null) {
			log.info(configKey + " validating - " + validatedLine);
			Pattern pattern = Pattern.compile(regXPattern);
			Matcher matcher = pattern.matcher(validatedLine);
			boolean matches = matcher.matches();
			if (!matches) {
				log.info(validatedLine);
//				String s=",";
//					throw new FailedValidationException(configKey + "-" + validatedLine + " Failed");
				return ","+configKey;
			}
		}
		
		return "";


	}
	
	public String validationJpaAccount(JpaAccountTemp account) throws FailedValidationException {
		StringBuffer  remarks = new StringBuffer("");
		remarks.append(this.regexValidator(account.getAccountNumber(), ValidationRegex.ACCOUNT_NUMBER, "Account Number is having with special characters"));
		remarks.append(this.regexValidator(account.getDescription(), ValidationRegex.ACCOUNT_DESCRIPTION, "Description is having with special characters"));
		remarks.append(this.regexValidator(account.getMemberId(), ValidationRegex.ACCOUNT_MEMBER_ID, "Member Id"));
//		 	remarks.append(this.regexValidator(account.getIban(), ValidationRegex.ACCOUNT_IBAN,"Account Iban"));
		remarks.append(this.regexValidator(String.valueOf(account.getMainAccountIndicator()), ValidationRegex.ACCOUNT_MAIN_INDICATOR,
				"Main Account Indicator is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(account.getDefaultMainAccount()), ValidationRegex.ACCOUNT_DEFAULT_MAIN,
				"Default Main Account is having with special characters"));
		remarks.append(this.regexValidator(account.getStatementDeliveryBic(), ValidationRegex.ACCOUNT_STATEMENT_DELIVERY_BIC,
				"Statement Delivery Bic is having with special characters"));
		remarks.append(this.regexValidator(account.getAccountStatus(), ValidationRegex.ACCOUNT_STATUS, "Account Status is having with special characters"));
		remarks.append(this.regexValidator(account.getCurrencyCode(), ValidationRegex.ACCOUNT_CURRENCY_CODE, "Currency Code is having with special characters"));
		
		remarks.append(this.regexValidator(String.valueOf(account.getEndOfDayStatement()), ValidationRegex.ACCOUNT_EOD_STATEMENT,
				"End Of Day Statement is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(account.getValueDate()),
				ValidationRegex.ACCOUNT_VALUE_DATE, "Value Date is having with special characters"));
		remarks.append(this.regexValidator(new SimpleDateFormat("yyyy-MM-dd").format(account.getActivationDate()),
				ValidationRegex.ACCOUNT_ACTIVATION_DATE, "Activation Date is having with special characters"));
		remarks.append(this.regexValidator(account.getGlCategory(), ValidationRegex.ACCOUNT_GL_CATEGORY, "GL Caterogy is having with special characters"));
		remarks.append(this.regexValidator(account.getCostCentre(), ValidationRegex.ACCOUNT_COST_CENTRE, "Cost Centre is having with special characters"));
		remarks.append(this.regexValidator(account.getGlAccountNumber(), ValidationRegex.ACCOUNT_GL_ACCOUNT_NUMBER,
				"GL Account Number is having with special characters"));
		remarks.append(this.regexValidator(account.getStatementInterval(), ValidationRegex.ACCOUNT_STATEMENT_INTERVAL,
				"Statement Interval is having with special characters"));
		remarks.append(this.regexValidator(account.getAction(), ValidationRegex.ACTION, "Action"));
		remarks.append(this.regexValidator(account.getStatus(), ValidationRegex.STATUS, "Status"));
		remarks.append(this.regexValidator(account.getModifiedBy(), ValidationRegex.MODIFIED_BY, "Modified By"));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(account.getModifiedDate()),
				ValidationRegex.MODIFIED_DATE, "Modified Date"));
		remarks.append(this.regexValidator(account.getApprovedBy(), ValidationRegex.APPROVED_BY, "Approved By"));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(account.getApprovedDate()),
				ValidationRegex.APPROVED_DATE, "Approved Date"));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(account.getCreatedDate()),
				ValidationRegex.CREATED_DATE, "Created Date"));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(account.getEffectiveDate()),
				ValidationRegex.EFFECTIVE_DATE, "Effective Date"));
		remarks.append(this.regexValidator(account.getApprovalRemark(), ValidationRegex.APPROVAL_REMARK, "Approval Remark"));
		remarks.append(this.regexValidator(String.valueOf(account.getWorkflowStatusId()), ValidationRegex.WORK_FLOW_STATUS_ID,
				"Workflow Status ID"));
//		remarks.append(this.regexValidator(account.getAccountSettlementPurpose().getCurrencyCode(),
//				ValidationRegex.ACCOUNT_SETTLEMENT_PURPOSE_CURRENCY_CODE, "Settlement Purpose Currency Code"));
//		remarks.append(this.regexValidator(account.getAccountNameAddress().getNameAndAddress(), ValidationRegex.ACCOUNT_NAME_ADDRESS,
//				""));
		return remarks.toString().trim();
	}

	public String validationJpaAccountPosition(JpaAccountPositionTemp accountPosition) throws FailedValidationException {
		StringBuffer  remarks = new StringBuffer("");
		remarks.append(this.regexValidator(accountPosition.getAccountId(), ValidationRegex.ACCOUNT_POSITION_ACCOUNT_TYPE,"Account Number is having with special characters"));
		remarks.append(this.regexValidator(accountPosition.getMemberId(), ValidationRegex.ACCOUNT_POSITION_MEMBER_CODE,"Member Code is having with special characters"));
		remarks.append(this.regexValidator(accountPosition.getCurrencyCode(), ValidationRegex.ACCOUNT_POSITION_CURRENCY_CODE,"Currency Code is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(accountPosition.getOpeningAccountBalance()), ValidationRegex.ACCOUNT_POSITION_OPENING_BALANCE, "Opening Balance is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(accountPosition.getAvailableBalance()), ValidationRegex.AVAILABLE_BALANCE, "Available Balance is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(accountPosition.getCurrentAccountBalance()), ValidationRegex.ACCOUNT_BALANCE, "Current Account Balance is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(accountPosition.getQueueCount()), ValidationRegex.QUEUED_COUNT, "Queue Count is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(accountPosition.getQueueAmount()), ValidationRegex.QUEUED_AMOUNT, "Queue Amount is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(accountPosition.getSettledPaymentsCount()), ValidationRegex.SETTLED_COUNT, "Settled Payment Count is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(accountPosition.getSettledPaymentsAmount()), ValidationRegex.SETTLED_AMOUNT, "Settled Payment Amount is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(accountPosition.getSettledReceiptsCount()), ValidationRegex.SETTLED_COUNT, "Settled Receipt Count is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(accountPosition.getSettledReceiptsAmount()), ValidationRegex.SETTLED_AMOUNT, "Settled Receipt Amount is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(accountPosition.getCreatedDate()), ValidationRegex.CREATED_DATE, "Created Date should be in this format dd-MM-yyyy HH:mm:ss"));
		System.out.println("remarks -"+remarks);
		return remarks.toString().trim();
	}
	public String validationJpaMember(JpaMember member) throws FailedValidationException {
		StringBuffer remarks = new StringBuffer("");
		remarks.append(this.regexValidator(member.getId(), ValidationRegex.ID, "ID"));
		remarks.append(this.regexValidator(member.getMemberCode(), ValidationRegex.MEMBER_CODE, "Member Code"));
		remarks.append(this.regexValidator(member.getSwiftBic(), ValidationRegex.MEMBER_SWIFT_BIC, "SWIFT Bic"));
		remarks.append(this.regexValidator(String.valueOf(member.getSwiftMember()), ValidationRegex.MEMBER_SWIFT_MEMBER, "SWIFT Member"));
		remarks.append(this.regexValidator(String.valueOf(member.getBankCode()), ValidationRegex.MEMBER_BANK_CODE, "Bank Code"));
		remarks.append(this.regexValidator(member.getName(), ValidationRegex.NAME, "Member Name"));
		remarks.append(this.regexValidator(member.getShortName(), ValidationRegex.SHORT_NAME, "Member Short Name"));
		remarks.append(this.regexValidator(member.getMemberStatus(), ValidationRegex.MEMBER_STATUS, "Member Status"));
		remarks.append(this.regexValidator(member.getMemberType(), ValidationRegex.MEMBER_TYPE, "Member Type"));
		remarks.append(this.regexValidator(member.getSectorId(), ValidationRegex.MEMBER_SECTOR_ID, "Sector ID"));
		remarks.append(this.regexValidator(member.getMcbId(), ValidationRegex.MEMBER_MCB_ID, "MCB ID"));
		remarks.append(this.regexValidator(member.getUen(), ValidationRegex.MEMBER_UEN, "UEN"));
		remarks.append(this.regexValidator(member.getLei(), ValidationRegex.MEMBER_LEI, "LEI"));
		remarks.append(this.regexValidator(member.getMemberClassification(), ValidationRegex.MEMBER_CLASSIFICATION,
				"Member Classification"));
		remarks.append(this.regexValidator(String.valueOf(member.getExemptedFromBilling()), ValidationRegex.EXEMPTED_FORM_BILLING, "Exempted From Billing"));
		remarks.append(this.regexValidator(String.valueOf(member.getExemptedFromSystemLimit()), ValidationRegex.EXEMPTED_FROM_SYSTEM_LIMIT, "Exempted From System Limit"));
		remarks.append(this.regexValidator(member.getLocation(), ValidationRegex.LOCATION, "Location Code"));
		remarks.append(this.regexValidator(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(member.getActivationDate()), ValidationRegex.MEMBER_ACTIVATION_DATE, "Activation Date"));
		//remarks.append(this.regexValidator(member.getAction(), ValidationRegex.ACTION, "Action"));
		remarks.append(this.regexValidator(member.getStatus(), ValidationRegex.STATUS, "Status"));
		remarks.append(
				this.regexValidator(member.getModifiedBy(), ValidationRegex.MODIFIED_BY, "Modified By"));
		remarks.append(this.regexValidator(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(member.getModifiedDate()),
				ValidationRegex.MODIFIED_DATE, "Modified Date"));
		remarks.append(
				this.regexValidator(member.getApprovedBy(), ValidationRegex.APPROVED_BY, "Approved By"));
		remarks.append(this.regexValidator(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(member.getApprovedDate()),
				ValidationRegex.APPROVED_DATE, "Approved Date"));
		remarks.append(this.regexValidator(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(member.getCreatedDate()),
				ValidationRegex.CREATED_DATE, "Created Date"));
		remarks.append(this.regexValidator(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(member.getEffectiveDate()),
				ValidationRegex.EFFECTIVE_DATE, "Effective Date"));
//		remarks.append(this.regexValidator(member.getApprovalRemark(), ValidationRegex.APPROVAL_REMARK,
//				"Approval Remark"));
		remarks.append(this.regexValidator(String.valueOf(member.getWorkflowStatusId()),
				ValidationRegex.WORKFLOW_STATUS_ID, "Workflow Status ID"));
		remarks.append(this.regexValidator(member.getFiGroup(), ValidationRegex.FI_GROUP, "FI Group"));
		//remarks.append(this.regexValidator(member.getBillingProfileId(), ValidationRegex.BILLING_PROFILE_ID, "Billing	 Profile ID"));
		remarks.append(this.regexValidator(String.valueOf(member.getMcbIntraday()), ValidationRegex.MCB_INTRADAY, "Intraday MCB Requirement(%)"));
		remarks.append(this.regexValidator(String.valueOf(member.getMcbEodMinimum()), ValidationRegex.MCB_EOD_MINIMUM, "Minimum End-of-day MCB Requirement(%)"));
		remarks.append(this.regexValidator(String.valueOf(member.getMcbEodMaximum()), ValidationRegex.MCB_EOD_MAXIMUM, "Maximum End-of-day MCB Requirement(%)"));
		remarks.append(this.regexValidator(String.valueOf(member.getMcbAverage()), ValidationRegex.MCB_AVERAGE, "Average End-of-day MCB Requirement(%)"));
		remarks.append(this.regexValidator(member.getTaxProfileId(), ValidationRegex.TAX_PROFILE_ID, "Tax Profile ID"));
		remarks.append(this.regexValidator(member.getVerifiedBy(), ValidationRegex.VERIFIED_BY, "Verified By"));
		remarks.append(this.regexValidator(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(member.getVerifiedDate()), ValidationRegex.VERIFIED_DATE, "Verified Date"));
		return remarks.toString().trim();
	}

//	public boolean validationJpaSssCouponSchedules(JpaSssCouponSchedules ssscouponschedules)
//			throws FailedValidationException {
//		this.regexValidator(ssscouponschedules.getSecuritiesCode(), ValidationRegex.SECURITIES_CODE, "SECURITIES_CODE");
//		this.regexValidator(ssscouponschedules.getSecuriiesType(), ValidationRegex.SECURIIES_TYPE, "SECURIIES_TYPE");
//		this.regexValidator(ssscouponschedules.getIssuer(), ValidationRegex.ISSUER, "ISSUER");
//		this.regexValidator(ssscouponschedules.getCouponStructure(), ValidationRegex.COUPON_STRUCTURE,
//				"COUPON_STRUCTURE");
//		this.regexValidator(ssscouponschedules.getCouponPaymentFrequency(), ValidationRegex.COUPON_PAYMENT_FREQUENCY,
//				"COUPON_PAYMENT_FREQUENCY");
//		this.regexValidator(ssscouponschedules.getCouponDate(), ValidationRegex.COUPON_DATE, "COUPON_DATE");
//		this.regexValidator(ssscouponschedules.getCouponRate(), ValidationRegex.COUPON_RATE, "COUPON_RATE");
//		return true;
//	}

	

	public boolean validationJpaSSSAccount(JpaSSSAccount account) throws FailedValidationException {
		this.regexValidator(account.getAccountId(), ValidationRegex.ACCOUNT_ID, "ACCOUNT_ID");
		this.regexValidator(account.getMemberId(), ValidationRegex.ACCOUNT_MEMBER_CODE, "ACCOUNT_MEMBER_CODE");
		this.regexValidator(account.getCustodyAccountTypeId(), ValidationRegex.ACCOUNT_CUSTODY_ACCOUNT_TYPE_ID,
				"ACCOUNT_CUSTODY_ACCOUNT_TYPE_ID");
//		this.regexValidator(account.getAccountCategory(), ValidationRegex.ACCOUNT_CATEGORY, "ACCOUNT_CATEGORY");
//		this.regexValidator(account.getInvestorIndividualId(), ValidationRegex.ACCOUNT_INVESTOR_INDIVIDUAL_ID,"ACCOUNT_INVESTOR_INDIVIDUAL_ID");
//		this.regexValidator(account.getInvestorName(), ValidationRegex.ACCOUNT_INVESTOR_NAME, "ACCOUNT_INVESTOR_NAME");
		this.regexValidator(account.getAccountStatus(), ValidationRegex.ACCOUNT_STATUS, "ACCOUNT_STATUS");
		this.regexValidator(account.getTaxProfileId(), ValidationRegex.ACCOUNT_TAX_PROFILE_ID,
				"ACCOUNT_TAX_PROFILE_ID");
		this.regexValidator(account.getStatementDeliveryBic(), ValidationRegex.ACCOUNT_STATEMENT_DELIVERY_BIC,
				"ACCOUNT_STATEMENT_DELIVERY_BIC");
		return true;
	}

	public String validationJpaSssAllotment(JpaSssAllotmentTemp securityPos) {
		StringBuffer remarks = new StringBuffer("");
		remarks.append(this.regexValidator(securityPos.getSecuritiesCode(), ValidationRegex.SECURITIES_CODE, "Securities Code is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(securityPos.getAuctionDate())), ValidationRegex.AUCTION_DATE, "Auction Date is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getIssuanceDate()), ValidationRegex.ISSUANCE_DATE,
				"Issuance Date "));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getAllotmentPrice()), ValidationRegex.ALLOTMENT_PRICE,
				"Allotment Price"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getTotalNominalAmountAlloted()),
				ValidationRegex.TOTAL_NOMINAL_AMOUNT_ALLOTED, "Total Nominal Amount "));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getTotalNominalAmountToBeAlloted()),
				ValidationRegex.TOTAL_NOMINAL_AMOUNT_TO_BE_ALLOTED, "Total Nominal Amount To Be Alloted"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getFirstAllotment()), ValidationRegex.FIRST_ALLOTMENT,
				"First Allotment is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getTotalSettlementAmount()),
				ValidationRegex.TOTAL_SETTLEMENT_AMOUNT, "Total Settlement Amount"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getProcessed()), ValidationRegex.PROCESSED, "Processed"));
		remarks.append(this.regexValidator(securityPos.getAction(), ValidationRegex.ACTION, "Action"));
		remarks.append(this.regexValidator(securityPos.getStatus(), ValidationRegex.STATUS, "Status"));
		remarks.append(this.regexValidator(securityPos.getModifiedBy(), ValidationRegex.MODIFIED_BY, "Modified By"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getModifiedDate()), ValidationRegex.MODIFIED_DATE,
				"Modified Date"));
		remarks.append(this.regexValidator(securityPos.getApprovedBy(), ValidationRegex.APPROVED_BY, "Approved By"));
		remarks.append(this.regexValidator(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(securityPos.getApprovedDate()),
				ValidationRegex.APPROVED_DATE, "Approved Date"));
		remarks.append(this.regexValidator(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(securityPos.getEffectiveDate()),
				ValidationRegex.EFFECTIVE_DATE, "Effective Date"));
		remarks.append(this.regexValidator(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(securityPos.getCreatedDate()),
				ValidationRegex.CREATED_DATE, "Created Date"));
		remarks.append(this.regexValidator(securityPos.getApprovalRemark(), ValidationRegex.APPROVAL_REMARK, "Approval Remark"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getWorkflowStatusId()), ValidationRegex.WORK_FLOW_STATUS_ID,
				"Workflow Status Id"));
//		remarks.append(this.regexValidator(String.valueOf(securityPos.getPlannedTotalNominalAmountAllotted()),
//				ValidationRegex.PLANNED_TOTAL_NOMINAL_AMOUNT_ALLOTTED, "Planned Total Nominal Amount Allotted"));

		return remarks.toString().trim();
	}

	public String validationJpaSSSTransaction(JpaSssTransactionTemp ssstransaction) {
		StringBuffer remarks = new StringBuffer("");
		remarks.append(this.regexValidator(ssstransaction.getSssReference(), ValidationRegex.SSS_REFERENCE, "Sss Reference is having with special characters"));
		// remarks.append(this.regexValidator(ssstransaction.getMessageLogId(),ValidationRegex.MESSAGE_LOG_ID,"Message Log ID is having with special characters"));
		remarks.append(this.regexValidator(ssstransaction.getSecuritiesCode(), ValidationRegex.SECURITIES_CODE, "Securities Code is having with special characters"));
		remarks.append(this.regexValidator(ssstransaction.getTransactionType(), ValidationRegex.TRANSACTION_TYPE, "Transaction Type is having with special characters"));
		remarks.append(this.regexValidator(ssstransaction.getStatus(), ValidationRegex.STATUS, "Status is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(ssstransaction.getSettlementDate()), ValidationRegex.SETTLEMENT_DATE,
				"Settlement Date is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(ssstransaction.getTradeDate()), ValidationRegex.TRADE_DATE, "Trade Date is having with special characters"));
		remarks.append(this.regexValidator(ssstransaction.getCurrencyCode(), ValidationRegex.CURRENCY_CODE, "Currency Code is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(ssstransaction.getSettlementAmount()), ValidationRegex.SETTLEMENT_AMOUNT,
				"Settlement Amount is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(ssstransaction.getNominalAmount()), ValidationRegex.NOMINAL_AMOUNT,
				"Nominal Amount is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(ssstransaction.getDealPrice()), ValidationRegex.DEAL_PRICE, "Deal Price is having with special characters"));
		remarks.append(this.regexValidator(ssstransaction.getDelivererMemberCode(), ValidationRegex.DELIVERER_MEMBER_CODE,
				"Deliverer Member ID is having with special characters"));
		remarks.append(this.regexValidator(ssstransaction.getReceiverMemberCode(), ValidationRegex.RECEIVER_MEMBER_CODE,
				"Receiver Mmeber ID is having with special characters"));
		// remarks.append(this.regexValidator(String.valueOf(ssstransaction.getRepoClosingDate()),ValidationRegex.REPO_CLOSING_DATE,"Repo Closing Date is having with special characters"));
		// remarks.append(this.regexValidator(String.valueOf(ssstransaction.getRepoClosingSettlementAmount()),ValidationRegex.REPO_CLOSING_SETTLEMENT_AMOUNT,"Repo Closing Settlement Amount is having with special characters"));
		// remarks.append(this.regexValidator(ssstransaction.getReasonCode(),ValidationRegex.REASON_CODE,"Reason Code is having with special characters"));
		//remarks.append(this.regexValidator(ssstransaction.getTransactionReference(),ValidationRegex.TRANSACTION_REFERENCE,"Transaction Reference is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(ssstransaction.getProcessedDate()), ValidationRegex.PROCESSED_DATE,
				"Processed Date is having with special characters "));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd/MM/yyyy").format(ssstransaction.getCreatedDate()),
				ValidationRegex.CREATED_DATE, "Created Date should be in this format dd-MM-yyyy HH:mm:ss"));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd/MM/yyyy").format(ssstransaction.getModifiedDate()),
				ValidationRegex.MODIFIED_DATE, "Modified Date should be in this format dd-MM-yyyy HH:mm:ss"));
//		remarks.append(this.regexValidator(ssstransaction.getTransactionReceivedDate(),ValidationRegex.TRANSACTION_RECEIVED_DATE,"Transaction Received Date is having with special characters"));
//		remarks.append(this.regexValidator(ssstransaction.getSenderMemberSwiftCode(), ValidationRegex.SENDER_SWIFT_MEMBER_CODE,"Sender Member Swift Code is having with special characters"));
//		remarks.append(this.regexValidator(ssstransaction.getPayerMemberSwiftCode(), ValidationRegex.PAYER_SWIFT_MEMBER_CODE,"Payer Member Swift Code is having with special characters"));
//		remarks.append(this.regexValidator(ssstransaction.getPayeeMemberSwiftCode(), ValidationRegex.PAYEE_SWIFT_MEMBER_CODE,"Payee Member Swift Code is having with special characters"));
//		remarks.append(this.regexValidator(ssstransaction.getReceiverMemberSwiftCode(), ValidationRegex.RECEIVER_SWIFT_MEMBER_CODE,"Receiver Member Swift Code is having with special characters"));
//		remarks.append(this.regexValidator(ssstransaction.getSenderMemberCode(), ValidationRegex.SENDER_MEMBER_CODE,"Sender Member Code is having with special characters"));
//		remarks.append(this.regexValidator(ssstransaction.getSenderType(), ValidationRegex.SENDER_TYPE,"Sender Type is having with special characters"));
//		remarks.append(this.regexValidator(ssstransaction.getMessageType(), ValidationRegex.MESSAGE_TYPE,"Message Type is having with special characters"));
		remarks.append(this.regexValidator(ssstransaction.getReceiverRtgsAccount(), ValidationRegex.RECEIVER_RTGS_ACCOUNT,
				"Receiver Rtgs Account is having with special characters"));
		remarks.append(this.regexValidator(ssstransaction.getDelivererRtgsAccount(), ValidationRegex.DELIVERER_RTGS_ACCOUNT,
				"Deliverer Rtgs Account is having with special characters"));
		remarks.append(this.regexValidator(ssstransaction.getReceiverRtgsMemberCode(), ValidationRegex.RECEIVER_RTGS_MEMBER_CODE,
				"Receiver Rtgs Member Code  is having with special characters"));
		remarks.append(this.regexValidator(ssstransaction.getDelivererRtgsMemberCode(), ValidationRegex.DELIVERER_RTGS_MEMBER_CODE,
				"Deliverer Rtgs Member Code  is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(ssstransaction.getHoldIndicator()), ValidationRegex.HOLD_INDICATOR,
				"Hold Indicator is having with special characters"));
		remarks.append(this.regexValidator(ssstransaction.getPaymentType(), ValidationRegex.PAYMENT_TYPE, "Payment Type is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(ssstransaction.getGridLockIndicator()), ValidationRegex.GRID_LOCK_INDICATOR,
				"Grid Lock Indicator is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(ssstransaction.getRolloverCount()), ValidationRegex.ROLLOVER_COUNT,
				"Rollover Count is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(ssstransaction.getPendingCancellationIndicator()),
				ValidationRegex.PENDING_CANCELLATION_INDICATOR, "Pending Cancellation Indicator is having with special characters"));

		//remarks.append(this.regexValidator(ssstransaction.getDelivererMemberId(), ValidationRegex.DELIVERER_MEMBER_ID,"Deliverer Member ID is having with special characters"));
	    //remarks.append(this.regexValidator(ssstransaction.getReceiverMemberId(), ValidationRegex.RECEIVER_MEMBER_ID,"Receiver Member ID is having with special characters"));
		//remarks.append(this.regexValidator(ssstransaction.getReceiverAccountId(), ValidationRegex.RECEIVER_ACCOUNT_ID,"Receiver Account ID is having with special characters"));
		//remarks.append(this.regexValidator(ssstransaction.getDelivererAccountId(),ValidationRegex.DELIVERER_ACCOUNT_ID,"Deliverer Account ID is having with special characters"));
		// remarks.append(this.regexValidator(ssstransaction.getSenderMemberId(),ValidationRegex.SENDER_MEMBER_ID,"Sender Member ID  is having with special characters "));
		remarks.append(this.regexValidator(String.valueOf(ssstransaction.getPdmIndicator()), ValidationRegex.PDM_INDICATOR,
				"Pdm Indicator is having with special characters"));
		// remarks.append(this.regexValidator(ssstransaction.getErfReference(), ValidationRegex.ERF_REFERENCE,"Erf Reference is having with special characters"));
		remarks.append(this.regexValidator(ssstransaction.getDelivererAccountNo(), ValidationRegex.DELIVERER_ACCOUNT_NO,
				"Deliverer Account No is having with special characters"));
		// remarks.append(this.regexValidator(ssstransaction.getReceiverAccountNo(),ValidationRegex.RECEIVER_ACCOUNT_NO,"Receiver Account NO is having with special characters"));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd/MM/yyyy").format(ssstransaction.getMsgReceivedTimestamp()),
				ValidationRegex.MSG_RECEIVED_TIMESTAMP, "Msg Received Timstamp is having with special characters"));
		remarks.append(this.regexValidator(ssstransaction.getReceiverSenderId(), ValidationRegex.RECEIVER_SENDER_ID,
				"Receiver Sender ID is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(ssstransaction.getDelivererOnbehalf()), ValidationRegex.DELIVERER_ON_BEHALF,
				"Deliverer On behalf is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(ssstransaction.getReceiverOnbehalf()), ValidationRegex.RECEIVER_ON_BEHALF,
				"Receiver On behalf is having with special characters"));
		remarks.append(this.regexValidator(ssstransaction.getProcessFlag(), ValidationRegex.PROCESS_FLAG, "Process Flag is having with special characters"));
		// remarks.append(this.regexValidator(ssstransaction.getDelivererBeneficiaryAccount(),ValidationRegex.DELIVERER_BENEFICIARY_ACCOUNT,"Deliverer Beneficiary Account is having with special characters"));
		// remarks.append(this.regexValidator(ssstransaction.getUnderlyingId(), ValidationRegex.UNDERLYING_ID,"Underlying ID is having with special characters"));
		// remarks.append(this.regexValidator(ssstransaction.getBalanceType(),ValidationRegex.BALANCE_TYPE,"Balance Type is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(ssstransaction.getAccuredInterestAmount()),
				ValidationRegex.ACCRUED_INTEREST_AMOUNT, "Accured Interest Amount is having with special characters"));
		remarks.append(this.regexValidator(ssstransaction.getDelivererChannel(), ValidationRegex.DELIVERER_CHANNEL,
				"Deliverer Channel is having with special characters"));
		remarks.append(this.regexValidator(ssstransaction.getReceiverChannel(), ValidationRegex.RECEIVER_CHANNEL, "Receiver Channel is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(ssstransaction.getRepoRate()), ValidationRegex.REPO_RATE, "Repo Rate is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(ssstransaction.getHaircutRate()), ValidationRegex.HAIRCUT_RATE,
				"Haircut Rate is having with special characters"));
		// remarks.append(this.regexValidator(ssstransaction.getOriginalAccountNo(),ValidationRegex.ORIGINAL_ACCOUNT_NO,"Original Account NO is having with special characters"));

		System.out.println("remarks -" + remarks);
		return remarks.toString().trim();
	}


	public String validationJpaSssSeurityPos(JpaSssSecuritiesPositionStatsTemp securityPos) {
		StringBuffer remarks = new StringBuffer("");
		remarks.append(this.regexValidator(securityPos.getAccountId(), ValidationRegex.ACCOUNT_ID, "Account ID"));
		remarks.append(this.regexValidator(securityPos.getSecuritiesCode(), ValidationRegex.SECURITIES_CODE, "Securities Code is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getValueDate()), ValidationRegex.VALUE_DATE, "Value Date"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getOpeningNominalAmount()),
				ValidationRegex.OPENING_NOMINAL_AMOUNT, "Opening Nominal Amount"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getClosingNominalAmount()),
				ValidationRegex.CLOSING_NOMINAL_AMOUNT, "Closing Nominal Amount"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getSettledCount()), ValidationRegex.SETTLED_COUNT,
				"Settled Count"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getSettledAmount()), ValidationRegex.SETTLED_AMOUNT,
				"Settled Amount"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getReceiptCount()), ValidationRegex.RECEIPT_COUNT,
				"Receipt Count"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getReceiptAmount()), ValidationRegex.RECEIPT_AMOUNT,
				"Receipt Amount"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getQueuedCount()), ValidationRegex.QUEUED_COUNT, "Queued Count"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getQueuedAmount()), ValidationRegex.QUEUED_AMOUNT,
				"Queued Amount"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getRejectedCount()), ValidationRegex.REJECTED_COUNT,
				"Rejected Count"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getRejectedAmount()), ValidationRegex.REJECTED_AMOUNT,
				"Rejected Amount"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getCancelledCount()), ValidationRegex.CANCELLED_COUNT,
				"Cancelled Count"));
		remarks.append(this.regexValidator(String.valueOf(securityPos.getCancelledAmount()), ValidationRegex.CANCELLED_AMOUNT,
				"Cancelled Amount"));
		return remarks.toString().trim(); 
	}

	public String validationJpaSecurityPrice(JpaSssSecuritiesPriceTemp securityPrice) {
		StringBuffer remarks =new StringBuffer("");
		remarks.append(this.regexValidator(securityPrice.getSecuritiesCode(), ValidationRegex.SECURITIES_CODE, "Securities Code is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(securityPrice.getPricingDate()), ValidationRegex.PRICING_DATE, "Pricing Date is having with special characters"));
		remarks.append(this.regexValidator(securityPrice.getPricingType(), ValidationRegex.PRICING_TYPE, "Pricing Type is having with special characters"));
		remarks.append(this.regexValidator(securityPrice.getDescription(), ValidationRegex.DESCRIPTION,
				"Securities Description"));
		remarks.append(this.regexValidator(String.valueOf(securityPrice.getPrice()), ValidationRegex.PRICE, "Price"));
		remarks.append(this.regexValidator(securityPrice.getIssueCode(), ValidationRegex.ISSUE_CODE, "Issue Code is having with special characters"));
		// remarks.append(this.regexValidator(securityPrice.getAction(),
				// ValidationRegex.ACTION, "Action"));
				remarks.append(this.regexValidator(securityPrice.getStatus(), ValidationRegex.STATUS, "Status"));
				remarks.append(
						this.regexValidator(securityPrice.getModifiedBy(), ValidationRegex.MODIFIED_BY, "Modified By"));
				remarks.append(this.regexValidator(
						new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(securityPrice.getModifiedDate()),
						ValidationRegex.MODIFIED_DATE, "Modified Date"));
				remarks.append(
						this.regexValidator(securityPrice.getApprovedBy(), ValidationRegex.APPROVED_BY, "Approved By"));
				remarks.append(this.regexValidator(
						new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(securityPrice.getApprovedDate()),
						ValidationRegex.APPROVED_DATE, "Approved Date"));
				remarks.append(this.regexValidator(
						new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(securityPrice.getCreatedDate()),
						ValidationRegex.CREATED_DATE, "Created Date"));
				remarks.append(this.regexValidator(
						new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(securityPrice.getEffectiveDate()),
						ValidationRegex.EFFECTIVE_DATE, "Effective Date"));
				// remarks.append(this.regexValidator(securityPrice.getApprovalRemark(),
				// ValidationRegex.APPROVAL_REMARK, "Approval Remark"));
				remarks.append(this.regexValidator(String.valueOf(securityPrice.getWorkflowStatusId()),
						ValidationRegex.WORK_FLOW_STATUS_ID, "Workflow Status ID"));
		return remarks.toString().trim(); 
	}

	
	public String validationJpaSSSSecuritiesCode(JpaSssSecuritiesCode securitiesCode) {
		StringBuffer remarks =new StringBuffer("");
		remarks.append(regexValidator(securitiesCode.getSecuritiesCode(), ValidationRegex.SECURITIES_CODE, "Securities Code is having with special characters"));
		remarks.append(this.regexValidator(securitiesCode.getIssueCode(), ValidationRegex.ISSUE_CODE, "Issue code is having with special characters"));
		remarks.append(this.regexValidator(securitiesCode.getDescription(), ValidationRegex.DESCRIPTION, "Description is having with special characters"));
		remarks.append(this.regexValidator(securitiesCode.getSecuritiesTypeId(), ValidationRegex.SECURITIES_TYPE_ID,
				"Securities Type Id is having with special characters"));
		remarks.append(this.regexValidator(securitiesCode.getSecuritiesTenorPeriodUnit(),
				ValidationRegex.SECURITIES_TENOR_PERIOD_UNIT, "Securities Tenor Period Unit is having with special characters"));
//		remarks.append(this.regexValidator(securitiesCode.getCreditRating(), ValidationRegex.CREDIT_RATING, "Credit Rating"));
		remarks.append(this.regexValidator(securitiesCode.getCurrencyCode(), ValidationRegex.CURRENCY_CODE, "Currency Code is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getDenomination()), ValidationRegex.DENOMINATION,
				"Denomination is having with special characters"));
		remarks.append(this.regexValidator(securitiesCode.getIssuerId(), ValidationRegex.ISSUER_ID, "Issuer Id is having with special characters"));
		remarks.append(this.regexValidator(securitiesCode.getIpa(), ValidationRegex.IPA, "Ipa"));
//	     this.regexValidator(sssSecuritiesCode.getFacilityEligibilityId(), ValidationRegex.FACILITY_ELIGIBILITY_ID,"FACILITY_ELIGIBILITY_ID");
//	     this.regexValidator(sssSecuritiesCode.getHaircutId(), ValidationRegex.HAIRCUT_ID,"HAIRCUT_ID");
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getTradable()), ValidationRegex.TRADABLE, "Tradable"));
		remarks.append(this.regexValidator((new SimpleDateFormat("yyyy-MM-dd").format(securitiesCode.getFirstAuctionDate())), ValidationRegex.AUCTION_DATE, "Auction Date"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getFirstIssuanceDate()), ValidationRegex.ISSUANCE_DATE,
				"Issuence Date"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getFirstIssuanceAmount()),
				ValidationRegex.FIRST_ISSUANCE_AMOUNT, "First Issuence Amount"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getRedemptionDate()), ValidationRegex.REDEMPTION_DATE, "Redemption Date"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getOptionalRedemptionDate()), ValidationRegex.OPTIONAL_REDEMPTION_DATE,
				"Optional Redemption Date"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getRedemptionPrice()), ValidationRegex.REDEMPTION_PRICE,
				"Redemption Price"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getOptionalRedemptionPrice()), ValidationRegex.OPTIONAL_REDEMPTION_PRICE,
				"Optional Redemptional Price"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getCentralBankAppliedAmount()),
				ValidationRegex.CENTRAL_BANK_APPLIED_AMOUNT, "Central Bank Applied Amount"));
		remarks.append(this.regexValidator(securitiesCode.getCouponStructure(), ValidationRegex.COUPON_STRUCTURE,
				"Coupon Structure"));
		remarks.append(this.regexValidator(securitiesCode.getCouponPaymentFrequency(), ValidationRegex.COUPON_PAYMENT_FREQUENCY,
				"Coupon Payment Frequancy"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getCouponRate()), ValidationRegex.COUPON_RATE,
				"Coupon Rate"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getAverageYield()), ValidationRegex.AVERAGE_YIELD, "Average Yield"));
		remarks.append(this.regexValidator(securitiesCode.getDayCountConvention(), ValidationRegex.DAY_COUNT_CONVENTION,
				"Day Count Convenstion"));
		remarks.append(this.regexValidator(securitiesCode.getRoundingOption(), ValidationRegex.ROUNDING_OPTION, "Rounding Option"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getRecordDatePeriod()), ValidationRegex.RECORD_DATE_PERIOD,
				"Record Date Period"));
		remarks.append(this.regexValidator(securitiesCode.getAction(), ValidationRegex.ACTION, "Action"));
		remarks.append(this.regexValidator(securitiesCode.getStatus(), ValidationRegex.STATUS, "Status"));
		remarks.append(this.regexValidator(securitiesCode.getModifiedBy(), ValidationRegex.MODIFIED_BY, "Modified By"));
		remarks.append(this.regexValidator(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(securitiesCode.getModifiedDate()),
				ValidationRegex.MODIFIED_DATE, "Modified Date"));
		remarks.append(this.regexValidator(securitiesCode.getApprovedBy(), ValidationRegex.APPROVED_BY, "Approved By"));
		remarks.append(this.regexValidator(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(securitiesCode.getApprovedDate()),
				ValidationRegex.APPROVED_DATE, "Approved Date"));
		remarks.append(this.regexValidator(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(securitiesCode.getCreatedDate()),
				ValidationRegex.CREATED_DATE, "Created Date"));
		remarks.append(this.regexValidator(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(securitiesCode.getEffectiveDate()),
				ValidationRegex.EFFECTIVE_DATE, "Effective Date"));
		remarks.append(this.regexValidator(securitiesCode.getApprovalRemark(), ValidationRegex.APPROVAL_REMARK, "Approval Remark"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getWorkflowStatusId()), ValidationRegex.WORKFLOW_STATUS_ID,
				"Workflow Status Id"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getTenorPeriod()), ValidationRegex.TENOR_PERIOD,
				"Tenor Period"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getIssuePrice()), ValidationRegex.ISSUE_PRICE,
				"Issue Price"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getIssueYield()), ValidationRegex.ISSUE_YIELD,
				"Issue Yield"));
		remarks.append(this.regexValidator(securitiesCode.getRedemptionReimburse(), ValidationRegex.REDEMPTION_REIMBURSE,
				"Redemption Reimburse"));
		remarks.append(this.regexValidator(securitiesCode.getTaxSchemeId(), ValidationRegex.TAX_SCHEME_ID, "Tax Scheme Id"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCode.getFirstCouponDate()), ValidationRegex.FIRST_COUPON_DATE,
				"First Coupon Date"));
		remarks.append(this.regexValidator(securitiesCode.getBenchmarkIndicator(), ValidationRegex.BENCHMARK_INDICATOR,
				"Benchmark Indicator"));
		System.out.println("remarks -"+remarks);
		

		return remarks.toString().trim();
	}
	public String validationJpaSSSSecuritiesCodeStatistics(JpaSssSecuritiesCodeStatisticsTemp securitiesCodeTemp) {
		StringBuffer remarks =new StringBuffer("");
		
		remarks.append(this.regexValidator(securitiesCodeTemp.getSecuritiesCode(), ValidationRegex.SECURITIES_CODE,
				"Securities Code"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCodeTemp.getTotalRedeemedAmount()), ValidationRegex.TOTAL_REDEEMED_AMOUNT,
				"Total Redeemed Amount"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCodeTemp.getTotalNominalAmountIssuedForErf()),
				ValidationRegex.TOTAL_NOMINAL_AMOUNT_ISSUED_FOR_ERF, "Total Nominal Amount Issued For Erf"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCodeTemp.getOutstandingNominalAmount()),
				ValidationRegex.OUSTANDING_NOMINAL_AMOUNT, "Outstanding Nominal Amount"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCodeTemp.getNextCouponDate()), ValidationRegex.NEXT_COUPON_DATE,
				"Next Coupon Date"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCodeTemp.getLastCouponDate()), ValidationRegex.LAST_COUPON_DATE,
				"Last Coupon Date"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCodeTemp.getNextIndexEndDate()), ValidationRegex.NEXT_INDEX_END_DATE,
				"Next Index End Date"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCodeTemp.getAmountAllotedToCentralBank()),
				ValidationRegex.AMOUNT_ALLOTED_TO_CENTRAL_BANK, "Amount Alloted To The Central Bank"));
		remarks.append(this.regexValidator(String.valueOf(securitiesCodeTemp.getAmountAllotedToOthers()),
				ValidationRegex.AMOUNT_ALLOTED_TO_OTHERS, "Amount Alloted To Others"));
		return remarks.toString().trim();
	}

	public String validationJpaCbmDepositRate(JpaCbmDepositRateTemp cbmDepositRates) {
		StringBuffer remarks = new StringBuffer("");
		remarks.append(this.regexValidator(String.valueOf(cbmDepositRates.getTenorPeriod()), ValidationRegex.TENOR_PERIOD,
				"Tenor Period is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(cbmDepositRates.getDepositRate()), ValidationRegex.DEPOSIT_RATE,
				"Invalid Deposit Rate"));
		remarks.append(this.regexValidator(cbmDepositRates.getFacilityId(), ValidationRegex.FACILITY_ID, "Facility ID"));
		remarks.append(this.regexValidator(cbmDepositRates.getAction(), ValidationRegex.ACTION, "Action"));
		remarks.append(this.regexValidator(cbmDepositRates.getStatus(), ValidationRegex.STATUS, "Status"));
		remarks.append(this.regexValidator(cbmDepositRates.getModifiedBy(), ValidationRegex.MODIFIED_BY, "Modified By"));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cbmDepositRates.getModifiedDate()),
				ValidationRegex.MODIFIED_DATE, "Modified Date"));
		remarks.append(this.regexValidator(cbmDepositRates.getApprovedBy(), ValidationRegex.APPROVED_BY, "Approved By"));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cbmDepositRates.getApprovedDate()),
				ValidationRegex.APPROVED_DATE, "Approved Date"));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cbmDepositRates.getCreatedDate()),
				ValidationRegex.CREATED_DATE, "Created Date"));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cbmDepositRates.getEffectiveDate()),
				ValidationRegex.EFFECTIVE_DATE, "Effective Date"));
		remarks.append(this.regexValidator(cbmDepositRates.getApprovalRemark(), ValidationRegex.APPROVAL_REMARK, "Approval Remark"));
		remarks.append(this.regexValidator(String.valueOf(cbmDepositRates.getWorkflowStatusId()), ValidationRegex.WORK_FLOW_STATUS_ID,
				"Workflow Status ID"));
		return remarks.toString().trim(); 
	}

	public String validationJpaCbmLiabilitiesBase(JpaCbmLiabilitiesBaseTemp cbmLiabilitiesBase) {
		StringBuffer remarks = new StringBuffer("");
		remarks.append(this.regexValidator(cbmLiabilitiesBase.getMemberId(), ValidationRegex.MEMBER_ID, "Member ID is having with special characters"));
		remarks.append(this.regexValidator(cbmLiabilitiesBase.getSectorId(), ValidationRegex.SECTOR_ID, "Sector ID"));
		remarks.append(this.regexValidator(cbmLiabilitiesBase.getCurrencyCode(), ValidationRegex.CURRENCY_CODE, "Currency Code is having with special characters"));
		remarks.append(this.regexValidator(cbmLiabilitiesBase.getMcbId(), ValidationRegex.MCB_ID, "Mcb ID"));
		remarks.append(this.regexValidator(cbmLiabilitiesBase.getQlLbType(), ValidationRegex.QL_LB_TYPE, "QL/LB Type is having with special characters"));
		remarks.append(this.regexValidator(cbmLiabilitiesBase.getAction(), ValidationRegex.ACTION, "Action"));
		remarks.append(this.regexValidator(cbmLiabilitiesBase.getStatus(), ValidationRegex.STATUS, "Status"));
		remarks.append(this.regexValidator(cbmLiabilitiesBase.getModifiedBy(), ValidationRegex.MODIFIED_BY, "Modified By"));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cbmLiabilitiesBase.getModifiedDate()),
				ValidationRegex.MODIFIED_DATE, "Modified Date"));
		remarks.append(this.regexValidator(cbmLiabilitiesBase.getApprovedBy(), ValidationRegex.APPROVED_BY, "Approved By"));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cbmLiabilitiesBase.getApprovedDate()),
				ValidationRegex.APPROVED_DATE, "Approved Date"));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cbmLiabilitiesBase.getCreatedDate()),
				ValidationRegex.CREATED_DATE, "Created Date"));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cbmLiabilitiesBase.getEffectiveDate()),
				ValidationRegex.EFFECTIVE_DATE, "Effective Date should be in this format dd-MM-yyyy HH:mm:ss"));
		remarks.append(this.regexValidator(cbmLiabilitiesBase.getApprovalRemark(), ValidationRegex.APPROVAL_REMARK, "Approval Remark"));
		remarks.append(this.regexValidator(String.valueOf(cbmLiabilitiesBase.getWorkflowStatusId()), ValidationRegex.WORK_FLOW_STATUS_ID,
				"Workflow Status ID"));
		return remarks.toString().trim(); 
	}

	public String validationJpaCbmLiabilitiesBaseDetail(JpaCbmLiabilitiesBaseDetailTemp cbmLiabilitiesBaseDetails) {
		StringBuffer remarks = new StringBuffer("");
		remarks.append(this.regexValidator(String.valueOf(cbmLiabilitiesBaseDetails.getId()), ValidationRegex.ID, "ID "));
		remarks.append(this.regexValidator(cbmLiabilitiesBaseDetails.getMemberId(), ValidationRegex.MEMBER_ID, "Member ID is having with special characters"));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cbmLiabilitiesBaseDetails.getStartDate()),
				ValidationRegex.START_DATE, "Start Date should be in this format dd-MM-yyyy HH:mm:ss"));
		remarks.append(this.regexValidator(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cbmLiabilitiesBaseDetails.getEndDate()),
				ValidationRegex.END_DATE, "End Date should be in this format dd-MM-yyyy HH:mm:ss"));
		remarks.append(this.regexValidator(String.valueOf(cbmLiabilitiesBaseDetails.getQlLb()), ValidationRegex.QL_LB, "QL/LB is having with special characters"));
		return remarks.toString().trim(); 
	}

	public boolean validationJpaSSSMember(JpaSssMember sssmember) {

//		this.regexValidator(sssmember.getMemberCode(), ValidationRegex.MEMBER_CODE,"MEMBER_CODE");
//		this.regexValidator(sssmember.getMemberName(), ValidationRegex.MEMBER_NAME,"MEMBER_NAME");
//		this.regexValidator(sssmember.getActivationDate(), ValidationRegex.ACTIVATION_DATE,"ACTIVATION_DATE");
//		this.regexValidator(sssmember.getMemberShortName(), ValidationRegex.MEMBER_SHORT_NAME,"MEMBER_SHORT_NAME");
//		this.regexValidator(sssmember.getMemberStatus(), ValidationRegex.MEMBER_STATUS,"MEMBER_STATUS");
//		this.regexValidator(String.valueOf(sssmember.getMemberType()), ValidationRegex.MEMBER_TYPE,"MEMBER_TYPE");
//		this.regexValidator(sssmember.getSectorId(), ValidationRegex.SECTOR_ID,"SECTOR_ID");
//		this.regexValidator(sssmember.getBankCode(), ValidationRegex.BANK_CODE,"BANK_CODE");
//		this.regexValidator(sssmember.getAutoDebitIndicator(), ValidationRegex.AUTO_DEBIT_INDICATOR,"AUTO_DEBIT_INDICATOR");
//		this.regexValidator(sssmember.getLocation(), ValidationRegex.LOCATION,"LOCATION");
//		this.regexValidator(sssmember.getFacilityEligibilityId(), ValidationRegex.FACILITY_ELIGIBILITY_ID,"FACILITY_ELIGIBILITY_ID");
//		this.regexValidator(sssmember.getLei(), ValidationRegex.LEI,"LEI");
//		this.regexValidator(sssmember.getUen(), ValidationRegex.UEN,"UEN");
//		this.regexValidator(sssmember.getZeroHoldingsStatement(), ValidationRegex.ZERO_HOLDINGS_STATEMENT,"ZERO_HOLDINGS_STATEMENT");
//		this.regexValidator(sssmember.getExemptedFromBilling(), ValidationRegex.EXEMPTED_FROM_BILLING,"EXEMPTED_FROM_BILLING");
//		this.regexValidator(sssmember.getExemptedFromSystemLimit(), ValidationRegex.EXEMPTED_FROM_SYSTEM_LIMIT,"EXEMPTED_FROM_SYSTEM_LIMIT");
//		this.regexValidator(sssmember.getStatementDeliveryBic(), ValidationRegex.STATEMENT_DELIVERY_BIC,"STATEMENT_DELIVERY_BIC");
//		this.regexValidator(sssmember.getTaxProfileId(), ValidationRegex.TAX_PROFILE_ID,"TAX_PROFILE_ID");
//		this.regexValidator(sssmember.getFeeProfileId(), ValidationRegex.FEE_PROFILE_ID,"FEE_PROFILE_ID");
//		this.regexValidator(sssmember.getEndOfDayStatement(), ValidationRegex.END_OF_DAY_STATEMENT,"END_OF_DAY_STATEMENT");

		return true;
	}

	public String validationCbmCostCenterTemp(JpaCbmCostCentreTemp cbmCostCentre) {
		StringBuffer remarks = new StringBuffer("");
		remarks.append(this.regexValidator(cbmCostCentre.getCostCentre(), ValidationRegex.COST_CENTRE, "Cost Centre is having with special characters"));
	remarks.append(this.regexValidator(cbmCostCentre.getDescription(), ValidationRegex.DESCRIPTION, "Description is having with special characters"));
		remarks.append(
				this.regexValidator(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cbmCostCentre.getCreatedDate()),
						ValidationRegex.CREATED_DATE, "Created Date"));
		remarks.append(
				this.regexValidator(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cbmCostCentre.getModifiedDate()),
						ValidationRegex.MODIFIED_DATE, "Modified Date"));
		return remarks.toString().trim();
	}

	public String validationCbmGlAccount(JpaCbmGlAccountTemp cbmGlAccount) {
		StringBuffer remarks = new StringBuffer("");
		remarks.append(this.regexValidator(cbmGlAccount.getGlAccountIndicator(), ValidationRegex.GL_ACCOUNT_INDICATOR, "GL_ACCOUNT_INDICATOR is having with special characters"));
		remarks.append(this.regexValidator(cbmGlAccount.getGlAccount(), ValidationRegex.GL_ACCOUNT, "GL_ACCOUNT is having with special characters"));
		remarks.append(this.regexValidator(cbmGlAccount.getGlAccountDescription(), ValidationRegex.GL_ACCOUNT_DESCRIPTION,
				"GL_ACCOUNT_DESCRIPTION is having with special characters"));
		remarks.append(this.regexValidator(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cbmGlAccount.getCreatedDate()),
				ValidationRegex.CREATED_DATE, "CREATED_DATE"));
		remarks.append(this.regexValidator(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cbmGlAccount.getModifiedDate()),
				ValidationRegex.MODIFIED_DATE, "MODIFIED_DATE"));
		return remarks.toString().trim();
	}

	public String validationJpaCbmFloatingRate(JpaCbmFloatingRateTemp cbmFloatingRatesTemp) {
		StringBuffer remarks = new StringBuffer("");
		remarks.append(this.regexValidator(String.valueOf(cbmFloatingRatesTemp.getReferenceRate()), ValidationRegex.REFERENCE_RATE,
				"Reference Rate is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(cbmFloatingRatesTemp.getPublicationDate()),
				ValidationRegex.PUBLICATION_DATE, "Publication Date is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(cbmFloatingRatesTemp.getValueDate()),
				ValidationRegex.VALUE_DATE, "Value Date is having with special characters"));
		remarks.append(
				this.regexValidator(String.valueOf(cbmFloatingRatesTemp.getRate()), ValidationRegex.RATE, "Rate is having with special characters"));
		 remarks.append(this.regexValidator(cbmFloatingRatesTemp.getAction(),
		 ValidationRegex.ACTION, "Action"));
		remarks.append(this.regexValidator(cbmFloatingRatesTemp.getStatus(), ValidationRegex.STATUS, "Status"));
		remarks.append(
				this.regexValidator(cbmFloatingRatesTemp.getModifiedBy(), ValidationRegex.MODIFIED_BY, "Modified By"));
		remarks.append(this.regexValidator(
				new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cbmFloatingRatesTemp.getModifiedDate()),
				ValidationRegex.MODIFIED_DATE, "Modified Date"));
		remarks.append(
				this.regexValidator(cbmFloatingRatesTemp.getApprovedBy(), ValidationRegex.APPROVED_BY, "Approved By"));
		remarks.append(this.regexValidator(
				new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cbmFloatingRatesTemp.getApprovedDate()),
				ValidationRegex.APPROVED_DATE, "Approved Date"));
		remarks.append(this.regexValidator(
				new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cbmFloatingRatesTemp.getCreatedDate()),
				ValidationRegex.CREATED_DATE, "Created Date"));
		remarks.append(this.regexValidator(
				new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cbmFloatingRatesTemp.getEffectiveDate()),
				ValidationRegex.EFFECTIVE_DATE, "Effective Date"));
		 remarks.append(this.regexValidator(cbmFloatingRatesTemp.getApprovalRemark(),
		 ValidationRegex.APPROVAL_REMARK, "Approval Remark"));
		remarks.append(this.regexValidator(String.valueOf(cbmFloatingRatesTemp.getWorkflowStatusId()),
				ValidationRegex.WORK_FLOW_STATUS_ID, "Workflow Status ID"));
		return remarks.toString().trim();
	}
	
	public String validationJpaSssFloatingRate(JpaSssFloatingRatesTemp sssFloatingRatesTemp) {
		StringBuffer remarks = new StringBuffer("");
		remarks.append(this.regexValidator(String.valueOf(sssFloatingRatesTemp.getReferenceRate()), ValidationRegex.REFERENCE_RATE,
				"Reference Rate is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(sssFloatingRatesTemp.getPublicationDate()),
				ValidationRegex.PUBLICATION_DATE, "Publication Date is having with special characters"));
		remarks.append(this.regexValidator(String.valueOf(sssFloatingRatesTemp.getValueDate()),
				ValidationRegex.VALUE_DATE, "Value Date is having with special characters"));
		remarks.append(
				this.regexValidator(String.valueOf(sssFloatingRatesTemp.getRate()), ValidationRegex.RATE, "Rate is having with special characters"));
		// remarks.append(this.regexValidator(sssFloatingRatesTemp.getAction(),
		// ValidationRegex.ACTION, "Action"));
		remarks.append(this.regexValidator(sssFloatingRatesTemp.getStatus(), ValidationRegex.STATUS, "Status"));
		remarks.append(
				this.regexValidator(sssFloatingRatesTemp.getModifiedBy(), ValidationRegex.MODIFIED_BY, "Modified By"));
		remarks.append(this.regexValidator(
				new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(sssFloatingRatesTemp.getModifiedDate()),
				ValidationRegex.MODIFIED_DATE, "Modified Date"));
		remarks.append(
				this.regexValidator(sssFloatingRatesTemp.getApprovedBy(), ValidationRegex.APPROVED_BY, "Approved By"));
		remarks.append(this.regexValidator(
				new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(sssFloatingRatesTemp.getApprovedDate()),
				ValidationRegex.APPROVED_DATE, "Approved Date"));
		remarks.append(this.regexValidator(
				new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(sssFloatingRatesTemp.getCreatedDate()),
				ValidationRegex.CREATED_DATE, "Created Date"));
		remarks.append(this.regexValidator(
				new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(sssFloatingRatesTemp.getEffectiveDate()),
				ValidationRegex.EFFECTIVE_DATE, "Effective Date"));
		// remarks.append(this.regexValidator(sssFloatingRatesTemp.getApprovalRemark(),
		// ValidationRegex.APPROVAL_REMARK, "Approval Remark"));
		remarks.append(this.regexValidator(String.valueOf(sssFloatingRatesTemp.getWorkflowStatusId()),
				ValidationRegex.WORK_FLOW_STATUS_ID, "Workflow Status ID"));
		return remarks.toString().trim();
	}

}
