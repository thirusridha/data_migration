package scrips.datamigration.common;

public class ValidationRegex {

	//ACCOUNT
	public static final String ACCOUNT_NUMBER = "[0-9]{1,34}";
	public static final String ACCOUNT_DESCRIPTION = "[a-zA-Z0-9_ ]{1,35}";
	public static final String ACCOUNT_MEMBER_ID = "[a-zA-Z0-9_-]{1,36}";
	//public static final String ACCOUNT_IBAN = ".{13}";
	public static final String ACCOUNT_TYPE = "[a-zA-Z0-9_ ]{1,100}";
	public static final String ACCOUNT_MAIN_INDICATOR = "[a-zA-Z0-9_ ]{1,100}";
	public static final String ACCOUNT_DEFAULT_MAIN = "[a-zA-Z0-9_ ]{1,100}";
	public static final String ACCOUNT_STATEMENT_DELIVERY_BIC = "[a-zA-Z0-9_ ]{1,100}";
	public static final String ACCOUNT_STATUS = "[a-zA-Z0-9_ ]{1,100}";
	public static final String ACCOUNT_CURRENCY_CODE = "[a-zA-Z0-9_ ]{1,100}";
	public static final String ACCOUNT_MINIMUM_BALANCE = "[a-zA-Z0-9_ ]{1,10}";
	public static final String ACCOUNT_EOD_STATEMENT = "[a-zA-Z0-9_ ]{1,12}";
//	public static final String ACCOUNT_VALUE_DATE = "^(0?[1-9]|[12][0-9]|3[01])[\\/\\-](0?[1-9]|1[012])[\\/\\-]\\d{4}$";
	public static final String ACCOUNT_VALUE_DATE ="[0-9:-]{1,25}";
//	public static final String ACCOUNT_ACTIVATION_DATE = "^(0?[1-9]|[12][0-9]|3[01])[\\/\\-](0?[1-9]|1[012])[\\/\\-]\\d{4}$";
	public static final String ACCOUNT_ACTIVATION_DATE ="[a-zA-Z0-9_-]{1,25}";
	public static final String ACCOUNT_GL_CATEGORY = "[a-zA-Z0-9_ ]{1,100}";
	public static final String ACCOUNT_COST_CENTRE = "[a-zA-Z0-9_ ]{1,100}";
	public static final String ACCOUNT_GL_ACCOUNT_NUMBER = "[a-zA-Z0-9_ ]{1,100}";
	public static final String ACCOUNT_STATEMENT_INTERVAL = "[a-zA-Z0-9_ ]{1,100}";
	public static final String ACCOUNT_SETTLEMENT_PURPOSE_TRANSACTION_ID = "[a-zA-Z0-9_ ]{1,36}";
	public static final String ACCOUNT_SETTLEMENT_PURPOSE_CURRENCY_CODE = "[a-zA-Z0-9_ ]{1,255}";
	public static final String ACCOUNT_SETTLEMENT_PURPOSE_ACCOUNT_ID = "[a-zA-Z0-9_ ]{1,13}";
	public static final String ACCOUNT_NAME_ADDRESS_ACCOUNT_ID = "[a-zA-Z0-9_ ]{13}";
	public static final String ACCOUNT_NAME_ADDRESS = "([a-zA-Z0-9 .,-@&$]*)";
	
	//ACCOUNT POSITION
	public static final String ACCOUNT_POSITION_ACCOUNT_TYPE = "[a-zA-Z0-9_\\s ]{1,100}";
	public static final String ACCOUNT_POSITION_ACCOUNT_NUMBER = "[a-zA-Z0-9_]{1,100}";
	public static final String ACCOUNT_POSITION_MEMBER_CODE = "([a-zA-Z0-9 .,-@&$]*)";
	public static final String ACCOUNT_POSITION_MEMBER_NAME = "[a-zA-Z0-9_ ]{1,100}";
	public static final String ACCOUNT_POSITION_CURRENCY_CODE = "[a-zA-Z0-9_ ]{1,100}";
	public static final String ACCOUNT_POSITION_VALUE_DATE = "^(0?[1-9]|[12][0-9]|3[01])[\\/\\-](0?[1-9]|1[012])[\\/\\-]\\d{4}$";
	public static final String ACCOUNT_POSITION_OPENING_BALANCE = "[0-9.]{1,100}";
	public static final String ACCOUNT_POSITION_DEBIT = "[a-zA-Z0-9_]{1,100}";
	public static final String ACCOUNT_POSITION_CREDIT = "[a-zA-Z0-9_]{1,100}";
	public static final String ACCOUNT_POSITION_TOTAL = "[a-zA-Z0-9_]{1,100}";
	public static final String ACCOUNT_POSITION_CURRENT_BALANCE = "[a-zA-Z0-9_]{1,100}";
	
