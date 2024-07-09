package scrips.datamigration.jpa.cbm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface JpaCbmLiabilitiesBaseSourceDAO extends JpaRepository<JpaCbmLiabilitiesBaseSource, String>{

}
