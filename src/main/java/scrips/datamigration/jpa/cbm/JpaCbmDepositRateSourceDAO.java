package scrips.datamigration.jpa.cbm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface JpaCbmDepositRateSourceDAO extends JpaRepository<JpaCbmDepositRateSource, String> {

}
