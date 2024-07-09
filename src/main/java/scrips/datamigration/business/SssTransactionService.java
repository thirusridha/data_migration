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
import scrips.datamigration.jpa.cbm.JpaCbmLiabilitiesBaseTemp;
import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.fileupload.JpaFileUploadError;
import scrips.datamigration.jpa.fileupload.JpaFileUploadExecution;
import scrips.datamigration.jpa.fileupload.JpaFileUploadHeader;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeTemp;
import scrips.datamigration.jpa.sss.transaction.JpaSssIncomingTransaction;
import scrips.datamigration.jpa.sss.transaction.JpaSssTransaction;
import scrips.datamigration.jpa.sss.transaction.JpaSssTransactionTemp;
import scrips.datamigration.jpa.sss.transaction.SssIncomingTransactionDAO;
import scrips.datamigration.jpa.sss.transaction.SssTransactionDAO;
import scrips.datamigration.jpa.sss.transaction.SssTransactionTempDAO;

@Service
@Slf4j
public class SssTransactionService {
	@Autowired
	ReadFileAndConvertService fileConvertService;

	@Autowired
	ValidationService validationService;

	@Autowired
	FileUploadExecutionService fileuploadExecService;

	@Autowired
	FileUploadErrorService fileErrorService;

	@Autowired
	SssTransactionDAO sssTransactionDAO;

	@Autowired
	SssTransactionTempDAO sssTransactionTempDAO;
	@Autowired
	SssIncomingTransactionDAO sssIncomingTransactionDAO;

	@Autowired
	FileUploadService fileService;
	
	private final Logger logger = LogManager.getLogger(SssTransactionService.class);

