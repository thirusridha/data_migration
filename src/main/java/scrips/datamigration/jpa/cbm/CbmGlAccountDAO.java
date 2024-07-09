package scrips.datamigration.jpa.cbm;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CbmGlAccountDAO extends JpaRepository<JpaCbmGlAccount,String>{

	JpaCbmGlAccount findByGlAccount(String glAccount);


}
