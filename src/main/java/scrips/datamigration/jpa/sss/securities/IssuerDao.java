package scrips.datamigration.jpa.sss.securities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuerDao extends JpaRepository<JpaIssuer, String>{

	List<JpaIssuer> findByIssuerCode(String issuerCode);
}
