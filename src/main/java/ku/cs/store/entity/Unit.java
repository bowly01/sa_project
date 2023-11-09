package ku.cs.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;


@Entity
@Data
public class Unit {
    @Id
    @GeneratedValue
    private Long id;  // Primary key field
    @NotNull
    private String name;
    @NotNull
    private int quantity;

}
