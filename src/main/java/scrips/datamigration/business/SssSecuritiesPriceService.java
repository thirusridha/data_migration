package scrips.datamigration.business;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.print.DocFlavor.STRING;

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
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesPositionStats;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesPositionStatsTemp;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesPrice;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesPriceTemp;
import scrips.datamigration.jpa.sss.securities.SssSecuritiesPriceDAO;
import scrips.datamigration.jpa.sss.securities.SssSecuritiesPriceTempDAO;
@Service
@Slf4j
public class SssSecuritiesPriceService {
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
	SssSecuritiesPriceDAO securityPriceDAO;

	@Autowired
	SssSecuritiesPriceTempDAO securityPriceTempDAO;

	@Autowired
	FileUploadService fileService;

	public String migrateSssSecurityPrice(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
			List<String> fileRecords) throws NumberFormatException, ParseException, DatabaseException {
		
//		List<JpaSssSecuritiesPriceTemp> securityPriceTemp = fileConvertService.convertToSssSecurityPriceList(fileRecords, draftDBDetails);
		fileConvertService.createAndSaveSecuritiesPriceSourceData(fileRecords, draftDBDetails);
		List<JpaSssSecuritiesPriceTemp> securityPriceTemp = fileConvertService.convertToSssSecurityPriceList();
		List<JpaSssSecuritiesPrice> validSecurityPriceList = new ArrayList<JpaSssSecuritiesPrice>();
		List<JpaFileUploadExecution> fileExecList = new ArrayList<JpaFileUploadExecution>();
		List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
		boolean response = false;
		List<JpaSssSecuritiesPrice> securityPriceList = convertToJpaSssSecurityPrice(securityPriceTemp);
		if (!securityPriceTemp.isEmpty()) {
			securityPriceTemp.stream().forEach(securityPrice -> {
				JpaSssSecuritiesPrice duplicateSecuritiesCode = securityPriceDAO
						.findBySecuritiesCode(securityPrice.getSecuritiesCode());
				
				if (duplicateSecuritiesCode != null) {
					System.out.println("Duplicate Securities code Found " + duplicateSecuritiesCode.getSecuritiesCode());
				}
				try {
					String remarks = validationService.validationJpaSecurityPrice(securityPrice);
					if (remarks.isEmpty() && duplicateSecuritiesCode == null) {
						JpaSssSecuritiesPriceTemp tempObj = securityPrice;
						log.info("validated");
//						securityPriceDAO.save(convertToJpaSssSecurityPrice(Arrays.asList(tempObj)).get(0));
//						securityPriceTempDAO.save(securityPrice);
						boolean isLiveDataHasError = false;
						try {
							securityPriceDAO.save(convertToJpaSssSecurityPrice(Arrays.asList(tempObj)).get(0));
						} catch (Exception e) {
							isLiveDataHasError = true;
							logger.error(", error while saving securities code live table data {}", e.getMessage());
							e.printStackTrace();
						}

						if (isLiveDataHasError)
							tempObj.setRemarks(", Error while saving securities code live table data");
						securityPriceTempDAO.save(securityPrice);
						JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
								fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
								UUID.randomUUID().toString(), "Migratted Sucessfully");
						fileExecList.add(fileUplodExec);
					} else {
						if (duplicateSecuritiesCode != null) {
							remarks = remarks.concat(",Duplicate Securities Code");
						}
						JpaSssSecuritiesPriceTemp tempObj = securityPrice;
						if (tempObj != null) {
							remarks = remarks.substring(1);
							JpaSssSecuritiesPriceTemp temp = tempObj;
							temp.setRemarks(remarks);
							securityPriceTempDAO.save(temp);
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
		return fileErrList.size()>0?"Partialy Migrated":"Migrated Successfully";
		
	}
	public List<JpaSssSecuritiesPrice> convertToJpaSssSecurityPrice(List<JpaSssSecuritiesPriceTemp> securityPosListTemp) {
		List<JpaSssSecuritiesPrice> list = new ArrayList<>();
		for (JpaSssSecuritiesPriceTemp securityPosTemp : securityPosListTemp) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			JpaSssSecuritiesPrice securityPos= mapper.map(securityPosTemp, JpaSssSecuritiesPrice.class);
			list.add(securityPos);
			
		}
		return list;
	}
}