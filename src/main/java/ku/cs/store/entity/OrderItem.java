package ku.cs.store.entity;

import ku.cs.store.entity.Product;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class OrderItem {


    @EmbeddedId
    private OrderItemKey id;


    private int quantity;


    private int stock;


    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private PurchaseOrder purchaseOrder;


    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;


    public double getSubtotal() {
        return product.getPrice() * quantity;
    }


    public void setOrder(PurchaseOrder purchaseOrder) {
    }
}

