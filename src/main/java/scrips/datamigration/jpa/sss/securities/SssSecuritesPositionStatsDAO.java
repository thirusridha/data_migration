package scrips.datamigration.jpa.sss.securities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SssSecuritesPositionStatsDAO extends JpaRepository<JpaSssSecuritiesPositionStats,String>{

	JpaSssSecuritiesPositionStats findBySecuritiesCode(String securitiesCode);

//	JpaSssSecuritiesPositionStats findByAccountId(String accountId);

	JpaSssSecuritiesPositionStats findBySecuritiesCodeAndAccountId(String securitiesCode, String string);

}
