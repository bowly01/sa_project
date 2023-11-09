package ku.cs.store.model;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import ku.cs.store.common.StatusProduct;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


import java.util.UUID;

@Data
public class ProductRequest {
    @NotNull(message = "กรุณากรอกชื่อสินค้า")
    @Column(name = "name")
    private String name;
    @Column(name = "categoryId")
    private UUID categoryId;
    @NotNull(message = "กรุณากรอกรายละเอียดสินค้า")
    @Column(name = "detail")
    private String detail;
    @Min(value = 1,message ="กรุณากรอกเลขมากกว่าเท่ากับ 1")
    @Max(value = 100000,message = "กรุณากรอกเลขมากกว่าเท่ากับ 100000")
    @Column(name = "price")
    private double price;

    @Column(name = "unit")
    private Long unitId;
    @Min(value = 1,message ="กรุณากรอกเลขมากกว่าเท่ากับ 1")
    @Max(value = 500,message = "กรุณากรอกเลขมากกว่าเท่ากับ 500")
    @Column(name = "stock")
    private int stock;
    @Min(value = 1,message ="กรุณากรอกเลขมากกว่าเท่ากับ 1")
    @Max(value = 500,message = "กรุณากรอกเลขมากกว่าเท่ากับ 500")
    @Column(name = "requireProduct")
    private int requireProduct;
    @Column(name = "imageFile")
    private MultipartFile imageFile;
    private StatusProduct statusProduct;

}