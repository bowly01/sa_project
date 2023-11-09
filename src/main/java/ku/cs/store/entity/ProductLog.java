package ku.cs.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private OperationType operationType; // increase, create, unavailable
    @NotNull
    private String detail;
    @NotNull
    private String user;
    @NotNull
    private StatusProduct status; // Status of the product at the time of the action
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

//    @OneToOne(cascade = CascadeType.ALL)
//    @NotNull
//    @JoinColumn(name = "product_id")
//    private Product product;
@ManyToOne
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
