package scrips.datamigration.jpa.cbm;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCbmLiabilitiesBaseDAO extends JpaRepository<JpaCbmLiabilitiesBase, String> {

	JpaCbmLiabilitiesBase findByMemberId(String memberId);

}
