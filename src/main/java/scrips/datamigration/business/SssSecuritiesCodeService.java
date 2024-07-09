package scrips.datamigration.business;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPCmd;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import scrips.datamigration.exception.DatabaseException;
import scrips.datamigration.exception.FailedValidationException;
import scrips.datamigration.jpa.coupon.data.JpaStepupCoupon;
import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.fileupload.JpaFileUploadError;
import scrips.datamigration.jpa.fileupload.JpaFileUploadHeader;
import scrips.datamigration.jpa.sss.securities.CouponScheduleDAO;
import scrips.datamigration.jpa.sss.securities.JpaCouponShedules;
import scrips.datamigration.jpa.sss.securities.JpaSecuritiesCoupon;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCode;
import scrips.datamigration.jpa.sss.securities.JpaSssSecuritiesCodeTemp;
import scrips.datamigration.jpa.sss.securities.SecuritiesCouponDAO;
import scrips.datamigration.jpa.sss.securities.SssSecuritiesCodeDAO;
import scrips.datamigration.jpa.sss.securities.SssSecuritiesCodeTempDAO;

@Service
@Slf4j
//@PropertySource("classpath:fileupload.properties")
public class SssSecuritiesCodeService {

	private final Logger logger = LogManager.getLogger(SssSecuritiesCodeService.class);
	@Autowired
	private Environment env;

	@Autowired
	ReadFileAndConvertService fileConvertService;
	@Autowired
	ValidationService validationService;

	@Autowired
	FileUploadExecutionService fileuploadExecService;

	@Autowired
	FileUploadErrorService fileErrorService;

	@Autowired
	SssSecuritiesCodeDAO sssSecuritiesCodeDAO;

	@Autowired
	SssSecuritiesCodeTempDAO sssSecuritiesCodeTempDAO;

	@Autowired
	FileUploadService fileService;

	@Autowired
	CouponScheduleDAO couponSheduleDAO;

	@Autowired
	SecuritiesCouponDAO securitiesCouponDAO;
	
	@Autowired
	SftpService sftpService;
	
