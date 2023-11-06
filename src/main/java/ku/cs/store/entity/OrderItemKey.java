package ku.cs.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class OrderItemKey implements Serializable {


    @Column(name = "order_id")
    private UUID orderId;


    @Column(name = "product_id")
    private UUID productId;

}
