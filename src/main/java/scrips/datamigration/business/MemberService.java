package scrips.datamigration.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import scrips.datamigration.exception.DatabaseException;
import scrips.datamigration.exception.FailedValidationException;
import scrips.datamigration.jpa.fileupload.JpaFileUploadDetails;
import scrips.datamigration.jpa.fileupload.JpaFileUploadError;
import scrips.datamigration.jpa.fileupload.JpaFileUploadExecution;
import scrips.datamigration.jpa.fileupload.JpaFileUploadHeader;
import scrips.datamigration.jpa.member.JpaMember;
import scrips.datamigration.jpa.member.JpaMemberTemp;
import scrips.datamigration.jpa.member.MemberDAO;
import scrips.datamigration.jpa.member.MemberTempDAO;
import scrips.datamigration.jpa.sss.transaction.JpaSssTransaction;
import scrips.datamigration.jpa.sss.transaction.JpaSssTransactionTemp;

@Service
public class MemberService {

	@Autowired
	ReadFileAndConvertService fileConvertService;

	@Autowired
	ValidationService validationService;

	@Autowired
	FileUploadExecutionService fileuploadExecService;

	@Autowired
	FileUploadErrorService fileErrorService;

	@Autowired
	MemberDAO memberRepo;

	@Autowired
	MemberTempDAO memberTempRepo;

	@Autowired
	FileUploadService fileService;

	public String migrateMember(JpaFileUploadHeader fileHeaderObj, List<JpaFileUploadDetails> draftDBDetails,
			List<String> fileRecords)
			throws DatabaseException, FailedValidationException, ParseException, SQLException {
		List<JpaMember> memberObjlist = fileConvertService.convertToMemberList(fileRecords, draftDBDetails);
		List<JpaMember> validMemberList = new ArrayList<JpaMember>();
		List<JpaMemberTemp> memberTempList = new ArrayList<JpaMemberTemp>();
		List<JpaFileUploadExecution> fileExecList = new ArrayList<JpaFileUploadExecution>();
		List<JpaFileUploadError> fileErrList = new ArrayList<JpaFileUploadError>();
		List<JpaMemberTemp> jpaMemberTemp = converttoJpaMemberTemp(memberObjlist);

		System.out.println("memberObjlist - " + memberObjlist.size());
		if (!memberObjlist.isEmpty()) {

			memberObjlist.stream().forEach(member -> {
				try {
					String remarks = validationService.validationJpaMember(member);
					if (remarks.isEmpty()) {

						validMemberList.add(member);
						JpaFileUploadExecution fileUplodExec = fileuploadExecService.createFileUploadExecution(
								fileHeaderObj.getFileUploadId(), fileHeaderObj.getFileUploadCode(),
								UUID.randomUUID().toString(), "Migratted Sucessfully");
						fileExecList.add(fileUplodExec);

					} else {

						Optional<JpaMemberTemp> tempObj = jpaMemberTemp.stream()
								.filter(x -> x.getId().equals(member.getId())).findAny();
						if (tempObj != null && tempObj.get() != null) {
							remarks = remarks.substring(1);
							JpaMemberTemp temp = tempObj.get();
							temp.setRemarks(remarks);
							memberTempRepo.save(temp);
						}
//                    JpaFileUploadError errObj=fileErrorService.createFileUploadError(fileHeaderObj.getFileUploadId(),fileHeaderObj.getFileUploadCode(),UUID.randomUUID().toString(),101);
//                    fileErrList.add(errObj);
//                    System.out.println("Not a valid obj");
					}
				} catch (FailedValidationException Fve) {
					JpaFileUploadError errObj = fileErrorService.createFileUploadError(fileHeaderObj.getFileUploadId(),
							fileHeaderObj.getFileUploadCode(), UUID.randomUUID().toString(), 102);
					fileErrList.add(errObj);
					System.out.println(Fve.getMessage());
				} catch (Exception e) {
					JpaFileUploadError errObj = fileErrorService.createFileUploadError(fileHeaderObj.getFileUploadId(),
							fileHeaderObj.getFileUploadCode(), UUID.randomUUID().toString(), 102);
					fileErrList.add(errObj);
					System.out.println(e.getStackTrace());
				}
			});
		}
		System.out.println("validMemberList -" + validMemberList.size());
		System.out.println("memberTempList -" + memberTempList.size());
//        memberRepo.saveAll(validMemberList);
//        memberTempRepo.saveAll(memberTempList);
		try {
			if (!jpaMemberTemp.isEmpty())
				memberTempRepo.saveAll(jpaMemberTemp);
//			if (!fileExecList.isEmpty())
//				fileService.saveFileUploadExecutions(fileExecList);
			// if (!fileErrList.isEmpty())
			// fileService.saveFileUploadErrors(fileErrList);
			if (!validMemberList.isEmpty())
				memberRepo.saveAll(validMemberList);

		} catch (Exception e) {

			throw new DatabaseException(e.getMessage());
		}
		return fileErrList.size() > 0 ? "Partialy Migrated" : "Migrated Successfully";
	}

	public List<JpaMemberTemp> converttoJpaMemberTemp(List<JpaMember> memberList) {
		List<JpaMemberTemp> list = new ArrayList<>();
		for (JpaMember jpaMember : memberList) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			JpaMemberTemp jpaMemberTemp = mapper.map(jpaMember, JpaMemberTemp.class);
			list.add(jpaMemberTemp);

		}
		return list;
	}
}