	//MEMBER
	public static final String MEMBER_CODE = "[a-zA-Z0-9\\s-]{1,8}";
	public static final String NAME = "[a-zA-Z0-9\\s().]{1,100}";
	public static final String SHORT_NAME = "[a-zA-Z0-9\\s]{1,30}";
	public static final String MEMBER_TYPE = "[a-zA-Z0-9\\s]{1,3}";
	public static final String MEMBER_STATUS = "[a-zA-Z0-9\\s]{1,3}";
	public static final String MEMBER_ACTIVATION_DATE = "[a-zA-Z0-9_:/\\s-]{1,100}";
	public static final String MEMBER_SWIFT_MEMBER = "[a-zA-Z0-9\\s]{1,36}";
	public static final String MEMBER_SWIFT_BIC = "[a-zA-Z0-9\\s]{1,8}";
	public static final String MEMBER_LOCATION_CODE = "[a-zA-Z0-9\\s]{1,3}";
	public static final String MEMBER_FL_GROUP = "[a-zA-Z0-9\\s]{1,1}";
	public static final String MEMBER_BANK_CODE = "[a-zA-Z0-9\\s]{1,4}";
	public static final String MEMBER_SECTOR_ID = "[a-zA-Z0-9\\s]{1,30}";
	public static final String MEMBR_TAX_RATE = "[a-zA-Z0-9\\s]{1,100}";
	public static final String MEMBER_FEE_PROFILE_ID = "[a-zA-Z0-9\\s]{1,100}";
	public static final String MEMBER_MCB_ID = "[a-zA-Z0-9\\s]{1,100}";
	public static final String MEMBER_UEN = "[a-zA-Z0-9\\s]{1,10}";
	public static final String MEMBER_LEI = "[a-zA-Z0-9\\s]{1,20}";
	
	//SSSAccount
	public static final String ACCOUNT_ID ="[a-zA-Z0-9_-]{1,36}";
	public static final String ACCOUNT_MEMBER_CODE = "[a-zA-Z0-9_]{1,100}";
	public static final String ACCOUNT_CUSTODY_ACCOUNT_TYPE_ID =  "[a-zA-Z0-9_\\s-]{1,100}";
	public static final String ACCOUNT_CATEGORY ="[a-zA-Z0-9_]{1,100}";
	public static final String ACCOUNT_INVESTOR_INDIVIDUAL_ID ="[a-zA-Z0-9_]{1,100}";
	public static final String ACCOUNT_INVESTOR_NAME = "[a-zA-Z0-9_]{1,100}";
	//public static final String ACCOUNT_STATUS1 ="[a-zA-Z0-9_]{1,100}";
	public static final String ACCOUNT_TAX_PROFILE_ID = "[a-zA-Z0-9_]{1,100}";
	//public static final String ACCOUNT_STATEMENT_DELIVERY_BIC1 ="[a-zA-Z0-9_]{1,100}";
	
	
	//SSSTransaction
	public static final String SSS_REFERENCE ="[a-zA-Z0-9_-]{1,100}";
	public static final String MESSAGE_LOG_ID = "[a-zA-Z0-9_]{1,100}";
	public static final String SECURITIES_CODE = "[a-zA-Z0-9_]{1,100}";
	public static final String TRANSACTION_TYPE ="[a-zA-Z0-9_]{1,100}";
	public static final String STATUS = "[a-zA-Z0-9_]{1,100}";
	public static final String SETTLEMENT_DATE = "[a-zA-Z0-9_-]{1,100}";
	public static final String TRADE_DATE = "[a-zA-Z0-9_-]{1,100}";
	public static final String CURRENCY_CODE = "[a-zA-Z0-9_]{1,100}";
	public static final String SETTLEMENT_AMOUNT = "[a-zA-Z0-9_.]{1,100}";
	public static final String NOMINAL_AMOUNT = "[a-zA-Z0-9_.]{1,100}";
	public static final String DEAL_PRICE = "[a-zA-Z0-9_.]{1,100}";
	public static final String DELIVERER_MEMBER_CODE ="[a-zA-Z0-9_-]{1,100}";
	public static final String RECEIVER_MEMBER_CODE = "[a-zA-Z0-9_-]{1,100}";
	public static final String REPO_CLOSING_DATE = "[a-zA-Z0-9_-]{1,100}";
	public static final String REPO_CLOSING_SETTLEMENT_AMOUNT = "[a-zA-Z0-9_.]{1,100}";
	public static final String REASON_CODE = "[a-zA-Z0-9_]{1,100}";

