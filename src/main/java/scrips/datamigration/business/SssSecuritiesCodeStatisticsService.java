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
import scrips.datamigration.jpa.accountposition.JpaAccountPositionTemp;
import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.fileupload.JpaFileUploadError;
import scrips.datamigration.jpa.fileupload.JpaFileUploadExecution;
import scrips.datamigration.jpa.fileupload.JpaFileUploadHeader;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCode;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeStatistics;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeStatisticsTemp;
import scrips.datamigration.jpa.sss.securities.SssSecuritiesCodeDAO;
import scrips.datamigration.jpa.sss.securities.SssSecuritiesCodeStatisticsDAO;
import scrips.datamigration.jpa.sss.securities.SssSecuritiesCodeStatisticsTempDAO;

@Service
@Slf4j
public class SssSecuritiesCodeStatisticsService 
{
	private final Logger logger = LogManager.getLogger(SssSecuritiesCodeStatisticsService.class);
	@Autowired
	ReadFileAndConvertService fileConvertService;
	@Autowired
	ValidationService validationService;

	@Autowired
	FileUploadExecutionService fileuploadExecService;

	@Autowired
	FileUploadErrorService fileErrorService;

	@Autowired
	SssSecuritiesCodeStatisticsDAO securitiesCodeStatisticsDAO;

	@Autowired
	SssSecuritiesCodeDAO securitesCodeDAO;
	@Autowired
	SssSecuritiesCodeStatisticsTempDAO securitiesCodeStatisticsTempDAO;

	@Autowired
	FileUploadService fileService;

	public String migrateSssSecuritiesCodeStatistics(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
			List<String> fileRecords) throws NumberFormatException, ParseException, DatabaseException {
//		List<JpaSssSecuritiesCodeStatisticsTemp> securitiesCodeTempList = fileConvertService.convertToSssSecuritiesCodeStatisticsList(fileRecords, draftDBDetails);
		fileConvertService.createAndSaveSecurityCodeStatisticsSourceData(fileRecords, draftDBDetails);
		List<JpaSssSecuritiesCodeStatisticsTemp> securitiesCodeTempList = fileConvertService.convertToSssSecuritiesCodeStatisticsList();
		List<JpaFileUploadExecution> fileExecList = new ArrayList<JpaFileUploadExecution>();
		List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
//		List<JpaSssSecuritiesCodeStatistics> tempSecurityCodeList = convertTJpaSssSecuritiesCodeStatistics(securitiesCodeTempssList);
		if (!securitiesCodeTempList.isEmpty()) {

			securitiesCodeTempList.stream().forEach(securitiesCodeTemp -> {
				try {
					JpaSssSecuritiesCode securitiesCodeRecord = securitesCodeDAO.findBySecuritiesCode(securitiesCodeTemp.getSecuritiesCode());
					String securityCode = null;
					Long totalRedeemedAmount=null;
					if (securitiesCodeRecord == null) {
						JpaSssSecuritiesCodeStatistics tempObj = convertTJpaSssSecuritiesCodeStatistics(Arrays.asList(securitiesCodeTemp)).get(0);
						if (tempObj != null) {
							securityCode = null;
						}
						System.out.println(securitiesCodeTemp.getSecuritiesCode()+ " is not found in securities code table");
					}else if(securitiesCodeRecord.getSecuritiesTypeId().equals("SSB")){
							totalRedeemedAmount=securitiesCodeTemp.getTotalRedeemedAmount();
						}else
							totalRedeemedAmount=0L;
					JpaSssSecuritiesCodeStatistics duplicateSecurityCode = securitiesCodeStatisticsDAO.findBySecuritiesCode(securitiesCodeTemp.getSecuritiesCode());
					if (duplicateSecurityCode != null) {
						System.out.println("Duplicate security code Found " + duplicateSecurityCode.getSecuritiesCode());
					}
					try {

						String remarks = validationService.validationJpaSSSSecuritiesCodeStatistics(securitiesCodeTemp);
						if (remarks.isEmpty() && securitiesCodeRecord != null && duplicateSecurityCode == null) {
							JpaSssSecuritiesCodeStatisticsTemp tempObj = securitiesCodeTemp;
							securityCode = securitiesCodeRecord.getSecuritiesCode();
							JpaSssSecuritiesCodeStatisticsTemp temp = tempObj;
							temp.setSecuritiesCode(securityCode);
							temp.setTotalRedeemedAmount(totalRedeemedAmount);
							log.info("validated");
//							
//							
							boolean isLiveDataHasError = false;
							try {
								securitiesCodeStatisticsDAO.save(convertTJpaSssSecuritiesCodeStatistics(Arrays.asList(tempObj)).get(0));
							} catch (Exception e) {
								isLiveDataHasError = true;
								logger.error(", error while saving securities code live table data {}", e.getMessage());
								e.printStackTrace();
							}

							if (isLiveDataHasError)
								temp.setRemarks(", Error while saving securities code live table data");
							securitiesCodeStatisticsTempDAO.save(securitiesCodeTemp);
							JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
									fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
									UUID.randomUUID().toString(), "Migratted Sucessfully");
							fileExecList.add(fileUplodExec);
						} else {
							if (duplicateSecurityCode != null) {
								securityCode=securitiesCodeTemp.getSecuritiesCode();
								remarks = remarks.concat(",Duplicate Securities Code");
							}
							if (securitiesCodeRecord == null) {
								remarks = remarks.concat(",Securities Code not found in securities_code table");
							} else {
								securityCode =securitiesCodeRecord.getSecuritiesCode();							}
//								Optional<JpaAccountTemp> tempObj=tempUniqueAccountList.stream().filter(x->x.getId().equals(account.getId())).findAny();
							JpaSssSecuritiesCodeStatisticsTemp tempObj = securitiesCodeTemp;
							if (tempObj != null) {

								remarks = remarks.substring(1);
								JpaSssSecuritiesCodeStatisticsTemp temp = tempObj;
								temp.setRemarks(remarks);
								temp.setSecuritiesCode(securityCode);
								temp.setTotalRedeemedAmount(totalRedeemedAmount);
								securitiesCodeStatisticsTempDAO.save(temp);
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
				} catch (Exception e) {
					log.error("Db error {}", e.getMessage());

					throw new DatabaseException("Db error " + e.getMessage());
				}
			});
			

		}
		return fileErrList.size() > 0 ? "Partially Migrated" : "Migrated Sucessfully";
	}
	public List<JpaSssSecuritiesCodeStatistics> convertTJpaSssSecuritiesCodeStatistics(List<JpaSssSecuritiesCodeStatisticsTemp> securitiesCodeListTemp) {
		List<JpaSssSecuritiesCodeStatistics> list = new ArrayList<>();
		for (JpaSssSecuritiesCodeStatisticsTemp securitiesCodeTemp : securitiesCodeListTemp) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			JpaSssSecuritiesCodeStatistics securitiesCode= mapper.map(securitiesCodeTemp, JpaSssSecuritiesCodeStatistics.class);
			list.add(securitiesCode);
			//log.info(securityPosTemp.getAccountId());
		}
		return list;
	}
}