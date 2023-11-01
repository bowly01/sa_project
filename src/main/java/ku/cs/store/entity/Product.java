package ku.cs.store.entity;

import jakarta.persistence.*;
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
    private String name;
    private String detail;

    private double price;

    private int stock;

    private int requireProduct;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDateTime;

    @Temporal(TemporalType.DATE)
    private Date dateStock;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDateTime;
    @ManyToOne
    private Unit unit;
    @ManyToOne
    private Category category;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String  imageFile;

 }