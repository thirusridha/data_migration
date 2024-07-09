package scrips.datamigration.jpa.fileupload;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA Repository for FILE_UPLOAD_HEADER DB table
 * @author Siva Kuruva
 */

@Transactional
public interface FileUploadRepository extends JpaRepository<JpaFileUploadHeader,Long> {
    @Query(value="SELECT * FROM FILE_UPLOAD_HEADER where FILE_UPLOAD_CODE = :fileUploadCode", nativeQuery = true)
    JpaFileUploadHeader findByFileUploadCode(@Param("fileUploadCode") String fileUploadCode);
}
