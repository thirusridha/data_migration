package scrips.datamigration.jpa.sss.securities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SssSecuritiesCodeDAO extends JpaRepository<JpaSssSecuritiesCode,String>{

	JpaSssSecuritiesCode getBySecuritiesCode(String securitiesCode);
	JpaSssSecuritiesCode findBySecuritiesCode(String securitiesCode);

}
