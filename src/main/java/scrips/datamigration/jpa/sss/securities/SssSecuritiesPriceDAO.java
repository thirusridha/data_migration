package scrips.datamigration.jpa.sss.securities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SssSecuritiesPriceDAO extends JpaRepository<JpaSssSecuritiesPrice, String>{

	JpaSssSecuritiesPrice findBySecuritiesCode(String securitiesCode);

}

