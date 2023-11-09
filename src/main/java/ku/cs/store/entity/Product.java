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
    @NotNull
    @Min(value = 1,message ="กรุณากรอกเลขมากกว่าเท่ากับ 1")
    private double price;
    @NotNull
    @Min(value = 1,message = "กรุณากรอกเลขมากกว่าเท่ากับ 1")
    private int stock;
    @NotNull
    @Min(value = 1,message = "กรุณากรอกเลขมากกว่าเท่ากับ 1")
    private int requireProduct;

    @ManyToOne
    @NotNull
    private Unit unit;
    @ManyToOne
    @NotNull
    private Category category;
    @Lob
    @NotNull
    @Column(columnDefinition = "MEDIUMBLOB")
    private String  imageFile;
    @NotNull
    private StatusProduct statusProduct;

    @ManyToOne
    private PurchaseOrder purchaseOrder;

 }