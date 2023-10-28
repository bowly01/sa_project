package ku.cs.store.entity;

import jakarta.persistence.*;
import lombok.Data;



@Entity
@Data
public class Unit {
    @Id
    @GeneratedValue
    private Long id;  // Primary key field
    private String name;
    private int quantity;

}
