package scrips.datamigration.business;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import scrips.datamigration.jpa.sss.Member.JpaSssMember;
import scrips.datamigration.jpa.sss.Member.JpaSssMemberTemp;
import scrips.datamigration.jpa.sss.Member.SssMemberDAO;
import scrips.datamigration.jpa.sss.Member.SssMemberTempDAO;
import scrips.datamigration.jpa.sss.coupon.SssFloatingRatesDAO;
import scrips.datamigration.jpa.sss.coupon.SssFloatingRatesTempDAO;
@Service
@Slf4j
public class SssMemberService {
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
	SssMemberDAO sssmemberDAO;

	@Autowired
	SssMemberTempDAO sssmemberTempDAO;
	
	public String migrateSssMember(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
	List<String> fileRecords) throws NumberFormatException, ParseException, DatabaseException {

		List<JpaSssMember> sssMemberObjlist = fileConvertService.convertToSssMemberList(fileRecords, draftDBDetails);
		draftDBDetails.forEach(a->{log.info(a.getTableFieldName());});
		List<JpaSssMember> ValidSssMemberList = new ArrayList<JpaSssMember>();
		List<JpaFileUploadExecution> fileExecList = new ArrayList<JpaFileUploadExecution>();
		List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
		List<JpaSssMemberTemp> tempSssMemberList = converttoJpaSssMemberTemp(sssMemberObjlist);
		if (!sssMemberObjlist.isEmpty()) {
			sssMemberObjlist.stream().forEach(sssmember -> {
			 try {
				 if (validationService.validationJpaSSSMember(sssmember)) {
				 			log.info("validated");
				 			ValidSssMemberList.add(sssmember);
//				 			JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
//				 			fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
//				 			UUID.randomUUID().toString(), "Migratted Sucessfully");
//				 			fileExecList.add(fileUplodExec);
				 }
				 else {
				 		  JpaFileUploadError errObj = fileErrorService.createFileUploadError(
				 		  fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
				 		  UUID.randomUUID().toString(), 101);
				 		  fileErrList.add(errObj);
				 }
			 }catch (FailedValidationException Fve) {
				 	JpaFileUploadError errObj = fileErrorService.createFileUploadError(fileHeaderObj.getFileUploadId(),
				 	fileHeaderObj.getFileUploadCode(), UUID.randomUUID().toString(), 102);
				 	fileErrList.add(errObj);
				 	log.info("{} - {}", Fve.getMessage());
			 } catch (Exception e) {
				 	log.error("Errors {}", e.getMessage()); }
			});
		}
		try {
			if (!tempSssMemberList.isEmpty())
				sssmemberTempDAO.saveAll(tempSssMemberList);
		//	if (!fileExecList.isEmpty())
				//	fileService.saveFileUploadExecutions(fileExecList);
		//	if (!fileErrList.isEmpty())
				//	fileService.saveFileUploadErrors(fileErrList);
			if (!ValidSssMemberList.isEmpty())
				sssmemberDAO.saveAll(ValidSssMemberList);
		} 
		catch (Exception e) {
				throw new DatabaseException(e.getMessage());
		}
		return fileErrList.size()>0?"partially Migrated":"Migrated Successfully";
		//return response;
	}
	private List<JpaSssMemberTemp> converttoJpaSssMemberTemp(List<JpaSssMember> sssMemberList) {

		List<JpaSssMemberTemp> list = new ArrayList<>();
		for (JpaSssMember jpaSssMember : sssMemberList) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		JpaSssMemberTemp jpaSssMemberTemp = mapper.map(jpaSssMember, JpaSssMemberTemp.class);
		list.add(jpaSssMemberTemp);
		//System.out.println("List--------->"+list);
		}
		return list;
	}
}
