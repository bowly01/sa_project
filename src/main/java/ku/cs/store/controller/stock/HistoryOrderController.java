package ku.cs.store.controller.stock;


import ku.cs.store.entity.OrderComparator;
import ku.cs.store.entity.OrderItem;
import ku.cs.store.entity.PurchaseOrder;
import ku.cs.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/salesHistory")
public class HistoryOrderController {


    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderComparator orderComparator;

    @GetMapping
    public String showSalesHistory(Model model) {
        List<PurchaseOrder> salesHistory = orderService.getSalesHistory();
        Collections.sort(salesHistory, orderComparator::compareByTimestamp);
        model.addAttribute("salesHistory", salesHistory);
        return "sales-history";
    }

    @GetMapping("/view-order/{orderId}")
    public String viewOrder(@PathVariable UUID orderId, Model model) {
        List<OrderItem> orderItems = orderService.getOrderItems(orderId);
        PurchaseOrder order = orderService.getOrder(orderId);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("order", order);
        return "view_order";
    }


    @PostMapping("/delete-order/{orderId}")
    public String deleteOrder(@PathVariable UUID orderId, Model model) {
        orderService.deleteOrder(orderId);
        return "redirect:/salesHistory";
    }
}