	public static final String TRANSACTION_REFERENCE = "[a-zA-Z0-9_]{1,100}";
	public static final String PROCESSED_DATE = "[a-zA-Z0-9_-]{1,100}";
	public static final String CREATED_DATE = "[a-zA-Z0-9_:/\\s-]{1,100}";
	public static final String TRANSACTION_RECEIVED_DATE = "[a-zA-Z0-9_-]{1,100}";
	public static final String SENDER_SWIFT_MEMBER_CODE ="[a-zA-Z0-9_-]{1,100}";
	public static final String PAYER_SWIFT_MEMBER_CODE = "[a-zA-Z0-9_-]{1,100}";
	public static final String PAYEE_SWIFT_MEMBER_CODE = "[a-zA-Z0-9_-]{1,100}";
	public static final String RECEIVER_SWIFT_MEMBER_CODE ="[a-zA-Z0-9_-]{1,100}";
	public static final String SENDER_MEMBER_CODE = "[a-zA-Z0-9_-]{1,100}";

	public static final String SENDER_TYPE = "[a-zA-Z0-9_]{1,100}";
	public static final String MESSAGE_TYPE = "[a-zA-Z0-9_.//s]{1,100}";
	public static final String DELIVERER_RTGS_ACCOUNT = "[a-zA-Z0-9_\\s-]{1,100}";
	public static final String RECEIVER_RTGS_ACCOUNT = "[a-zA-Z0-9_\\s-]{1,100}";
	public static final String RECEIVER_RTGS_MEMBER_CODE = "[a-zA-Z0-9_-]{1,100}";
	public static final String DELIVERER_RTGS_MEMBER_CODE = "[a-zA-Z0-9_-]{1,100}";
	public static final String HOLD_INDICATOR = "[a-zA-Z0-9_]{1,100}";
	public static final String PAYMENT_TYPE = "[a-zA-Z0-9_\\s]{1,100}";
	public static final String GRID_LOCK_INDICATOR = "[a-zA-Z0-9_]{1,100}";
	public static final String ROLLOVER_COUNT = "[a-zA-Z0-9_]{1,100}";
	public static final String PENDING_CANCELLATION_INDICATOR = "[a-zA-Z0-9_]{1,100}";
	public static final String DELIVERER_MEMBER_ID= "[a-zA-Z0-9_-]{1,100}";
	public static final String RECEIVER_MEMBER_ID= "[a-zA-Z0-9_-]{1,100}";
	public static final String RECEIVER_ACCOUNT_ID ="[a-zA-Z0-9_-]{1,100}";
	public static final String DELIVERER_ACCOUNT_ID = "[a-zA-Z0-9_-]{1,100}";
	public static final String SENDER_MEMBER_ID= "[a-zA-Z0-9_-]{1,100}";
	public static final String PDM_INDICATOR= "[a-zA-Z0-9_]{1,100}";
	public static final String ERF_REFERENCE= "[a-zA-Z0-9_\\s]{1,100}";
	public static final String DELIVERER_ACCOUNT_NO= "[a-zA-Z0-9_-]{1,100}";
	public static final String RECEIVER_ACCOUNT_NO= "[a-zA-Z0-9_-]{1,100}";
	public static final String MSG_RECEIVED_TIMESTAMP= "[a-zA-Z0-9_/:-]{1,100}";
	public static final String RECEIVER_SENDER_ID= "[a-zA-Z0-9_]{1,100}";
	public static final String DELIVERER_ON_BEHALF= "[a-zA-Z0-9_]{1,100}";
	public static final String RECEIVER_ON_BEHALF= "[a-zA-Z0-9_]{1,100}";
	public static final String PROCESS_FLAG= "[a-zA-Z0-9_]{1,100}";
	public static final String DELIVERER_BENEFICIARY_ACCOUNT= "[a-zA-Z0-9_]{1,100}";
	public static final String UNDERLYING_ID= "[a-zA-Z0-9_\\s-]{1,100}";
	public static final String BALANCE_TYPE= "[a-zA-Z0-9_]{1,100}";
	public static final String ACCRUED_INTEREST_AMOUNT = "[a-zA-Z0-9_.]{1,100}";
	public static final String DELIVERER_CHANNEL = "[a-zA-Z0-9_]{1,100}";
	public static final String RECEIVER_CHANNEL = "[a-zA-Z0-9_]{1,100}";
	public static final String REPO_RATE= "[a-zA-Z0-9_.]{1,100}";
	public static final String HAIRCUT_RATE= "[a-zA-Z0-9_.]{1,100}";
	public static final String ORIGINAL_ACCOUNT_NO= "[a-zA-Z0-9_]{1,100}";
	//SSS ALLOTMENT
	//public static final String SECURITIES_CODE = "[a-zA-Z0-9_-]{1,100}";
	public static final String ISSUE_CODE = "[a-zA-Z0-9_]{1,100}";
	public static final String ALLOTMENT_DATE = "[a-zA-Z0-9_-]{1,100}";
	public static final String ISSUANCE_DATE = "[a-zA-Z0-9_-]{1,100}";
	public static final String TOTAL_NOMINAL_AMOUNT = "[a-zA-Z0-9_]{1,100}";
	public static final String TOTAL_SETTLEMENT_AMOUNT = "[a-zA-Z0-9_.]{1,100}";
	public static final String TOTAL_ALLOTMENT_FROM_AUCTION_RESULT = "[a-zA-Z0-9_]{1,100}";
	public static final String FIRST_ALLOTMENT = "[a-zA-Z0-9_]{1,100}";
//	public static final String ACCOUNT_ID ="[a-zA-Z0-9_]{1,100}";
	public static final String ALLOTMENT_PRICE = "[a-zA-Z0-9_.]{1,100}";
	//public static final String NOMINAL_AMOUNT = "[a-zA-Z0-9_]{1,100}";

