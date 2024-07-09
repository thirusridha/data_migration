package scrips.datamigration.jpa.sss.securities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxSchemeDao extends JpaRepository<JpaTaxScheme, String> {
	List<JpaTaxScheme> findByTaxSchemeId(String taxSchemeId);

}