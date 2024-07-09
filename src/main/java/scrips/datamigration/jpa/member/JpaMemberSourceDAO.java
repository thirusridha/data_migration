package scrips.datamigration.jpa.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface JpaMemberSourceDAO extends JpaRepository<JpaMemberSource, Long>{

}