	public static final String ALLOTMENT_STATUS = "[a-zA-Z0-9_]{1,100}";
	public static final String SECURITIES_OPENING_ID ="[a-zA-Z0-9_]{1,100}";
	//SSS SECURITIES POSITION STATS

	public static final String VALUE_DATE = "[a-zA-Z0-9_:/\\s-]{1,100}";
	public static final String CLOSING_NOMINAL_AMOUNT = "[a-zA-Z0-9_.]{1,100}";
	public static final String SETTLED_COUNT = "[a-zA-Z0-9_]{1,100}";
	public static final String SETTLED_AMOUNT = "[a-zA-Z0-9_.]{1,100}";
	public static final String RECEIPT_COUNT = "[a-zA-Z0-9_]{1,100}";
	public static final String RECEIPT_AMOUNT = "[a-zA-Z0-9_.]{1,100}";
	public static final String QUEUED_COUNT = "[a-zA-Z0-9_]{1,100}";
	public static final String QUEUED_AMOUNT = "[a-zA-Z0-9_.]{1,100}";
	public static final String REJECTED_COUNT = "[a-zA-Z0-9_]{1,100}";
	public static final String CANCELLED_COUNT = "[a-zA-Z0-9_]{1,100}";
	public static final String CANCELLED_AMOUNT = "[a-zA-Z0-9_.]{1,100}";
	public static final String REJECTED_AMOUNT = "[a-zA-Z0-9_.]{1,100}";
	public static final String OPENING_NOMINAL_AMOUNT = "[a-zA-Z0-9_.]{1,100}";
	public static final String SECURITIES_TYPE="[a-zA-Z0-9_-]{1,100}";
//=======
//	public static final String VALUE_DATE = "[a-zA-Z0-9_-]{1,100}";
//	public static final String CLOSING_NOMINAL_AMOUNT = "[a-zA-Z0-9_]{1,100}";
//	public static final String SETTLED_COUNT = "[a-zA-Z0-9_]{1,100}";
//	public static final String SETTLED_AMOUNT = "[a-zA-Z0-9_]{1,100}";
//	public static final String RECEIPT_COUNT = "[a-zA-Z0-9_]{1,100}";
//	public static final String RECEIPT_AMOUNT = "[a-zA-Z0-9_]{1,100}";
//	public static final String QUEUED_COUNT = "[a-zA-Z0-9_]{1,100}";
//	public static final String QUEUED_AMOUNT = "[a-zA-Z0-9_]{1,100}";
//	public static final String REJECTED_COUNT = "[a-zA-Z0-9_]{1,100}";
//	public static final String COLLECTED_COUNT = "[a-zA-Z0-9_]{1,100}";
//	public static final String REJECTED_AMOUNT = "[a-zA-Z0-9_]{1,100}";
//	public static final String OPENING_NOMINAL_AMOUNT = "[a-zA-Z0-9_]{1,100}";
//>>>>>>> f5cbe987242c4d82db2bd5f1779748aaec86b752
//	
	//SSS SECURITIES PRICE
	public static final String PRICING_DATE = "[a-zA-Z0-9_\\s-]{1,100}";
	public static final String PRICING_TYPE = "[a-zA-Z0-9_\\s]{1,36}";
	public static final String PRICE = "[a-zA-Z0-9_.]{1,100}";
	public static final String SECURITIES_CODE1 = "[a-zA-Z0-9_-]{1,36}";

