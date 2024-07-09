package scrips.datamigration.business;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import scrips.datamigration.jpa.account.AccountTempDAO;
import scrips.datamigration.jpa.accountposition.JpaAccountPosition;
import scrips.datamigration.jpa.cbm.CbmCostCenterDAO;
import scrips.datamigration.jpa.cbm.CbmCostCenterTempDAO;
import scrips.datamigration.jpa.cbm.JpaCbmCostCentre;
import scrips.datamigration.jpa.cbm.JpaCbmCostCentreTemp;
import scrips.datamigration.jpa.coupon.data.JpaStepupCoupon;
import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.fileupload.JpaFileUploadError;
import scrips.datamigration.jpa.fileupload.JpaFileUploadExecution;
import scrips.datamigration.jpa.fileupload.JpaFileUploadHeader;
import scrips.datamigration.jpa.member.JpaMemberTemp;
import scrips.datamigration.jpa.sss.coupon.JpaSssFloatingRates;
import scrips.datamigration.jpa.sss.coupon.JpaSssFloatingRatesTemp;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeTemp;

@Service
@Slf4j
public class CbmCostCenterService {
	private final Logger logger = LogManager.getLogger(CbmCostCenterService.class);
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
	CbmCostCenterDAO costCentreDAO;

	@Autowired
	CbmCostCenterTempDAO costCenterTempDAO;
	Map<String, JpaStepupCoupon> map = null;

	public String migrateCbmCostCentre(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
			List<String> fileRecords) throws NumberFormatException, ParseException, DatabaseException {
		map = null;

		fileConvertService.createAndSaveCbmCostCentreSourceData(fileRecords, draftDBDetails);
		List<JpaCbmCostCentreTemp> cbmCostCentreTempList = fileConvertService.convertToCbmCostCenterList();
		List<JpaFileUploadExecution> fileExecList = new ArrayList<JpaFileUploadExecution>();
		List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
		List<JpaCbmCostCentre> CbmCostCentreList = converttoJpaCbmCostCentre(cbmCostCentreTempList);

		if (!cbmCostCentreTempList.isEmpty()) {
			cbmCostCentreTempList.stream().forEach(cbmCostCentreTemp -> {
				try {
					JpaCbmCostCentre duplicateAccount = costCentreDAO
							.findByCostCentre(cbmCostCentreTemp.getCostCentre());
					if (duplicateAccount != null) {
						System.out.println("Duplicate Cost Centre Found " + duplicateAccount.getCostCentre());
					}
					try {
						String remarks = validationService.validationCbmCostCenterTemp(cbmCostCentreTemp);
						if (remarks.isEmpty() && duplicateAccount == null) {
							JpaCbmCostCentreTemp temp = cbmCostCentreTemp;
							log.info("validated");
							boolean isLiveDataHasError = false;
							try {
								costCentreDAO.save(converttoJpaCbmCostCentre(Arrays.asList(temp)).get(0));
							} catch (Exception e) {
								isLiveDataHasError = true;
								logger.error("error while saving securities code live table data {}", e.getMessage());
								e.printStackTrace();
							}
							if (isLiveDataHasError)
								temp.setRemarks("Error while saving securities code live table data");
							costCenterTempDAO.save(temp);
							JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
									fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
									UUID.randomUUID().toString(), "Migratted Sucessfully");
							fileExecList.add(fileUplodExec);
						} else {
							if (duplicateAccount != null) {
								remarks = remarks.concat(",Duplicate Cost Centre");
							}
							JpaCbmCostCentreTemp tempObj = cbmCostCentreTemp;
							if (tempObj != null) {
								remarks = remarks.substring(1);
								JpaCbmCostCentreTemp temp = tempObj;
								temp.setRemarks(remarks);
								costCenterTempDAO.save(temp);
							}
							JpaFileUploadError errObj = fileErrorService.createFileUploadError(
									fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
									UUID.randomUUID().toString(), 101);
							fileErrList.add(errObj);
						}
					} catch (FailedValidationException Fve) {
						JpaFileUploadError errObj = fileErrorService.createFileUploadError(
								fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
								UUID.randomUUID().toString(), 102);
						fileErrList.add(errObj);
						log.info("{} - {}", Fve.getMessage());
					} catch (Exception e) {
						log.error("Error {}", e.getMessage());
					}
				} catch (Exception e) {
					log.error("Db error {}", e.getMessage());

					throw new DatabaseException("Db error " + e.getMessage());
				}
			});

		}
		return fileErrList.size() > 0 ? "Partialy Migrated" : "Migrated Successfully";
	}

	private List<JpaCbmCostCentre> converttoJpaCbmCostCentre(List<JpaCbmCostCentreTemp> cbmCostCentreTemp) {
		List<JpaCbmCostCentre> list = new ArrayList<>();
		for (JpaCbmCostCentreTemp jpaCbmCostCentreTemp : cbmCostCentreTemp) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			JpaCbmCostCentre jpaCbmCostCentre = mapper.map(jpaCbmCostCentreTemp, JpaCbmCostCentre.class);
			list.add(jpaCbmCostCentre);
		}
		return list;

	}
}
