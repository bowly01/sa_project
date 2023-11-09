package ku.cs.store.service;

import ku.cs.store.common.StatusOrder;
import ku.cs.store.entity.*;
import ku.cs.store.model.AddCartRequest;
import ku.cs.store.repository.MemberRepository;
import ku.cs.store.repository.OrderItemRepository;
import ku.cs.store.repository.ProductRepository;
import ku.cs.store.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class OrderService {


    @Autowired
    private PurchaseOrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrderItemRepository itemRepository;


    @Autowired
    private ProductRepository productRepository;


    private UUID currentOrderId;




//    public void submitOrder() {
//        PurchaseOrder currentOrder = orderRepository.findById(currentOrderId).orElse(null);
//
//        if (currentOrder == null) {
//            return;
//        }
//
//        if (currentOrder.getStatus() != StatusOrder.ORDER) {
//
//            return;
//        }
//
//
//        List<OrderItem> orderItems = itemRepository.findByPurchaseOrder(currentOrder);
//
//
//        for (OrderItem orderItem : orderItems) {
//            Product product = orderItem.getProduct();
//            int quantity = orderItem.getQuantity();
//
//
//            Product storedProduct = productRepository.findById(product.getId()).orElse(null);
//
//            if (storedProduct != null && storedProduct.getStock() >= quantity ) {
//                storedProduct.setStock(storedProduct.getStock() - quantity);
//                productRepository.save(storedProduct);
//            } else {
//            }
//        }
//        currentOrder.setTimestamp(LocalDateTime.now());
//        currentOrder.setStatus(StatusOrder.CONFIRM);
//        orderRepository.save(currentOrder);
//        currentOrderId = null;
//
//    }

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

            if (storedProduct != null) {
                if (quantity <= 0) {
                    // ถ้าปริมาณสินค้าเป็น 0 หรือติดลบ
                    // คุณต้องทำการลบรายการสินค้านี้จากรายการตะกร้าสินค้า
                    itemRepository.delete(orderItem);
                } else if (storedProduct.getStock() >= quantity) {
                    storedProduct.setStock(storedProduct.getStock() - quantity);
                    productRepository.save(storedProduct);
                } else {
                    // จัดการกรณีที่ไม่มีสต็อกเพียงพอ
                }
            }
        }

        currentOrder.setTimestamp(LocalDateTime.now());
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member member = memberRepository.findByUsername(username);
        PurchaseOrder newOrder = new PurchaseOrder();
        newOrder.setStatus(StatusOrder.ORDER);
        newOrder.setMember(member);
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

    public List<PurchaseOrder> getSalesHistory() {

        List<PurchaseOrder> salesHistory = orderRepository.findByStatusInCustom(StatusOrder.CONFIRM, StatusOrder.DELETE);


//        salesHistory.add(orderRepository.findByStatus(StatusOrder.DELETE));
        return salesHistory;
//        return salesHistory;
    }










    public List<PurchaseOrder> getAllOrders() {
        return orderRepository.findAll();
    }
    public PurchaseOrder getById(UUID orderId) {
        return orderRepository.findById(orderId).get();
    }

    public List<OrderItem> getOrderItems(UUID orderId) {
        PurchaseOrder order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            return itemRepository.findByPurchaseOrder(order);
        } else {
            return Collections.emptyList();
        }
    }

    public PurchaseOrder getOrder(UUID orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public void deleteOrder(UUID orderId) {
        PurchaseOrder order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            List<OrderItem> orderItems = itemRepository.findByPurchaseOrder(order);

            for (OrderItem orderItem : orderItems) {
                Product product = orderItem.getProduct();
                int quantity = orderItem.getQuantity();

                Product storedProduct = productRepository.findById(product.getId()).orElse(null);

                if (storedProduct != null) {
                    storedProduct.setStock(storedProduct.getStock() + quantity);
                    productRepository.save(storedProduct);
                }
            }
            order.setStatus(StatusOrder.DELETE);
            orderRepository.save(order);

//            orderRepository.delete(order);
        }
    }

    public void deleteCartItem(UUID orderId, UUID productId) {
        // ค้นหารายการสินค้าที่ต้องการลบ
        OrderItem orderItem = itemRepository.findById(new OrderItemKey(orderId, productId)).orElse(null);

        if (orderItem != null) {
            // คืนจำนวนสินค้ากลับไปยังคลัง
            Product product = orderItem.getProduct();
            int quantity = orderItem.getQuantity();

            Product storedProduct = productRepository.findById(product.getId()).orElse(null);

            if (storedProduct != null) {
                // คืนสินค้าเข้าคลัง
                storedProduct.setStock(storedProduct.getStock() + quantity);
                productRepository.save(storedProduct);
            }

            // ลบรายการสินค้าจากฐานข้อมูล
            itemRepository.delete(orderItem);
        }
    }
















}


