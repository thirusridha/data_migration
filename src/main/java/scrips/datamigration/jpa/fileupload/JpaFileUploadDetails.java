package scrips.datamigration.jpa.fileupload;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Siva Kuruva
 */

@Entity
@Table(name="FILE_UPLOAD_DETAILS")
public class JpaFileUploadDetails implements Serializable{
    @Column(name ="FILE_UPLOAD_ID")
    private Integer fileUploadId;
    @Column(name ="FILE_UPLOAD_CODE")
    private String fileUploadCode;
    @Id
    @Column(name ="SEQUENCE_NO")
    private Integer sequenceNo;
    @Column(name ="FILE_HEADER_NAME")
    private String fileHeaderName;
    @Column(name ="TABLE_FIELD_NAME")
    private String tableFieldName;
    @Column(name ="DATA_TYPE")
    private String dataType;
    @Column(name ="DATA_SIZE")
    private Integer dataSize;
    @Column(name ="IS_NOT_NULL")
    private Integer isNotNull;
    private String dataFormat;
    private String pkTable;
    private String pkField;

    

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
     * @return String return the sequenceNo
     */
    public Integer getSequenceNo() {
        return sequenceNo;
    }

    /**
     * @param sequenceNo the sequenceNo to set
     */
    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    /**
     * @return String return the fileHeaderName
     */
    public String getFileHeaderName() {
        return fileHeaderName;
    }

    /**
     * @param fileHeaderName the fileHeaderName to set
     */
    public void setFileHeaderName(String fileHeaderName) {
        this.fileHeaderName = fileHeaderName;
    }

    /**
     * @return String return the tableFieldName
     */
    public String getTableFieldName() {
        return tableFieldName;
    }

    /**
     * @param tableFieldName the tableFieldName to set
     */
    public void setTableFieldName(String tableFieldName) {
        this.tableFieldName = tableFieldName;
    }

    /**
     * @return String return the dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * @return Integer return the dataSize
     */
    public Integer getDataSize() {
        return dataSize;
    }

    /**
     * @param dataSize the dataSize to set
     */
    public void setDataSize(Integer dataSize) {
        this.dataSize = dataSize;
    }

    /**
     * @return Integer return the isNotNull
     */
    public Integer getIsNotNull() {
        return isNotNull;
    }

    /**
     * @param isNotNull the isNotNull to set
     */
    public void setIsNotNull(Integer isNotNull) {
        this.isNotNull = isNotNull;
    }

    /**
     * @return String return the dataFormat
     */
    public String getDataFormat() {
        return dataFormat;
    }

    /**
     * @param dataFormat the dataFormat to set
     */
    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    /**
     * @return String return the pkTable
     */
    public String getPkTable() {
        return pkTable;
    }

    /**
     * @param pkTable the pkTable to set
     */
    public void setPkTable(String pkTable) {
        this.pkTable = pkTable;
    }

    /**
     * @return String return the pkField
     */
    public String getPkField() {
        return pkField;
    }

    /**
     * @param pkField the pkField to set
     */
    public void setPkField(String pkField) {
        this.pkField = pkField;
    }

}