	//SSS_SECURITIES_CODE_STATISTICS
	public static final String TOTAL_NOMINAL_AMOUNT_ISSUED ="[a-zA-Z0-9_]{1,100}";
	public static final String TOTAL_NOMINAL_AMOUNT_ISSUED_FOR_ERF ="[a-zA-Z0-9_]{1,100}";
	public static final String TOTAL_REDEEMED_AMOUNT = "[a-zA-Z0-9_]{1,100}";
	public static final String OUSTANDING_NOMINAL_AMOUNT ="[a-zA-Z0-9_]{1,100}";
	public static final String NEXT_COUPON_DATE = "[a-zA-Z0-9_-]{1,100}";
	public static final String LAST_COUPON_DATE = "[a-zA-Z0-9_-]{1,100}";
	public static final String NEXT_INDEX_END_DATE = "[a-zA-Z0-9_-]{1,100}";
	public static final String AMOUNT_ALLOTED_TO_CENTRAL_BANK = "[a-zA-Z0-9_]{1,100}";
	public static final String AMOUNT_ALLOTED_TO_OTHERS ="[a-zA-Z0-9_]{1,100}";
	
	//JPA_CBM_DEPOSIT_RATE
	public static final String DEPOSIT_RATE ="[0-9.]{1,8}";
	public static final String FACILITY_ID ="[a-zA-Z0-9_-]{1,36}";
	public static final String ACTION = "[a-zA-Z0-9_\\s]{1,100}";
	public static final String MODIFIED_BY = "[a-zA-Z0-9_-]{1,36}";
	public static final String MODIFIED_DATE = "[a-zA-Z0-9_:/\\s-]{1,100}";
	public static final String APPROVED_BY ="[a-zA-Z0-9_-]{1,36}";
	public static final String APPROVED_DATE ="[a-zA-Z0-9_:\\s-]{1,100}";
	public static final String EFFECTIVE_DATE ="[a-zA-Z0-9_:\\s-]{1,100}";
	public static final String APPROVAL_REMARK ="[a-zA-Z0-9_\\s-]{1,140}";
	public static final String WORK_FLOW_STATUS_ID = "[a-zA-Z0-9_]{1,100}";
	
	
	//JPA_CBM_LIABILILTIES_BASE  &  JPA_CBM_LIABILILTIES_BASE_DETAIL
	public static final String MEMBER_ID = "[a-zA-Z0-9_-]{1,36}";
	public static final String SECTOR_ID = "[a-zA-Z0-9\\s]{1,10}";
	public static final String MCB_ID = "[a-zA-Z0-9\\s]{1,10}";
	public static final String QL_LB_TYPE = "[a-zA-Z0-9\\s]{1,4}";
	public static final String START_DATE = "[a-zA-Z0-9_:/\\s-]{1,100}";
	public static final String END_DATE = "[a-zA-Z0-9_:/\\s-]{1,100}";
	public static final String QL_LB = "[0-9.]{1,23}";


