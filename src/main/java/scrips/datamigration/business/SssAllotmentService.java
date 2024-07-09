package scrips.datamigration.business;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import scrips.datamigration.jpa.accountposition.JpaAccountPosition;
import scrips.datamigration.jpa.accountposition.JpaAccountPositionTemp;
import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.fileupload.JpaFileUploadError;
import scrips.datamigration.jpa.fileupload.JpaFileUploadExecution;
import scrips.datamigration.jpa.fileupload.JpaFileUploadHeader;
import scrips.datamigration.jpa.member.JpaMember;
import scrips.datamigration.jpa.sss.securities.JpaSssAllotment;
import scrips.datamigration.jpa.sss.securities.JpaSssAllotmentTemp;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCode;
import scrips.datamigration.jpa.sss.securities.SssAllotmentDAO;
import scrips.datamigration.jpa.sss.securities.SssAllotmentTempDAO;
import scrips.datamigration.jpa.sss.securities.SssSecuritiesCodeDAO;
@Service
@Slf4j
public class SssAllotmentService {
	private final Logger logger = LogManager.getLogger(SssAllotmentService.class);
	@Autowired
	ReadFileAndConvertService fileConvertService;
	@Autowired
	ValidationService validationService;

	@Autowired
	FileUploadExecutionService fileuploadExecService;

	@Autowired
	FileUploadErrorService fileErrorService;

	@Autowired
	SssAllotmentDAO sssAllotmentDAO;

	@Autowired
	SssAllotmentTempDAO sssAllotmentTempDAO;

	@Autowired
	FileUploadService fileService;
	
	@Autowired
	SssSecuritiesCodeDAO securitiesCodeDAO;

	public String migrateSssAllotment(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
			List<String> fileRecords) throws NumberFormatException, ParseException, DatabaseException {
//		List<JpaSssAllotmentTemp> tempSssAllotmentList = fileConvertService.convertToSssAllotmentList(fileRecords, draftDBDetails);
		fileConvertService.createAndSaveAllotmentSourceData(fileRecords, draftDBDetails);

		List<JpaSssAllotmentTemp> tempSssAllotmentList = fileConvertService.convertToSssAllotmentList();
		List<JpaSssAllotment> validSssAllotmentList = new ArrayList<JpaSssAllotment>();
		
		List<JpaFileUploadExecution> fileExecList = new ArrayList<JpaFileUploadExecution>();
		List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
		
		List<JpaSssAllotment> SssAllotmentList = convertToJpaSssAllotment(tempSssAllotmentList);
		if (!tempSssAllotmentList.isEmpty()) {

			tempSssAllotmentList.stream().forEach(sssAllotmentTemp -> {
				try {
					
					JpaSssAllotment duplicateSecurityCode = sssAllotmentDAO.findBySecuritiesCode(sssAllotmentTemp.getSecuritiesCode());
					if (duplicateSecurityCode != null) {
						System.out.println("Duplicate security code Found " + duplicateSecurityCode.getSecuritiesCode());
					}
					try {

						String remarks = validationService.validationJpaSssAllotment(sssAllotmentTemp);
						if (remarks.isEmpty()  && duplicateSecurityCode == null) {
							JpaSssAllotmentTemp tempObj = sssAllotmentTemp;
//							securityCode = securitiesCodeRecord.getSecuritiesCode();
							JpaSssAllotmentTemp temp = tempObj;
//							temp.setSecuritiesCode(securityCode);
							log.info("validated");
//							sssAllotmentDAO.save(convertToJpaSssAllotment(Arrays.asList(tempObj)).get(0));
//							sssAllotmentTempDAO.save(sssAllotmentTemp);
							boolean isLiveDataHasError = false;
							try {
								sssAllotmentDAO.save(convertToJpaSssAllotment(Arrays.asList(tempObj)).get(0));
							} catch (Exception e) {
								isLiveDataHasError = true;
								logger.error("error while saving account live table data {}", e.getMessage());
								e.printStackTrace();
							}
							if (isLiveDataHasError)
								tempObj.setRemarks("Error while saving liabilities base live table data");

							sssAllotmentTempDAO.save(sssAllotmentTemp);
							JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
									fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
									UUID.randomUUID().toString(), "Migratted Sucessfully");
							fileExecList.add(fileUplodExec);
						} else {
							if (duplicateSecurityCode != null) {
								remarks = remarks.concat(",Duplicate Securities Code");
							}
//							if (securitiesCodeRecord == null) {
//								remarks = remarks.concat(",Securities Code not found in securities_code table");
//							} else {
//								securityCode =securitiesCodeRecord.getSecuritiesCode();							}
//								Optional<JpaAccountTemp> tempObj=tempUniqueAccountList.stream().filter(x->x.getId().equals(account.getId())).findAny();
							JpaSssAllotmentTemp tempObj = sssAllotmentTemp;
							if (tempObj != null) {

								remarks = remarks.substring(1);
								JpaSssAllotmentTemp temp = tempObj;
								temp.setRemarks(remarks);
//								temp.setSecuritiesCode(securityCode);sss
								System.out.println("temp "+temp.getAllotmentPrice());
								sssAllotmentTempDAO.save(temp);
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
	public List<JpaSssAllotment> convertToJpaSssAllotment(List<JpaSssAllotmentTemp> sssAllotmentListTemp) {
		List<JpaSssAllotment> list = new ArrayList<>();
		for (JpaSssAllotmentTemp sssAllotmentTemp : sssAllotmentListTemp) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			JpaSssAllotment sssAllotment= mapper.map(sssAllotmentTemp, JpaSssAllotment.class);
			list.add(sssAllotment);
			//log.info(sssAllotmentTemp.getAccountId());
		}
		return list;
	}


}