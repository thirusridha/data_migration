package scrips.datamigration.business;

import scrips.datamigration.jpa.fileupload.JpaFileUploadError;

import org.springframework.stereotype.Service;

@Service
public class FileUploadErrorService {
    public JpaFileUploadError createFileUploadError(Integer fileId, String fileCode, String execId, Integer errorCode){
        JpaFileUploadError fileUploadErr = new JpaFileUploadError();
        fileUploadErr.setFileUploadId(fileId);
        fileUploadErr.setFileUploadCode(fileCode);
        fileUploadErr.setExecId(execId);
        fileUploadErr.setErrorCode(errorCode);
        return fileUploadErr;
    }
}
