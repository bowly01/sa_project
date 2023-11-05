package ku.cs.store.repository;

import ku.cs.store.entity.OrderItem;
import ku.cs.store.entity.OrderItemKey;
import ku.cs.store.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderItemRepository
        extends JpaRepository<OrderItem, OrderItemKey> {
    List<OrderItem> findByPurchaseOrder(PurchaseOrder purchaseOrder);

}