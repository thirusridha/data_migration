package scrips.datamigration.jpa.cbm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface JpaCbmFloatingRateDAO extends JpaRepository<JpaCbmFloatingRate, String>{

	JpaCbmFloatingRate findByReferenceRateAndValueDate(String referenceRate,Integer valueDate);

}
