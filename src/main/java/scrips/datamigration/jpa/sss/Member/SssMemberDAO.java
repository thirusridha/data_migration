package scrips.datamigration.jpa.sss.Member;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import scrips.datamigration.jpa.member.JpaMember;
@Repository
public interface SssMemberDAO extends JpaRepository<JpaSssMember, String> {
	List<JpaSssMember> findBymemberCode(String memberCode);
	List<JpaSssMember> findByMemberType(String memberType);
}
