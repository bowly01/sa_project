package ku.cs.store.repository;

import ku.cs.store.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Member findByUsername(String username);
}
