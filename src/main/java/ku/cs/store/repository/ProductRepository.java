package ku.cs.store.repository;

import ku.cs.store.common.StatusProduct;
import ku.cs.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByName(String name);
    Page<Product> findAll(Pageable pageable);
    List<Product> findByCategoryId(UUID categoryId);
    List<Product> findByUnitId(Long unitId);
    List<Product> findByStatusProduct(StatusProduct statusProduct);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.unit.id = :unitId")
    boolean existsByUnitId(Long unitId);
    boolean existsByCategoryId(UUID id);
}
