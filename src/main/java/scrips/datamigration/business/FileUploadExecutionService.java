package scrips.datamigration.business;

import java.util.Date;

import scrips.datamigration.jpa.fileupload.JpaFileUploadExecution;

import org.springframework.stereotype.Service;

@Service
public class FileUploadExecutionService {
    public JpaFileUploadExecution createFileUploadExecution(Integer fileId, String fileCode, String execId, String status){
        JpaFileUploadExecution fileUploadExec = new JpaFileUploadExecution();
        fileUploadExec.setFileUploadId(fileId);
        fileUploadExec.setFileUploadCode(fileCode);
        fileUploadExec.setExecID(execId);
        fileUploadExec.setStartTime(new Date());
        fileUploadExec.setStatus(status);
        return fileUploadExec;
    } 
}
