package scrips.datamigration.jpa.sss.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SssTransactionSourceDAO extends JpaRepository<JpaSssTransactionSource, String>{

}
