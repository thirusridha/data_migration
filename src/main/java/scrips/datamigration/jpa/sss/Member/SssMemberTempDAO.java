package scrips.datamigration.jpa.sss.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SssMemberTempDAO  extends JpaRepository<JpaSssMemberTemp, String>{

}
