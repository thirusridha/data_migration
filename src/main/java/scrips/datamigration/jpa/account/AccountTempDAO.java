package scrips.datamigration.jpa.account;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
@Transactional
public interface AccountTempDAO extends JpaRepository<JpaAccountTemp, String>{

	JpaAccountTemp findByAccountNumber(String accountNumber);

}