	//CBM COST CENTRE
	public static final String DESCRIPTION = "[a-zA-Z0-9_,\\s-]{1,50}";
	
	
	public static final String REPO_CLOSING_SETTLEMENT_AMOUONT ="[0-9]{1,23}([.][0-9]{1,5})?";
	public static final String MATCHED_SSS_REFERENCE = "[a-zA-Z0-9_-]{1,36}";
	public static final String SENDER_REFERENCE = "[a-zA-Z0-9_-]{1,35}";
//	public static final String MESSAGE_TYPE = "[a-zA-Z0-9_.-]{1,26}";
//	public static final String SENDER_MEMBER_ID = "[a-zA-Z0-9_-]{1,45}";
//	public static final String PDM_INDICATOR = "[0-9]{1,2}";
//	public static final String DELIVERER_ACCOUNT_NO = "[a-zA-Z0-9_-]{1,35}";
//	public static final String RECEIVER_ACCOUNT_NO = "[a-zA-Z0-9_-]{1,35}";
//	public static final String MSG_RECEIVED_TIMESTAMP = "[a-zA-Z0-9_ -:]{1,100}";
	public static final String SUBMITTED_CLO_SETTLE_AMOUNT = "[0-9]{1,23}([.][0-9]{1,5})?";
//	public static final String DELIVERER_BENEFICIARY_ACCOUNT = "[a-zA-Z0-9_-]{1,36}";
	public static final String ACCURED_INTEREST_AMOUNT = "[0-9]{1,23}([.][0-9]{1,5})?";
	public static final String SETTLEMENT_AMOUNT2 = "[0-9]{1,23}([.][0-9]{1,5})?";
	public static final String NOMINAL_AMOUNT2 = "[0-9]{1,23}([.][0-9]{1,5})?";
	public static final String DEAL_PRICE2 = "[0-9]{1,23}([.][0-9]{1,5})?";
	
	//CBM GL ACCOUNT	
	public static final String GL_ACCOUNT_INDICATOR = "[a-zA-Z0-9_\\s-]{1,14}";
	public static final String GL_ACCOUNT = "[a-zA-Z0-9_\\s-]{1,50}";
	public static final String GL_ACCOUNT_DESCRIPTION = "[a-zA-Z0-9_\\s-]{1,50}";

	//SSS COUPON SHEDULES
	public static final String ISSUER = "[a-zA-Z0-9_\\s-]{1,100}";
	public static final String COUPON_STRUCTURE = "[a-zA-Z0-9_\\s-]{1,100}";
	public static final String COUPON_PAYMENT_FREQUENCY = "[a-zA-Z0-9_\\s-]{1,100}";
	public static final String COUPON_DATE = "[a-zA-Z0-9_\\s-]{1,100}";
	public static final String COUPON_RATE = "[a-zA-Z0-9_\\s.-]{1,100}";
	//SSS FLOATING RATES
	public static final String RATE_INDEX = "[a-zA-Z0-9_\\s.-]{1,100}";
	public static final String PUBLICATION_DATE = "[a-zA-Z0-9_:/\\s-]{1,100}";
//	public static final String RATE = "[a-zA-Z0-9_\\s.-]{1,100}";
	
