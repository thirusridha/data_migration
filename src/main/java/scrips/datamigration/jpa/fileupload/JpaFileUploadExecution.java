package scrips.datamigration.jpa.fileupload;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author Siva Kuruva
 */

@Entity
@Table(name="FILE_UPLOAD_EXECUTION")
@Transactional
public class JpaFileUploadExecution implements Serializable{
    @Column(name ="FILE_UPLOAD_ID")
    private Integer fileUploadId;
    @Column(name ="FILE_UPLOAD_CODE")
    private String fileUploadCode;
    @Id
    @Column(name ="EXEC_ID")
    private String execID;
    private Date startTime;
    private Date endTime;
    @Column(name="STATUS")
    private String status;
    

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
     * @return Date return the startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return Date return the endTime
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


    /**
     * @return String return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }


    /**
     * @return String return the execID
     */
    public String getExecID() {
        return execID;
    }

    /**
     * @param execID the execID to set
     */
    public void setExecID(String execID) {
        this.execID = execID;
    }

}
