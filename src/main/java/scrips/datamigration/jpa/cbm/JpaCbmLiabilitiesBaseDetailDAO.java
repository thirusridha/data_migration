package scrips.datamigration.jpa.cbm;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCbmLiabilitiesBaseDetailDAO extends JpaRepository<JpaCbmLiabilitiesBaseDetail, String>{

	JpaCbmLiabilitiesBaseDetail findByMemberId(String memberId);

}