	//SSS MEMBER
	public static final String ACTIVATION_DATE = "[a-zA-Z0-9_\\s.-]{1,100}";
	//public static final String SECTOR_ID = "[a-zA-Z0-9_\\s.-]{1,100}";
	public static final String BANK_CODE = "[a-zA-Z0-9_\\s.-]{1,100}";
	public static final String AUTO_DEBIT_INDICATOR = "[a-zA-Z0-9_\\s.-]{1,100}";
	public static final String LOCATION = "[a-zA-Z0-9_\\s,.-]{1,100}";
//	public static final String FACILITY_ELIGIBILITY_ID = "[a-zA-Z0-9_\\s.null-]{1,100}";
	public static final String LEI = "[a-zA-Z0-9_\\s.-]{1,100}";
	public static final String UEN = "[a-zA-Z0-9_\\s.-]{1,100}";
	public static final String ZERO_HOLDINGS_STATEMENT = "[a-zA-Z0-9_\\s.-]{1,100}";
	public static final String EXEMPTED_FROM_BILLING = "[a-zA-Z0-9_\\s.-]{1,100}";
	public static final String EXEMPTED_FROM_SYSTEM_LIMIT = "[a-zA-Z0-9_\\s.-]{1,100}";
	public static final String STATEMENT_DELIVERY_BIC = "[a-zA-Z0-9_\\s.-]{1,100}";
	public static final String TAX_PROFILE_ID = "[a-zA-Z0-9_\\s.-]{1,100}";
	public static final String END_OF_DAY_STATEMENT = "[a-zA-Z0-9_\\s.-]{1,100}";
	public static final String FEE_PROFILE_ID = "[a-zA-Z0-9_\\s.-]{1,100}";
	
	//Securities Code
	public static final String ISSUER_CODE =  "[a-zA-Z0-9_-]{1,100}";
    public static final String SECURITIES_STATUS =  "[a-zA-Z0-9_]{1,100}";
    public static final String TENOR_PERIOD_UNIT = "[a-zA-Z0-9]{1,100}";
    public static final String DENOMINATION =  "[a-zA-Z0-9]{1,100}";
    public static final String IPA =  "[a-zA-Z0-9_\\s-]{1,100}";
    public static final String PRIMARY_REGISTRY_ID =  "[a-zA-Z0-9_-]{1,100}";
//    public static final String HAIRCUT_ID = "[a-zA-Z0-9_]{1,100}";
    public static final String TRADABLE =  "[a-zA-Z0-9_]{1,100}";
    public static final String AUTOMATIC_EVENT_GENERATION =  "[a-zA-Z0-9_]{1,100}";
    public static final String TAX_SCHEME_ID =  "[a-zA-Z0-9-]{1,100}";
    public static final String FIRST_AUCTION_DATE =  "[a-zA-Z0-9_-]{1,100}";
    public static final String FIRST_ISSUANCE_AMOUNT =  "[a-zA-Z0-9_-]{1,100}";
    public static final String REDEMPTION_DATE = "[a-zA-Z0-9_-]{1,100}";
    public static final String REDEMPTION_PRICE = "[a-zA-Z0-9_.-]{1,100}";
    public static final String CENTRAL_BANK_APPLIED_AMOUNT =  "[a-zA-Z0-9_-]{1,100}";
    public static final String ISSUE_PRICE =  "[a-zA-Z0-9_.]{1,100}";
    public static final String ISSUE_YIELD = "[a-zA-Z0-9_.]{1,100}";
    public static final String DAY_COUNT_CONVENTION = "[a-zA-Z0-9_\\s /]{1,100}";
    public static final String ROUNDING_OPTION = "[a-zA-Z0-9_]{1,100}";
    public static final String RECORD_DATE_PERIOD = "[a-zA-Z0-9_]{1,100}";
    public static final String VOLUNTARY_REDEMPTION_FREQUENCY = "[a-zA-Z0-9_]{1,100}";
    public static final String REDEMPTION_END_DATE_OFFSET = "[a-zA-Z0-9_]{1,100}";
    public static final String TENOR_PERIOD = "[a-zA-Z0-9_-]{1,36}";
	public static final String FIRST_COUPON_DATE ="[a-zA-Z0-9_-]{1,100}";;

