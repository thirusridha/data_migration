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
import scrips.datamigration.jpa.accountposition.JpaAccountPositionTemp;
import scrips.datamigration.jpa.cbm.JpaCbmFloatingRate;
import scrips.datamigration.jpa.cbm.JpaCbmFloatingRateTemp;
import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.fileupload.JpaFileUploadError;
import scrips.datamigration.jpa.fileupload.JpaFileUploadExecution;
import scrips.datamigration.jpa.fileupload.JpaFileUploadHeader;
import scrips.datamigration.jpa.sss.coupon.JpaSssFloatingRates;
import scrips.datamigration.jpa.sss.coupon.JpaSssFloatingRatesTemp;
import scrips.datamigration.jpa.sss.coupon.SssFloatingRatesDAO;
import scrips.datamigration.jpa.sss.coupon.SssFloatingRatesTempDAO;
import scrips.datamigration.jpa.sss.transaction.JpaSssTransaction;
import scrips.datamigration.jpa.sss.transaction.JpaSssTransactionTemp;
import scrips.datamigration.jpa.sss.transaction.SssTransactionDAO;
import scrips.datamigration.jpa.sss.transaction.SssTransactionTempDAO;

@Service
@Slf4j
public class SssFloatingRatesService {
	private final Logger logger = LogManager.getLogger(SssFloatingRatesService.class);
	@Autowired
	ReadFileAndConvertService fileConvertService;

	@Autowired
	ValidationService validationService;

	@Autowired
	FileUploadExecutionService fileuploadExecService;

	@Autowired
	FileUploadErrorService fileErrorService;

	@Autowired
	SssFloatingRatesDAO floatingratesDAO;

	@Autowired
	SssFloatingRatesTempDAO floatingratesTempDAO;

	@Autowired
	FileUploadService fileService;
	@Autowired
	SssFloatingRatesDAO sssFloatingRatesDAO;
	@Autowired
	SssFloatingRatesTempDAO sssFloatingRatesTempDAO;	
	
	public String migrateSssFloatingRates(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
			List<String> fileRecords) throws NumberFormatException, ParseException, DatabaseException
			{
//				List<JpaSssFloatingRatesTemp> sssfloatingratesTemp = fileConvertService.convertToSssfloatingratesList(fileRecords, draftDBDetails);
				fileConvertService.createAndSaveSssFloatingRatesSourceData(fileRecords, draftDBDetails);
				List<JpaSssFloatingRatesTemp> sssfloatingratesTemp = fileConvertService.convertToSssfloatingratesList();
				List<JpaSssFloatingRates> validSssfloatingrates = new ArrayList<JpaSssFloatingRates>();
				List<JpaFileUploadExecution> fileExecList = new ArrayList<JpaFileUploadExecution>();
				List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
				boolean response = false;
				List<JpaSssFloatingRates> sssfloatingrates = converttoJpaSssFloatingRates(sssfloatingratesTemp);
				if (!sssfloatingratesTemp.isEmpty()) {

					sssfloatingratesTemp.stream().forEach(floatingratesTemp -> {
//						JpaSssFloatingRates duplicateReferenceRate = sssFloatingRatesDAO.findByReferenceRate(floatingratesTemp.getReferenceRate());
//						JpaSssFloatingRates duplicateValueDate = sssFloatingRatesDAO.findByValueDate(floatingratesTemp.getValueDate());
						JpaSssFloatingRates duplicateKey = sssFloatingRatesDAO.findByReferenceRateAndValueDate(
								floatingratesTemp.getReferenceRate(), floatingratesTemp.getValueDate());
						if (duplicateKey != null) {
							System.out.println("Duplicate primary key Found " + duplicateKey.getReferenceRate());
						}
//						
						try {
						String remarks = validationService.validationJpaSssFloatingRate(floatingratesTemp);
						if (remarks.isEmpty() && duplicateKey==null) {
							JpaSssFloatingRatesTemp tempObj = floatingratesTemp;
							log.info("validated");
							boolean isLiveDataHasError = false;
							try {
								sssFloatingRatesDAO.save(converttoJpaSssFloatingRates(Arrays.asList(tempObj)).get(0));
							} catch (Exception e) {
								isLiveDataHasError = true;
								logger.error(", error while saving securities code live table data {}", e.getMessage());
								e.printStackTrace();
							}

							if (isLiveDataHasError)
								tempObj.setRemarks(", Error while saving securities code live table data");
							sssFloatingRatesTempDAO.save(floatingratesTemp);
							JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
									fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
									UUID.randomUUID().toString(), "Migratted Sucessfully");
							fileExecList.add(fileUplodExec);
						} else {
							if (duplicateKey != null) {
								remarks = remarks.concat(",Duplicate ReferenceRate,Duplicate ValueDate");
//								remarks = remarks.concat(",Duplicate Primary key");
							}
//							if (duplicateValueDate != null) {
//								remarks = remarks.concat(",Duplicate ValueDate");
//							}
//							if (duplicateValueDate != null && duplicateReferenceRate != null) {
//								remarks = remarks.concat(",Duplicate ReferenceRate,Duplicate ValueDate");
//							}
							JpaSssFloatingRatesTemp tempObj = floatingratesTemp;
							if(tempObj != null) {
							remarks = remarks.substring(1);
							JpaSssFloatingRatesTemp temp = tempObj;
							temp.setRemarks(remarks);
							sssFloatingRatesTempDAO.save(temp);
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
			public List<JpaSssFloatingRates> converttoJpaSssFloatingRates(List<JpaSssFloatingRatesTemp> sssfloatingratesListTemp) {
				List<JpaSssFloatingRates> list = new ArrayList<>();
				for (JpaSssFloatingRatesTemp jpaSssFloatingRatesTemp : sssfloatingratesListTemp) {
					ModelMapper mapper = new ModelMapper();
					mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
					JpaSssFloatingRates sssFloatingRates = mapper.map(jpaSssFloatingRatesTemp, JpaSssFloatingRates.class);
					list.add(sssFloatingRates);
							}
				return list;
			}
		}