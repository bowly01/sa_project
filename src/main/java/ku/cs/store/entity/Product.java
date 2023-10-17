package ku.cs.store.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private double price;
    private int amount;
    @ManyToOne
    private Category category;
}
