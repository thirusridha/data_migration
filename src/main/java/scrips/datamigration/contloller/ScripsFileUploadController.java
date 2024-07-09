package scrips.datamigration.contloller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import scrips.datamigration.business.AccountPositionServvice;
//import scrips.datamigration.business.AccountPositionServvice;
import scrips.datamigration.business.AccountService;
import scrips.datamigration.business.CbmCostCenterService;
import scrips.datamigration.business.CbmDepositRateService;
import scrips.datamigration.business.CbmFloatingRateService;
import scrips.datamigration.business.CbmGlAccountService;
import scrips.datamigration.business.CbmLiabilitiesBaseDetailService;
import scrips.datamigration.business.CbmLiabilitiesBaseService;
import scrips.datamigration.business.FileUploadService;
import scrips.datamigration.business.FkChecksEnableDisable;
import scrips.datamigration.business.MemberService;
import scrips.datamigration.business.SssAccountService;
import scrips.datamigration.business.SssAllotmentService;
import scrips.datamigration.business.SssFloatingRatesService;
import scrips.datamigration.business.SssMemberService;
import scrips.datamigration.business.SssSecuritesPositionStatsService;
import scrips.datamigration.business.SssSecuritiesCodeService;
import scrips.datamigration.business.SssSecuritiesCodeStatisticsService;
import scrips.datamigration.business.SssSecuritiesPriceService;
import scrips.datamigration.business.SssTransactionService;
import scrips.datamigration.exception.DatabaseException;
import scrips.datamigration.exception.FailedValidationException;
import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.fileupload.JpaFileUploadHeader;
import scrips.datamigration.jpa.sss.Member.SssMemberDAO;

/**
 * @author Siva Kuruva
 */

//@RestController
//@RequestMapping("/scrips/migrate")
//@Api(value="SCRIPS migrate")
@Controller
public class ScripsFileUploadController {
	@Autowired
	FileUploadService fileService;

	@Autowired
	MemberService memberService;

	@Autowired
	AccountService accountService;

	@Autowired
	AccountPositionServvice accountPositionServvice;

	@Autowired
	SssAllotmentService sssAllotmentService;

	@Autowired
	SssSecuritiesPriceService securityPriceService;

	@Autowired
	SssSecuritesPositionStatsService securityPosStats;

	@Autowired
	SssAccountService sssAccountService;

	@Autowired
	SssTransactionService sssTransactionService;

	@Autowired
	SssFloatingRatesService sssFloatingRatesService;

	@Autowired
	SssSecuritiesCodeService sssSecuritiesCodeService;

	@Autowired
	SssMemberService sssMemberService;

	@Autowired
	CbmCostCenterService cbmCostCenterService;

	@Autowired
	CbmGlAccountService cbmGlAccountService;

	@Autowired
	CbmDepositRateService cbmDepositRateService;

	@Autowired
	CbmLiabilitiesBaseService cbmLiabilitiesBaseService;

	@Autowired
	CbmLiabilitiesBaseDetailService cbmLiabilitiesBaseDetailService;

	@Autowired
	SssMemberDAO sssMemberDAO;
	@Autowired
	SssSecuritiesCodeStatisticsService sssSecuritiesCodeStatisticsService;
	@Autowired
	CbmFloatingRateService cbmFloatingRateService;
	
	@Autowired
	private FkChecksEnableDisable fkChecksEnableDisable;
	
	private static List<String> fileNames =null ;
	
	@PostConstruct
	public void loadData()
	{
		fileNames =  Arrays.asList("SSS_SECURITIES_CODE_FILE_UPLOAD","MEMBER_FILE_UPLOAD");
		//fileNames = Arrays.asList(env.getProperty("MIGRATION_FILE.NAMES"));
	}
	public void migrateFile() throws BindException, DatabaseException, FailedValidationException, ParseException, NumberFormatException,
	FileNotFoundException, ClassNotFoundException, SQLException,IOException {
		File directoryPath =null;
		 directoryPath = new File("./MigrationFiles/");
		// List of all files and directories
		String contents[] = directoryPath.list();
		System.out.println("List of files and directories in the specified directory:");
		if (contents != null && contents.length > 0) {
			for (String fileName : contents) {
				fileName = fileName.substring(0, fileName.indexOf("."));
				if (fileNames.contains(fileName)) {
					System.out.println("process started for :" + fileName);
					migrateFile(fileName, null);
				}
			}
		}
	}
	
