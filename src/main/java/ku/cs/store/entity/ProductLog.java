package ku.cs.store.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class ProductLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID productId;
    private String productName;
    private String operationType; // Add, Create, Delete

    private String user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    // Constructors, getters, and setters
}
