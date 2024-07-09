package scrips.datamigration.jpa.sss.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SssFloatingRatesSourceDAO extends JpaRepository <JpaSssFloatingRatesSource, String> {

}
