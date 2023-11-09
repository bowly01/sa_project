package ku.cs.store.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import ku.cs.store.common.StatusOrder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@Entity
public class PurchaseOrder {
    @Id
    @GeneratedValue
    private UUID id;


    private LocalDateTime timestamp;
    private StatusOrder status;


    @OneToMany(mappedBy = "purchaseOrder")
    private List<OrderItem> items = new ArrayList<>();
    public double getTotal() {
        double total = 0;
        for (OrderItem item : items)
            total += item.getSubtotal();
        return total;
    }

    public StatusOrder getStatus() {
        return status;
    }

    public void setStatus(StatusOrder status) {
        this.status = status;
    }

}
