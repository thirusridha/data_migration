package scrips.datamigration.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import scrips.datamigration.exception.DatabaseException;
import scrips.datamigration.exception.FailedValidationException;
import scrips.datamigration.jpa.account.JpaAccount;
import scrips.datamigration.jpa.account.JpaAccountTemp;
import scrips.datamigration.jpa.accountposition.AccountPositionDAO;
import scrips.datamigration.jpa.accountposition.AccountPositionDAOTemp;
import scrips.datamigration.jpa.accountposition.JpaAccountPosition;
import scrips.datamigration.jpa.accountposition.JpaAccountPositionTemp;
import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.fileupload.JpaFileUploadError;
import scrips.datamigration.jpa.fileupload.JpaFileUploadExecution;
import scrips.datamigration.jpa.fileupload.JpaFileUploadHeader;
import scrips.datamigration.jpa.member.JpaMember;
import scrips.datamigration.jpa.member.MemberDAO;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeTemp;

@Service
@Slf4j
public class AccountPositionServvice {
	private final Logger logger = LogManager.getLogger(AccountPositionServvice.class);
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
	MemberDAO memberDAO;
	@Autowired
	AccountPositionDAO accountPosDAO;
	@Autowired
	AccountPositionDAOTemp accountPosTempDAO;

	public String migrateAccountPosition(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
			List<String> fileRecords) throws DatabaseException, FailedValidationException, ParseException {
		List<JpaFileUploadExecution> fileExecList = new ArrayList<JpaFileUploadExecution>();
		fileConvertService.createAndSaveAccountPositionSourceData(fileRecords, draftDBDetails);
		List<JpaAccountPositionTemp> accountObjlistTemp = fileConvertService.convertToJpaAccountPosition();
		List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
		accountObjlistTemp.forEach(accountPositionTemp -> {
			try {
				JpaAccountPosition duplicateAccount = accountPosDAO.findByAccountId(accountPositionTemp.getAccountId());
				System.out.println("account number -" + duplicateAccount);
				System.out.println("get account Number" + accountPositionTemp.getAccountId());
				if (duplicateAccount != null) {
					System.out.println("Duplicate Account Number Found " + duplicateAccount.getAccountId());
				}
				String remarks = validationService.validationJpaAccountPosition(accountPositionTemp);
				System.out.println(remarks);
				System.out.println(duplicateAccount);
				if (remarks.isEmpty() && duplicateAccount == null) {
					JpaAccountPositionTemp tempObj = accountPositionTemp;
					JpaAccountPositionTemp temp = tempObj;
					log.info("validated");
					boolean isLiveDataHasError = false;
					try {
						accountPosDAO.save(converAccPosition(Arrays.asList(tempObj)).get(0));
					} catch (Exception e) {
						isLiveDataHasError = true;
						logger.error(", error while saving securities code live table data {}", e.getMessage());
						e.printStackTrace();
					}

					if (isLiveDataHasError)
						temp.setRemarks(", Error while saving securities code live table data");
					accountPosTempDAO.save(temp);
					JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
							fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
							UUID.randomUUID().toString(), "Migratted Sucessfully");
					fileExecList.add(fileUplodExec);
				} else {
					if (duplicateAccount != null) {
						remarks = remarks.concat(",Duplicate AccountNo");
					}

					JpaAccountPositionTemp tempObj = accountPositionTemp;
					if (tempObj != null) {
						remarks = remarks.substring(1);
						JpaAccountPositionTemp temp = tempObj;
						temp.setRemarks(remarks);
						accountPosTempDAO.save(temp);
					}
					JpaFileUploadError errObj = fileErrorService.createFileUploadError(fileHeaderObj.getFileUploadId(),
							fileHeaderObj.getFileUploadCode(), UUID.randomUUID().toString(), 101);
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
		return fileErrList.size() > 0 ? "Partially Migrated" : "Migrated Sucessfully";

	}

	public List<JpaAccountPosition> converAccPosition(List<JpaAccountPositionTemp> list2) {
		List<JpaAccountPosition> list = new ArrayList<>();
		for (JpaAccountPositionTemp jpaAccountTemp : list2) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			JpaAccountPosition jpaAccount = mapper.map(jpaAccountTemp, JpaAccountPosition.class);
			list.add(jpaAccount);
			// log.info(jpaAccountTemp.getAccountSettlementPurpose().getCurrencyCode());
		}
		return list;
	}
}