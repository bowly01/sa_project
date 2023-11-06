package ku.cs.store.repository;

import ku.cs.store.common.StatusOrder;
import ku.cs.store.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface PurchaseOrderRepository
        extends JpaRepository<PurchaseOrder, UUID> {
    List<PurchaseOrder> findByStatus(StatusOrder status);
    @Query("SELECT o FROM PurchaseOrder o WHERE o.status = :status1 OR o.status = :status2")
    List<PurchaseOrder> findByStatusInCustom(@Param("status1") StatusOrder status1, @Param("status2") StatusOrder status2);
}