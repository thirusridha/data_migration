package scrips.datamigration.jpa.account;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
@Transactional
public interface AccountDAO extends JpaRepository<JpaAccount, String>{

	JpaAccount findByAccountNumber(String accountNumber);

}
