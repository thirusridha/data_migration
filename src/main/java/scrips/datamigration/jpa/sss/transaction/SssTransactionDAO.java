package scrips.datamigration.jpa.sss.transaction;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SssTransactionDAO extends JpaRepository<JpaSssTransaction, String>{

	JpaSssTransaction findBySssReference(String sssReference);

}
