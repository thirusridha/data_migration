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
import scrips.datamigration.jpa.cbm.JpaCbmDepositRate;
import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.fileupload.JpaFileUploadError;
import scrips.datamigration.jpa.fileupload.JpaFileUploadExecution;
import scrips.datamigration.jpa.fileupload.JpaFileUploadHeader;
import scrips.datamigration.jpa.member.JpaMember;
import scrips.datamigration.jpa.member.MemberDAO;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeTemp;

@Service
@Slf4j
public class AccountService {

	private final Logger logger = LogManager.getLogger(AccountService.class);
	@Autowired
	ReadFileAndConvertService fileConvertService;
	@Autowired
	ValidationService validationService;
	@Autowired
	FileUploadExecutionService fileuploadExecService;
	@Autowired
	FileUploadErrorService fileErrorService;
	@Autowired
	AccountDAO accountDAO;
	@Autowired
	AccountTempDAO accountTempDAO;
	@Autowired
	FileUploadService fileService;
	@Autowired
	MemberDAO memberDAO;

	public String migrateAccount(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
			List<String> fileRecords) throws NumberFormatException, ParseException, DatabaseException {
		fileConvertService.createAndSaveAccountSourceData(fileRecords, draftDBDetails);
		List<JpaAccountTemp> accountObjlist = fileConvertService.convertToAccountList();
		List<JpaFileUploadExecution> fileExecList = new ArrayList<JpaFileUploadExecution>();
		List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
		List<JpaAccount> jpaAccount = converttoJpaAccount(accountObjlist);

		accountObjlist.stream().forEach(account -> {
			try {
				JpaAccount duplicateAccount = accountDAO.findByAccountNumber(account.getAccountNumber());
				if (duplicateAccount != null) {
					System.out.println("Duplicate Account Number Found " + duplicateAccount.getAccountNumber());
				}
				try {
					String remarks = validationService.validationJpaAccount(account);
					if (remarks.isEmpty() && duplicateAccount == null) {
						JpaAccountTemp tempObj = account;
						log.info("validated");
						boolean isLiveDataHasError = false;
						try {
							accountDAO.save(converttoJpaAccount(Arrays.asList(tempObj)).get(0));
						} catch (Exception e) {
							isLiveDataHasError = true;
							logger.error("error while saving account live table data {}", e.getMessage());
							e.printStackTrace();
						}
						if (isLiveDataHasError)
							tempObj.setRemarks("Error while saving liabilities base live table data");

						accountTempDAO.save(account);
						JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
								fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
								UUID.randomUUID().toString(), "Migratted Sucessfully");
						fileExecList.add(fileUplodExec);
					} else {
						if (duplicateAccount != null) {
							remarks = remarks.concat(",Duplicate AccountNo");
						}
						JpaAccountTemp tempObj = account;
						if (tempObj != null) {
							remarks = remarks.substring(1);
							JpaAccountTemp temp = tempObj;
							temp.setRemarks(remarks);
							accountTempDAO.save(temp);
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
		return fileErrList.size() > 0 ? "Partially Migrated" : "Migrated Sucessfully";
	}

	public List<JpaAccount> converttoJpaAccount(List<JpaAccountTemp> accountList) {
		List<JpaAccount> list = new ArrayList<>();
		for (JpaAccountTemp jpaAccountTemp : accountList) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			JpaAccount jpaAccount = mapper.map(jpaAccountTemp, JpaAccount.class);
			list.add(jpaAccount);
		}
		return list;
	}
}
