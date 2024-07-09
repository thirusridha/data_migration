package scrips.datamigration.business;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import scrips.datamigration.exception.DatabaseException;
import scrips.datamigration.exception.FailedValidationException;
import scrips.datamigration.jpa.account.JpaAccount;
import scrips.datamigration.jpa.account.JpaAccountTemp;
import scrips.datamigration.jpa.cbm.CbmGlAccountDAO;
import scrips.datamigration.jpa.cbm.CbmGlAccountSourceDAO;
import scrips.datamigration.jpa.cbm.CbmGlAccountTempDAO;
import scrips.datamigration.jpa.cbm.JpaCbmCostCentreTemp;
import scrips.datamigration.jpa.cbm.JpaCbmGlAccount;
import scrips.datamigration.jpa.cbm.JpaCbmGlAccountSource;
import scrips.datamigration.jpa.cbm.JpaCbmGlAccountTemp;
import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.fileupload.JpaFileUploadError;
import scrips.datamigration.jpa.fileupload.JpaFileUploadExecution;
import scrips.datamigration.jpa.fileupload.JpaFileUploadHeader;
import scrips.datamigration.jpa.member.JpaMember;

@Service
@Slf4j
public class CbmGlAccountService {
	private final Logger logger = LogManager.getLogger(CbmGlAccountService.class);
	@Autowired
	ReadFileAndConvertService fileConvertService;

	@Autowired
	ValidationService validationService;

	@Autowired
	FileUploadExecutionService fileuploadExecService;

	@Autowired
	FileUploadErrorService fileErrorService;

	@Autowired
	FileUploadService fileService;

	@Autowired
	CbmGlAccountDAO glAccountDAO;

	@Autowired
	CbmGlAccountTempDAO glAccountTempDAO;
	@Autowired
	CbmGlAccountSourceDAO glAccountSourceDAO;

	public String migrateCbmGlAccount(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
			List<String> fileRecords) throws NumberFormatException, ParseException, DatabaseException {
		fileConvertService.createAndSaveCbmGlAccountSourceData(fileRecords, draftDBDetails);
		List<JpaCbmGlAccountTemp> cbmGlAccountList = fileConvertService.convertToCbmGlAccountList();
		List<JpaFileUploadExecution> fileExecList = new ArrayList<JpaFileUploadExecution>();
		List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
		List<JpaCbmGlAccount> tempCbmGlAccountList = converttoJpaCbmGlAccount(cbmGlAccountList);

		if (!cbmGlAccountList.isEmpty()) {
			cbmGlAccountList.stream().forEach(cbmGlAccountTemp -> {
				try {

					JpaCbmGlAccount duplicateGlAccount = glAccountDAO.findByGlAccount(cbmGlAccountTemp.getGlAccount());
					if (duplicateGlAccount != null) {
						System.out.println("Duplicate Account Number Found " + duplicateGlAccount.getGlAccount());
					}
					String remarks = validationService.validationCbmGlAccount(cbmGlAccountTemp);
					if (remarks.isEmpty() && duplicateGlAccount == null) {
						JpaCbmGlAccountTemp tempObj = cbmGlAccountTemp;
						log.info("validated");
						if (remarks.isEmpty() && duplicateGlAccount == null) {
							JpaCbmGlAccountTemp temp = cbmGlAccountTemp;
							log.info("validated");
							boolean isLiveDataHasError = false;
							try {
								glAccountDAO.save(converttoJpaCbmGlAccount(Arrays.asList(temp)).get(0));
							} catch (Exception e) {
								isLiveDataHasError = true;
								logger.error("error while saving securities code live table data {}", e.getMessage());
								e.printStackTrace();
							}
							if (isLiveDataHasError)
								temp.setRemarks("Error while saving securities code live table data");
							glAccountTempDAO.save(temp);
							JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
									fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
									UUID.randomUUID().toString(), "Migratted Sucessfully");
							fileExecList.add(fileUplodExec);
						}
					} else {
						if (duplicateGlAccount != null) {
							remarks = remarks.concat(",Duplicate AccountNo");
						}
						JpaCbmGlAccountTemp tempObj2 = cbmGlAccountTemp;
						if (tempObj2 != null) {
							remarks = remarks.substring(1);
							JpaCbmGlAccountTemp temp = tempObj2;
							temp.setRemarks(remarks);
							glAccountTempDAO.save(temp);
						}
						JpaFileUploadError errObj = fileErrorService.createFileUploadError(
								fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
								UUID.randomUUID().toString(), 101);
						fileErrList.add(errObj);
					}
				} catch (FailedValidationException Fve) {
					JpaFileUploadError errObj = fileErrorService.createFileUploadError(fileHeaderObj.getFileUploadId(),
							fileHeaderObj.getFileUploadCode(), UUID.randomUUID().toString(), 102);
					fileErrList.add(errObj);
					log.info("{} - {}", Fve.getMessage());
				} catch (Exception e) {
					log.error("Error {}", e.getMessage());
				}

			});
		}
		return fileErrList.size() > 0 ? "Partially Migrated" : "Migrated Sucessfully";
	}

	private List<JpaCbmGlAccount> converttoJpaCbmGlAccount(List<JpaCbmGlAccountTemp> cbmGlAccountTemp) {
		List<JpaCbmGlAccount> list = new ArrayList<>();
		for (JpaCbmGlAccountTemp jpaCbmGlAccountTemp : cbmGlAccountTemp) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			JpaCbmGlAccount jpaCbmGlAccount = mapper.map(jpaCbmGlAccountTemp, JpaCbmGlAccount.class);
			list.add(jpaCbmGlAccount);
		}
		return list;

	}
}