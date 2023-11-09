package ku.cs.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import ku.cs.store.common.StatusProduct;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue
    private UUID id;
    @NotNull(message = "กรุณากรอกชื่อสินค้า")
    private String name;
    @NotNull(message = "กรุณากรอกรายละเอียดสินค้า")

    private String detail;

    @Min(value = 1,message ="กรุณากรอกเลขมากกว่าเท่ากับ 1")
    private double price;
    @Min(value = 1,message = "กรุณากรอกเลขมากกว่าเท่ากับ 1")
    private int stock;
    @Min(value = 1,message = "กรุณากรอกเลขมากกว่าเท่ากับ 1")
    private int requireProduct;

    @ManyToOne
    private Unit unit;
    @ManyToOne
    private Category category;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String  imageFile;
    private StatusProduct statusProduct;

    @ManyToOne
    private PurchaseOrder purchaseOrder;

 }