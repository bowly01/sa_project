package ku.cs.store.model;

import lombok.Data;

import java.util.UUID;
@Data
public class ProductRequest {
    private String name;
    private UUID categoryId;
    private double price;
    private int amount;
}
