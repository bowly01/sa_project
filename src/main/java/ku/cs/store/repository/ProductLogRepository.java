package ku.cs.store.repository;

import ku.cs.store.entity.ProductLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLogRepository extends JpaRepository<ProductLog, Long> {
}
