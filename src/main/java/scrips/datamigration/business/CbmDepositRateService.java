package scrips.datamigration.business;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
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
import scrips.datamigration.jpa.accountposition.JpaAccountPositionTemp;
import scrips.datamigration.jpa.cbm.CbmCostCenterTempDAO;
import scrips.datamigration.jpa.cbm.JpaCbmDepositRate;
import scrips.datamigration.jpa.cbm.JpaCbmDepositRateDAO;
import scrips.datamigration.jpa.cbm.JpaCbmDepositRateTemp;
import scrips.datamigration.jpa.cbm.JpaCbmDepositRateTempDAO;
import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.fileupload.JpaFileUploadError;
import scrips.datamigration.jpa.fileupload.JpaFileUploadExecution;
import scrips.datamigration.jpa.fileupload.JpaFileUploadHeader;
import scrips.datamigration.jpa.sss.transaction.JpaSssTransactionTemp;

@Service
@Slf4j
public class CbmDepositRateService {

	private final Logger logger = LogManager.getLogger(CbmDepositRateService.class);
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
	JpaCbmDepositRateDAO jpaCbmDepositRateDAO;

	@Autowired
	JpaCbmDepositRateTempDAO jpaCbmDepositRateTempDAO;

	public String migrateCbmDepositRates(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
			List<String> fileRecords) throws NumberFormatException, ParseException, DatabaseException {
		fileConvertService.createAndSaveCbmDepositRateSourceData(fileRecords, draftDBDetails);
		List<JpaCbmDepositRateTemp> cbmDepositRateTemp = fileConvertService.convertToJpaCbmDepositRateList();
		List<JpaFileUploadExecution> fileExecList = new ArrayList<JpaFileUploadExecution>();
		List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
		List<JpaCbmDepositRate> cbmDepositRate = converttoJpaCbmDepositRate(cbmDepositRateTemp);

		if (!cbmDepositRateTemp.isEmpty()) {
			cbmDepositRateTemp.stream().forEach(cbmDepositRatesTemp -> {
				try {
					String remarks = validationService.validationJpaCbmDepositRate(cbmDepositRatesTemp);
					if (remarks.isEmpty()) {
						JpaCbmDepositRateTemp tempObj = cbmDepositRatesTemp;
						log.info("validated");
						boolean isLiveDataHasError = false;
						try {
							jpaCbmDepositRateDAO.save(converttoJpaCbmDepositRate(Arrays.asList(tempObj)).get(0));
						} catch (Exception e) {
							isLiveDataHasError = true;
							logger.error("error while saving deposit rate live table data {}", e.getMessage());
							e.printStackTrace();
						}
						if (isLiveDataHasError)
							tempObj.setRemarks("Error while saving deposit rate live table data");
						jpaCbmDepositRateTempDAO.save(cbmDepositRatesTemp);
						JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
								fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
								UUID.randomUUID().toString(), "Migratted Sucessfully");
						fileExecList.add(fileUplodExec);
					} else {
						JpaCbmDepositRateTemp tempObj = cbmDepositRatesTemp;
						remarks = remarks.substring(1);
						JpaCbmDepositRateTemp temp = tempObj;
						temp.setRemarks(remarks);
						jpaCbmDepositRateTempDAO.save(temp);

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

	public List<JpaCbmDepositRate> converttoJpaCbmDepositRate(List<JpaCbmDepositRateTemp> cbmDepositRateTemp) {
		List<JpaCbmDepositRate> list = new ArrayList<>();
		for (JpaCbmDepositRateTemp jpaCbmDepositRatesTemp : cbmDepositRateTemp) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			JpaCbmDepositRate jpaCbmDepositRate = mapper.map(jpaCbmDepositRatesTemp, JpaCbmDepositRate.class);
			list.add(jpaCbmDepositRate);
		}
		return list;
	}
}
