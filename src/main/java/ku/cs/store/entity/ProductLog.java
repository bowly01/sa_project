package ku.cs.store.entity;

import jakarta.persistence.*;
import ku.cs.store.common.OperationType;
import ku.cs.store.common.StatusProduct;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class ProductLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private OperationType operationType; // increase, create, unavailable
    private String detail;
    private String user;
    private StatusProduct status; // Status of the product at the time of the action

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    // Constructors, getters, and setters
    // ที่ใช้เพื่อดึง product_name
    public String getProductName() {
        if (product != null) {
            return product.getName();
        }
        return null;
    }
}
