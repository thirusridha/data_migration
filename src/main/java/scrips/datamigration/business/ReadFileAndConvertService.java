package scrips.datamigration.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import scrips.datamigration.common.CommonUtils;
import scrips.datamigration.common.UserData;
import scrips.datamigration.exception.DatabaseException;
import scrips.datamigration.exception.FailedValidationException;
import scrips.datamigration.jpa.account.AccountSourceDAO;
import scrips.datamigration.jpa.account.JpaAccountSource;
import scrips.datamigration.jpa.account.JpaAccountTemp;
import scrips.datamigration.jpa.accountposition.AccountPositionSourceDAO;
import scrips.datamigration.jpa.accountposition.JpaAccountPosition;
import scrips.datamigration.jpa.accountposition.JpaAccountPositionSource;
import scrips.datamigration.jpa.accountposition.JpaAccountPositionTemp;
import scrips.datamigration.jpa.cbm.CbmCostCentreSourceDAO;
import scrips.datamigration.jpa.cbm.CbmGlAccountSourceDAO;
import scrips.datamigration.jpa.cbm.JpaCbmCostCentreSource;
import scrips.datamigration.jpa.cbm.JpaCbmCostCentreTemp;
import scrips.datamigration.jpa.cbm.JpaCbmDepositRateSource;
import scrips.datamigration.jpa.cbm.JpaCbmDepositRateSourceDAO;
import scrips.datamigration.jpa.cbm.JpaCbmDepositRateTemp;
import scrips.datamigration.jpa.cbm.JpaCbmFloatingRateSource;
import scrips.datamigration.jpa.cbm.JpaCbmFloatingRateSourceDAO;
import scrips.datamigration.jpa.cbm.JpaCbmFloatingRateTemp;
import scrips.datamigration.jpa.cbm.JpaCbmGlAccountSource;
import scrips.datamigration.jpa.cbm.JpaCbmGlAccountTemp;
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBaseDetailSource;
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBaseDetailSourceDAO;
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBaseDetailTemp;
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBaseSource;
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBaseSourceDAO;
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBaseTemp;
import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.member.JpaMember;
import scrips.datamigration.jpa.member.JpaMemberSource;
import scrips.datamigration.jpa.member.JpaMemberSourceDAO;
import scrips.datamigration.jpa.member.JpaMemberTemp;
import scrips.datamigration.jpa.member.MemberDAO;
import scrips.datamigration.jpa.sss.Member.JpaSssMember;
import scrips.datamigration.jpa.sss.Member.SssMemberDAO;
import scrips.datamigration.jpa.sss.account.JpaSSSAccount;
import scrips.datamigration.jpa.sss.account.SSSAccountDAO;
import scrips.datamigration.jpa.sss.coupon.JpaSssFloatingRatesSource;
import scrips.datamigration.jpa.sss.coupon.JpaSssFloatingRatesTemp;
import scrips.datamigration.jpa.sss.coupon.SssFloatingRatesSourceDAO;
import scrips.datamigration.jpa.sss.securities.IssuerDao;
import scrips.datamigration.jpa.sss.securities.JpaIssuer;
import scrips.datamigration.jpa.sss.securities.JpaSecuritiesType;
import scrips.datamigration.jpa.sss.securities.JpaSssAllotment;
import scrips.datamigration.jpa.sss.securities.JpaSssAllotmentSource;
import scrips.datamigration.jpa.sss.securities.JpaSssAllotmentTemp;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCode;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeSource;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeStatistics;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeStatisticsSource;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeStatisticsTemp;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeTemp;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesPositionStatsSource;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesPositionStatsSourceDAO;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesPositionStatsTemp;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesPriceSource;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesPriceTemp;
import scrips.datamigration.jpa.sss.securities.JpaTaxScheme;
import scrips.datamigration.jpa.sss.securities.SecuritiesTypeDao;
import scrips.datamigration.jpa.sss.securities.SssAllotmentSourceDAO;
import scrips.datamigration.jpa.sss.securities.SssSecuritiesCodeDAO;
import scrips.datamigration.jpa.sss.securities.SssSecuritiesCodeSourceDAO;
import scrips.datamigration.jpa.sss.securities.SssSecuritiesCodeStatisticsSourceDAO;
import scrips.datamigration.jpa.sss.securities.SssSecuritiesPriceSourceDAO;
import scrips.datamigration.jpa.sss.securities.TaxSchemeDao;
import scrips.datamigration.jpa.sss.transaction.JpaSssTransaction;
import scrips.datamigration.jpa.sss.transaction.JpaSssTransactionSource;
import scrips.datamigration.jpa.sss.transaction.JpaSssTransactionTemp;
import scrips.datamigration.jpa.sss.transaction.SssTransactionSourceDAO;
import scrips.datamigration.jpa.user.JpaUser;
import scrips.datamigration.jpa.user.UserDAO;

@Service
@Slf4j

public class ReadFileAndConvertService {
	@Autowired
	private Environment env;
	@Autowired
	private AccountSourceDAO accountSourceDAO;
	@Autowired
	private SssTransactionSourceDAO sssTransactionSourceDAO;
	@Autowired
	private SssSecuritiesCodeSourceDAO sourceData;
	@Autowired
	private AccountPositionSourceDAO accountPosSourceDAO;
	@Autowired
	private CbmCostCentreSourceDAO costCentreSourceDAO;
	@Autowired
	private CbmGlAccountSourceDAO glAccountSourceDAO;
	@Autowired
	private JpaMemberSourceDAO memberSource;
	@Autowired
	private JpaCbmDepositRateSourceDAO cbmDepositRateSourceDAO;
	@Autowired
	private JpaCbmLiabilitiesBaseSourceDAO cbmLiabilitiesBaseSourceDAO;
	@Autowired
	private JpaCbmLiabilitiesBaseDetailSourceDAO cbmLiabilitiesBaseDetailSourceDAO;
	@Autowired
	private JpaSssSecuritiesPositionStatsSourceDAO sssSecuritiesPositionStatsSourceDAO;
	@Autowired
	MemberDAO memberDAO;
	@Autowired
	private JpaCbmFloatingRateSourceDAO cbmFloatingRateSourceDAO;
	@Autowired
	private SssAllotmentSourceDAO allotmentSourceDAO;

	@Autowired
	private SssFloatingRatesSourceDAO sssFloatingRatesSourceDAO;
	@Autowired
	private SssSecuritiesPriceSourceDAO sssSecuritiesPriceSourceDAO;

	@Autowired
	private IssuerDao issuerDao;
	@Autowired
	private TaxSchemeDao taxSchemeDao;
	@Autowired
	private SecuritiesTypeDao securitiesTypeDao;
	@Autowired
	private SssSecuritiesCodeStatisticsSourceDAO securitiesCodeStatisticsSourceDAO;
	@Autowired
	SssMemberDAO sssMemberDAO;
	@Autowired
	private SssSecuritiesCodeDAO securitiesCodeDAO;
	@Autowired
	private SSSAccountDAO sssAccountDAO;
	@Autowired
	private UserData userData;
	
	private final Logger logger = LogManager.getLogger(ReadFileAndConvertService.class);

