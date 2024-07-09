package scrips.datamigration.jpa.sss.account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SSSAccountDAO extends JpaRepository<JpaSSSAccount, String>{

	List<JpaSSSAccount> findByAccountId(String receiverAccountNo);
}
