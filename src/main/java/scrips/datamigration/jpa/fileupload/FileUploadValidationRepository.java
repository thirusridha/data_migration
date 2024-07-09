package scrips.datamigration.jpa.fileupload;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * JPA Repository for FILE_UPLOAD_HEADER DB table
 * @author Siva Kuruva
 */

@Transactional
public interface FileUploadValidationRepository extends JpaRepository<JpaFileUploadDetails,Long>{
    @Query(value="select * from FILE_UPLOAD_DETAILS where FILE_UPLOAD_CODE = :fileUploadCode ORDER BY (SEQUENCE_NO)", nativeQuery = true)
    List<JpaFileUploadDetails> getFileUploadDetails(@Param("fileUploadCode") String fileUploadCode);
}
