package scrips.datamigration.business;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FkChecksEnableDisable {

	private final Logger logger = LogManager.getLogger(FkChecksEnableDisable.class);
	@Autowired
	private EntityManager entityManager;

	@Transactional
	public void enableFkChecks() {
		try {
			logger.info("enable FkChecks called");
			entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
			logger.info("enable FkChecks done");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while enabling foreign key checks");
		}
	}

	//@PostConstruct
	@Transactional
	public void disableFkChecks() {
		try {
			logger.info("disable FkChecks called");
			entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
			logger.info("disable FkChecks done");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while disabling foreign key checks");
		}
	}

	@SuppressWarnings("unchecked")
	public Integer fkStatus() {
		try {

			List<Object[]> result =  entityManager
					.createNativeQuery("SELECT @@GLOBAL.foreign_key_checks, @@SESSION.foreign_key_checks")
					.getResultList();
			logger.info("foreign_key_checks status - {}",Integer.parseInt(result.get(0)[1].toString()));
			return Integer.parseInt(result.get(0)[1].toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