	private Map<String, JpaStepupCoupon> initializeStepUpData(JpaFileUploadHeader fileHeaderObj) throws Exception {
		Map<String, JpaStepupCoupon> map = new HashMap<String, JpaStepupCoupon>();
//		FTPClient ftpCtrl = new FTPClient();
//		
	logger.info("ftp status {}",env.getProperty("FTP.ENABLED"));
		String ftpStatus = env.getProperty("FTP.ENABLED");
		if (ftpStatus != null && ftpStatus.trim().equalsIgnoreCase("y")) {
			try {
//				logger.info("Starting connect to - {}", fileHeaderObj.getIpAddress());
//				ftpCtrl.connect(fileHeaderObj.getIpAddress(),Integer.parseInt(env.getProperty("FTP.PORT")));
//				logger.info("Connection Success ");
//				boolean isSuccess = ftpCtrl.login(env.getProperty("FTP.USER_NAME"), env.getProperty("FTP.PASSWORD"));
//				logger.info("isSuccess -" + isSuccess);
//				logger.info(ftpCtrl.getReplyString());
//				ftpCtrl.sendCommand(FTPCmd.CWD, env.getProperty("STEPUP_PATHNAME"));
//				logger.info(ftpCtrl.getReplyString());
//				if (isSuccess) {
//					logger.info("Login to remote is success");
//					logger.info(ftpCtrl.getReplyString());
//					InputStream fileStream = ftpCtrl.retrieveFileStream(env.getProperty("STEPUP_FILE_NAME"));
				map.clear();
//				 fileStream = sftpService.readFileInputStreamFromSFTPDirectroy(
//						env.getProperty("STEPUP_PATHNAME") + env.getProperty("STEPUP_FILE_NAME"));
//				 sc = new Scanner(fileStream).useDelimiter("\\A");
				List<String> fileRecords=sftpService.readFileFromSFTPDirectroy(env.getProperty("FTP.FILE_PATH") , env.getProperty("STEPUP_FILE_NAME"));
				 for(String str: fileRecords) {
					String id = UUID.randomUUID().toString();
					String arr1[] = (str).split("\\|");
					JpaStepupCoupon coupon = JpaStepupCoupon.builder().id(id).securityCode(arr1[0]).couponPaymentDate(arr1[1]).couponRate(arr1[2]).build();
					map.put(id, coupon);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
//					sc.close();
//				}
//			} catch (IOException Err) {
//				System.out.println(Err);
//				Err.printStackTrace();
//			} catch (Exception e) {
//				logger.error("error while parsing stepup file {}", e.getMessage());
//				throw e;
//			} finally {
//				try {
//
//					if (ftpCtrl.isConnected()) {
//						ftpCtrl.logout();
//						ftpCtrl.disconnect();
//					}
//				} catch (IOException ex) {
//					ex.printStackTrace();
//				}
			// }
		} else {
			try {
				InputStream fileStream = null;
				fileStream = new BufferedInputStream(
						new FileInputStream("./MigrationFiles/" + env.getProperty("STEPUP_FILE_NAME")));
				Scanner sc = new Scanner(fileStream).useDelimiter("\\A");
				while (sc.hasNextLine()) {
					String id = UUID.randomUUID().toString();
					String arr1[] = (sc.nextLine()).split("\\|");
					JpaStepupCoupon coupon = JpaStepupCoupon.builder().id(id).securityCode(arr1[0])
							.couponPaymentDate(arr1[1]).couponRate(arr1[2]).build();
					map.put(id, coupon);
				}
				sc.close();
			} catch (Exception e) {
				logger.error("error while parsing stepup file {}", e.getMessage());
				throw e;
			}
		}
		return map;
	}

	Map<String, JpaStepupCoupon> map = null;

	public String migrateSssSecuritiesCode(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
			List<String> fileRecords) throws NumberFormatException, ParseException, DatabaseException,
			FileNotFoundException, ClassNotFoundException, SQLException {
		map = null;

		fileConvertService.createAndSaveSecuritiesCodeSourceData(fileRecords, draftDBDetails);

		List<JpaSssSecuritiesCodeTemp> securitiesCodeTempList = fileConvertService.convertToSssSecuritiesCodeList();
		try {
			map = initializeStepUpData( fileHeaderObj);
		} catch (Exception e) {
			logger.error("errror while reading stepup data {}", e.getMessage());
			map = null;
		}

		// List<JpaSssSecuritiesCode> validsecuritiesCodeList = new
		// ArrayList<JpaSssSecuritiesCode>();
		// List<JpaFileUploadExecution> fileExecList = new
		// ArrayList<JpaFileUploadExecution>();
		List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
		List<JpaSssSecuritiesCode> securitiesCodeList = convertTJpaSssSecuritiesCode(securitiesCodeTempList);

		if (!securitiesCodeList.isEmpty() && map != null) {
			securitiesCodeList.stream().forEach(securitiesCode -> {
				try {
					JpaSssSecuritiesCode duplicateSecuritiesCode = sssSecuritiesCodeDAO
							.findBySecuritiesCode(securitiesCode.getSecuritiesCode());
					String remarks = validationService.validationJpaSSSSecuritiesCode(securitiesCode);
					JpaSecuritiesCoupon duplicateSecuritiesCoupon = securitiesCouponDAO
							.findBySecuritiesCode(securitiesCode.getSecuritiesCode());
					if (duplicateSecuritiesCode != null || duplicateSecuritiesCoupon != null) {
						remarks = remarks.concat(",duplicate Securities Code");
					}
					if (remarks.isEmpty() && ( duplicateSecuritiesCode == null || duplicateSecuritiesCoupon != null)) {

						Optional<JpaSssSecuritiesCodeTemp> tempObj = securitiesCodeTempList.stream()
								.filter(x -> x.getSecuritiesCode().equals(securitiesCode.getSecuritiesCode()))
								.findAny();
						JpaSssSecuritiesCodeTemp temp = tempObj.get();

						boolean isLiveDataHasError = false;
						try {
							sssSecuritiesCodeDAO.save(securitiesCode);

						} catch (Exception e) {
							isLiveDataHasError = true;
							logger.error("error while saving securities code live table data {}", e.getMessage());
							e.printStackTrace();
						}

						boolean isCouponLiveDataHasError = false;
						if (!isLiveDataHasError) {
							try {
								saveSecuritiesCoupon(securitiesCode);
								saveCouponSheduleData(securitiesCode);
							} catch (Exception e) {
								isCouponLiveDataHasError = true;
								logger.error("error while saving Coupon Schedule data {}", e.getMessage());
								e.printStackTrace();
							}
						}
						if (isCouponLiveDataHasError && isLiveDataHasError)
							temp.setRemarks("Error while saving securities code and CouponShedule live table data");
						if (isLiveDataHasError)
							temp.setRemarks("Error while saving securities code live table data");
						if (isCouponLiveDataHasError)
							temp.setRemarks("Error while saving CouponShedule live table data");

						sssSecuritiesCodeTempDAO.save(temp);

//						JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
//								fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
//								UUID.randomUUID().toString(), "Migratted Sucessfully");
//						fileExecList.add(fileUplodExec);
					} else {
						Optional<JpaSssSecuritiesCodeTemp> tempObj = securitiesCodeTempList.stream()
								.filter(x -> x.getSecuritiesCode().equals(securitiesCode.getSecuritiesCode()))
								.findFirst();
						if (duplicateSecuritiesCode != null || duplicateSecuritiesCoupon!=null) {
							List<JpaSssSecuritiesCodeTemp> tempList = new ArrayList<JpaSssSecuritiesCodeTemp>();
							tempList.add(tempObj.get());
							tempObj = securitiesCodeTempList.stream()
									.filter(x -> x.getSecuritiesCode().equals(securitiesCode.getSecuritiesCode())
											&& !x.getId().equals(tempList.get(0).getId()))
									.findFirst();
						}
						if (tempObj != null && tempObj.get() != null) {
							remarks = remarks.substring(1);
							JpaSssSecuritiesCodeTemp temp = tempObj.get();
							temp.setRemarks(remarks);
							sssSecuritiesCodeTempDAO.save(temp);
						}
						JpaFileUploadError errObj = fileErrorService.createFileUploadError(1, "SECURITIES_CODE",
								UUID.randomUUID().toString(), 101);
						fileErrList.add(errObj);

					}
				} catch (FailedValidationException Fve) {
					JpaFileUploadError errObj = fileErrorService.createFileUploadError(1, "SECURITIES_CODE",
							UUID.randomUUID().toString(), 102);
					fileErrList.add(errObj);
					log.info("{} - {}", Fve.getMessage());
				} catch (Exception e) {
					log.error("Error {}", e.getMessage());

				}
			});
		}
		return fileErrList.size() > 0 ? "Partially Migrated" : "Migrated Sucessfully";
	}

	private void saveSecuritiesCoupon(JpaSssSecuritiesCode securitiesCode) {
		JpaSecuritiesCoupon jpaSecuritiesCoupon = (JpaSecuritiesCoupon.builder())
				.securitiesCode(securitiesCode.getSecuritiesCode()).action(securitiesCode.getAction())
				.status(securitiesCode.getStatus()).modifiedBy(securitiesCode.getModifiedBy())
				.modifiedDate(securitiesCode.getModifiedDate()).approvedBy(securitiesCode.getApprovedBy())
				.approvedDate(securitiesCode.getApprovedDate()).createdDate(securitiesCode.getCreatedDate())
				.effectiveDate(securitiesCode.getEffectiveDate()).approvalRemark(securitiesCode.getApprovalRemark())
				.workflowStatusId(securitiesCode.getWorkflowStatusId()).build();
		securitiesCouponDAO.save(jpaSecuritiesCoupon);

	}

	private void saveCouponSheduleData(JpaSssSecuritiesCode securitiesCode) throws ParseException, SQLException {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Date couponDate = simpleDateFormat.parse(securitiesCode.getFirstCouponDate().toString());
		logger.info("coupon date : {}", couponDate);
		Date redemptionDate = simpleDateFormat.parse(securitiesCode.getRedemptionDate().toString());
		logger.info("redemptionDate : {}", redemptionDate);
		c.setTime(couponDate);
		c.add(Calendar.MONTH, 6);
		String cDate1 = simpleDateFormat.format(couponDate);
		String dateAfterSixMonths = simpleDateFormat.format(c.getTime());
		Date dateAfterSixMonths2 = simpleDateFormat.parse(dateAfterSixMonths);
		int q = 0;
//		if (securitiesCode.getCouponPaymentFrequency().equals("3")) {

			

			while (dateAfterSixMonths2.compareTo(redemptionDate) <= 0) {
				q++;
				String s2 = simpleDateFormat.format(dateAfterSixMonths2);
				Double couponRate = securitiesCode.getCouponRate();
				if (securitiesCode.getSecuritiesTypeId().equals("SSB"))// SSB
				{
					for (String s : map.keySet()) {
						logger.info(securitiesCode.getSecuritiesCode() + "  " + map.get(s).getSecurityCode());
						if (securitiesCode.getSecuritiesCode().equals(map.get(s).getSecurityCode())) {
							JpaCouponShedules couponSchedule = JpaCouponShedules.builder()
//									.id(UUID.randomUUID().toString())
									.securitiesCode(map.get(s).getSecurityCode())
									.couponDate(map.get(s).getCouponPaymentDate())
//		                            .security(jpaSssSecuritiesCode)
									.couponRate(map.get(s).getCouponRate()).build();
							couponSheduleDAO.save(couponSchedule);
							logger.info("ssb  addeded successfully.....");
						}
					}
				}
				else if (securitiesCode.getSecuritiesTypeId().equals("MASRIMFRN")) {
					if (q == 1) {
						JpaCouponShedules couponSchedule = JpaCouponShedules.builder()
//								.id(UUID.randomUUID().toString())
								.securitiesCode(securitiesCode.getSecuritiesCode()).couponDate(cDate1).couponRate(null)
								.build();
						couponSheduleDAO.save(couponSchedule);
					}
					JpaCouponShedules couponSchedule = JpaCouponShedules.builder()
//							.id(UUID.randomUUID().toString())
							.securitiesCode(securitiesCode.getSecuritiesCode()).couponDate(s2).couponRate(null).build();
					couponSheduleDAO.save(couponSchedule);
				} else if (securitiesCode.getSecuritiesTypeId().equals("SGSISBond")) {
					if (q == 1) {
						JpaCouponShedules couponSchedule = JpaCouponShedules.builder()
//								.id(UUID.randomUUID().toString())
								.securitiesCode(securitiesCode.getSecuritiesCode()).couponDate(cDate1)
								.couponRate(couponRate.toString()).build();
						couponSheduleDAO.save(couponSchedule);
						couponRate = couponRate + 0.1;
					}
					JpaCouponShedules couponSchedule = JpaCouponShedules.builder()
//							.id(UUID.randomUUID().toString())
							.securitiesCode(securitiesCode.getSecuritiesCode()).couponDate(s2)
							.couponRate(couponRate.toString()).build();
					couponSheduleDAO.save(couponSchedule);
					couponRate = couponRate + 0.1;
				}
				c.setTime(dateAfterSixMonths2);
				c.add(Calendar.MONTH, 6);
				String ds = simpleDateFormat.format(c.getTime());
				dateAfterSixMonths2 = simpleDateFormat.parse(ds);

			}
//		}
	}

	public List<JpaSssSecuritiesCode> convertTJpaSssSecuritiesCode(List<JpaSssSecuritiesCodeTemp> securitiesCodeList) {
		List<JpaSssSecuritiesCode> list = new ArrayList<>();
		for (JpaSssSecuritiesCodeTemp securitiesCode : securitiesCodeList) {

			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			JpaSssSecuritiesCode securitiesCodeTemp = mapper.map(securitiesCode, JpaSssSecuritiesCode.class);
			list.add(securitiesCodeTemp);
			// log.info(securityPosTemp.getAccountId());
		}
		return list;
	}

	public void migrateSss_member(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
			List<String> fileRecords) {

	}
}