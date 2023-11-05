package ku.cs.store.repository;

import ku.cs.store.entity.Category;
import ku.cs.store.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    Optional<Unit> findByName(String name);
    @Modifying
    @Query("DELETE FROM Unit u WHERE u.id = :unitId")
    void deleteById(Long unitId);

}
