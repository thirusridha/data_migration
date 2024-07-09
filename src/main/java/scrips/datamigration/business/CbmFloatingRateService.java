package scrips.datamigration.business;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import scrips.datamigration.jpa.cbm.JpaCbmDepositRate;
import scrips.datamigration.jpa.cbm.JpaCbmDepositRateTemp;
import scrips.datamigration.jpa.cbm.JpaCbmFloatingRate;
import scrips.datamigration.jpa.cbm.JpaCbmFloatingRateDAO;
import scrips.datamigration.jpa.cbm.JpaCbmFloatingRateTemp;
import scrips.datamigration.jpa.cbm.JpaCbmFloatingRateTempDAO;
import scrips.datamigration.jpa.cbm.JpaCbmGlAccount;
import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.fileupload.JpaFileUploadError;
import scrips.datamigration.jpa.fileupload.JpaFileUploadExecution;
import scrips.datamigration.jpa.fileupload.JpaFileUploadHeader;

@Service
@Slf4j
public class CbmFloatingRateService {
	private final Logger logger = LogManager.getLogger(CbmFloatingRateService.class);

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
	JpaCbmFloatingRateDAO jpaCbmFloatingRateDAO;
	@Autowired
	JpaCbmFloatingRateTempDAO jpaCbmFloatingRateTempDAO;

	public String migrateCbmFloatingRates(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
			List<String> fileRecords) throws NumberFormatException, ParseException, DatabaseException {
		fileConvertService.createAndSaveCbmFloatingRateSourceData(fileRecords, draftDBDetails);
		List<JpaCbmFloatingRateTemp> cbmFloatingRateTemp = fileConvertService.convertToCbmfloatingrateList();
		List<JpaFileUploadExecution> fileExecList = new ArrayList<JpaFileUploadExecution>();
		List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
		List<JpaCbmFloatingRate> cbmFloatingRate = converttoJpaCbmFloatingRate(cbmFloatingRateTemp);

		if (!cbmFloatingRateTemp.isEmpty()) {
			cbmFloatingRateTemp.stream().forEach(cbmFloatingRatesTemp -> {
				JpaCbmFloatingRate duplicateKey = jpaCbmFloatingRateDAO.findByReferenceRateAndValueDate(
						cbmFloatingRatesTemp.getReferenceRate(), cbmFloatingRatesTemp.getValueDate());
				if (duplicateKey != null) {
					System.out.println("Duplicate Reference Rate Found " + duplicateKey.getReferenceRate());
					System.out.println("Duplicate Value Date Found " + duplicateKey.getValueDate());
				}

				try {
					String remarks = validationService.validationJpaCbmFloatingRate(cbmFloatingRatesTemp);
					if (remarks.isEmpty() && duplicateKey == null) {
						JpaCbmFloatingRateTemp tempObj = cbmFloatingRatesTemp;
						log.info("validated");
						boolean isLiveDataHasError = false;
						try {
							jpaCbmFloatingRateDAO.save(converttoJpaCbmFloatingRate(Arrays.asList(tempObj)).get(0));
						} catch (Exception e) {
							isLiveDataHasError = true;
							logger.error("error while saving floating rate live table data {}", e.getMessage());
							e.printStackTrace();
						}
						if (isLiveDataHasError)
							tempObj.setRemarks("Error while saving floating rate live table data");

						jpaCbmFloatingRateTempDAO.save(cbmFloatingRatesTemp);
						JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
								fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
								UUID.randomUUID().toString(), "Migratted Sucessfully");
						fileExecList.add(fileUplodExec);
					} else {
						if (duplicateKey != null) {
							remarks = remarks.concat(",Duplicate ReferenceRate,Duplicate ValueDate");
						}
						JpaCbmFloatingRateTemp tempObj = cbmFloatingRatesTemp;
						if (tempObj != null) {
							remarks = remarks.substring(1);
							JpaCbmFloatingRateTemp temp = tempObj;
							temp.setRemarks(remarks);
							jpaCbmFloatingRateTempDAO.save(temp);
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

	public List<JpaCbmFloatingRate> converttoJpaCbmFloatingRate(List<JpaCbmFloatingRateTemp> cbmFloatingRateTemp) {
		List<JpaCbmFloatingRate> list = new ArrayList<>();
		for (JpaCbmFloatingRateTemp jpaCbmFloatingRatesTemp : cbmFloatingRateTemp) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			JpaCbmFloatingRate jpaCbmFloatingRate = mapper.map(jpaCbmFloatingRatesTemp, JpaCbmFloatingRate.class);
			list.add(jpaCbmFloatingRate);
		}
		return list;
	}

}
