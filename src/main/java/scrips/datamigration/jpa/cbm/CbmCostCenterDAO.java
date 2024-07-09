package scrips.datamigration.jpa.cbm;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CbmCostCenterDAO extends JpaRepository<JpaCbmCostCentre,String>{

	JpaCbmCostCentre findByCostCentre(String costCentre);

}
