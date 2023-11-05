package ku.cs.store.service;

import ku.cs.store.common.StatusOrder;
import ku.cs.store.entity.OrderItem;
import ku.cs.store.entity.OrderItemKey;
import ku.cs.store.entity.Product;
import ku.cs.store.entity.PurchaseOrder;
import ku.cs.store.model.AddCartRequest;
import ku.cs.store.repository.OrderItemRepository;
import ku.cs.store.repository.ProductRepository;
import ku.cs.store.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class OrderService {


    @Autowired
    private PurchaseOrderRepository orderRepository;


    @Autowired
    private OrderItemRepository itemRepository;


    @Autowired
    private ProductRepository productRepository;


    private UUID currentOrderId;




    public void submitOrder() {
        PurchaseOrder currentOrder = orderRepository.findById(currentOrderId).orElse(null);

        if (currentOrder == null) {
            return;
        }

        if (currentOrder.getStatus() != StatusOrder.ORDER) {

            return;
        }


        List<OrderItem> orderItems = itemRepository.findByPurchaseOrder(currentOrder);


        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            int quantity = orderItem.getQuantity();


            Product storedProduct = productRepository.findById(product.getId()).orElse(null);

            if (storedProduct != null && storedProduct.getStock() >= quantity ) {
                storedProduct.setStock(storedProduct.getStock() - quantity);
                productRepository.save(storedProduct);
            } else {
            }
        }


        currentOrder.setStatus(StatusOrder.CONFIRM);


        orderRepository.save(currentOrder);

        currentOrderId = null;

    }





    public PurchaseOrder getCurrentOrder() {
        if (currentOrderId == null)
            createNewOrder();
        return orderRepository.findById(currentOrderId).get();
    }


    public void createNewOrder() {
        PurchaseOrder newOrder = new PurchaseOrder();
        newOrder.setStatus(StatusOrder.ORDER);
        PurchaseOrder record = orderRepository.save(newOrder);
        currentOrderId = record.getId();
    }

    public void order(UUID productId, AddCartRequest request) {
        if (currentOrderId == null)
            createNewOrder();

        PurchaseOrder currentOrder = orderRepository.findById(currentOrderId).get();
        Product product = productRepository.findById(productId).get();

        int requestedQuantity = request.getQuantity();
        int availableStock = product.getStock();

        if (requestedQuantity > 0) {
            if (requestedQuantity <= availableStock) {
                OrderItem item = new OrderItem();
                item.setId(new OrderItemKey(currentOrderId, productId));
                item.setPurchaseOrder(currentOrder);
                item.setProduct(product);
                item.setQuantity(requestedQuantity);
                itemRepository.save(item);
            } else {
                throw new RuntimeException("ไม่สามารถเพิ่มสินค้า " + product.getName() + " ในตะกร้าได้ เนื่องจากสินค้าใน stock มีจำนวนน้อยกว่ายอดต้องการ");
            }
        } else {
            throw new RuntimeException("ไม่สามารถเพิ่มจำนวนติดลบลงในตะกร้าได้");
        }
    }







    public List<PurchaseOrder> getAllOrders() {
        return orderRepository.findAll();
    }
    public PurchaseOrder getById(UUID orderId) {
        return orderRepository.findById(orderId).get();
    }


    public void finishOrder(UUID orderId) {
        PurchaseOrder record = orderRepository.findById(orderId).get();
        record.setStatus(StatusOrder.FINISH);
        orderRepository.save(record);
    }

}