	//SssCouponSchedules
	public static final String SECURITIES_CODE3 = "[a-zA-Z0-9_]{1,36}";
	public static final String SECURIIES_TYPE = "[a-zA-Z0-9_]{1,100}";
	
	
	//SssFloatingRates
	public static final String PUBLICTION_DATE = "[a-zA-Z0-9_]{1,100}";
	public static final String VALUE_DATE2 = "[a-zA-Z0-9_]{1,100}";

	public static final String RATE = "[0-9.]{1,100}";
	public static final String REFERENCE_RATE = "[a-zA-Z0-9_.\\s]{1,100}";


	//SSS ALLOTMENT
	public static final String AUCTION_DATE = "[0-9:\\s-]{1,30}";
	public static final String TOTAL_NOMINAL_AMOUNT_ALLOTED = "[0-9]{1,10}";
	public static final String TOTAL_NOMINAL_AMOUNT_TO_BE_ALLOTED = "[0-9]{1,10}";
	public static final String PROCESSED =  "[0-9]{1,10}";
//	public static final String ACTION =  "[a-zA-Z0-9_]{1,20}";
//	public static final String MODIFIED_BY =  "[a-zA-Z0-9_-]{1,36}";
//	public static final String APPROVED_BY =  "[a-zA-Z0-9_-]{1,36}";
//	public static final String APPROVED_DATE = "[a-zA-Z0-9_\\s.:-]{1,20}";
//	public static final String EFFECTIVE_DATE = "[a-zA-Z0-9_\\s.:-]{1,20}";
//	public static final String APPROVAL_REMARK = "[a-zA-Z0-9_]{1,140}";
//	public static final String WORK_FLOW_STATUS_ID = "[0-9]{1,10}";
//	public static final String PLANNED_TOTAL_NOMINAL_AMOUNT_ALLOTTED = "[0-9]{1,10}";
	//SSS SECURITIES CODE
	public static final String SECURITIES_TYPE_ID = "[a-zA-Z0-9_]{1,10}";
	public static final String SECURITIES_TENOR_PERIOD_UNIT = "[a-zA-Z0-9_]{1,10}";
	public static final String CREDIT_RATING = "[a-zA-Z0-9_]{1,5}";
	public static final String ISSUER_ID = "[a-zA-Z0-9_-]{1,36}";
	public static final String OPTIONAL_REDEMPTION_DATE = "[a-zA-Z0-9_]{1,100}";
	public static final String OPTIONAL_REDEMPTION_PRICE = "[a-zA-Z0-9_.]{1,100}";
	public static final String AVERAGE_YIELD = "[a-zA-Z0-9_.]{1,100}";
	public static final String WORKFLOW_STATUS_ID = "[a-zA-Z0-9_]{1,100}";
	public static final String REDEMPTION_REIMBURSE = "[a-zA-Z0-9_\\s]{1,20}";
	public static final String BENCHMARK_INDICATOR = "[a-zA-Z0-9_]{1,36}";
	public static final String COUPON_ROLE= "[a-zA-Z0-9_]{1,36}";
	public static final String ID ="[a-zA-Z0-9_-]{1,36}";
	public static final String MEMBER_CLASSIFICATION = "[a-zA-Z0-9_]{1,100}";
	public static final String EXEMPTED_FORM_BILLING ="[a-zA-Z0-9_]{1,100}";
	public static final String FI_GROUP = "[a-zA-Z0-9_]{1,100}";
	public static final String BILLING_PROFILE_ID = "[a-zA-Z0-9_-]{1,36}";
	public static final String MCB_INTRADAY = "[a-zA-Z0-9_.]{1,100}";
	public static final String MCB_EOD_MINIMUM = "[a-zA-Z0-9_.]{1,100}";
	public static final String MCB_EOD_MAXIMUM = "[a-zA-Z0-9_.]{1,100}";
	public static final String MCB_AVERAGE = "[a-zA-Z0-9_.]{1,100}";
	public static final String VERIFIED_BY = "[a-zA-Z0-9_-]{1,36}";
	public static final String VERIFIED_DATE =  "[a-zA-Z0-9_:/\\s-]{1,100}";
	public static final String COST_CENTRE = "[a-zA-Z0-9_]{1,6}";
	public static final String AVAILABLE_BALANCE ="[0-9.]{1,20}";
	public static final String ACCOUNT_BALANCE ="[0-9.]{1,20}";
}