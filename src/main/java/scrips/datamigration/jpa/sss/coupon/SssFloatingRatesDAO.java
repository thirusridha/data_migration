package scrips.datamigration.jpa.sss.coupon;

import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import scrips.datamigration.jpa.cbm.JpaCbmFloatingRate;

@Repository
@Transactional
public interface SssFloatingRatesDAO extends JpaRepository <JpaSssFloatingRates, String> {
	JpaSssFloatingRates findByReferenceRate(String referenceRate);

	JpaSssFloatingRates findByValueDate(Integer valueDate);

	JpaSssFloatingRates findByReferenceRateAndValueDate(String referenceRate, Integer valueDate);
}