	private List<String> convertToList(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);
		List<String> list = new ArrayList<>();
		String line;
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			if (!line.equals("")) {
				list.add(line);
			}
		}
		scanner.close();
		return list;

	}

	public List<JpaMember> convertToMemberList(List<String> list, List<JpaFileUploadDetails> draftDBDetails)
			throws DatabaseException, FailedValidationException, ParseException, SQLException {
		List<JpaMember> memberList = new ArrayList<JpaMember>();
		int lineCount = 0;
		Date date = new Date();
		for (String line : list) {
			lineCount++;
			List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
			Map<String, String> dbRecorsMap = new HashMap<>();
			int count = 0;
			for (JpaFileUploadDetails db : draftDBDetails) {
				log.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
				dbRecorsMap.put(db.getTableFieldName(), content.get(count));
				count++;
			}
			JpaMemberSource jpaMemberSource = JpaMemberSource.builder().id(UUID.randomUUID().toString())
					.memberCode(dbRecorsMap.get("member_code")).swiftBic(dbRecorsMap.get("swift_bic"))
					.swiftMember(Short.parseShort(dbRecorsMap.get("swift_member")))
					.bankCode(Short.parseShort(dbRecorsMap.get("bank_code"))).name(dbRecorsMap.get("name"))
					.shortName(dbRecorsMap.get("short_name")).memberStatus(dbRecorsMap.get("member_status"))
					.memberType(dbRecorsMap.get("member_type")).sectorId(dbRecorsMap.get("sector_id"))
					.mcbId(dbRecorsMap.get("mcb_id")).uen(dbRecorsMap.get("uen")).lei(dbRecorsMap.get("lei"))
					.memberClassification(dbRecorsMap.get("member_classification"))
					.exemptedFromBilling(Short.parseShort(dbRecorsMap.get("exempted_from_billing")))
					.exemptedFromSystemLimit(Short.parseShort(dbRecorsMap.get("exempted_from_system_limit")))
					.location(dbRecorsMap.get("location"))
					.activationDate(
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dbRecorsMap.get("activation_date")))
					.action(dbRecorsMap.get("action")).status(dbRecorsMap.get("status")).modifiedBy(null)
					.modifiedDate(null).approvedBy(null).approvedDate(null).createdDate(null).effectiveDate(null)
					.approvalRemark(dbRecorsMap.get("approval_remark")).workflowStatusId(null)
					.fiGroup(dbRecorsMap.get("fi_group")).billingProfileId(dbRecorsMap.get("billing_profile_id"))
					.mcbIntraday(Double.parseDouble(dbRecorsMap.get("mcb_intraday")))
					.mcbEodMinimum(Double.parseDouble(dbRecorsMap.get("mcb_eod_minimum")))
					.mcbEodMaximum(Double.parseDouble(dbRecorsMap.get("mcb_eod_maximum")))
					.mcbAverage(Double.parseDouble(dbRecorsMap.get("mcb_average")))
					.taxProfileId(dbRecorsMap.get("tax_profile_id")).verifiedBy(dbRecorsMap.get("verified_by"))
					.verifiedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dbRecorsMap.get("verified_date")))
					.build();
			memberSource.save(jpaMemberSource);
			try {
				JpaMember jpaMember = JpaMember.builder().id(UUID.randomUUID().toString())
						.memberCode(dbRecorsMap.get("member_code")).swiftBic(dbRecorsMap.get("swift_bic"))
						.swiftMember(Short.parseShort(dbRecorsMap.get("swift_member")))
						.bankCode(Short.parseShort(dbRecorsMap.get("bank_code"))).name(dbRecorsMap.get("name"))
						.shortName(dbRecorsMap.get("short_name")).memberStatus(dbRecorsMap.get("member_status"))
						.memberType(dbRecorsMap.get("member_type")).sectorId(dbRecorsMap.get("sector_id"))
						.mcbId(dbRecorsMap.get("mcb_id")).uen(dbRecorsMap.get("uen")).lei(dbRecorsMap.get("lei"))
						.memberClassification(dbRecorsMap.get("member_classification"))
						.exemptedFromBilling(Short.parseShort(dbRecorsMap.get("exempted_from_billing")))
						.exemptedFromSystemLimit(Short.parseShort(dbRecorsMap.get("exempted_from_system_limit")))
						.location(dbRecorsMap.get("location"))
						.activationDate(
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dbRecorsMap.get("activation_date")))
						.action(null).status(dbRecorsMap.get("status")).modifiedBy("System").modifiedDate(date)
						.approvedBy("System").approvedDate(date).createdDate(date).effectiveDate(date)
						.approvalRemark(null).workflowStatusId(null).fiGroup(dbRecorsMap.get("fi_group"))
						.billingProfileId(dbRecorsMap.get("billing_profile_id"))
						.mcbIntraday(Double.parseDouble(dbRecorsMap.get("mcb_intraday")))
						.mcbEodMinimum(Double.parseDouble(dbRecorsMap.get("mcb_eod_minimum")))
						.mcbEodMaximum(Double.parseDouble(dbRecorsMap.get("mcb_eod_maximum")))
						.mcbAverage(Double.parseDouble(dbRecorsMap.get("mcb_average")))
						.taxProfileId(dbRecorsMap.get("tax_profile_id")).verifiedBy(dbRecorsMap.get("verified_by"))
						.verifiedDate(date).build();
				memberList.add(jpaMember);
			} catch (Exception e) {
				System.out.println("line -" + lineCount + " skip due to conversion issues");
				System.out.println("exception -" + e);
			}

		}
		return memberList;
	}

	public static Object objectMapper(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		JpaMemberTemp memberTempObj = mapper.convertValue(object, JpaMemberTemp.class);
		log.info("{} - {}", memberTempObj, memberTempObj.getMemberCode());
		return memberTempObj;
	}

	public static Object objectMapperJpaMember(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		JpaMember memberObj = mapper.convertValue(object, JpaMember.class);
		log.info("{} - {}", memberObj, memberObj.getMemberCode());
		return memberObj;
	}

	public HashMap<String, Integer> getTableDetailsMap(List<JpaFileUploadDetails> draftDBDetails) {
		HashMap<String, Integer> tableDetailsMap = new HashMap<String, Integer>();
		for (JpaFileUploadDetails fud : draftDBDetails) {
			tableDetailsMap.put(fud.getTableFieldName(), fud.getSequenceNo() - 1);
		}
		tableDetailsMap.forEach((x, y) -> System.out.println(x + "=" + y));

		return tableDetailsMap;
	}

	public void createAndSaveAccountSourceData(List<String> fileRecords, List<JpaFileUploadDetails> draftDBDetails) {
		try {
			int lineCount = 0;
			for (String line : fileRecords) {
				lineCount++;
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				if (line == null || line.trim().isEmpty()) {
					JpaAccountSource jpaAccountSource = new JpaAccountSource();
					jpaAccountSource.setRemarks("No columns present");
					accountSourceDAO.save(jpaAccountSource);
					continue;
				}
				List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
				if (content.size() != 14) {
					JpaAccountSource jpaAccountSource = new JpaAccountSource();
					jpaAccountSource.setRemarks("Invalid number of columns received");
					accountSourceDAO.save(jpaAccountSource);
					continue;
				}
				if (content.size() != draftDBDetails.size()) {
					JpaAccountSource jpaAccountSource = new JpaAccountSource();
					jpaAccountSource
							.setRemarks("Header columns count in file upload properties not matching with sourcer");
					accountSourceDAO.save(jpaAccountSource);
					continue;
				}

				Map<String, String> dbRecorsMap = new HashMap<>();
				int count = 0;
				for (JpaFileUploadDetails db : draftDBDetails) {
					logger.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
					dbRecorsMap.put(db.getTableFieldName(), content.get(count));
					count++;
				}

				JpaAccountSource accountSource = JpaAccountSource.builder().id(id)
						.accountNumber(dbRecorsMap.get("account_number")).description(dbRecorsMap.get("description"))
						.memberId(dbRecorsMap.get("member_code")).accountType(dbRecorsMap.get("account_type"))
						.mainAccountIndicator(dbRecorsMap.get("main_account_indicator"))
						.defaultMainAccount(dbRecorsMap.get("default_main_account"))
						.statementDeliveryBic(dbRecorsMap.get("statement_delivery_bic"))
						.accountStatus(dbRecorsMap.get("account_status")).currencyCode(dbRecorsMap.get("currency_code"))
						.endOfDayStatement(dbRecorsMap.get("end_of_day_statement")).payerReference(null)
						.relatedReference(null).counterpartyId(null).valueDate(null).activationDate(null)
						.glCategory(dbRecorsMap.get("gl_category")).costCentre(dbRecorsMap.get("cost_centre"))
						.glAccountNumber(dbRecorsMap.get("gl_account_number"))
						.statementInterval(dbRecorsMap.get("statement_interval")).action(null).status(null)
						.modifiedBy(null).modifiedDate(null).approvedBy(null).approvedDate(null).createdDate(null)
						.effectiveDate(null).approvalRemark(null)
						.workflowStatusId(dbRecorsMap.get("workflow_status_id"))
						.complianceCalculation(dbRecorsMap.get("compliance_calculation")).build();
				accountSourceDAO.save(accountSource);

			}
			logger.info("Total records in File: {}" + lineCount);
		} catch (Exception e) {
			logger.error("Exception while preparting source data ", e.getMessage());
		}

	}

	public List<JpaAccountTemp> convertToAccountList() {
		List<JpaAccountTemp> accountList = new ArrayList<JpaAccountTemp>();
		Date date = new Date();
		try {
			List<JpaAccountSource> accountSource = accountSourceDAO.findAll();
			String id=userData.getSystemUserId();
			if (accountSource != null && !accountSource.isEmpty()) {
				List<String> errors = new ArrayList<String>();
				String member_id = null;
				for (JpaAccountSource sourceRec : accountSource) {
					if (sourceRec.getRemarks() == null) {
						List<String> validationRemarks = new ArrayList<String>();

						if (errors.size() > 0) {
							sourceRec.setRemarks(String.join(", ", errors));
							accountSourceDAO.save(sourceRec);
							continue;
						}
						
//						try {
//							List<JpaUser> userList = userDAO.findByName("System");
//							if (userList != null && !userList.isEmpty())
//								id = userList.get(0).getId();
//						} catch (Exception e) {
//							errors.add("error reading user data");
//						}
						try {
							List<JpaMember> memberList = memberDAO.findByMemberCode(sourceRec.getMemberId());
							if (memberList != null && !memberList.isEmpty()) {
								member_id = memberList.get(0).getId();
							}
						} catch (Exception e) {
							validationRemarks.add("member code is not present in member table");
						}

						String accountIndicator = null;
						String defaultMainAccount = null;
						if (sourceRec.getMainAccountIndicator().equals("Y"))
							accountIndicator = "1";
						if (sourceRec.getDefaultMainAccount().equals("Y"))
							defaultMainAccount = "1";
						if (sourceRec.getMainAccountIndicator().equals("N"))
							accountIndicator = "0";
						if (sourceRec.getDefaultMainAccount().equals("N"))
							defaultMainAccount = "0";

						if (validationRemarks.size() > 0) {
							sourceRec.setRemarks(String.join(", ", validationRemarks));
							accountSourceDAO.save(sourceRec);
							continue;
						}

						JpaAccountTemp jpaAccount = JpaAccountTemp.builder().id(sourceRec.getId())
								.accountNumber(sourceRec.getAccountNumber()).description(sourceRec.getDescription())
								.memberId(member_id).accountType(sourceRec.getAccountType())
								.mainAccountIndicator(Integer.parseInt(accountIndicator))
								.defaultMainAccount(Integer.parseInt(defaultMainAccount))
								.statementDeliveryBic(sourceRec.getStatementDeliveryBic())
								.accountStatus(sourceRec.getAccountStatus()).currencyCode(sourceRec.getCurrencyCode())
								.endOfDayStatement(sourceRec.getEndOfDayStatement()).payerReference("NA")
								.relatedReference("NA").counterpartyId("NA").valueDate(0)
								.glCategory(sourceRec.getGlCategory()).costCentre(sourceRec.getCostCentre())
								.glAccountNumber(sourceRec.getGlAccountNumber())
								.statementInterval(sourceRec.getStatementInterval()).activationDate(date)
								.workflowStatusId(null).complianceCalculation(Short.parseShort("0")).action(" ")
								.status("ACTIVE").modifiedBy(id).modifiedDate(date).approvedBy(id)
								.approvedDate(date).createdDate(date).effectiveDate(date)
								.approvalRemark("Data Migration").build();

//				JpaAccountSettlementPurpose jpaAccountSettlementPurpose = JpaAccountSettlementPurpose.builder()
//						.transactionId(dbRecorsMap.get("account_type")).account(jpaAccount).build();
//				JpaAccountNameAddress jpaAccountNameAddress = JpaAccountNameAddress.builder()
//						.nameAndAddress(dbRecorsMap.get("member_id")).account(jpaAccount).build();
//
//				jpaAccount.setAccountNameAddress(jpaAccountNameAddress);
//				jpaAccount.setAccountSettlementPurpose(jpaAccountSettlementPurpose);
						accountList.add(jpaAccount);
					}
				}
			}

		} catch (Exception e) {
			logger.error("Error in temp {}", e.getMessage());
			e.printStackTrace();
		}
		return accountList;
	}

	public List<JpaSSSAccount> convertToSssAccountList(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) throws NumberFormatException, ParseException {
		List<JpaSSSAccount> sssaccountList = new ArrayList<JpaSSSAccount>();
		Date date = new Date();
		for (String line : fileRecords) {
			List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
			Map<String, String> dbRecorsMap = new HashMap<>();
			int count = 0;
			for (JpaFileUploadDetails db : draftDBDetails) {
				log.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
				dbRecorsMap.put(db.getTableFieldName(), content.get(count));
				count++;
			}

			JpaSSSAccount jpaSSSAccount = JpaSSSAccount.builder().id(UUID.randomUUID().toString())
					.accountId(dbRecorsMap.get("account_id")).memberId(dbRecorsMap.get("member_id"))
					.custodyAccountTypeId(dbRecorsMap.get("custody_account_type_id"))
					.description(dbRecorsMap.get("description"))
					.corporateInvestor(dbRecorsMap.get("corporate_investor"))
					.accountStatus(dbRecorsMap.get("account_status")).taxProfileId(dbRecorsMap.get("tax_profile_id"))
					.statementDeliveryBic(dbRecorsMap.get("statement_delivery_bic")).action(dbRecorsMap.get("action"))
					.status(dbRecorsMap.get("status")).modifiedBy(dbRecorsMap.get("modified_by")).modifiedDate(date)
					.approvedBy(dbRecorsMap.get("approved_by")).approvedDate(date).createdDate(date).effectiveDate(date)
					.approvalRemark(dbRecorsMap.get("approval_remark"))
					.workflowStatusId(Integer.parseInt(dbRecorsMap.get("workflow_status_id"))).build();
			sssaccountList.add(jpaSSSAccount);
		}
		return sssaccountList;
	}

	public String getMemberId(Connection con1, String code) throws SQLException {
		String memberId = null;
		PreparedStatement p = con1.prepareStatement("select id from member where member_code='" + code + "'");
		ResultSet rs = p.executeQuery();
		while (rs.next()) {
			memberId = rs.getString(1);
		}
		return memberId;

	}

	public String getAccountUUID(Connection con1, String memberId, String number) throws SQLException {
		String accountUUID = null;
		PreparedStatement p = con1.prepareStatement(
				"select id from sss_account where member_id='" + memberId + "' and account_id='" + number + "'");
		ResultSet rs1 = p.executeQuery();
		while (rs1.next()) {
			accountUUID = rs1.getString(1);
			break;
		}
		return accountUUID;
	}

	public void createAndSaveTransactionSourceData(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) {
		try {

			int lineCount = 0;
			for (String line : fileRecords) {
				lineCount++;
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				if (line == null || line.trim().isEmpty()) {
					JpaSssTransactionSource transactionSource = new JpaSssTransactionSource();
					transactionSource.setRemarks("No columns present");
					sssTransactionSourceDAO.save(transactionSource);
					continue;
				}
				List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
				if (content.size() != 23) {
					JpaSssTransactionSource transactionSource = new JpaSssTransactionSource();
					transactionSource.setRemarks("Invalid number of columns received");
					sssTransactionSourceDAO.save(transactionSource);
					continue;
				}
				if (content.size() != draftDBDetails.size()) {
					JpaSssTransactionSource transactionSource = new JpaSssTransactionSource();
					transactionSource
							.setRemarks("Header columns count in file upload properties not matching with sourcer");
					sssTransactionSourceDAO.save(transactionSource);
					continue;
				}

				Map<String, String> dbRecorsMap = new HashMap<>();
				int count = 0;
				for (JpaFileUploadDetails db : draftDBDetails) {
					logger.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
					dbRecorsMap.put(db.getTableFieldName(), content.get(count));
					count++;
				}
				JpaSssTransactionSource jpasssTransactionSource = JpaSssTransactionSource.builder().id(id)
						.sssReference(dbRecorsMap.get("sss_reference")).messageLogId(null)
						.securitiesCode(dbRecorsMap.get("securities_code"))
						.transactionType(dbRecorsMap.get("transaction_type")).status(null)
						.settlementDate(dbRecorsMap.get("settlement_date")).tradeDate(dbRecorsMap.get("trade_date"))
						.currencyCode(dbRecorsMap.get("currency_code"))
						.settlementAmount(dbRecorsMap.get("settlement_amount"))
						.nominalAmount(dbRecorsMap.get("nominal_amount")).dealPrice(dbRecorsMap.get("deal_price"))
						.delivererMemberCode(dbRecorsMap.get("deliverer_member_code"))
						.receiverMemberCode(dbRecorsMap.get("receiver_member_code")).repoClosingDate(null)
						.repoClosingSettlementAmount(null).reasonCode(null)
						.transactionReference(dbRecorsMap.get("transaction_reference")).processedDate(null)
						.createdDate(dbRecorsMap.get("created_date")).modifiedDate(dbRecorsMap.get("modified_date"))
						.senderMemberSwiftCode(null).payerMemberSwiftCode(null).payeeMemberSwiftCode(null)
						.receiverMemberSwiftCode(dbRecorsMap.get("receiver_member_swift_code")).senderMemberCode(null)
						.messageType(dbRecorsMap.get("message_type")).senderType(null).receiverRtgsAccount(null)
						.delivererRtgsAccount(null).receiverRtgsMemberCode(dbRecorsMap.get("receiver_rtgs_member_code"))
						.delivererRtgsMemberCode(dbRecorsMap.get("deliverer_rtgs_member_code")).holdIndicator(null)
						.paymentType(null).gridLockIndicator(null).rolloverCount(null)
						.pendingCancellationIndicator(null).delivererMemberId(null).receiverMemberId(null)
						.receiverAccountId(null).delivererAccountId(null).senderMemberId(null).pdmIndicator(null)
						.erfReference(null).delivererAccountNo(dbRecorsMap.get("deliverer_account_no"))
						.receiverAccountNo(dbRecorsMap.get("receiver_account_no")).msgReceivedTimestamp(null)
						.receiverSenderId(dbRecorsMap.get("receiver_sender_id"))
						.delivererSenderId(dbRecorsMap.get("deliverer_sender_id")).delivererOnbehalf(null)
						.receiverOnbehalf(null).processFlag(null).delivererBeneficiaryAccount(null).underlyingId(null)
						.balanceType(null).accuredInterestAmount(dbRecorsMap.get("accured_interest_amount"))
						.delivererChannel(null).receiverChannel(null).repoRate(null).haircutRate(null)
						.originalAccountNo(null).build();
				sssTransactionSourceDAO.save(jpasssTransactionSource);

			}
			logger.info("Total records in File: {}" + lineCount);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception while preparting source data ", e.getMessage());
		}

	}

	public List<JpaSssTransactionTemp> convertToSssTransactionList() {
		List<JpaSssTransactionTemp> sssTransactionList = new ArrayList<JpaSssTransactionTemp>();
		Date date = new Date();
		try {
			List<JpaSssTransactionSource> transactionSource = sssTransactionSourceDAO.findAll();
			if (transactionSource != null && !transactionSource.isEmpty()) {
				List<String> errors = new ArrayList<String>();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (JpaSssTransactionSource sourceRec : transactionSource) {

					String transaction_type = sourceRec.getTransactionType();
					String receiver_sender_id = sourceRec.getReceiverSenderId();
					String status = "FUTU";
					String deliverer_member_id = null; // dbRecorsMap.get("deliverer_member_code");
					String receiver_member_id = null; // dbRecorsMap.get("receiver_member_code");
					String deliverer_account_id = null; // dbRecorsMap.get("deliverer_account_no");
					String receiver_account_id = null; // dbRecorsMap.get("receiver_account_no");
					String payee_member_swift_code = null; // dbRecorsMap.get("deliverer_member_code");
					String payer_member_swift_code = null; // dbRecorsMap.get("receiver_member_code");
					String transaction_types = null;
					if (sourceRec.getRemarks() == null) {
						List<String> validationRemarks = new ArrayList<String>();
						if (errors.size() > 0) {
							sourceRec.setRemarks(String.join(", ", errors));
							sssTransactionSourceDAO.save(sourceRec);
							continue;
						}
						try {
							List<JpaSssMember> memberList = sssMemberDAO
									.findBymemberCode(sourceRec.getDelivererMemberCode());

							if (memberList != null && !memberList.isEmpty()) {
								deliverer_member_id = memberList.get(0).getId();
								payee_member_swift_code = memberList.get(0).getSwiftBic();
							} else
								validationRemarks.add("Member Code is Not Present in Member Table");
						} catch (Exception e) {
							validationRemarks.add("Getting Error while Loading Member Table");
						}
						try {
							List<JpaSssMember> memberList = sssMemberDAO
									.findBymemberCode(sourceRec.getReceiverMemberCode());
							if (memberList != null && !memberList.isEmpty()) {
								receiver_member_id = memberList.get(0).getId();
								payer_member_swift_code = memberList.get(0).getSwiftBic();
							} else
								validationRemarks.add("Member Code is Not Present in Member Table");
						} catch (Exception e) {
							validationRemarks.add("Member Code is Not Present in Member Table");
						}
						try {
							List<JpaSSSAccount> accountList = sssAccountDAO
									.findByAccountId(sourceRec.getDelivererAccountNo());
							if (accountList != null && !accountList.isEmpty()) {
								deliverer_account_id = accountList.get(0).getId();
							} else
								validationRemarks.add("Account No is Not Present in Account Table");
						} catch (Exception e) {
							validationRemarks.add("Getting error while loading Account Table");
						}
						try {
							List<JpaSSSAccount> accountList = sssAccountDAO
									.findByAccountId(sourceRec.getReceiverAccountNo());
							if (accountList != null && !accountList.isEmpty()) {
								receiver_account_id = accountList.get(0).getId();
							} else
								validationRemarks.add("Account No is Not Present in Account Table");
						} catch (Exception e) {
							validationRemarks.add("Getting error while loading Account Table");
						}
						
						
						//settlement date,trade date,
						//settlement amount,nominal amount,deal price,accured interest amount
						//created date,modified date
						
						
						
						if (!CommonUtils.validateParseInteger(sourceRec.getSettlementDate()))
							validationRemarks.add("Invalid Settlement Date");
						if (!CommonUtils.validateParseInteger(sourceRec.getTradeDate()))
							validationRemarks.add("Invalid Trade Date");
						if (!CommonUtils.validateParseDouble(sourceRec.getSettlementAmount()))
							validationRemarks.add("Invalid Settlement Amount");
						if (!CommonUtils.validateParseDouble(sourceRec.getNominalAmount()))
							validationRemarks.add("Invalid Nominal Amount");
						if (!CommonUtils.validateParseDouble(sourceRec.getDealPrice()))
							validationRemarks.add("Invalid Deal Price");
						if (!CommonUtils.validateParseDouble(sourceRec.getAccuredInterestAmount()))
							validationRemarks.add("Invalid Accured Interest Amount");
						
						Date createdDate = null;
						if (sourceRec.getCreatedDate() != null) {
							if (CommonUtils.validateParseDate_yyyyMMdd_HHmmss(sourceRec.getCreatedDate()))
								createdDate = simpleDateFormat1.parse(sourceRec.getCreatedDate());
							else
								validationRemarks.add("Invalid Created Date");
						}
						Date modifiedDate = null;
						if (sourceRec.getModifiedDate()!= null) {
							if (CommonUtils.validateParseDate_yyyyMMdd_HHmmss(sourceRec.getModifiedDate()))
								modifiedDate = simpleDateFormat1.parse(sourceRec.getModifiedDate());
							else
								validationRemarks.add("Invalid Modified Date");
						}
						
						
						

						if (validationRemarks.size() > 0) {
							sourceRec.setRemarks(String.join(", ", validationRemarks));
							sssTransactionSourceDAO.save(sourceRec);
							continue;
						}

						if (transaction_type.equals("RPC") && receiver_sender_id.equals("ERF"))
							transaction_types = "ESRC";
						else if (transaction_type.equals("RPC") && !receiver_sender_id.equals("ERF")) {
							transaction_types = "REPC";
						}
						if (transaction_types.equals("REPC") || transaction_types.equals("ESRC")) {
							JpaSssTransactionTemp jpaSssTransactionTemp = JpaSssTransactionTemp.builder()
									.id(sourceRec.getId()).sssReference(sourceRec.getSssReference()).messageLogId(null)
									.securitiesCode(sourceRec.getSecuritiesCode()).transactionType(transaction_types)
									.status(status).settlementDate(Integer.parseInt(sourceRec.getSettlementDate()))
									.tradeDate(Integer.parseInt(sourceRec.getTradeDate()))
									.currencyCode(sourceRec.getCurrencyCode())
									.settlementAmount(Double.parseDouble(sourceRec.getSettlementAmount()))
									.nominalAmount(Double.parseDouble(sourceRec.getNominalAmount()))
									.dealPrice(Double.parseDouble(sourceRec.getDealPrice()))
									.delivererMemberCode(sourceRec.getDelivererMemberCode())
									.receiverMemberCode(sourceRec.getReceiverMemberCode()).repoClosingDate(null)
									.repoClosingSettlementAmount(null).reasonCode(null)
									.transactionReference(sourceRec.getTransactionReference()).processedDate(null)
									//.createdDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sourceRec.getCreatedDate()))
									//.modifiedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sourceRec.getModifiedDate()))
									.createdDate(createdDate).modifiedDate(modifiedDate)
									
									.senderMemberSwiftCode(null).payeeMemberSwiftCode(payee_member_swift_code)
									.payerMemberSwiftCode(payer_member_swift_code).receiverMemberSwiftCode(null)
									.senderMemberCode(null).messageType(sourceRec.getMessageType()).senderType(null)
									.receiverRtgsAccount(" ").delivererRtgsAccount(" ")
									.receiverRtgsMemberCode(sourceRec.getReceiverRtgsMemberCode())
									.delivererRtgsMemberCode(sourceRec.getDelivererRtgsMemberCode())
									.holdIndicator(Short.parseShort("0")).paymentType("APMT")
									.gridLockIndicator(Short.parseShort("0")).rolloverCount(Integer.parseInt("0"))
									.pendingCancellationIndicator(Short.parseShort("0"))
									.delivererMemberId(deliverer_member_id).receiverMemberId(receiver_member_id)
									.receiverAccountId(receiver_account_id).delivererAccountId(deliverer_account_id)
									.senderMemberId(null).pdmIndicator(Short.parseShort("0")).erfReference(null)
									.delivererAccountNo(sourceRec.getDelivererAccountNo())
									.receiverAccountNo(sourceRec.getReceiverAccountNo()).msgReceivedTimestamp(date)
									.receiverSenderId(sourceRec.getReceiverSenderId())
									.delivererSenderId(sourceRec.getDelivererSenderId())
									.delivererOnbehalf(Short.parseShort("0")).receiverOnbehalf(Short.parseShort("0"))
									.processFlag("No").delivererBeneficiaryAccount(null).underlyingId(null)
									.balanceType(null)
									.accuredInterestAmount(Double.parseDouble(sourceRec.getAccuredInterestAmount()))
									.delivererChannel("SWIFT").receiverChannel("SWIFT").repoRate(null).haircutRate(null)
									.originalAccountNo(null).build();

							sssTransactionList.add(jpaSssTransactionTemp);

						}

						Long random = (long) (Math.random() * 100000L);
						String sdf = new SimpleDateFormat("yyyyMMddHHmmssSS").format(date);
						String id = "ST".concat(sdf).concat(random.toString());
						if (transaction_types.equals("ESRC")) {
							JpaSssTransactionTemp jpaSssTransaction1 = JpaSssTransactionTemp.builder().sssReference(id)
									.messageLogId(null).securitiesCode(sourceRec.getSecuritiesCode())
									.transactionType("ERIC").status(status)
									.settlementDate(Integer.parseInt(sourceRec.getSettlementDate()))
									.tradeDate(Integer.parseInt(sourceRec.getTradeDate()))
									.currencyCode(sourceRec.getCurrencyCode())
									.settlementAmount(Double.parseDouble(sourceRec.getSettlementAmount()))
									.nominalAmount(Double.parseDouble(sourceRec.getNominalAmount())).dealPrice(null)
									.delivererMemberCode(sourceRec.getDelivererMemberCode())
									.receiverMemberCode("MASGSGSG").repoClosingDate(null)
									.repoClosingSettlementAmount(null).reasonCode(null)
									.transactionReference(sourceRec.getTransactionReference()).processedDate(null)
									.createdDate(date).modifiedDate(date).senderMemberSwiftCode(null)
									.payeeMemberSwiftCode(payee_member_swift_code)
									.payerMemberSwiftCode(payer_member_swift_code).receiverMemberSwiftCode(null)
									.senderMemberCode(null).messageType(sourceRec.getMessageType()).senderType(null)
									.receiverRtgsAccount(" ").delivererRtgsAccount(" ")
									.receiverRtgsMemberCode(sourceRec.getReceiverRtgsMemberCode())
									.delivererRtgsMemberCode(sourceRec.getDelivererRtgsMemberCode())
									.holdIndicator(Short.parseShort("1")).paymentType("APMT")
									.gridLockIndicator(Short.parseShort("0")).rolloverCount(Integer.parseInt("0"))
									.pendingCancellationIndicator(Short.parseShort("0"))
									.delivererMemberId(deliverer_member_id).receiverMemberId(receiver_member_id)
									.receiverAccountId(receiver_account_id).delivererAccountId(deliverer_account_id)
									.senderMemberId(null).pdmIndicator(Short.parseShort("0")).erfReference(null)
									.delivererAccountNo(sourceRec.getDelivererAccountNo()).receiverAccountNo("ALO")
									.msgReceivedTimestamp(date).receiverSenderId(sourceRec.getReceiverSenderId())
									.delivererSenderId(sourceRec.getDelivererSenderId())
									.delivererOnbehalf(Short.parseShort("0")).receiverOnbehalf(Short.parseShort("0"))
									.processFlag("No").delivererBeneficiaryAccount(null).underlyingId(null)
									.balanceType(null).accuredInterestAmount(null).delivererChannel("SWIFT")
									.receiverChannel("SWIFT").repoRate(null).haircutRate(null).originalAccountNo(null)
									.build();
							sssTransactionList.add(jpaSssTransaction1);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in temp {}", e.getMessage());
			e.printStackTrace();
		}
		return sssTransactionList;
	}	

	private String getSwiftBic(Connection con1, String member_code) throws SQLException {

		PreparedStatement p;
		ResultSet rs;
		String memberSwiftCodes = null;
		p = con1.prepareStatement("select swift_bic from sss_member where member_code='" + member_code + "'");
		rs = p.executeQuery();
		while (rs.next()) {
			memberSwiftCodes = rs.getString(1);
		}

		return memberSwiftCodes;
	}

	
	public List<JpaSssSecuritiesPositionStatsTemp> convertToSssSecurityPosList(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) {
		List<JpaSssSecuritiesPositionStatsTemp> SecurityPosList = new ArrayList<JpaSssSecuritiesPositionStatsTemp>();

		for (String line : fileRecords) {
			List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
			Map<String, String> dbRecorsMap = new HashMap<>();
			int count = 0;
			for (JpaFileUploadDetails db : draftDBDetails) {
				log.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
				dbRecorsMap.put(db.getTableFieldName(), content.get(count));
				count++;
			}
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			Date d = new Date();
			String s = new SimpleDateFormat("yyyyMMdd").format(d);
			JpaSssSecuritiesPositionStatsSource securitiesPositionStatsSource = JpaSssSecuritiesPositionStatsSource
					.builder().id(id).accountId(dbRecorsMap.get("account_id"))
					.securitiesCode(dbRecorsMap.get("securities_code")).valueDate(null)
					.openingNominalAmount(dbRecorsMap.get("opening_nominal_amount"))
					.closingNominalAmount(dbRecorsMap.get("closing_nominal_amount")).settledCount(null)
					.settledAmount(null).receiptCount(null).receiptAmount(null).queuedCount(null).queuedAmount(null)
					.rejectedCount(null).rejectedAmount(null).cancelledCount(null).cancelledAmount(null).build();
			sssSecuritiesPositionStatsSourceDAO.save(securitiesPositionStatsSource);
			JpaSssSecuritiesPositionStatsTemp jpaSssSecuritesPos = JpaSssSecuritiesPositionStatsTemp.builder().id(id)
					.accountId(dbRecorsMap.get("account_id")).securitiesCode(dbRecorsMap.get("securities_code"))
					.valueDate(Integer.parseInt(s))
					.openingNominalAmount(Double.parseDouble(dbRecorsMap.get("opening_nominal_amount")))
					.closingNominalAmount(Double.parseDouble(dbRecorsMap.get("closing_nominal_amount"))).settledCount(0)
					.settledAmount(0.0).receiptCount(0).receiptAmount(0.0).queuedCount(0).queuedAmount(0.0)
					.rejectedCount(0).rejectedAmount(0.0).cancelledCount(0).cancelledAmount(0.0).build();
			SecurityPosList.add(jpaSssSecuritesPos);

		}
		return SecurityPosList;
	}
//	public List<JpaSssFloatingRatesTemp> convertToSssfloatingratesList() throws NumberFormatException, ParseException {
//		List<JpaSssFloatingRatesTemp> sssFloatingRateTempList = new ArrayList<JpaSssFloatingRatesTemp>();
//		for (String line : fileRecords) {
//			List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
//			Map<String, String> dbRecorsMap = new HashMap<>();
//			int count = 0;
//			for (JpaFileUploadDetails db : draftDBDetails) {
//				log.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
//				dbRecorsMap.put(db.getTableFieldName(), content.get(count));
//				count++;
//			}
//			String id = UUID.randomUUID().toString().replaceAll("-", "");
//			Date date = new Date();
//			JpaSssFloatingRatesSource sssFloatingRateSource = JpaSssFloatingRatesSource.builder().id(id)
//					.referenceRate(dbRecorsMap.get("reference_rate"))
//					.publicationDate(dbRecorsMap.get("publication_date")).valueDate(dbRecorsMap.get("value_date"))
//					.rate(dbRecorsMap.get("rate")).action(null).status(null).modifiedBy(null).modifiedDate(null)
//					.approvedBy(null).approvedDate(null).createdDate(null).effectiveDate(null).approvalRemark(null)
//					.workflowStatusId(null).build();
//			sssFloatingRatesSourceDAO.save(sssFloatingRateSource);
//			JpaSssFloatingRatesTemp sssFloatingRate = JpaSssFloatingRatesTemp.builder().id(id)
//					.referenceRate(dbRecorsMap.get("reference_rate"))
//					.publicationDate(Integer.parseInt(dbRecorsMap.get("publication_date")))
//					.valueDate(Integer.parseInt(dbRecorsMap.get("value_date")))
//					.rate(Double.parseDouble(dbRecorsMap.get("rate"))).action(" ").status("ACTIVE")
//					.modifiedBy("Migration").modifiedDate(date).approvedBy("Migration").approvedDate(date)
//					.createdDate(date).effectiveDate(date).approvalRemark("Data Migration").workflowStatusId(null)
//					.build();
//			sssFloatingRateTempList.add(sssFloatingRate);
//		}
//		return sssFloatingRateTempList;
//	}

	public String getId(Connection con, String code) throws SQLException {
		String id = null;
		PreparedStatement ps = con.prepareStatement("select id from issuer where issuer_code='" + code + "'");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			id = rs.getString(1);
			break;
		}
		return id;

	}

	public void createAndSaveCbmCostCentreSourceData(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) {
		try {

			int lineCount = 0;
			for (String line : fileRecords) {
				lineCount++;
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				if (line == null || line.trim().isEmpty()) {
					JpaCbmCostCentreSource cbmCostCentreSource = new JpaCbmCostCentreSource();
					cbmCostCentreSource.setRemarks("No columns present");
					costCentreSourceDAO.save(cbmCostCentreSource);
					continue;
				}
				List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
				if (content.size() != 1) {
					JpaCbmCostCentreSource cbmCostCentreSource = new JpaCbmCostCentreSource();
					cbmCostCentreSource.setRemarks("Invalid number of columns received");
					costCentreSourceDAO.save(cbmCostCentreSource);
					continue;
				}
				if (content.size() != draftDBDetails.size()) {
					JpaCbmCostCentreSource cbmCostCentreSource = new JpaCbmCostCentreSource();
					cbmCostCentreSource
							.setRemarks("Header columns count in file upload properties not matching with sourcer");
					costCentreSourceDAO.save(cbmCostCentreSource);
					continue;
				}

				Map<String, String> dbRecorsMap = new HashMap<>();
				int count = 0;
				for (JpaFileUploadDetails db : draftDBDetails) {
					logger.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
					dbRecorsMap.put(db.getTableFieldName(), content.get(count));
					count++;
				}
				JpaCbmCostCentreSource costCentreSource = JpaCbmCostCentreSource.builder()// .costCentre(UUID.randomUUID().toString())
						.id(id).costCentre(dbRecorsMap.get("cost_centre")).description(null).createdDate(null)
						.modifiedDate(null).build();
				costCentreSourceDAO.save(costCentreSource);

			}
			logger.info("Total records in File: {}" + lineCount);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception while preparting source data ", e.getMessage());
		}

	}

	public void createAndSaveSecuritiesCodeSourceData(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) {
		try {

			int lineCount = 0;
			for (String line : fileRecords) {
				lineCount++;
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				if (line == null || line.trim().isEmpty()) {
					JpaSssSecuritiesCodeSource jpaSssSecuritiesCodeSource = new JpaSssSecuritiesCodeSource();
					jpaSssSecuritiesCodeSource.setRemarks("No columns present");
					sourceData.save(jpaSssSecuritiesCodeSource);
					continue;
				}
				List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
				if (content.size() != 22) {
					JpaSssSecuritiesCodeSource jpaSssSecuritiesCodeSource = new JpaSssSecuritiesCodeSource();
					jpaSssSecuritiesCodeSource.setRemarks("Invalid number of columns received");
					sourceData.save(jpaSssSecuritiesCodeSource);
					continue;
				}
				if (content.size() != draftDBDetails.size()) {
					JpaSssSecuritiesCodeSource jpaSssSecuritiesCodeSource = new JpaSssSecuritiesCodeSource();
					jpaSssSecuritiesCodeSource
							.setRemarks("Header columns count in file upload properties not matching with sourcer");
					sourceData.save(jpaSssSecuritiesCodeSource);
					continue;
				}

				Map<String, String> dbRecorsMap = new HashMap<>();
				int count = 0;
				for (JpaFileUploadDetails db : draftDBDetails) {
					logger.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
					dbRecorsMap.put(db.getTableFieldName(), content.get(count));
					count++;
				}

				JpaSssSecuritiesCodeSource jpaSssSecuritiesCodeSource = JpaSssSecuritiesCodeSource.builder().id(id)
						.securitiesCode(dbRecorsMap.get("securities_code")).issueCode(dbRecorsMap.get("issue_code"))
						.description(dbRecorsMap.get("description")).securitiesTypeId(null)
						.securitiesStatus(dbRecorsMap.get("securities_status")).securitiesTenorPeriodUnit(null)
						.currencyCode(dbRecorsMap.get("currency_code")).denomination(dbRecorsMap.get("denomination"))
						.issuerId(null).ipa(null).facilityEligibilityId(null).haircutId(null).tradable(null)
						.firstAuctionDate(dbRecorsMap.get("first_auction_date"))
						.firstIssuanceDate(dbRecorsMap.get("first_issuance_date"))
						.firstIssuanceAmount(dbRecorsMap.get("first_issuance_amount"))
						.redemptionDate(dbRecorsMap.get("redemption_date"))
						.optionalRedemptionDate(dbRecorsMap.get("optional_redemption_date"))
						.redemptionPrice(dbRecorsMap.get("redemption_price"))
						.optionalRedemptionPrice(dbRecorsMap.get("optional_redemption_price"))
						.centralBankAppliedAmount(dbRecorsMap.get("central_bank_applied_amount")).couponStructure(null)
						.couponPaymentFrequency(dbRecorsMap.get("coupon_payment_frequency"))
						.couponRate(dbRecorsMap.get("coupon_rate")).averageYield(dbRecorsMap.get("average_yield"))
						.dayCountConvention(null).roundingOption(null)
						.recordDatePeriod(dbRecorsMap.get("record_date_period")).action(null).status(null)
						.modifiedBy(null).modifiedDate(null).approvedBy(null).approvedDate(null).createdDate(null)
						.effectiveDate(null).approvalRemark(dbRecorsMap.get("approval_remark"))
						.workflowStatusId(dbRecorsMap.get("workflow_status_id"))
						.tenorPeriod(dbRecorsMap.get("tenor_period")).issuePrice(dbRecorsMap.get("issue_price"))
						.issueYield(dbRecorsMap.get("issue_yield")).redemptionReimburse(null)
						.taxSchemeId(dbRecorsMap.get("tax_scheme_id"))
						.firstCouponDate(dbRecorsMap.get("first_coupon_date")).benchmarkIndicator(null).build();
				sourceData.save(jpaSssSecuritiesCodeSource);
			}
			logger.info("Total records in File: {}" + lineCount);
		} catch (Exception e) {
			logger.error("Exception while preparting source data ", e.getMessage());
		}

	}

	public List<JpaCbmCostCentreTemp> convertToCbmCostCenterList() {
		List<JpaCbmCostCentreTemp> jpaCbmCostCentreTempList = new ArrayList<JpaCbmCostCentreTemp>();
		try {
			List<JpaCbmCostCentreSource> cbmCostCentreSourceList = costCentreSourceDAO.findAll();
			if (cbmCostCentreSourceList != null && !cbmCostCentreSourceList.isEmpty()) {
				List<String> errors = new ArrayList<String>();
				for (JpaCbmCostCentreSource sourceRec : cbmCostCentreSourceList) {
					if (sourceRec.getRemarks() == null) {

						if (errors.size() > 0) {
							sourceRec.setRemarks(String.join(", ", errors));
							costCentreSourceDAO.save(sourceRec);
							continue;
						}

						List<String> validationRemarks = new ArrayList<String>();
						if (validationRemarks.size() > 0) {
							sourceRec.setRemarks(String.join(", ", validationRemarks));
							costCentreSourceDAO.save(sourceRec);
							continue;
						}
						
						Date date = new Date();
						JpaCbmCostCentreTemp jpaCbmCostCentre = JpaCbmCostCentreTemp.builder().id(sourceRec.getId())
								.costCentre(sourceRec.getCostCentre()).description(" ").createdDate(date)
								.modifiedDate(date).build();
						jpaCbmCostCentreTempList.add(jpaCbmCostCentre);
					}
				}
			}

		} catch (Exception e) {
			logger.error("Error in temp {}", e.getMessage());
			e.printStackTrace();
		}
		return jpaCbmCostCentreTempList;
	}

	public List<JpaSssSecuritiesCodeTemp> convertToSssSecuritiesCodeList() {
		List<JpaSssSecuritiesCodeTemp> sssSecuritiesCodeList = new ArrayList<JpaSssSecuritiesCodeTemp>();

		try {
			List<JpaSssSecuritiesCodeSource> securitiesSourceList = sourceData.findAll();
			String id=userData.getSystemUserId();
			if (securitiesSourceList != null && !securitiesSourceList.isEmpty()) {
				List<String> errors = new ArrayList<String>();
				String masId = null;
				String agdId = null;
				try {
					masId = getId("MAS");
					agdId = getId("AGD");
				} catch (Exception e) {

					errors.add("error reading issuer data");
				}
				String facilityEligibilityId = null;
				String hairCutId = null;
				String benchMarkIndicator = null;
				String memberId = null;
				String taxSchemeId = null;
				try {
					List<JpaSssMember> memberList = sssMemberDAO.findByMemberType("CBK");
					if (memberList != null && !memberList.isEmpty())
						memberId = memberList.get(0).getId();
				} catch (Exception e) {
					errors.add("error reading member data");
				}
				try {
					List<JpaSecuritiesType> securitiesTypeList = securitiesTypeDao.findAll();
					if (securitiesTypeList != null && !securitiesTypeList.isEmpty()) {
						facilityEligibilityId = securitiesTypeList.get(0).getFacilityEligibilityId();
						hairCutId = securitiesTypeList.get(0).getHaircutId();
						benchMarkIndicator = securitiesTypeList.get(0).getBenchmarkIndicator();
					}
				} catch (Exception e) {
					errors.add("error reading securitiesTyp data");
				}
				try {
					List<JpaTaxScheme> taxSchemaList = taxSchemeDao.findByTaxSchemeId("SGWHT23");
					if (taxSchemaList != null && !taxSchemaList.isEmpty())
						taxSchemeId = taxSchemaList.get(0).getId();
				} catch (Exception e) {
					errors.add("error reading TaxScheme data");
				}
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
				for (JpaSssSecuritiesCodeSource sourceRec : securitiesSourceList) {
					if (sourceRec.getRemarks() == null) {

						if (errors.size() > 0) {
							sourceRec.setRemarks(String.join(", ", errors));
							sourceData.save(sourceRec);
							continue;
						}

						List<String> validationRemarks = new ArrayList<String>();
						String issuerId = null;
						String securityType = "null";
						String securitiesTenorPeriod = "daily";
						String dayCountConvention = " ";
						if (memberId == null)
							validationRemarks.add("Missing Member id for Mermber type CBK");

//						if (facilityEligibilityId == null || hairCutId == null || benchMarkIndicator == null)
//							validationRemarks.add("Missing SecuritiesType data");

						if (taxSchemeId == null)
							validationRemarks.add("Missing Tax Scheme data");

						if (masId == null || agdId == null)
							validationRemarks.add("Missing issuerId data");

						String couponStructure = sourceRec.getCouponStructure();

						Date issueDateAfterTwoHundDay = null;

						if (CommonUtils.validateParseDate_yyyyMMdd(sourceRec.getFirstIssuanceDate())) {
							Date firstIssueDate = simpleDateFormat.parse(sourceRec.getFirstIssuanceDate());
							Calendar c = Calendar.getInstance();
							c.setTime(firstIssueDate);
							c.add(Calendar.DAY_OF_YEAR, 200);
							String dateAfterSixMonths = simpleDateFormat.format(c.getTime());
							issueDateAfterTwoHundDay = simpleDateFormat.parse(dateAfterSixMonths);
						} else
							validationRemarks.add("Invalid firstIssuanceDate");

						Date redemptionDate = null;
						if (CommonUtils.validateParseDate_yyyyMMdd(sourceRec.getRedemptionDate()))
							redemptionDate = simpleDateFormat.parse(sourceRec.getRedemptionDate());
						else
							validationRemarks.add("Invalid redemptionDate");

						Double couponRate = null;
						if (sourceRec.getCouponRate() != null) {
							if (CommonUtils.validateParseDouble(sourceRec.getCouponRate()))
								couponRate = Double.parseDouble(sourceRec.getCouponRate());
							else
								validationRemarks.add("Invalid couponRate");
						}

						Integer recordDatePeriod = null;
						if (CommonUtils.validateParseInteger(sourceRec.getRecordDatePeriod()))
							recordDatePeriod = Integer.parseInt(sourceRec.getRecordDatePeriod()) + 1;
						else
							validationRemarks.add("Invalid recordDatePeriod");

						if ((sourceRec.getIssueCode()).startsWith("N") && (sourceRec.getIssueCode()).endsWith("2")) {
							securityType = "SGSISBond";
							couponStructure = "Fixed rate";
							securitiesTenorPeriod = "yearly";
							dayCountConvention = "30/360";
							issuerId = agdId;
						} else if (sourceRec.getIssueCode().startsWith("GX")) {
							securityType = "SSB";
							couponStructure = "Fixed Multiple";
							securitiesTenorPeriod = "yearly";
							couponRate = null;
							dayCountConvention = "30/360";
							issuerId = agdId;
						} else if (sourceRec.getIssueCode().startsWith("M1")) {
							securityType = "MASRIMFRN";
							couponStructure = "Floating rate";
							securitiesTenorPeriod = "monthly";
							couponRate = null;
							issuerId = masId;
							if (issueDateAfterTwoHundDay.compareTo(redemptionDate) <= 0) {
								dayCountConvention = "Actual/Actual";
							} else
								dayCountConvention = "Actual/365";
						} else if (sourceRec.getIssueCode().startsWith("GX")) {
							securityType = "SSB";
							couponStructure = "Fixed Multiple";
							securitiesTenorPeriod = "yearly";
							couponRate = null;
							dayCountConvention = "30/360";
							issuerId = agdId;
						} else if (sourceRec.getIssueCode().startsWith("M1")) {
							securityType = "MASRIMFRN";
							couponStructure = "Floating rate";
							securitiesTenorPeriod = "monthly";
							couponRate = null;
							issuerId = masId;
							if (issueDateAfterTwoHundDay.compareTo(redemptionDate) <= 0) {
								dayCountConvention = "Actual/Actual";
							} else
								dayCountConvention = "Actual/365";
						}
						Date date = new Date();
						if (!CommonUtils.validateParseLong(sourceRec.getDenomination()))
							validationRemarks.add("Invalid Denomination");
						if (!CommonUtils.validateParseLong(sourceRec.getCentralBankAppliedAmount()))
							validationRemarks.add("Invalid CentralBankAppliedAmount");
						if (!CommonUtils.validateParseLong(sourceRec.getFirstIssuanceAmount()))
							validationRemarks.add("Invalid FirstIssuanceAmount");
						if (!CommonUtils.validateParseDate_yyyy_MM_dd(sourceRec.getFirstAuctionDate()))
							validationRemarks.add("Invalid FirstAuctionDate");

						if (sourceRec.getWorkflowStatusId() != null
								&& !CommonUtils.validateParseInteger(sourceRec.getWorkflowStatusId()))
							validationRemarks.add("Invalid WorkflowStatusId");

						if (!CommonUtils.validateParseInteger(sourceRec.getTenorPeriod()))
							validationRemarks.add("Invalid TenorPeriod");
						if (!CommonUtils.validateParseDouble(sourceRec.getIssuePrice()))
							validationRemarks.add("Invalid IssuePrice");
						if (!CommonUtils.validateParseDouble(sourceRec.getIssueYield()))
							validationRemarks.add("Invalid IssueYield");
						if (!CommonUtils.validateParseDouble(sourceRec.getAverageYield()))
							validationRemarks.add("Invalid AverageYield");
						if (!CommonUtils.validateParseDouble(sourceRec.getOptionalRedemptionPrice()))
							validationRemarks.add("Invalid OptionalRedemptionPrice");
						if (!CommonUtils.validateParseDouble(sourceRec.getRedemptionPrice()))
							validationRemarks.add("Invalid RedemptionPrice");
						if (!CommonUtils.validateParseInteger(sourceRec.getFirstCouponDate()))
							validationRemarks.add("Invalid FirstCouponDate");

						if (validationRemarks.size() > 0) {
							sourceRec.setRemarks(String.join(", ", validationRemarks));
							sourceData.save(sourceRec);
							continue;
						}

						JpaSssSecuritiesCodeTemp jpaSssSecuritiesCode = (JpaSssSecuritiesCodeTemp.builder())
								.id(sourceRec.getId()).securitiesCode(sourceRec.getSecuritiesCode())
								.issueCode(sourceRec.getIssueCode()).description(sourceRec.getDescription())
								.securitiesTypeId(securityType).securitiesStatus(sourceRec.getSecuritiesStatus())
								.securitiesTenorPeriodUnit(securitiesTenorPeriod)
								.currencyCode(sourceRec.getCurrencyCode())
								.denomination(Long.parseLong(sourceRec.getDenomination())).issuerId(issuerId)
								.ipa(memberId).facilityEligibilityId(facilityEligibilityId).haircutId(hairCutId)
								.tradable(Short.parseShort("1"))
								.firstAuctionDate(
										new SimpleDateFormat("yyyy-MM-dd").parse(sourceRec.getFirstAuctionDate()))
								.firstIssuanceDate(Integer.parseInt(sourceRec.getFirstIssuanceDate()))
								.firstIssuanceAmount(Long.parseLong(sourceRec.getFirstIssuanceAmount()))
								.redemptionDate(Integer.parseInt(sourceRec.getRedemptionDate()))
								.optionalRedemptionDate(Integer.parseInt(sourceRec.getOptionalRedemptionDate()))
								.redemptionPrice(Double.parseDouble(sourceRec.getRedemptionPrice()))
								.optionalRedemptionPrice(Double.parseDouble(sourceRec.getOptionalRedemptionPrice()))
								.centralBankAppliedAmount(Long.parseLong(sourceRec.getCentralBankAppliedAmount()))
								.couponStructure(couponStructure)
								.couponPaymentFrequency(sourceRec.getCouponPaymentFrequency()).couponRate(couponRate)
								.averageYield(Double.parseDouble(sourceRec.getAverageYield()))
								.dayCountConvention(dayCountConvention).roundingOption("Nearest")
								.recordDatePeriod(recordDatePeriod).action(" ").status("ACTIVE").modifiedBy(id)
								.modifiedDate(date).approvedBy(id).approvedDate(date).createdDate(date)
								.effectiveDate(date).approvalRemark("Data Migration").workflowStatusId(null)
								.tenorPeriod(Integer.parseInt(sourceRec.getTenorPeriod()))
								.issuePrice(Double.parseDouble(sourceRec.getIssuePrice()))
								.issueYield(Double.parseDouble(sourceRec.getIssueYield()))// .redemptionReimburse(dbRecorsMap.get("redemption_reimburse"))
								.redemptionReimburse("Fixed maturity").taxSchemeId(taxSchemeId)
								.firstCouponDate(Integer.parseInt(sourceRec.getFirstCouponDate()))
								.benchmarkIndicator(benchMarkIndicator).build();

						sssSecuritiesCodeList.add(jpaSssSecuritiesCode);
					}
				}
			}
		} catch (Exception e) {
			// System.out.println("line -"+lineCount+" skip due to conversion issues");
			// System.out.println("exception -" + e);
			logger.error("Error in temp {}", e.getMessage());
			e.printStackTrace();
		}

		return sssSecuritiesCodeList;
	}

	public String getId(String code) throws SQLException {
		List<JpaIssuer> issuerList = issuerDao.findByIssuerCode(code);
		if (issuerList == null || issuerList.isEmpty())
			return null;
		else
			return issuerList.get(0).getId();
	}

	public List<JpaSssSecuritiesCodeStatisticsTemp> convertToSssSecuritiesCodeStatisticsList(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) {
		List<JpaSssSecuritiesCodeStatisticsTemp> securitiesCodeStatisticsList = new ArrayList<JpaSssSecuritiesCodeStatisticsTemp>();
		for (String line : fileRecords) {
			List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
			Map<String, String> dbRecorsMap = new HashMap<>();
			int count = 0;
			for (JpaFileUploadDetails db : draftDBDetails) {
				log.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
				dbRecorsMap.put(db.getTableFieldName(), content.get(count));
				count++;
			}
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			JpaSssSecuritiesCodeStatisticsSource SssSecuritiesCodeStatisticsSource = JpaSssSecuritiesCodeStatisticsSource
					.builder().id(id).securitiesCode(dbRecorsMap.get("securities_code"))
					.totalRedeemedAmount(dbRecorsMap.get("total_redeemed_amount"))
					.totalNominalAmountIssuedForErf(dbRecorsMap.get("total_nominal_amount_issued_for_erf"))
					.totalRedeemedAmountForErf(dbRecorsMap.get("total_redeemed_amount_for_erf"))
					.outstandingNominalAmount(dbRecorsMap.get("outstanding_nominal_amount"))
					.nextCouponDate(dbRecorsMap.get("next_coupon_date"))
					.lastCouponDate(dbRecorsMap.get("last_coupon_date"))
					.nextIndexEndDate(dbRecorsMap.get("next_index_end_date"))
					.amountAllotedToCentralBank(dbRecorsMap.get("amount_alloted_to_central_bank"))
					.amountAllotedToOthers(dbRecorsMap.get("amount_alloted_to_others")).build();
			securitiesCodeStatisticsSourceDAO.save(SssSecuritiesCodeStatisticsSource);
			JpaSssSecuritiesCodeStatisticsTemp SssSecuritiesCodeStatisticsTemp = JpaSssSecuritiesCodeStatisticsTemp
					.builder().id(id)
					.securitiesCode(dbRecorsMap.get("securities_code"))
					.totalRedeemedAmount(Long.parseLong(dbRecorsMap.get("total_redeemed_amount")))
					.totalRedeemedAmountForErf(Long.parseLong(dbRecorsMap.get("total_redeemed_amount_for_erf")))
					.totalNominalAmountIssuedForErf(Long.parseLong(dbRecorsMap.get("total_nominal_amount_issued_for_erf")))
					.outstandingNominalAmount(Long.parseLong(dbRecorsMap.get("outstanding_nominal_amount")))
					.nextCouponDate(Integer.parseInt(dbRecorsMap.get("next_coupon_date")))
					.lastCouponDate(Integer.parseInt(dbRecorsMap.get("last_coupon_date")))
					.nextIndexEndDate(Integer.parseInt(dbRecorsMap.get("next_index_end_date")))
					.amountAllotedToCentralBank(Long.parseLong(dbRecorsMap.get("amount_alloted_to_central_bank")))
					.amountAllotedToOthers(Long.parseLong(dbRecorsMap.get("amount_alloted_to_others"))).build();
			securitiesCodeStatisticsList.add(SssSecuritiesCodeStatisticsTemp);
		}
		return securitiesCodeStatisticsList;
	}

	public void createAndSaveCbmDepositRateSourceData(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) {
		try {
			int lineCount = 0;
			for (String line : fileRecords) {
				lineCount++;
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				if (line == null || line.trim().isEmpty()) {
					JpaCbmDepositRateSource jpaDepositRateSource = new JpaCbmDepositRateSource();
					jpaDepositRateSource.setRemarks("No columns present");
					cbmDepositRateSourceDAO.save(jpaDepositRateSource);
					continue;
				}
				List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
				if (content.size() != 2) {
					JpaCbmDepositRateSource jpaDepositRateSource = new JpaCbmDepositRateSource();
					jpaDepositRateSource.setRemarks("Invalid number of columns received");
					cbmDepositRateSourceDAO.save(jpaDepositRateSource);
					continue;
				}
				if (content.size() != draftDBDetails.size()) {
					JpaCbmDepositRateSource jpaDepositRateSource = new JpaCbmDepositRateSource();
					jpaDepositRateSource
							.setRemarks("Header columns count in file upload properties not matching with sourcer");
					cbmDepositRateSourceDAO.save(jpaDepositRateSource);
					continue;
				}

				Map<String, String> dbRecorsMap = new HashMap<>();
				int count = 0;
				for (JpaFileUploadDetails db : draftDBDetails) {
					logger.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
					dbRecorsMap.put(db.getTableFieldName(), content.get(count));
					count++;
				}
				JpaCbmDepositRateSource jpaCbmDepositRateSource = JpaCbmDepositRateSource.builder().id(id)
						.tenorPeriod(dbRecorsMap.get("tenor_period")).depositRate(dbRecorsMap.get("deposit_rate"))
						.facilityId(null).action(null).status(null).modifiedBy(null).modifiedDate(null).approvedBy(null)
						.approvedDate(null).createdDate(null).effectiveDate(null).approvalRemark(null)
						.workflowStatusId(null).build();
				cbmDepositRateSourceDAO.save(jpaCbmDepositRateSource);
			}
			logger.info("Total records in File: {}" + lineCount);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception while preparting source data ", e.getMessage());
		}
	}

	public List<JpaCbmDepositRateTemp> convertToJpaCbmDepositRateList() {
		List<JpaCbmDepositRateTemp> jpaCbmDepositRateList = new ArrayList<JpaCbmDepositRateTemp>();
		Date date = new Date();
		try {
			List<JpaCbmDepositRateSource> cbmDepositRateSource = cbmDepositRateSourceDAO.findAll();
			String id=userData.getSystemUserId();
			if (cbmDepositRateSource != null && !cbmDepositRateSource.isEmpty()) {
				List<String> errors = new ArrayList<String>();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (JpaCbmDepositRateSource sourceRec : cbmDepositRateSource) {
					if (sourceRec.getRemarks() == null) {
						List<String> validationRemarks = new ArrayList<String>();
						if (errors.size() > 0) {
							sourceRec.setRemarks(String.join(", ", errors));
							cbmDepositRateSourceDAO.save(sourceRec);
							continue;
						}

						if (!CommonUtils.validateParseInteger(sourceRec.getTenorPeriod()))
							validationRemarks.add("Invalid Tenor Period");
						if (!CommonUtils.validateParseDouble(sourceRec.getDepositRate()))
							validationRemarks.add("Invalid Deposit Rate");
						
						
						if (validationRemarks.size() > 0) {
							sourceRec.setRemarks(String.join(", ", validationRemarks));
							cbmDepositRateSourceDAO.save(sourceRec);
							continue;
						}

						JpaCbmDepositRateTemp jpaCbmDepositRate = JpaCbmDepositRateTemp.builder().id(sourceRec.getId())
								.tenorPeriod(Integer.parseInt(sourceRec.getTenorPeriod()))
								.depositRate(Double.parseDouble(sourceRec.getDepositRate())).facilityId("SF")
								.action(" ").status("ACTIVE").modifiedBy(id).modifiedDate(date)
								.approvedBy(id).approvedDate(date).createdDate(date).effectiveDate(date)
								.approvalRemark("Data Migration").workflowStatusId(null).build();
						jpaCbmDepositRateList.add(jpaCbmDepositRate);

					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in temp {}", e.getMessage());
			e.printStackTrace();
		}
		return jpaCbmDepositRateList;
	}

	public void createAndSaveCbmLiabilitiesBaseSourceData(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) {
		try {
			int lineCount = 0;
			for (String line : fileRecords) {
				lineCount++;
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				if (line == null || line.trim().isEmpty()) {
					JpaCbmLiabilitiesBaseSource jpaCbmLiabilitiesBaseSource = new JpaCbmLiabilitiesBaseSource();
					jpaCbmLiabilitiesBaseSource.setRemarks("No columns present");
					cbmLiabilitiesBaseSourceDAO.save(jpaCbmLiabilitiesBaseSource);
					continue;
				}
				List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
				if (content.size() != 4) {
					JpaCbmLiabilitiesBaseSource jpaCbmLiabilitiesBaseSource = new JpaCbmLiabilitiesBaseSource();
					jpaCbmLiabilitiesBaseSource.setRemarks("Invalid number of columns received");
					cbmLiabilitiesBaseSourceDAO.save(jpaCbmLiabilitiesBaseSource);
					continue;
				}
				if (content.size() != draftDBDetails.size()) {
					JpaCbmLiabilitiesBaseSource jpaCbmLiabilitiesBaseSource = new JpaCbmLiabilitiesBaseSource();
					jpaCbmLiabilitiesBaseSource
							.setRemarks("Header columns count in file upload properties not matching with sourcer");
					cbmLiabilitiesBaseSourceDAO.save(jpaCbmLiabilitiesBaseSource);
					continue;
				}

				Map<String, String> dbRecorsMap = new HashMap<>();
				int count = 0;
				for (JpaFileUploadDetails db : draftDBDetails) {
					logger.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
					dbRecorsMap.put(db.getTableFieldName(), content.get(count));
					count++;
				}
				JpaCbmLiabilitiesBaseSource jpaCbmLiabilitiesBaseSource = JpaCbmLiabilitiesBaseSource.builder().id(id)
						.memberId(dbRecorsMap.get("member_code")).sectorId(null)
						.currencyCode(dbRecorsMap.get("currency_code")).mcbId(null)
						.qlLbType(dbRecorsMap.get("ql_lb_type")).action(null).status(null).modifiedBy(null)
						.modifiedDate(null).approvedBy(null).approvedDate(null).createdDate(null)
						.effectiveDate(dbRecorsMap.get("effective_date")).approvalRemark(null).workflowStatusId(null)
						.build();

				cbmLiabilitiesBaseSourceDAO.save(jpaCbmLiabilitiesBaseSource);

			}
			logger.info("Total records in File: {}" + lineCount);
		} catch (Exception e) {

			e.printStackTrace();
			logger.error("Exception while preparting source data ", e.getMessage());

		}

	}

	public void createAndSaveCbmLiabilitiesBaseDetailSourceData(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) {
		try {
			int lineCount = 0;
			for (String line : fileRecords) {
				lineCount++;
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				if (line == null || line.trim().isEmpty()) {
					JpaCbmLiabilitiesBaseDetailSource jpaCbmLiabilitiesBaseDetailSource = new JpaCbmLiabilitiesBaseDetailSource();
					jpaCbmLiabilitiesBaseDetailSource.setRemarks("No columns present");
					cbmLiabilitiesBaseDetailSourceDAO.save(jpaCbmLiabilitiesBaseDetailSource);
					continue;
				}
				List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
				if (content.size() != 4) {
					JpaCbmLiabilitiesBaseDetailSource jpaCbmLiabilitiesBaseDetailSource = new JpaCbmLiabilitiesBaseDetailSource();
					jpaCbmLiabilitiesBaseDetailSource.setRemarks("Invalid number of columns received");
					cbmLiabilitiesBaseDetailSourceDAO.save(jpaCbmLiabilitiesBaseDetailSource);
					continue;
				}
				if (content.size() != draftDBDetails.size()) {
					JpaCbmLiabilitiesBaseDetailSource jpaCbmLiabilitiesBaseDetailSource = new JpaCbmLiabilitiesBaseDetailSource();
					jpaCbmLiabilitiesBaseDetailSource
							.setRemarks("Header columns count in file upload properties not matching with sourcer");
					cbmLiabilitiesBaseDetailSourceDAO.save(jpaCbmLiabilitiesBaseDetailSource);
					continue;
				}

				Map<String, String> dbRecorsMap = new HashMap<>();
				int count = 0;
				for (JpaFileUploadDetails db : draftDBDetails) {
					logger.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
					dbRecorsMap.put(db.getTableFieldName(), content.get(count));
					count++;
				}
				JpaCbmLiabilitiesBaseDetailSource jpaCbmLiabilitiesBaseDetailSource = JpaCbmLiabilitiesBaseDetailSource
						.builder().id(id).memberId(dbRecorsMap.get("member_code"))
						.startDate(dbRecorsMap.get("start_date")).endDate(dbRecorsMap.get("end_date"))
						.qlLb(dbRecorsMap.get("ql_lb")).build();

				cbmLiabilitiesBaseDetailSourceDAO.save(jpaCbmLiabilitiesBaseDetailSource);
			}
			logger.info("Total records in File: {}" + lineCount);
		} catch (Exception e) {

			e.printStackTrace();
			logger.error("Exception while preparting source data ", e.getMessage());

		}

	}

	public List<JpaCbmLiabilitiesBaseTemp> convertToJpaCbmLiabilitiesBaseList() {
		Date date = new Date();
		List<JpaCbmLiabilitiesBaseTemp> jpaCbmLiabilitiesBaseTempList = new ArrayList<JpaCbmLiabilitiesBaseTemp>();
		try {
			List<JpaCbmLiabilitiesBaseSource> cbmLiabilitiesBaseSource = cbmLiabilitiesBaseSourceDAO.findAll();
			String id=userData.getSystemUserId();
			if (cbmLiabilitiesBaseSource != null && !cbmLiabilitiesBaseSource.isEmpty()) {
				List<String> errors = new ArrayList<String>();
				String member_id = null;
				String mcb_id = null;
				String sector_id = null;

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (JpaCbmLiabilitiesBaseSource sourceRec : cbmLiabilitiesBaseSource) {
					if (sourceRec.getRemarks() == null) {
						List<String> validationRemarks = new ArrayList<String>();
						if (errors.size() > 0) {
							sourceRec.setRemarks(String.join(", ", errors));
							cbmLiabilitiesBaseSourceDAO.save(sourceRec);
							continue;
						}
						try {
							List<JpaMember> memberList = memberDAO.findByMemberCode(sourceRec.getMemberId());
							if (memberList != null && !memberList.isEmpty()) {
								member_id = memberList.get(0).getId();
								sector_id = memberList.get(0).getSectorId();
								mcb_id = memberList.get(0).getMcbId();
							}
						} catch (Exception e) {
							validationRemarks.add("member code is not present in member table");
						}

						Date effectiveDate = null;
						if (sourceRec.getEffectiveDate() != null) {
							if (CommonUtils.validateParseDate_yyyyMMdd_HHmmss(sourceRec.getEffectiveDate()))
								effectiveDate = simpleDateFormat.parse(sourceRec.getEffectiveDate());
							else
								validationRemarks.add("Invalid Effective Date");
						}

						if (validationRemarks.size() > 0) {
							sourceRec.setRemarks(String.join(", ", validationRemarks));
							cbmLiabilitiesBaseSourceDAO.save(sourceRec);
							continue;
						}

						JpaCbmLiabilitiesBaseTemp jpaCbmLiabilitiesBase = JpaCbmLiabilitiesBaseTemp.builder()
								.id(sourceRec.getId()).memberId(member_id).sectorId(sector_id)
								.currencyCode(sourceRec.getCurrencyCode()).mcbId(mcb_id)
								.qlLbType(sourceRec.getQlLbType()).action(" ").status("ACTIVE").modifiedBy(id)
								.modifiedDate(date).approvedBy(id).approvedDate(date).createdDate(date)
								.effectiveDate(effectiveDate).approvalRemark("Data Migration").workflowStatusId(null)
								.build();
						jpaCbmLiabilitiesBaseTempList.add(jpaCbmLiabilitiesBase);

					}
				}
			}
		} catch (Exception e) {
			// System.out.println("line -"+lineCount+" skip due to conversion issues");
			// System.out.println("exception -" + e);
			logger.error("Error in temp {}", e.getMessage());
			e.printStackTrace();
		}
		return jpaCbmLiabilitiesBaseTempList;

	}

	public List<JpaCbmLiabilitiesBaseDetailTemp> convertToJpaCbmLiabilitiesBaseDetailList() {

		Date date = new Date();
		List<JpaCbmLiabilitiesBaseDetailTemp> jpaCbmLiabilitiesBaseDetailTempList = new ArrayList<JpaCbmLiabilitiesBaseDetailTemp>();
		try {
			List<JpaCbmLiabilitiesBaseDetailSource> cbmLiabilitiesBaseDetailSource = cbmLiabilitiesBaseDetailSourceDAO
					.findAll();
			if (cbmLiabilitiesBaseDetailSource != null && !cbmLiabilitiesBaseDetailSource.isEmpty()) {
				List<String> errors = new ArrayList<String>();
				String member_id = null;

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (JpaCbmLiabilitiesBaseDetailSource sourceRec : cbmLiabilitiesBaseDetailSource) {
					if (sourceRec.getRemarks() == null) {
						List<String> validationRemarks = new ArrayList<String>();
						if (errors.size() > 0) {
							sourceRec.setRemarks(String.join(", ", errors));
							cbmLiabilitiesBaseDetailSourceDAO.save(sourceRec);
							continue;
						}
						try {
							List<JpaMember> memberList = memberDAO.findByMemberCode(sourceRec.getMemberId());
							if (memberList != null && !memberList.isEmpty()) {
								member_id = memberList.get(0).getId();
							}
						} catch (Exception e) {
							validationRemarks.add("member code is not present in member table");
						}

						Date startDate = null;
						if (sourceRec.getStartDate() != null) {
							if (CommonUtils.validateParseDate_yyyyMMdd_HHmmss(sourceRec.getStartDate()))
								startDate = simpleDateFormat.parse(sourceRec.getStartDate());
							else
								validationRemarks.add("Invalid Start Date");
						}
						Date endDate = null;
						if (sourceRec.getEndDate() != null) {
							if (CommonUtils.validateParseDate_yyyyMMdd_HHmmss(sourceRec.getEndDate()))
								endDate = simpleDateFormat.parse(sourceRec.getEndDate());
							else
								validationRemarks.add("Invalid End Date");
						}

						if (!CommonUtils.validateParseDouble(sourceRec.getQlLb()))
							validationRemarks.add("Invalid QL_LB");

						if (validationRemarks.size() > 0) {
							sourceRec.setRemarks(String.join(", ", validationRemarks));
							cbmLiabilitiesBaseDetailSourceDAO.save(sourceRec);
							continue;
						}

						JpaCbmLiabilitiesBaseDetailTemp jpaCbmLiabilitiesBaseDetail = JpaCbmLiabilitiesBaseDetailTemp
								.builder().id(sourceRec.getId()).memberId(member_id).startDate(startDate)
								.endDate(endDate).qlLb(Double.parseDouble(sourceRec.getQlLb())).build();
						jpaCbmLiabilitiesBaseDetailTempList.add(jpaCbmLiabilitiesBaseDetail);
					}
				}
			}

		} catch (Exception e) {
			logger.error("Error in temp {}", e.getMessage());
			e.printStackTrace();
		}
		return jpaCbmLiabilitiesBaseDetailTempList;

	}

	public List<JpaSssMember> convertToSssMemberList(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) throws NumberFormatException, ParseException {

		List<JpaSssMember> sssmemberList = new ArrayList<JpaSssMember>();
		for (String line : fileRecords) {

			List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
			Map<String, String> dbRecorsMap = new HashMap<>();
			int count = 0;
			for (JpaFileUploadDetails db : draftDBDetails) {

				log.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
				dbRecorsMap.put(db.getTableFieldName(), content.get(count));
				count++;

			}
			try {

				JpaSssMember jpaSssMember = JpaSssMember.builder().id(UUID.randomUUID().toString())
//				.memberCode(dbRecorsMap.get("member_code"))
//				.memberName(dbRecorsMap.get("member_name"))
//				.activationDate(dbRecorsMap.get("activation_date"))
//				// .activationDate(new SimpleDateFormat("yyyyDDmm").parse(dbRecorsMap.get("activation_date")))
//				.memberShortName(dbRecorsMap.get("member_short_name"))
//				.memberStatus(dbRecorsMap.get("member_status"))
//				.memberType(Integer.parseInt(dbRecorsMap.get("member_type")))
//				// .memberType(dbRecorsMap.get("member_type"))
//				.sectorId(dbRecorsMap.get("sector_id"))
//				.bankCode(dbRecorsMap.get("bank_code"))
//				.autoDebitIndicator(dbRecorsMap.get("auto_debit_indicator"))
//				.location(dbRecorsMap.get("location"))
//				.facilityEligibilityId(dbRecorsMap.get("facility_eligibility_id"))
//				.lei(dbRecorsMap.get("lei"))
//				.uen(dbRecorsMap.get("uen"))
//				.zeroHoldingsStatement(dbRecorsMap.get("zero_holdings_statement"))
//				.exemptedFromBilling(dbRecorsMap.get("exempted_from_billing"))
//				.exemptedFromSystemLimit(dbRecorsMap.get("exempted_from_system_limit"))
//				// .exemptedFromSystemLimit(Integer.parseInt(dbRecorsMap.get("exempted_from_system_limit")))
//				.statementDeliveryBic(dbRecorsMap.get("statement_delivery_bic"))
//				.taxProfileId(dbRecorsMap.get("tax_profile_id"))
//				.feeProfileId(dbRecorsMap.get("fee_profile_id"))
//				.endOfDayStatement(dbRecorsMap.get("end_of_day_statement"))
//				.build();
//				sssmemberList.add(jpaSssMember);

						.memberCode(dbRecorsMap.get("member_code")).name(dbRecorsMap.get("name"))
						.shortName(dbRecorsMap.get("short_name")).memberType(dbRecorsMap.get("member_type"))
						.memberStatus(dbRecorsMap.get("member_status"))
						// .activationDate(dbRecorsMap.get("activation_date"))
						.activationDate(new SimpleDateFormat("yyyyDDmm").parse(dbRecorsMap.get("activation_date")))
						.swiftBic(dbRecorsMap.get("swift_bic")).location(dbRecorsMap.get("location"))

						.fiCode(dbRecorsMap.get("fi_code")).traderStatus(dbRecorsMap.get("trader_status"))
						.sectorId(dbRecorsMap.get("sector_id"))
						.autoDebitIndicator(Short.parseShort(dbRecorsMap.get("auto_debit_indicator")))
						.facilityEligibilityId(dbRecorsMap.get("facility_eligibility_id")).lei(dbRecorsMap.get("lei"))
						.uen(dbRecorsMap.get("uen")).statementDeliveryBic(dbRecorsMap.get("statement_delivery_bic"))
						.endOfDayStatement(dbRecorsMap.get("end_of_day_statement"))

						.exemptedFromBilling(Short.parseShort(dbRecorsMap.get("exempted_from_billing")))
						.exemptedFromSystemLimit(Short.parseShort(dbRecorsMap.get("exempted_from_system_limit")))
						// .exemptedFromSystemLimit(Integer.parseInt(dbRecorsMap.get("exempted_from_system_limit")))
						.zeroHoldingsStatement(dbRecorsMap.get("zero_holdings_statement"))
						.rtgsAccount(dbRecorsMap.get("rtgs_account")).taxProfileId(dbRecorsMap.get("tax_profile_id"))
						.billingProfileId(dbRecorsMap.get("billing_profile_id")).action(dbRecorsMap.get("action"))
						.status(dbRecorsMap.get("status")).modifiedBy(dbRecorsMap.get("modified_by"))
						.modifiedDate(
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dbRecorsMap.get("modified_date")))
						.approvedBy(dbRecorsMap.get("approved_by"))
						.approvedDate(
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dbRecorsMap.get("approved_date")))
						.createdDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dbRecorsMap.get("created_date")))
						.effectiveDate(
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dbRecorsMap.get("effective_date")))
						.workflowStatusId(Integer.parseInt(dbRecorsMap.get("work_flow_status_id")))
						.approvalRemark("Data Migration").build();
				sssmemberList.add(jpaSssMember);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sssmemberList;
	}

	public List<JpaCbmCostCentreTemp> convertToCbmCostCenterList(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) {
		List<JpaCbmCostCentreTemp> jpaCbmCostCentreList = new ArrayList<JpaCbmCostCentreTemp>();
		int lineCount = 0;
		for (String line : fileRecords) {
			lineCount++;
			List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
			Map<String, String> dbRecorsMap = new HashMap<>();
			int count = 0;
			for (JpaFileUploadDetails db : draftDBDetails) {

				log.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
				dbRecorsMap.put(db.getTableFieldName(), content.get(count));
				count++;

			}

			try {
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				Date d = new Date();
				JpaCbmCostCentreSource costCentreSource = JpaCbmCostCentreSource.builder()// .costCentre(UUID.randomUUID().toString())
						.id(id).costCentre(dbRecorsMap.get("cost_centre")).description(null).createdDate(null)
						.modifiedDate(null).build();
				costCentreSourceDAO.save(costCentreSource);
				JpaCbmCostCentreTemp jpaCbmCostCentre = JpaCbmCostCentreTemp.builder()// .costCentre(UUID.randomUUID().toString())
						.id(id).costCentre(dbRecorsMap.get("cost_centre")).description(" ").createdDate(d)
						.modifiedDate(d).build();
				jpaCbmCostCentreList.add(jpaCbmCostCentre);

			} catch (Exception e) {
				System.out.println("line -" + lineCount + " skip due to conversion issues");
				System.out.println("exception -" + e);
			}
		}
		return jpaCbmCostCentreList;
	}

	public List<JpaCbmGlAccountTemp> convertToCbmGlAccountList(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) throws ParseException {
		List<JpaCbmGlAccountTemp> cbmGlAccountTemp = new ArrayList<JpaCbmGlAccountTemp>();
		for (String line : fileRecords) {

			List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
			Map<String, String> dbRecorsMap = new HashMap<>();
			int count = 0;
			for (JpaFileUploadDetails db : draftDBDetails) {
				log.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
				dbRecorsMap.put(db.getTableFieldName(), content.get(count));
				count++;

			}
			try {
				Date date = new Date();
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				JpaCbmGlAccountSource glAccountSource = JpaCbmGlAccountSource.builder().id(id)
						// .costCentre(dbRecorsMap.get("cost_centre"))
						.glAccountIndicator(null).glAccount(dbRecorsMap.get("gl_account"))
						.glAccountDescription(dbRecorsMap.get("gl_account_description")).createdDate(null)
						.modifiedDate(null).build();
				glAccountSourceDAO.save(glAccountSource);
				JpaCbmGlAccountTemp jpaCbmGlAccountTemp = JpaCbmGlAccountTemp.builder()//
						.id(id)
						// .costCentre(dbRecorsMap.get("cost_centre"))
						.glAccountIndicator("GL Account1").glAccount(dbRecorsMap.get("gl_account"))
						.glAccountDescription(dbRecorsMap.get("gl_account_description")).createdDate(date)
						.modifiedDate(date).build();

				cbmGlAccountTemp.add(jpaCbmGlAccountTemp);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cbmGlAccountTemp;
	}

	public void createAndSaveCbmFloatingRateSourceData(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) {
		try {
			int lineCount = 0;
			for (String line : fileRecords) {
				lineCount++;
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				if (line == null || line.trim().isEmpty()) {
					JpaCbmFloatingRateSource jpaCbmFloatingRateSource = new JpaCbmFloatingRateSource();
					jpaCbmFloatingRateSource.setRemarks("No columns present");
					cbmFloatingRateSourceDAO.save(jpaCbmFloatingRateSource);
					continue;
				}
				List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
				if (content.size() != 4) {
					JpaCbmFloatingRateSource jpaCbmFloatingRateSource = new JpaCbmFloatingRateSource();
					jpaCbmFloatingRateSource.setRemarks("Invalid number of columns received");
					cbmFloatingRateSourceDAO.save(jpaCbmFloatingRateSource);
					continue;
				}
				if (content.size() != draftDBDetails.size()) {
					JpaCbmFloatingRateSource jpaCbmFloatingRateSource = new JpaCbmFloatingRateSource();
					jpaCbmFloatingRateSource
							.setRemarks("Header columns count in file upload properties not matching with sourcer");
					cbmFloatingRateSourceDAO.save(jpaCbmFloatingRateSource);
					continue;
				}

				Map<String, String> dbRecorsMap = new HashMap<>();
				int count = 0;
				for (JpaFileUploadDetails db : draftDBDetails) {
					logger.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
					dbRecorsMap.put(db.getTableFieldName(), content.get(count));
					count++;
				}
				JpaCbmFloatingRateSource cbmFloatingRateSource = JpaCbmFloatingRateSource.builder().id(id)
						.referenceRate(dbRecorsMap.get("reference_rate"))
						.publicationDate(dbRecorsMap.get("publication_date")).valueDate(dbRecorsMap.get("value_date"))
						.rate(dbRecorsMap.get("rate")).action(null).status(null).modifiedBy(null).modifiedDate(null)
						.approvedBy(null).approvedDate(null).createdDate(null).effectiveDate(null).approvalRemark(null)
						.workflowStatusId(null).build();
				cbmFloatingRateSourceDAO.save(cbmFloatingRateSource);

			}
			logger.info("Total records in File: {}" + lineCount);
		} catch (Exception e) {

			e.printStackTrace();
			logger.error("Exception while preparting source data ", e.getMessage());

		}

	}

	public List<JpaCbmFloatingRateTemp> convertToCbmfloatingrateList() {
		List<JpaCbmFloatingRateTemp> cbmFloatingRateTempList = new ArrayList<JpaCbmFloatingRateTemp>();
		Date date = new Date();
		try {
			List<JpaCbmFloatingRateSource> cbmFloatingRateSource = cbmFloatingRateSourceDAO.findAll();
			String id=userData.getSystemUserId();
			if (cbmFloatingRateSource != null && !cbmFloatingRateSource.isEmpty()) {
				List<String> errors = new ArrayList<String>();
				for (JpaCbmFloatingRateSource sourceRec : cbmFloatingRateSource) {
					if (sourceRec.getRemarks() == null) {
						List<String> validationRemarks = new ArrayList<String>();
						if (errors.size() > 0) {
							sourceRec.setRemarks(String.join(", ", errors));
							cbmFloatingRateSourceDAO.save(sourceRec);
							continue;
						}

						if (!CommonUtils.validateParseInteger(sourceRec.getPublicationDate()))
							validationRemarks.add("Invalid Publication Date");
						if (!CommonUtils.validateParseInteger(sourceRec.getValueDate()))
							validationRemarks.add("Invalid Value Date");
						if (!CommonUtils.validateParseDouble(sourceRec.getRate()))
							validationRemarks.add("Invalid Rate");

						if (validationRemarks.size() > 0) {
							sourceRec.setRemarks(String.join(", ", validationRemarks));
							cbmFloatingRateSourceDAO.save(sourceRec);
							continue;
						}

						JpaCbmFloatingRateTemp jpaCbmFloatingRate = JpaCbmFloatingRateTemp.builder()
								.id(sourceRec.getId()).referenceRate(sourceRec.getReferenceRate())
								.publicationDate(Integer.parseInt(sourceRec.getPublicationDate()))
								.valueDate(Integer.parseInt(sourceRec.getValueDate()))
								.rate(Double.parseDouble(sourceRec.getRate())).action(" ").status("ACTIVE")
								.modifiedBy(id).modifiedDate(date).approvedBy(id).approvedDate(date)
								.createdDate(date).effectiveDate(date).approvalRemark("Data Migration")
								.workflowStatusId(null).build();
						cbmFloatingRateTempList.add(jpaCbmFloatingRate);

					}
				}
			}

		} catch (Exception e) {
			// System.out.println("line -"+lineCount+" skip due to conversion issues");
			// System.out.println("exception -" + e);
			logger.error("Error in temp {}", e.getMessage());
			e.printStackTrace();
		}
		return cbmFloatingRateTempList;
	}

	public void createAndSaveCbmGlAccountSourceData(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) {
		try {

			int lineCount = 0;
			for (String line : fileRecords) {
				lineCount++;

				if (line == null || line.trim().isEmpty()) {
					JpaCbmGlAccountSource cbmGlAccountSource = new JpaCbmGlAccountSource();
					cbmGlAccountSource.setRemarks("No columns present");
					glAccountSourceDAO.save(cbmGlAccountSource);
					continue;
				}
				List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
				if (content.size() != 2) {
					JpaCbmGlAccountSource cbmGlAccountSource = new JpaCbmGlAccountSource();
					cbmGlAccountSource.setRemarks("Invalid number of columns received");
					glAccountSourceDAO.save(cbmGlAccountSource);
					continue;
				}
				if (content.size() != draftDBDetails.size()) {
					JpaCbmGlAccountSource cbmGlAccountSource = new JpaCbmGlAccountSource();
					cbmGlAccountSource
							.setRemarks("Header columns count in file upload properties not matching with sourcer");
					glAccountSourceDAO.save(cbmGlAccountSource);
					continue;
				}

				Map<String, String> dbRecorsMap = new HashMap<>();
				int count = 0;
				for (JpaFileUploadDetails db : draftDBDetails) {
					logger.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
					dbRecorsMap.put(db.getTableFieldName(), content.get(count));
					count++;
				}
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				JpaCbmGlAccountSource glAccountSource = JpaCbmGlAccountSource.builder().id(id).glAccountIndicator(null)
						.glAccount(dbRecorsMap.get("gl_account"))
						.glAccountDescription(dbRecorsMap.get("gl_account_description")).createdDate(null)
						.modifiedDate(null).build();
				glAccountSourceDAO.save(glAccountSource);

			}
			logger.info("Total records in File: {}" + lineCount);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception while preparting source data ", e.getMessage());
		}

	}

	public List<JpaCbmGlAccountTemp> convertToCbmGlAccountList() {
		List<JpaCbmGlAccountTemp> cbmGlAccountTempList = new ArrayList<JpaCbmGlAccountTemp>();
		try {
			List<JpaCbmGlAccountSource> cbmGlAccountSourceList = glAccountSourceDAO.findAll();
			if (cbmGlAccountSourceList != null && !cbmGlAccountSourceList.isEmpty()) {
				List<String> errors = new ArrayList<String>();
				for (JpaCbmGlAccountSource sourceRec : cbmGlAccountSourceList) {
					if (sourceRec.getRemarks() == null) {

						if (errors.size() > 0) {
							sourceRec.setRemarks(String.join(", ", errors));
							glAccountSourceDAO.save(sourceRec);
							continue;
						}

						List<String> validationRemarks = new ArrayList<String>();
						if (validationRemarks.size() > 0) {
							sourceRec.setRemarks(String.join(", ", validationRemarks));
							glAccountSourceDAO.save(sourceRec);
							continue;
						}
						
						Date date = new Date();
						JpaCbmGlAccountTemp jpaCbmGlAccountTemp = JpaCbmGlAccountTemp.builder()//
								.id(sourceRec.getId())
								// .costCentre(dbRecorsMap.get("cost_centre"))
								.glAccountIndicator("Account 1").glAccount(sourceRec.getGlAccount())
								.glAccountDescription(sourceRec.getGlAccountDescription()).createdDate(date)
								.modifiedDate(date).build();
						cbmGlAccountTempList.add(jpaCbmGlAccountTemp);
					}
				}
			}

		} catch (Exception e) {
			logger.error("Error in temp {}", e.getMessage());
			e.printStackTrace();
		}
		return cbmGlAccountTempList;
	}

	public void createAndSaveAccountPositionSourceData(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) {
		try {

			int lineCount = 0;
			for (String line : fileRecords) {
				lineCount++;
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				if (line == null || line.trim().isEmpty()) {
					JpaAccountPositionSource accountPositionSource = new JpaAccountPositionSource();
					accountPositionSource.setRemarks("No columns present");
					accountPosSourceDAO.save(accountPositionSource);
					continue;
				}
				List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
				if (content.size() != 7) {
					JpaAccountPositionSource accountPositionSource = new JpaAccountPositionSource();
					accountPositionSource.setRemarks("Invalid number of columns received");
					accountPosSourceDAO.save(accountPositionSource);
					continue;
				}
				if (content.size() != draftDBDetails.size()) {
					JpaAccountPositionSource accountPositionSource = new JpaAccountPositionSource();
					accountPositionSource
							.setRemarks("Header columns count in file upload properties not matching with sourcer");
					accountPosSourceDAO.save(accountPositionSource);
					continue;
				}

				Map<String, String> dbRecorsMap = new HashMap<>();
				int count = 0;
				for (JpaFileUploadDetails db : draftDBDetails) {
					logger.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
					dbRecorsMap.put(db.getTableFieldName(), content.get(count));
					count++;
				}
				JpaAccountPositionSource accountPosSource = JpaAccountPositionSource.builder().id(id)
						.accountId(dbRecorsMap.get("account_id")).currencyCode(dbRecorsMap.get("currency_code"))
						.memberId(dbRecorsMap.get("member_code"))
						.currentAccountBalance(dbRecorsMap.get("current_account_balance"))
						.openingAccountBalance(dbRecorsMap.get("opening_account_balance"))
						.availableBalance(dbRecorsMap.get("available_balance")).queueCount(null).queueAmount(null)
						.settledPaymentsCount(null).settledPaymentsAmount(null).settledReceiptsCount(null)
						.settledReceiptsAmount(null).createdDate(dbRecorsMap.get("created_date")).modifiedDate(null)
						.build();
				accountPosSourceDAO.save(accountPosSource);

			}
			logger.info("Total records in File: {}" + lineCount);
		} catch (Exception e) {
			logger.error("Exception while preparting source data ", e.getMessage());
		}

	}

	public List<JpaAccountPositionTemp> convertToJpaAccountPosition() {
		List<JpaAccountPositionTemp> AccountPostitionList = new ArrayList<JpaAccountPositionTemp>();
		try {
			List<JpaAccountPositionSource> securitiesSourceList = accountPosSourceDAO.findAll();
			if (securitiesSourceList != null && !securitiesSourceList.isEmpty()) {
				List<String> errors = new ArrayList<String>();
				String memberId = null;
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
				for (JpaAccountPositionSource sourceRec : securitiesSourceList) {
					List<JpaMember> memberList = memberDAO.findByMemberCode(sourceRec.getMemberId());
					if (sourceRec.getRemarks() == null) {
						if (errors.size() > 0) {
							sourceRec.setRemarks(String.join(", ", errors));
							accountPosSourceDAO.save(sourceRec);
							continue;
						}
						if (memberList != null)
							memberId = memberList.get(0).getId();
						List<String> validationRemarks = new ArrayList<String>();
						if (memberId == null)
							validationRemarks.add("MemberCode is not found in  Member Table");
						if (!CommonUtils.validateParseDouble(sourceRec.getAvailableBalance()))
							validationRemarks.add("Invalid Available Balance");
						if (!CommonUtils.validateParseDouble(sourceRec.getCurrentAccountBalance()))
							validationRemarks.add("Invalid Current Account Balance");
						if (!CommonUtils.validateParseDouble(sourceRec.getOpeningAccountBalance()))
							validationRemarks.add("Invalid Opening Account Balance");
						if (!CommonUtils.validateParseDate_yyyyMMdd_HHmmss(sourceRec.getCreatedDate()))
							validationRemarks.add("Invalid Created Date");
						if (validationRemarks.size() > 0) {
							sourceRec.setRemarks(String.join(", ", validationRemarks));
							accountPosSourceDAO.save(sourceRec);
							continue;
						}
						Date date = new Date();
						JpaAccountPositionTemp jpaAccountPositionTemp = JpaAccountPositionTemp.builder()
								.id(sourceRec.getId()).accountId(sourceRec.getAccountId())
								.currencyCode(sourceRec.getCurrencyCode()).memberId(memberId)
								.currentAccountBalance(Double.parseDouble(sourceRec.getCurrentAccountBalance()))
								.openingAccountBalance(Double.parseDouble(sourceRec.getOpeningAccountBalance()))
								.availableBalance(Double.parseDouble(sourceRec.getAvailableBalance())).queueCount(0)
								.queueAmount(0.0).settledPaymentsCount(0).settledPaymentsAmount(0.0)
								.settledReceiptsCount(0).settledReceiptsAmount(0.0)
								.createdDate(
										new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sourceRec.getCreatedDate()))
								.modifiedDate(date).build();

						AccountPostitionList.add(jpaAccountPositionTemp);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in temp {}", e.getMessage());
			e.printStackTrace();
		}
		return AccountPostitionList;
	}

	public List<JpaSssAllotmentTemp> convertToSssAllotmentList() {
		List<JpaSssAllotmentTemp> AllotmentTempList = new ArrayList<JpaSssAllotmentTemp>();
		try {
			List<JpaSssAllotmentSource> allotementSourceList = allotmentSourceDAO.findAll();
			String id=userData.getSystemUserId();
			if (allotementSourceList != null && !allotementSourceList.isEmpty()) {
				List<String> errors = new ArrayList<String>();
				String processed = null;
				for (JpaSssAllotmentSource sourceRec : allotementSourceList) {
					JpaSssSecuritiesCode securitiesCodeRecord = securitiesCodeDAO.findBySecuritiesCode(sourceRec.getSecuritiesCode());
					if (sourceRec.getRemarks() == null) {
						if (errors.size() > 0) {
							sourceRec.setRemarks(String.join(", ", errors));
							allotmentSourceDAO.save(sourceRec);
							continue;
						}
						if (sourceRec.getProcessed().equals("Y")) {
							processed = "1";
						} else if (sourceRec.getProcessed().equals("N"))
							processed = "0";
						List<String> validationRemarks = new ArrayList<String>();
						if (securitiesCodeRecord == null)
							validationRemarks.add("SecuritesCode is not found in  SecuritiesCode Table");
						if (!CommonUtils.validateParseDate_yyyyMMdd_HHmmss(sourceRec.getAuctionDate()))
								validationRemarks.add("Invalid ActionDate");
						if (!CommonUtils.validateParseInteger(sourceRec.getIssuanceDate()))
							validationRemarks.add("Invalid Current Issuence Date");
						if (!CommonUtils.validateParseDouble(sourceRec.getAllotmentPrice()))
							validationRemarks.add("Invalid AllotmentPrice");
						if (!CommonUtils.validateParseLong(sourceRec.getTotalNominalAmountToBeAlloted()))
							validationRemarks.add("Invalid TotalNominalAmountToBeAlloted");
						if (!CommonUtils.validateParseInteger(sourceRec.getFirstAllotment()))
							validationRemarks.add("Invalid FirstAllotment");
						if (!CommonUtils.validateParseDouble(sourceRec.getTotalSettlementAmount()))
							validationRemarks.add("Invalid TotalSettlementAmount");
//						if (!CommonUtils.validateParseInteger(sourceRec.getProcessed()))
//							validationRemarks.add("Invalid Processed");
						if (validationRemarks.size() > 0) {
							sourceRec.setRemarks(String.join(", ", validationRemarks));
							allotmentSourceDAO.save(sourceRec);
							continue;
						}
						Date date = new Date();
						JpaSssAllotmentTemp jpaSssAllotment = JpaSssAllotmentTemp.builder().id(sourceRec.getId())
								.securitiesCode(sourceRec.getSecuritiesCode())
								.auctionDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sourceRec.getAuctionDate()))
								.issuanceDate(Integer.parseInt(sourceRec.getIssuanceDate()))
//								.allotmentPrice(dbRecorsMap.get("allotment_price"))
								.allotmentPrice(Double.parseDouble(sourceRec.getAllotmentPrice()))		
								.totalNominalAmountAlloted(0L)
								.totalNominalAmountToBeAlloted(Long.parseLong(sourceRec.getTotalNominalAmountToBeAlloted()))
								.firstAllotment(Short.parseShort(sourceRec.getFirstAllotment()))
								.totalSettlementAmount(Double.parseDouble(sourceRec.getTotalSettlementAmount()))
								.processed(Short.parseShort(processed)).action(" ").status("ACTIVE").modifiedBy(id)
								.modifiedDate(date).approvedBy(id).approvedDate(date).effectiveDate(date).createdDate(date)
								.approvalRemark("Data Migration").workflowStatusId(null).plannedTotalNominalAmountAllotted(null)
								.build();
						AllotmentTempList.add(jpaSssAllotment);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in temp {}", e.getMessage());
			e.printStackTrace();
		}
		return AllotmentTempList;
	}

	public void createAndSaveAllotmentSourceData(List<String> fileRecords, List<JpaFileUploadDetails> draftDBDetails) {
		try {
			int lineCount = 0;
			for (String line : fileRecords) {
				lineCount++;
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				if (line == null || line.trim().isEmpty()) {
					JpaSssAllotmentSource allotmentSource = new JpaSssAllotmentSource();
					allotmentSource.setRemarks("No columns present");
					allotmentSourceDAO.save(allotmentSource);
					continue;
				}
				List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
				if (content.size() != 8) {
					JpaSssAllotmentSource allotmentSource = new JpaSssAllotmentSource();
					allotmentSource.setRemarks("Invalid number of columns received");
					allotmentSourceDAO.save(allotmentSource);
					continue;
				}
				if (content.size() != draftDBDetails.size()) {
					JpaSssAllotmentSource allotmentSource = new JpaSssAllotmentSource();
					allotmentSource
							.setRemarks("Header columns count in file upload properties not matching with sourcer");
					allotmentSourceDAO.save(allotmentSource);
					continue;
				}

				Map<String, String> dbRecorsMap = new HashMap<>();
				int count = 0;
				for (JpaFileUploadDetails db : draftDBDetails) {
					logger.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
					dbRecorsMap.put(db.getTableFieldName(), content.get(count));
					count++;
				}
				JpaSssAllotmentSource jpaSssAllotmentSource = JpaSssAllotmentSource.builder().id(id)

						.securitiesCode(dbRecorsMap.get("securities_code")).auctionDate(dbRecorsMap.get("auction_date"))
						.issuanceDate(dbRecorsMap.get("issuance_date")).allotmentPrice(dbRecorsMap.get("allotment_price"))
						.totalNominalAmountAlloted(null)
						.totalNominalAmountToBeAlloted(dbRecorsMap.get("total_nominal_amount_to_be_alloted"))
						.firstAllotment(dbRecorsMap.get("first_allotment"))
						.totalSettlementAmount(dbRecorsMap.get("total_settlement_amount"))
						.processed(dbRecorsMap.get("processed")).action(null).status(null).modifiedBy(null)
						.modifiedDate(null).approvedBy(null).approvedDate(null).effectiveDate(null).createdDate(null)
						.approvalRemark(null).workflowStatusId(null).plannedTotalNominalAmountAllotted(null).build();
				allotmentSourceDAO.save(jpaSssAllotmentSource);

			}
			logger.info("Total records in File: {}" + lineCount);
		} catch (Exception e) {
			logger.error("Exception while preparting source data ", e.getMessage());
		}
		
	}

	public List<JpaSssSecuritiesPriceTemp> convertToSssSecurityPriceList() {
		List<JpaSssSecuritiesPriceTemp> SecuritiesPriceTempList = new ArrayList<JpaSssSecuritiesPriceTemp>();
		try {
			List<JpaSssSecuritiesPriceSource> SecuritiesPriceSourceList = sssSecuritiesPriceSourceDAO.findAll();
			if (SecuritiesPriceSourceList != null && !SecuritiesPriceSourceList.isEmpty()) {
				List<String> errors = new ArrayList<String>();
				String processed = null;
				String id=userData.getSystemUserId();
				for (JpaSssSecuritiesPriceSource sourceRec : SecuritiesPriceSourceList) {
					JpaSssSecuritiesCode securitiesCodeRecord = securitiesCodeDAO.findBySecuritiesCode(sourceRec.getSecuritiesCode());
					if (sourceRec.getRemarks() == null) {
						if (errors.size() > 0) {
							sourceRec.setRemarks(String.join(", ", errors));
							sssSecuritiesPriceSourceDAO.save(sourceRec);
							continue;
						}
						List<String> validationRemarks = new ArrayList<String>();
						if (securitiesCodeRecord == null)
							validationRemarks.add("SecuritesCode is not found in  SecuritiesCode Table");
						if (!CommonUtils.validateParseInteger(sourceRec.getPricingDate()))
							validationRemarks.add("Invalid Current Issuence Date");
						if (!CommonUtils.validateParseDouble(sourceRec.getPrice()))
							validationRemarks.add("Invalid TotalSettlementAmount");
//						
						if (validationRemarks.size() > 0) {
							sourceRec.setRemarks(String.join(", ", validationRemarks));
							sssSecuritiesPriceSourceDAO.save(sourceRec);
							continue;
						}
						Date date = new Date();
						JpaSssSecuritiesPriceTemp jpasecuritiesPriceTemp = JpaSssSecuritiesPriceTemp.builder().id(sourceRec.getId())
								.securitiesCode(sourceRec.getSecuritiesCode()).pricingDate(Integer.parseInt(sourceRec.getPricingDate()))
								.pricingType("T Price").description("SGCANCELSHA1-MASCANI1")
								.price(Double.parseDouble(sourceRec.getPrice()))
								.issueCode(sourceRec.getIssueCode()).action(null).status("ACTIVE").modifiedBy(id)
								.modifiedDate(date).approvedBy(id).approvedDate(date).createdDate(date).effectiveDate(date)
								.approvalRemark("Data Migration").workflowStatusId(null).build();

						SecuritiesPriceTempList.add(jpasecuritiesPriceTemp);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in temp {}", e.getMessage());
			e.printStackTrace();
		}
		return SecuritiesPriceTempList;
	}

	public void createAndSaveSecuritiesPriceSourceData(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) {
		try {
			int lineCount = 0;
			for (String line : fileRecords) {
				lineCount++;
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				if (line == null || line.trim().isEmpty()) {
					JpaSssSecuritiesPriceSource securitiesPriceSource = new JpaSssSecuritiesPriceSource();
					securitiesPriceSource.setRemarks("No columns present");
					sssSecuritiesPriceSourceDAO.save(securitiesPriceSource);
					continue;
				}
				List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
				if (content.size() != 4) {
					JpaSssSecuritiesPriceSource securitiesPriceSource = new JpaSssSecuritiesPriceSource();
					securitiesPriceSource.setRemarks("Invalid number of columns received");
					sssSecuritiesPriceSourceDAO.save(securitiesPriceSource);
					continue;
				}
				if (content.size() != draftDBDetails.size()) {
					JpaSssSecuritiesPriceSource securitiesPriceSource = new JpaSssSecuritiesPriceSource();
					securitiesPriceSource.setRemarks("Header columns count in file upload properties not matching with sourcer");
					sssSecuritiesPriceSourceDAO.save(securitiesPriceSource);
					continue;
				}

				Map<String, String> dbRecorsMap = new HashMap<>();
				int count = 0;
				for (JpaFileUploadDetails db : draftDBDetails) {
					logger.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
					dbRecorsMap.put(db.getTableFieldName(), content.get(count));
					count++;
				}
				JpaSssSecuritiesPriceSource jpasecuritiesPriceSource = JpaSssSecuritiesPriceSource.builder().id(id)
						.securitiesCode(dbRecorsMap.get("securities_code")).pricingDate(dbRecorsMap.get("pricing_date"))
						.pricingType(null).description(null).price(dbRecorsMap.get("price"))
						.issueCode(dbRecorsMap.get("issue_code")).action(null).status(null).modifiedBy(null)
						.modifiedDate(null).approvedBy(null).approvedDate(null).createdDate(null).effectiveDate(null)
						.approvalRemark(null).workflowStatusId(null).build();
				sssSecuritiesPriceSourceDAO.save(jpasecuritiesPriceSource);

			}
			logger.info("Total records in File: {}" + lineCount);
		} catch (Exception e) {
			logger.error("Exception while preparting source data ", e.getMessage());
		}
		
	}
	public void createAndSaveSecuritiesPosSourceData(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) {
		try {
			int lineCount = 0;
			for (String line : fileRecords) {
				lineCount++;
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				if (line == null || line.trim().isEmpty()) {
					JpaSssSecuritiesPositionStatsSource securitiesPosSource = new JpaSssSecuritiesPositionStatsSource();
					securitiesPosSource.setRemarks("No columns present");
					sssSecuritiesPositionStatsSourceDAO.save(securitiesPosSource);
					continue;
				}
				List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
				if (content.size() != 4) {
					JpaSssSecuritiesPositionStatsSource securitiesPosSource = new JpaSssSecuritiesPositionStatsSource();
					securitiesPosSource.setRemarks("Invalid number of columns received");
					sssSecuritiesPositionStatsSourceDAO.save(securitiesPosSource);
					continue;
				}
				if (content.size() != draftDBDetails.size()) {
					JpaSssSecuritiesPositionStatsSource securitiesPosSource = new JpaSssSecuritiesPositionStatsSource();
					securitiesPosSource.setRemarks("Header columns count in file upload properties not matching with sourcer");
					sssSecuritiesPositionStatsSourceDAO.save(securitiesPosSource);
					continue;
				}

				Map<String, String> dbRecorsMap = new HashMap<>();
				int count = 0;
				for (JpaFileUploadDetails db : draftDBDetails) {
					logger.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
					dbRecorsMap.put(db.getTableFieldName(), content.get(count));
					count++;
				}
				JpaSssSecuritiesPositionStatsSource securitiesPositionStatsSource = JpaSssSecuritiesPositionStatsSource
						.builder().id(id).accountId(dbRecorsMap.get("account_id"))
						.securitiesCode(dbRecorsMap.get("securities_code")).valueDate(null)
						.openingNominalAmount(dbRecorsMap.get("opening_nominal_amount"))
						.closingNominalAmount(dbRecorsMap.get("closing_nominal_amount")).settledCount(null)
						.settledAmount(null).receiptCount(null).receiptAmount(null).queuedCount(null).queuedAmount(null)
						.rejectedCount(null).rejectedAmount(null).cancelledCount(null).cancelledAmount(null).build();
				sssSecuritiesPositionStatsSourceDAO.save(securitiesPositionStatsSource);

			}
			logger.info("Total records in File: {}" + lineCount);
		} catch (Exception e) {
			logger.error("Exception while preparting source data ", e.getMessage());
		}
		
	}
	public List<JpaSssSecuritiesPositionStatsTemp> convertToSssSecurityPosList() {
		List<JpaSssSecuritiesPositionStatsTemp> SecuritiesPosTempList = new ArrayList<JpaSssSecuritiesPositionStatsTemp>();
		try {
			List<JpaSssSecuritiesPositionStatsSource> SecuritiesPosSourceList = sssSecuritiesPositionStatsSourceDAO.findAll();
			if (SecuritiesPosSourceList != null && !SecuritiesPosSourceList.isEmpty()) {
				List<String> errors = new ArrayList<String>();
				for (JpaSssSecuritiesPositionStatsSource sourceRec : SecuritiesPosSourceList) {
					JpaSssSecuritiesCode securitiesCodeRecord = securitiesCodeDAO.findBySecuritiesCode(sourceRec.getSecuritiesCode());
					List<JpaSSSAccount> sssAccountRecord = sssAccountDAO.findByAccountId(sourceRec.getAccountId());
					if (sourceRec.getRemarks() == null) {
						if (errors.size() > 0) {
							sourceRec.setRemarks(String.join(", ", errors));
							sssSecuritiesPositionStatsSourceDAO.save(sourceRec);
							continue;
						}
						List<String> validationRemarks = new ArrayList<String>();
						if (securitiesCodeRecord == null)
							validationRemarks.add("SecuritesCode is not found in  SecuritiesCode Table");
						if (sssAccountRecord == null)
							validationRemarks.add("account_id is not found in  SssAccount Table");
						if (!CommonUtils.validateParseDouble(sourceRec.getOpeningNominalAmount()))
							validationRemarks.add("Invalid OpeningNominalAmount");
						if (!CommonUtils.validateParseDouble(sourceRec.getClosingNominalAmount()))
							validationRemarks.add("Invalid ClosingNominalAmount");
//						
						if (validationRemarks.size() > 0) {
							sourceRec.setRemarks(String.join(", ", validationRemarks));
							sssSecuritiesPositionStatsSourceDAO.save(sourceRec);
							continue;
						}
						Date d = new Date();
						String date = new SimpleDateFormat("yyyyMMdd").format(d);
						JpaSssSecuritiesPositionStatsTemp jpaSssSecuritesPos = JpaSssSecuritiesPositionStatsTemp.builder().id(sourceRec.getId())
								.accountId(sourceRec.getAccountId())
								.securitiesCode(sourceRec.getSecuritiesCode())
								.valueDate(Integer.parseInt(date))
								.openingNominalAmount(Double.parseDouble(sourceRec.getOpeningNominalAmount()))
								.closingNominalAmount(Double.parseDouble(sourceRec.getClosingNominalAmount())).settledCount(0)
								.settledAmount(0.0).receiptCount(0).receiptAmount(0.0).queuedCount(0).queuedAmount(0.0)
								.rejectedCount(0).rejectedAmount(0.0).cancelledCount(0).cancelledAmount(0.0).build();
						SecuritiesPosTempList.add(jpaSssSecuritesPos);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in temp {}", e.getMessage());
			e.printStackTrace();
		}
		return SecuritiesPosTempList;
	}

	public void createAndSaveSssFloatingRatesSourceData(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) {
		try {
			int lineCount = 0;
			for (String line : fileRecords) {
				lineCount++;
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				if (line == null || line.trim().isEmpty()) {
					JpaSssFloatingRatesSource  floatingRateSource = new JpaSssFloatingRatesSource();
					floatingRateSource.setRemarks("No columns present");
					sssFloatingRatesSourceDAO.save(floatingRateSource);
					continue;
				}
				List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
				if (content.size() != 4) {
					JpaSssFloatingRatesSource floatingRateSource = new JpaSssFloatingRatesSource();
					floatingRateSource.setRemarks("Invalid number of columns received");
					sssFloatingRatesSourceDAO.save(floatingRateSource);
					continue;
				}
				if (content.size() != draftDBDetails.size()) {
					JpaSssFloatingRatesSource floatingRateSource = new JpaSssFloatingRatesSource();
					floatingRateSource.setRemarks("Header columns count in file upload properties not matching with sourcer");
					sssFloatingRatesSourceDAO.save(floatingRateSource);
					continue;
				}

				Map<String, String> dbRecorsMap = new HashMap<>();
				int count = 0;
				for (JpaFileUploadDetails db : draftDBDetails) {
					logger.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
					dbRecorsMap.put(db.getTableFieldName(), content.get(count));
					count++;
				}
				JpaSssFloatingRatesSource sssFloatingRateSource = JpaSssFloatingRatesSource.builder().id(id)
						.referenceRate(dbRecorsMap.get("reference_rate"))
						.publicationDate(dbRecorsMap.get("publication_date")).valueDate(dbRecorsMap.get("value_date"))
						.rate(dbRecorsMap.get("rate")).action(null).status(null).modifiedBy(null).modifiedDate(null)
						.approvedBy(null).approvedDate(null).createdDate(null).effectiveDate(null).approvalRemark(null)
						.workflowStatusId(null).build();
				sssFloatingRatesSourceDAO.save(sssFloatingRateSource);

			}
			logger.info("Total records in File: {}" + lineCount);
		} catch (Exception e) {
			logger.error("Exception while preparting source data ", e.getMessage());
		}
		
	}

	public List<JpaSssFloatingRatesTemp> convertToSssfloatingratesList() {
		List<JpaSssFloatingRatesTemp> sssFloatingRateTempList = new ArrayList<JpaSssFloatingRatesTemp>();
		try {
			List<JpaSssFloatingRatesSource> sssFloatingRates = sssFloatingRatesSourceDAO.findAll();
			String id=userData.getSystemUserId();
			if (sssFloatingRates != null && !sssFloatingRates.isEmpty()) {
				List<String> errors = new ArrayList<String>();
				for (JpaSssFloatingRatesSource sourceRec : sssFloatingRates) {
					if (sourceRec.getRemarks() == null) {
						if (errors.size() > 0) {
							sourceRec.setRemarks(String.join(", ", errors));
							sssFloatingRatesSourceDAO.save(sourceRec);
							continue;
						}
						List<String> validationRemarks = new ArrayList<String>();
						if (!CommonUtils.validateParseDouble(sourceRec.getRate()))
							validationRemarks.add("Invalid Rate");
						if (!CommonUtils.validateParseInteger(sourceRec.getPublicationDate()))
							validationRemarks.add("Invalid PublicationDate");
						if (!CommonUtils.validateParseInteger(sourceRec.getValueDate()))
							validationRemarks.add("Invalid ClosingNominalAmount");
//						
						if (validationRemarks.size() > 0) {
							sourceRec.setRemarks(String.join(", ", validationRemarks));
							sssFloatingRatesSourceDAO.save(sourceRec);
							continue;
						}
						Date date = new Date();
						JpaSssFloatingRatesTemp sssFloatingRate = JpaSssFloatingRatesTemp.builder().id(sourceRec.getId())
								.referenceRate(sourceRec.getReferenceRate())
								.publicationDate(Integer.parseInt(sourceRec.getPublicationDate()))
								.valueDate(Integer.parseInt(sourceRec.getValueDate()))
								.rate(Double.parseDouble(sourceRec.getRate())).action(" ").status("ACTIVE")
								.modifiedBy(id).modifiedDate(date).approvedBy(id).approvedDate(date)
								.createdDate(date).effectiveDate(date).approvalRemark("Data Migration").workflowStatusId(null)
								.build();
						sssFloatingRateTempList.add(sssFloatingRate);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in temp {}", e.getMessage());
			e.printStackTrace();
		}
		return sssFloatingRateTempList;
	}

	public List<JpaSssSecuritiesCodeStatisticsTemp> convertToSssSecuritiesCodeStatisticsList() {
		List<JpaSssSecuritiesCodeStatisticsTemp> securitiesCodeStatisticsTempList = new ArrayList<JpaSssSecuritiesCodeStatisticsTemp>();
		try {
			List<JpaSssSecuritiesCodeStatisticsSource> securitiyCodeStatistics = securitiesCodeStatisticsSourceDAO.findAll();
			if (securitiyCodeStatistics != null && !securitiyCodeStatistics.isEmpty()) {
				List<String> errors = new ArrayList<String>();
				for (JpaSssSecuritiesCodeStatisticsSource sourceRec : securitiyCodeStatistics) {
					JpaSssSecuritiesCode securitiesCodeRecord = securitiesCodeDAO.findBySecuritiesCode(sourceRec.getSecuritiesCode());
					if (sourceRec.getRemarks() == null) {
						if (errors.size() > 0) {
							sourceRec.setRemarks(String.join(", ", errors));
							securitiesCodeStatisticsSourceDAO.save(sourceRec);
							continue;
						}
						List<String> validationRemarks = new ArrayList<String>();
						if (securitiesCodeRecord == null)
							validationRemarks.add("SecuritesCode is not found in  SecuritiesCode Table");
						if (!CommonUtils.validateParseLong(sourceRec.getTotalRedeemedAmount()))
							validationRemarks.add("Invalid TotalRedeemedAmount");
						if (!CommonUtils.validateParseLong(sourceRec.getTotalNominalAmountIssuedForErf()))
							validationRemarks.add("Invalid TotalNominalAmountIssuedForErf");
						if (!CommonUtils.validateParseLong(sourceRec.getTotalRedeemedAmountForErf()))
							validationRemarks.add("Invalid RedeemedAmountForErf");
						if (!CommonUtils.validateParseLong(sourceRec.getOutstandingNominalAmount()))
							validationRemarks.add("Invalid OutstandingNominalAmount");
						if (!CommonUtils.validateParseLong(sourceRec.getAmountAllotedToCentralBank()))
							validationRemarks.add("Invalid AmountAllotedToCentralBank");
						if (!CommonUtils.validateParseLong(sourceRec.getAmountAllotedToOthers()))
							validationRemarks.add("Invalid AmountAllotedToOthers");
						if (!CommonUtils.validateParseInteger(sourceRec.getNextCouponDate()))
							validationRemarks.add("Invalid NextCouponDate");
						if (!CommonUtils.validateParseInteger(sourceRec.getLastCouponDate()))
							validationRemarks.add("Invalid LastCouponDate");
						if (!CommonUtils.validateParseInteger(sourceRec.getNextIndexEndDate()))
							validationRemarks.add("Invalid NextIndexEndDate");
//						
						if (validationRemarks.size() > 0) {
							sourceRec.setRemarks(String.join(", ", validationRemarks));
							securitiesCodeStatisticsSourceDAO.save(sourceRec);
							continue;
						}
						Date date = new Date();
						JpaSssSecuritiesCodeStatisticsTemp SssSecuritiesCodeStatisticsTemp = JpaSssSecuritiesCodeStatisticsTemp
								.builder().id(sourceRec.getId())
								.securitiesCode(sourceRec.getSecuritiesCode())
								.totalRedeemedAmount(Long.parseLong(sourceRec.getTotalRedeemedAmount()))
								.totalRedeemedAmountForErf(Long.parseLong(sourceRec.getTotalRedeemedAmountForErf()) )
								.totalNominalAmountIssuedForErf(Long.parseLong(sourceRec.getTotalNominalAmountIssuedForErf()))
								.outstandingNominalAmount(Long.parseLong(sourceRec.getOutstandingNominalAmount()))
								.nextCouponDate(Integer.parseInt(sourceRec.getNextCouponDate()))
								.lastCouponDate(Integer.parseInt(sourceRec.getLastCouponDate()))
								.nextIndexEndDate(Integer.parseInt(sourceRec.getNextIndexEndDate()))
								.amountAllotedToCentralBank(Long.parseLong(sourceRec.getAmountAllotedToCentralBank()))
								.amountAllotedToOthers(Long.parseLong(sourceRec.getAmountAllotedToOthers())).build();
						securitiesCodeStatisticsTempList.add(SssSecuritiesCodeStatisticsTemp);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in temp {}", e.getMessage());
			e.printStackTrace();
		}
		return securitiesCodeStatisticsTempList;
	}

	public void createAndSaveSecurityCodeStatisticsSourceData(List<String> fileRecords,
			List<JpaFileUploadDetails> draftDBDetails) {
		try {
			int lineCount = 0;
			for (String line : fileRecords) {
				lineCount++;
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				if (line == null || line.trim().isEmpty()) {
					JpaSssSecuritiesCodeStatisticsSource  codeStaisticsSource = new JpaSssSecuritiesCodeStatisticsSource();
					codeStaisticsSource.setRemarks("No columns present");
					securitiesCodeStatisticsSourceDAO.save(codeStaisticsSource);
					continue;
				}
				List<String> content = Stream.of(line.split("\\|")).map(String::trim).collect(Collectors.toList());
				if (content.size() != 10) {
					JpaSssSecuritiesCodeStatisticsSource codeStaisticsSource = new JpaSssSecuritiesCodeStatisticsSource();
					codeStaisticsSource.setRemarks("Invalid number of columns received");
					securitiesCodeStatisticsSourceDAO.save(codeStaisticsSource);
					continue;
				}
				if (content.size() != draftDBDetails.size()) {
					JpaSssSecuritiesCodeStatisticsSource codeStaisticsSource = new JpaSssSecuritiesCodeStatisticsSource();
					codeStaisticsSource.setRemarks("Header columns count in file upload properties not matching with sourcer");
					securitiesCodeStatisticsSourceDAO.save(codeStaisticsSource);
					continue;
				}

				Map<String, String> dbRecorsMap = new HashMap<>();
				int count = 0;
				for (JpaFileUploadDetails db : draftDBDetails) {
					logger.info("{} - {} -{}", db.getTableFieldName(), content.get(count), content.get(count).length());
					dbRecorsMap.put(db.getTableFieldName(), content.get(count));
					count++;
				}
				JpaSssSecuritiesCodeStatisticsSource SssSecuritiesCodeStatisticsSource = JpaSssSecuritiesCodeStatisticsSource
						.builder().id(id).securitiesCode(dbRecorsMap.get("securities_code"))
						.totalRedeemedAmount(dbRecorsMap.get("total_redeemed_amount"))
						.totalNominalAmountIssuedForErf(dbRecorsMap.get("total_nominal_amount_issued_for_erf"))
						.totalRedeemedAmountForErf(dbRecorsMap.get("total_redeemed_amount_for_erf"))
						.outstandingNominalAmount(dbRecorsMap.get("outstanding_nominal_amount"))
						.nextCouponDate(dbRecorsMap.get("next_coupon_date"))
						.lastCouponDate(dbRecorsMap.get("last_coupon_date"))
						.nextIndexEndDate(dbRecorsMap.get("next_index_end_date"))
						.amountAllotedToCentralBank(dbRecorsMap.get("amount_alloted_to_central_bank"))
						.amountAllotedToOthers(dbRecorsMap.get("amount_alloted_to_others")).build();
				securitiesCodeStatisticsSourceDAO.save(SssSecuritiesCodeStatisticsSource);

			}
			logger.info("Total records in File: {}" + lineCount);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception while preparting source data ", e.getMessage());
		}
		
	}

	
	

}