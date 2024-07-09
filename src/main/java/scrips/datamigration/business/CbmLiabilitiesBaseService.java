package scrips.datamigration.business;

import java.sql.SQLException;
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
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBase;
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBaseDAO;
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBaseDetail;
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBaseDetailTemp;
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBaseTemp;
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBaseTempDAO;
import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.fileupload.JpaFileUploadError;
import scrips.datamigration.jpa.fileupload.JpaFileUploadExecution;
import scrips.datamigration.jpa.fileupload.JpaFileUploadHeader;
import scrips.datamigration.jpa.member.JpaMember;
import scrips.datamigration.jpa.member.MemberDAO;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeTemp;

@Service
@Slf4j
public class CbmLiabilitiesBaseService {

	private final Logger logger = LogManager.getLogger(CbmLiabilitiesBaseService.class);
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
	JpaCbmLiabilitiesBaseDAO jpaCbmLiabilitiesBaseDAO;

	@Autowired
	JpaCbmLiabilitiesBaseTempDAO cbmLiabilitiesBaseTempDAO;

	@Autowired
	MemberDAO memberDAO;

	@Autowired
	JpaCbmLiabilitiesBaseDAO cbmLiabilitiesBaseDAO;

	public String migrateCbmLiabilitiesBase(JpaFileUploadHeader fileHeaderObj,
			List<JpaFileUploadDetails> draftDBDetails, List<String> fileRecords)
			throws NumberFormatException, ParseException, DatabaseException, SQLException {
		fileConvertService.createAndSaveCbmLiabilitiesBaseSourceData(fileRecords, draftDBDetails);
		List<JpaCbmLiabilitiesBaseTemp> cbmLiabilitiesBaseTemp = fileConvertService
				.convertToJpaCbmLiabilitiesBaseList();
		List<JpaFileUploadExecution> fileExecList = new ArrayList<JpaFileUploadExecution>();
		List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
		List<JpaCbmLiabilitiesBase> cbmLiabilitiesBase = converttoJpaCbmLiabilitiesBase(cbmLiabilitiesBaseTemp);

		if (!cbmLiabilitiesBaseTemp.isEmpty()) {
			cbmLiabilitiesBaseTemp.stream().forEach(cbmLiabilitiesTemp -> {
				try {
					JpaCbmLiabilitiesBase duplicateAccount = cbmLiabilitiesBaseDAO
							.findByMemberId(cbmLiabilitiesTemp.getMemberId());
					if (duplicateAccount != null) {
						System.out.println("Duplicate Member Found " + duplicateAccount.getMemberId());
					}
					try {
						String remarks = validationService.validationJpaCbmLiabilitiesBase(cbmLiabilitiesTemp);
						if (remarks.isEmpty() && duplicateAccount == null) {
							JpaCbmLiabilitiesBaseTemp temp = cbmLiabilitiesTemp;
							log.info("validated");
							boolean isLiveDataHasError = false;
							try {
								cbmLiabilitiesBaseDAO.save(converttoJpaCbmLiabilitiesBase(Arrays.asList(temp)).get(0));
							} catch (Exception e) {
								isLiveDataHasError = true;
								logger.error("error while saving liabilities base live table data {}", e.getMessage());
								e.printStackTrace();
							}
							if (isLiveDataHasError)
								temp.setRemarks("Error while saving liabilities base live table data");
							cbmLiabilitiesBaseTempDAO.save(temp);
							JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
									fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
									UUID.randomUUID().toString(), "Migratted Sucessfully");
							fileExecList.add(fileUplodExec);
						} else {
							if (duplicateAccount != null) {
								remarks = remarks.concat(",Duplicate MemberId");
							}
							JpaCbmLiabilitiesBaseTemp tempObj = cbmLiabilitiesTemp;
							if (tempObj != null) {
								remarks = remarks.substring(1);
								JpaCbmLiabilitiesBaseTemp temp = cbmLiabilitiesTemp;
								temp.setRemarks(remarks);
								cbmLiabilitiesBaseTempDAO.save(temp);
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
		return fileErrList.size() > 0 ? "Partially Migrated" : "Migrated Sucessfully";
	}

	public List<JpaCbmLiabilitiesBase> converttoJpaCbmLiabilitiesBase(
			List<JpaCbmLiabilitiesBaseTemp> cbmLiabilitiesBasesTemp) {
		List<JpaCbmLiabilitiesBase> list = new ArrayList<>();
		for (JpaCbmLiabilitiesBaseTemp jpaCbmLiabilitiesBaseTemp : cbmLiabilitiesBasesTemp) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			JpaCbmLiabilitiesBase jpaCbmLiabilitiesBase = mapper.map(jpaCbmLiabilitiesBaseTemp,
					JpaCbmLiabilitiesBase.class);
			list.add(jpaCbmLiabilitiesBase);
		}
		return list;
	}
}