	public String migrateSssTransaction(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
			List<String> fileRecords) throws NumberFormatException, ParseException, DatabaseException, SQLException {
		fileConvertService.createAndSaveTransactionSourceData(fileRecords,
				draftDBDetails);
		List<JpaSssTransactionTemp> sssTransactionTemp = fileConvertService.convertToSssTransactionList();
		
		List<JpaFileUploadExecution> fileExecList = new ArrayList<JpaFileUploadExecution>();
		List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
		// boolean response = false;
		List<JpaSssTransaction> sssTransactionList = converttoJpaSssTransaction(sssTransactionTemp);

		if (!sssTransactionTemp.isEmpty()) {

			sssTransactionTemp.forEach(sssTransaction ->{
				try {
				JpaSssTransaction duplicateAccount = sssTransactionDAO.findBySssReference(sssTransaction.getSssReference());
				if (duplicateAccount != null) {
						System.out.println("Duplicate SssReference Found " + duplicateAccount.getSssReference());
					}
				try {
					String remarks=validationService.validationJpaSSSTransaction(sssTransaction);
					if (remarks.isEmpty() && duplicateAccount == null) {
							JpaSssTransactionTemp temp = sssTransaction;
							log.info("validated");
							boolean isLiveDataHasError = false;
							try {
								sssTransactionDAO.save(converttoJpaSssTransaction(Arrays.asList(temp)).get(0));
							} catch (Exception e) {
								isLiveDataHasError = true;
								logger.error("error while saving transaction live table data {}", e.getMessage());
								e.printStackTrace();
							}
							if (isLiveDataHasError)
								temp.setRemarks("Error while saving transaction live table data");
							if(!sssTransaction.getTransactionType().equals("ERIC"))
								sssTransactionTempDAO.save(temp);
							
//							for(JpaSssTransactionTemp temp:sssTransactionList) {
								if(temp.getTransactionType().equals("ESRC") ||temp.getTransactionType().equals("REPC")) {
									JpaSssIncomingTransaction delivererIncomingTranscation = JpaSssIncomingTransaction.builder()
											.sssReference(temp.getSssReference())
											//.messageLogId(sssTransactionList.get(0).getMessageLogId())
											.messageLogId("123")
											.securitiesCode(temp.getSecuritiesCode())
											.transactionType(temp.getTransactionType()).status(temp.getStatus())
											.settlementDate(temp.getSettlementDate())
											.tradeDate(temp.getTradeDate()).currencyCode(temp.getCurrencyCode())
											.settlementAmount(temp.getSettlementAmount())
											.nominalAmount(temp.getNominalAmount())
											.dealPrice(temp.getDealPrice())
											.delivererMemberCode(temp.getDelivererMemberCode())
											//.receiverMemberCode(temp.getReceiverMemberCode())
											.receiverMemberCode("NULL")
											.repoClosingDate(temp.getRepoClosingDate())
											.repoClosingSettlementAmount(temp.getRepoClosingSettlementAmount())
											.matchedSssReference(null)
//											.senderMemberCode(temp.get(0).getSenderMemberCode())
											.senderMemberCode("123")
											.senderReference(null).senderType(temp.getSenderType())
											.createdDate(temp.getCreatedDate())
											.modifiedDate(temp.getModifiedDate())
											.messageType(temp.getMessageType())
											//.receiverRtgsAccount(temp.getReceiverRtgsAccount())
											.receiverRtgsAccount(null)
											.delivererRtgsAccount(temp.getDelivererRtgsAccount())
											//.receiverRtgsMemberCode(temp.getReceiverRtgsMemberCode())
											.receiverRtgsMemberCode(null)
											.delivererRtgsMemberCode(temp.getDelivererRtgsMemberCode())
											.holdIndicator(temp.getHoldIndicator())
											.paymentType(temp.getPaymentType())
											.delivererMemberId(temp.getDelivererMemberId())
											//.receiverMemberId(temp.getReceiverMemberId())
											.receiverMemberId(null)
											//.receiverAccountId(temp.getReceiverAccountId())
											.receiverAccountId(null)
											.delivererAccountId(temp.getDelivererAccountId())
											.senderMemberId(temp.getSenderMemberId())
											.pdmIndicator(temp.getPdmIndicator())
											.pendingCancellationIndicator(temp.getPendingCancellationIndicator())
											.delivererAccountNo(temp.getDelivererAccountNo())
										//	.receiverAccountNo(temp.getReceiverAccountNo())
											.receiverAccountNo("NULL")
											.msgReceivedTimestamp(temp.getMsgReceivedTimestamp())
											.submittedCloSettleAmount(0.0)
											.delivererBeneficiaryAccount(" ")
											.accuredInterestAmount(null).build();
									sssIncomingTransactionDAO.save(delivererIncomingTranscation);
									
									JpaSssIncomingTransaction receiverIncomingTransaction = JpaSssIncomingTransaction.builder()
											
											//.sssReference(temp.getSssReference())
											.sssReference(UUID.randomUUID().toString())
											//.messageLogId(temp.getMessageLogId())
											.messageLogId("123")
											.securitiesCode(temp.getSecuritiesCode())
											.transactionType(temp.getTransactionType()).status(temp.getStatus())
											.settlementDate(temp.getSettlementDate())
											.tradeDate(temp.getTradeDate()).currencyCode(temp.getCurrencyCode())
											.settlementAmount(temp.getSettlementAmount())
											.nominalAmount(temp.getNominalAmount())
											.dealPrice(temp.getDealPrice())
											//.delivererMemberCode(temp.getDelivererMemberCode())
											.delivererMemberCode("NULL")
											.receiverMemberCode(temp.getReceiverMemberCode())
											.repoClosingDate(temp.getRepoClosingDate())
											.repoClosingSettlementAmount(temp.getRepoClosingSettlementAmount())
											.matchedSssReference(null)
											//.senderMemberCode(temp.getSenderMemberCode())
											.senderMemberCode("123")
											.senderReference(null).senderType(temp.getSenderType())
											.createdDate(temp.getCreatedDate())
											.modifiedDate(temp.getModifiedDate())
											.messageType(temp.getMessageType())
											.receiverRtgsAccount(temp.getReceiverRtgsAccount())
											.delivererRtgsAccount(null)
											//.delivererRtgsAccount(temp.getDelivererRtgsAccount())
											.receiverRtgsMemberCode(temp.getReceiverRtgsMemberCode())
											.delivererRtgsMemberCode(null)
											//.delivererRtgsMemberCode(temp.getDelivererRtgsMemberCode())
											.holdIndicator(temp.getHoldIndicator())
											.paymentType(temp.getPaymentType())
											//.delivererMemberId(temp.getDelivererMemberId())
											.delivererMemberId(null)
											.receiverMemberId(temp.getReceiverMemberId())
											.receiverAccountId(temp.getReceiverAccountId())
											//.delivererAccountId(ssstransaction.getDelivererAccountId())
											.delivererAccountId(null)
											.senderMemberId(temp.getSenderMemberId())
											.pdmIndicator(temp.getPdmIndicator())
											.pendingCancellationIndicator(temp.getPendingCancellationIndicator())
											//.delivererAccountNo(ssstransaction.getDelivererAccountNo())
											.delivererAccountNo("NULL")
											.receiverAccountNo(temp.getReceiverAccountNo())
											.msgReceivedTimestamp(temp.getMsgReceivedTimestamp())
											.submittedCloSettleAmount(0.0)
											//.delivererBeneficiaryAccount(temp.getDelivererBeneficiaryAccount())
											.delivererBeneficiaryAccount(" ")
											.accuredInterestAmount(null).build();
									sssIncomingTransactionDAO.save(receiverIncomingTransaction);
								}		
							
							
							
							JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
									fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
									UUID.randomUUID().toString(), "Migratted Sucessfully");
							fileExecList.add(fileUplodExec);
						} else {
							if (duplicateAccount != null) {
								remarks = remarks.concat(",Duplicate SssReference");
							}
							JpaSssTransactionTemp tempObj = sssTransaction;
							if (tempObj != null) {
								remarks = remarks.substring(1);
								JpaSssTransactionTemp temp = sssTransaction;
								temp.setRemarks(remarks);
								sssTransactionTempDAO.save(temp);
							}
								JpaFileUploadError errObj = fileErrorService.createFileUploadError(
										fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
										UUID.randomUUID().toString(), 101);
								fileErrList.add(errObj);
						}
						}	 catch (FailedValidationException Fve) {
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
						
						
			
						
//						
					
	public List<JpaSssTransaction> converttoJpaSssTransaction(List<JpaSssTransactionTemp> ssstransactionTemp) {
		List<JpaSssTransaction> list = new ArrayList<>();
		for (JpaSssTransactionTemp jpaSssTransactionTemp : ssstransactionTemp) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			JpaSssTransaction jpaSssTransaction = mapper.map(jpaSssTransactionTemp, JpaSssTransaction.class);
			list.add(jpaSssTransaction);

		}
		return list;
	}
}
