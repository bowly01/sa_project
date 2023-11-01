package ku.cs.store.model;


import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


import java.util.UUID;

@Data
public class ProductRequest {
    @Column(name = "name")
    private String name;
    @Column(name = "categoryId")
    private UUID categoryId;
    @Column(name = "detail")
    private String detail;
    @Column(name = "price")
    private double price;
    @Column(name = "unit")
    private Long unitId;
    @Column(name = "stock")
    private int stock;
    @Column(name = "requireProduct")
    private int requireProduct;
    @Column(name = "imageFile")
    private MultipartFile imageFile;
}