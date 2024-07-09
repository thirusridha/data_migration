package scrips.datamigration.jpa.fileupload;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Siva Kuruva
 */

@Entity
@Table(name="ERROR_LOG_MASTER")
public class JpaErrorLogMaster implements Serializable {
    @Id
    private Long errorCode;
    private String errorDescription;
    private String remarks;
    

    /**
     * @return Long return the errorCode
     */
    public Long getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return String return the errorDescrption
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    /**
     * @param errorDescrption the errorDescrption to set
     */
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    /**
     * @return String return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
