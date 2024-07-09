package scrips.datamigration.jpa.sss.coupon;

import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface SssFloatingRatesTempDAO extends JpaRepository <JpaSssFloatingRatesTemp, String> {
	Optional<JpaSssFloatingRatesTemp> findById(String id);
}
