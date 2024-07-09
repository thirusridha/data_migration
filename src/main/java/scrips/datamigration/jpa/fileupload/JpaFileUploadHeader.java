package scrips.datamigration.jpa.fileupload;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Siva Kuruva
 */

@Entity
@Table(name="FILE_UPLOAD_HEADER")
public class JpaFileUploadHeader implements Serializable{
    @Id
    private Integer fileUploadId;
    private String fileUploadCode;
    private String ipAddress;
    private String filePath;
    private String fileName;
    private String draftTableName;
    private String liveTableName;
    private String fileDelimiter;
    private Integer fileHeader;
    private Integer isActive;
    private String procedureName;

    

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
     * @return String return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return String return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @return String return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return String return the draftTableName
     */
    public String getDraftTableName() {
        return draftTableName;
    }

    /**
     * @param draftTableName the draftTableName to set
     */
    public void setDraftTableName(String draftTableName) {
        this.draftTableName = draftTableName;
    }

    /**
     * @return String return the liveTableName
     */
    public String getLiveTableName() {
        return liveTableName;
    }

    /**
     * @param liveTableName the liveTableName to set
     */
    public void setLiveTableName(String liveTableName) {
        this.liveTableName = liveTableName;
    }

    /**
     * @return String return the fileDelimiter
     */
    public String getFileDelimiter() {
        return fileDelimiter;
    }

    /**
     * @param fileDelimiter the fileDelimiter to set
     */
    public void setFileDelimiter(String fileDelimiter) {
        this.fileDelimiter = fileDelimiter;
    }

    /**
     * @return Integer return the fileHeader
     */
    public Integer getFileHeader() {
        return fileHeader;
    }

    /**
     * @param fileHeader the fileHeader to set
     */
    public void setFileHeader(Integer fileHeader) {
        this.fileHeader = fileHeader;
    }

    /**
     * @return Integer return the isActive
     */
    public Integer getIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    /**
     * @return String return the proceduteName
     */
    public String getProcedureName() {
        return procedureName;
    }

    /**
     * @param proceduteName the proceduteName to set
     */
    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

}
