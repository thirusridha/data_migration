package scrips.datamigration.jpa.fileupload;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author Siva Kuruva
 */

@Entity
@Table(name="FILE_UPLOAD_ERROR")
@Transactional
public class JpaFileUploadError implements Serializable{
    @Column(name ="FILE_UPLOAD_ID")
    private Integer fileUploadId;
    @Column(name ="FILE_UPLOAD_CODE")
    private String fileUploadCode;
    @Id
    @Column(name ="EXEC_ID")
    private String execId;
    @Column(name ="ERROR_CODE")
    private Integer errorCode;
    

    /**
     * @return Integer return the fileUploadId
     */
    public Integer getFileUploadId() {
        return fileUploadId;
    }

    /**
     * @param fileUploadId the fileUploadId to set
     */
    public void setFileUploadId(Integer fileUploadId) {
        this.fileUploadId = fileUploadId;
    }

    /**
     * @return String return the fileUploadCode
     */
    public String getFileUploadCode() {
        return fileUploadCode;
    }

    /**
     * @param fileUploadCode the fileUploadCode to set
     */
    public void setFileUploadCode(String fileUploadCode) {
        this.fileUploadCode = fileUploadCode;
    }

    /**
     * @return Integer return the errorCode
     */
    public Integer getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * @return String return the execId
     */
    public String getExecId() {
        return execId;
    }

    /**
     * @param execId the execId to set
     */
    public void setExecId(String execId) {
        this.execId = execId;
    }

}
