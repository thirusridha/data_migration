package scrips.datamigration.jpa.accountposition;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Transactional
@Repository
public interface AccountPositionDAOTemp extends JpaRepository<JpaAccountPositionTemp, Long>{

	JpaAccountPositionTemp findByAccountId(int accountId);


}
