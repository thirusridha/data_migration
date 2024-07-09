package scrips.datamigration.jpa.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserDAO extends JpaRepository<JpaUser, String> {
List<JpaUser> findByName(String string);

}
