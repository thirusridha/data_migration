package scrips.datamigration.jpa.sss.securities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import scrips.datamigration.jpa.account.JpaAccount;
import scrips.datamigration.jpa.accountposition.JpaAccountPosition;
@Repository
public interface SssAllotmentDAO extends JpaRepository<JpaSssAllotment,String>{

	JpaSssAllotment findBySecuritiesCode(String securitiesCode);
	
}