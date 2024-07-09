package scrips.datamigration.jpa.fileupload;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * JPA Repository for MEMBER_TEMP DB table
 * @author Siva Kuruva
 */

@Transactional
public interface FileUploadErrorRepository extends JpaRepository<JpaFileUploadError,Long>{
}
