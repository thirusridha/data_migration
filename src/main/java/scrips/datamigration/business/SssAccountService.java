package scrips.datamigration.business;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import scrips.datamigration.exception.DatabaseException;
import scrips.datamigration.exception.FailedValidationException;

import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.fileupload.JpaFileUploadError;
import scrips.datamigration.jpa.fileupload.JpaFileUploadExecution;
import scrips.datamigration.jpa.fileupload.JpaFileUploadHeader;
import scrips.datamigration.jpa.sss.account.JpaSSSAccount;
import scrips.datamigration.jpa.sss.account.JpaSSSAccountTemp;
import scrips.datamigration.jpa.sss.account.SSSAccountDAO;
import scrips.datamigration.jpa.sss.account.SSSAccountTempDAO;

@Service
@Slf4j
public class SssAccountService {
	@Autowired
	ReadFileAndConvertService fileConvertService;

	@Autowired
	ValidationService validationService;

	@Autowired
	FileUploadExecutionService fileuploadExecService;

	@Autowired
	FileUploadErrorService fileErrorService;

	@Autowired
	SSSAccountDAO sssaccountDAO;

	@Autowired
	SSSAccountTempDAO sssaccountTempDAO;

	@Autowired
	FileUploadService fileService;
	
	public String migrateSssAccount(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
			List<String> fileRecords) throws NumberFormatException, ParseException, DatabaseException {
		log.info("sssAccountObjlist");				
		List<JpaSSSAccount> sssAccountObjlist = fileConvertService.convertToSssAccountList(fileRecords, draftDBDetails);
		draftDBDetails.forEach(a->{log.info(a.getTableFieldName());});
		List<JpaSSSAccount> sssValidAccountList = new ArrayList<JpaSSSAccount>();
			List<JpaFileUploadExecution> fileExecList = new ArrayList<JpaFileUploadExecution>();
		List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
		boolean response = false;
		List<JpaSSSAccountTemp> tempSSSAccountList = converttoJpaSSSAccountTemp(sssAccountObjlist);
	
		
		if (!sssAccountObjlist.isEmpty()) {

			sssAccountObjlist.stream().forEach(sssaccount -> {
				log.info("sssaccount - {} - {}",sssaccount.getId(), sssaccount.toString());
				try {
					if (validationService.validationJpaSSSAccount(sssaccount)) {
						log.info("validated");
						sssValidAccountList.add(sssaccount);
						// JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
						// 		fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
						// 		UUID.randomUUID().toString(), "Migratted Sucessfully");
						// fileExecList.add(fileUplodExec);
					} else {
						// JpaFileUploadError errObj = fileErrorService.createFileUploadError(
						// 		fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
						// 		UUID.randomUUID().toString(), 101);
						// fileErrList.add(errObj);
					}

				} catch (FailedValidationException Fve) {
					// JpaFileUploadError errObj = fileErrorService.createFileUploadError(fileHeaderObj.getFileUploadId(),
					// 		fileHeaderObj.getFileUploadCode(), UUID.randomUUID().toString(), 102);
					// fileErrList.add(errObj);
					log.info("{} - {}", Fve.getMessage());
				} catch (Exception e) {
					log.error("Error {}", e.getMessage());

				}
			});
		}
		try {
			if (!tempSSSAccountList.isEmpty())
				sssaccountTempDAO.saveAll(tempSSSAccountList);
			// if (!fileExecList.isEmpty())
			// 	fileService.saveFileUploadExecutions(fileExecList);
			// if (!fileErrList.isEmpty())
			// 	fileService.saveFileUploadErrors(fileErrList);
			if (!sssValidAccountList.isEmpty())
				sssaccountDAO.saveAll(sssValidAccountList);
			
		} catch (Exception e) {
			
			throw new DatabaseException(e.getMessage());
		}
		
		return fileErrList.size()>0?"Partialy Migrated":"Migrated Successfully";
	}

	public List<JpaSSSAccountTemp> converttoJpaSSSAccountTemp(List<JpaSSSAccount> sssaccountList) {
		List<JpaSSSAccountTemp> list = new ArrayList<>();
		for (JpaSSSAccount jpaSSSAccount : sssaccountList) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			JpaSSSAccountTemp jpaSSSAccountTemp = mapper.map(jpaSSSAccount, JpaSSSAccountTemp.class);
			list.add(jpaSSSAccountTemp);
			
		}
		return list;
	}
}
