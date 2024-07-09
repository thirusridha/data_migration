package scrips.datamigration.business;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import scrips.datamigration.exception.DatabaseException;
import scrips.datamigration.exception.FailedValidationException;
import scrips.datamigration.jpa.account.AccountDAO;
import scrips.datamigration.jpa.account.AccountTempDAO;
import scrips.datamigration.jpa.account.JpaAccount;
import scrips.datamigration.jpa.account.JpaAccountTemp;
import scrips.datamigration.jpa.accountposition.JpaAccountPositionTemp;
import scrips.datamigration.jpa.cbm.JpaCbmFloatingRate;
import scrips.datamigration.jpa.cbm.JpaCbmFloatingRateTemp;
import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.fileupload.JpaFileUploadError;
import scrips.datamigration.jpa.fileupload.JpaFileUploadExecution;
import scrips.datamigration.jpa.fileupload.JpaFileUploadHeader;
import scrips.datamigration.jpa.sss.securities.JpaSssAllotment;
import scrips.datamigration.jpa.sss.securities.JpaSssAllotmentTemp;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCode;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesPositionStats;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesPositionStatsTemp;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeTemp;
import scrips.datamigration.jpa.sss.securities.SssAllotmentDAO;
import scrips.datamigration.jpa.sss.securities.SssAllotmentTempDAO;
import scrips.datamigration.jpa.sss.securities.SssSecuritesPositionStatsDAO;
import scrips.datamigration.jpa.sss.securities.SsscSecuritesPositionStatsTempDAO;

@Service
@Slf4j
public class SssSecuritesPositionStatsService {
	private final Logger logger = LogManager.getLogger(SssSecuritesPositionStatsService.class);
	@Autowired
	ReadFileAndConvertService fileConvertService;
	@Autowired
	ValidationService validationService;

	@Autowired
	FileUploadExecutionService fileuploadExecService;

	@Autowired
	FileUploadErrorService fileErrorService;

	@Autowired
	SssSecuritesPositionStatsDAO securityPositionStatsDAO;

	@Autowired
	SsscSecuritesPositionStatsTempDAO securityPositionStatsTempDAO;

	@Autowired
	FileUploadService fileService;

	public String migrateSssSecurityPos(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
			List<String> fileRecords) throws NumberFormatException, ParseException, DatabaseException {
//		List<JpaSssSecuritiesPositionStatsTemp> securityPosTemp = fileConvertService.convertToSssSecurityPosList(fileRecords, draftDBDetails);
		fileConvertService.createAndSaveSecuritiesPosSourceData(fileRecords, draftDBDetails);
		List<JpaSssSecuritiesPositionStatsTemp> securityPosTemp = fileConvertService.convertToSssSecurityPosList();
		//		List<JpaSssSecuritiesPositionStats> validSecurityPosList = new ArrayList<JpaSssSecuritiesPositionStats>();
		List<JpaFileUploadExecution> fileExecList = new ArrayList<JpaFileUploadExecution>();
		List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
		List<JpaSssSecuritiesPositionStats> securityPositionStats = convertToJpaSssSecPos(securityPosTemp);

		if (!securityPosTemp.isEmpty()) {

			securityPosTemp.stream().forEach(securityPos -> {

				JpaSssSecuritiesPositionStats duplicateKey = securityPositionStatsDAO
						.findBySecuritiesCodeAndAccountId(securityPos.getSecuritiesCode(),securityPos.getAccountId());
//				JpaSssSecuritiesPositionStats duplicateAccountId = securityPositionStatsDAO
//						.findByAccountId(securityPos.getAccountId());
				if (duplicateKey != null) {
					System.out.println("Duplicate Securities code Found " + duplicateKey.getSecuritiesCode());
				}
				try {
					String remarks = validationService.validationJpaSssSeurityPos(securityPos);
					if (remarks.isEmpty() && duplicateKey == null ) {
						JpaSssSecuritiesPositionStatsTemp tempObj = securityPos;
						log.info("validated");
//						securityPositionStatsDAO.save(convertToJpaSssSecPos(Arrays.asList(tempObj)).get(0));
//						securityPositionStatsTempDAO.save(securityPos);
						boolean isLiveDataHasError = false;
						try {
							securityPositionStatsDAO.save(convertToJpaSssSecPos(Arrays.asList(tempObj)).get(0));
						} catch (Exception e) {
							isLiveDataHasError = true;
							logger.error(", error while saving securities code live table data {}", e.getMessage());
							e.printStackTrace();
						}

						if (isLiveDataHasError)
							tempObj.setRemarks(", Error while saving securities code live table data");
						securityPositionStatsTempDAO.save(securityPos);
						JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
								fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
								UUID.randomUUID().toString(), "Migratted Sucessfully");
						fileExecList.add(fileUplodExec);
					} else {
						if (duplicateKey != null) {
							remarks = remarks.concat(",Duplicate primary key");
						}
						JpaSssSecuritiesPositionStatsTemp tempObj = securityPos;
						if (tempObj != null) {
							remarks = remarks.substring(1);
							JpaSssSecuritiesPositionStatsTemp temp = tempObj;
							temp.setRemarks(remarks);
							securityPositionStatsTempDAO.save(temp);
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

	public List<JpaSssSecuritiesPositionStats> convertToJpaSssSecPos(
			List<JpaSssSecuritiesPositionStatsTemp> securityPosTemp) {
		List<JpaSssSecuritiesPositionStats> list = new ArrayList<>();
		for (JpaSssSecuritiesPositionStatsTemp secPosTemp : securityPosTemp) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			JpaSssSecuritiesPositionStats securityPos = mapper.map(secPosTemp, JpaSssSecuritiesPositionStats.class);
			list.add(securityPos);
			// log.info(securityPosTemp.getAccountId());
		}
		return list;
	}
}