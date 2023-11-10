package ku.cs.store.repository;

import ku.cs.store.common.OperationType;
import ku.cs.store.common.StatusProduct;
import ku.cs.store.entity.Product;
import ku.cs.store.entity.ProductLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductLogRepository extends JpaRepository<ProductLog, Long> {

    List<ProductLog> findByOperationType(OperationType operationType);
}
