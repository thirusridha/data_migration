package scrips.datamigration.jpa.member;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import scrips.datamigration.jpa.account.JpaAccount;

public interface MemberDAO extends JpaRepository<JpaMember, Long>{

	JpaMember findBymemberCode(String memberCode);

	List<JpaMember> findByMemberCode(String memberId);

}

