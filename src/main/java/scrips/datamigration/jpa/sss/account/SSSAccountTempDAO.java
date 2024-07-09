package scrips.datamigration.jpa.sss.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SSSAccountTempDAO extends JpaRepository<JpaSSSAccountTemp,	String> {
	JpaSSSAccountTemp findByAccountId(String aacountId);
}