	//@RequestMapping(value = "/fromfiletodb", method = RequestMethod.POST)
	public ResponseEntity<String> migrateFile(@RequestBody String fileUploadCode, BindingResult errors)
			throws BindException, DatabaseException, FailedValidationException, ParseException, NumberFormatException,
			FileNotFoundException, ClassNotFoundException, SQLException,IOException {
		String result = "Failed to migrate";
		try {
			fkChecksEnableDisable.disableFkChecks();
			fkChecksEnableDisable.fkStatus();
		List<String> fileRecords = new ArrayList<String>();
		System.out.println("fileUploadCode - " + fileUploadCode);
		if (fileUploadCode != null) {
			// Get file details from FILE_UPLOAD_HEADER DB to connect to FTP
			JpaFileUploadHeader fileHeaderObj = fileService.getFileHeaderByCode(fileUploadCode);
			System.out.println("Live table name ->" + fileHeaderObj.getLiveTableName());
			// perform validation and get table column details using FILE_UPLOAD_DETAILS
			List<JpaFileUploadDetails> draftDBDetails = fileService.getFileDetailsByCode(fileUploadCode);

			if (fileHeaderObj != null) {
				fileRecords = fileService.readFileFromFTP(fileHeaderObj,fileUploadCode);
				fileRecords.stream().forEach(a -> System.out.println("fileRecors - " + a));
				draftDBDetails.stream()
						.forEach(fud -> System.out.println(fud.getSequenceNo() + ":" + fud.getTableFieldName()));

				if (fileRecords != null) {
					switch (fileHeaderObj.getLiveTableName()) {
					case "member":
						result = memberService.migrateMember(fileHeaderObj, draftDBDetails, fileRecords);
						break;

					case "account":
						result = accountService.migrateAccount(fileHeaderObj, draftDBDetails, fileRecords);
						break;

					case "account_position":
						result = accountPositionServvice.migrateAccountPosition(fileHeaderObj, draftDBDetails,
								fileRecords);
						break;

					case "sss_account":
						result = sssAccountService.migrateSssAccount(fileHeaderObj, draftDBDetails, fileRecords);
						break;

					case "sss_transaction":
						result = sssTransactionService.migrateSssTransaction(fileHeaderObj, draftDBDetails,
								fileRecords);
						break;

					case "allotment":
						result = sssAllotmentService.migrateSssAllotment(fileHeaderObj, draftDBDetails, fileRecords);
						break;

					case "sss_securities_price":
						result = securityPriceService.migrateSssSecurityPrice(fileHeaderObj, draftDBDetails,
								fileRecords);
						break;

					case "sss_securities_position_stats":
						result = securityPosStats.migrateSssSecurityPos(fileHeaderObj, draftDBDetails, fileRecords);
						break;

					case "sss_floating_rates":
						sssFloatingRatesService.migrateSssFloatingRates(fileHeaderObj, draftDBDetails, fileRecords);
						break;

					case "securities_code":
						result = sssSecuritiesCodeService.migrateSssSecuritiesCode(fileHeaderObj, draftDBDetails,
								fileRecords);
						break;

					case "sss_securities_code_statistics":
						result = sssSecuritiesCodeStatisticsService.migrateSssSecuritiesCodeStatistics(fileHeaderObj,
								draftDBDetails, fileRecords);
						break;

					case "cbm_deposit_rate":
						result = cbmDepositRateService.migrateCbmDepositRates(fileHeaderObj, draftDBDetails,
								fileRecords);
						break;

					case "cbm_liabilities_base":
						result = cbmLiabilitiesBaseService.migrateCbmLiabilitiesBase(fileHeaderObj, draftDBDetails,
								fileRecords);
						break;

					case "cbm_liabilities_base_detail":
						result = cbmLiabilitiesBaseDetailService.migrateCbmLiabilitiesBaseDetail(fileHeaderObj,
								draftDBDetails, fileRecords);
						break;
					case "sss_member":
						result = sssMemberService.migrateSssMember(fileHeaderObj, draftDBDetails, fileRecords);
						break;
					case "cbm_cost_centre":
						result = cbmCostCenterService.migrateCbmCostCentre(fileHeaderObj, draftDBDetails, fileRecords);
						break;
					case "cbm_gl_account":
						result = cbmGlAccountService.migrateCbmGlAccount(fileHeaderObj, draftDBDetails, fileRecords);
						break;
					case "cbm_floating_rate":
						result = cbmFloatingRateService.migrateCbmFloatingRates(fileHeaderObj, draftDBDetails, fileRecords);
						break;
						
					default:
						System.out.println("default case");
						break;
					}
				}
			}
		}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			fkChecksEnableDisable.enableFkChecks();
			fkChecksEnableDisable.fkStatus();
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
}