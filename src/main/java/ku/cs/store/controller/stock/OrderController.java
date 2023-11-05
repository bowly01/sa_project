package ku.cs.store.controller.stock;

import ku.cs.store.model.AddCartRequest;
import ku.cs.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Controller
@RequestMapping("/orders")
public class OrderController {


    @Autowired
    private OrderService orderService;

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cart", orderService.getCurrentOrder());
        return "cart";
    }


    @PostMapping
    public String submitOrder(Model model) {
        orderService.submitOrder();
        model.addAttribute("confirmOrder", true);
        return "redirect:/";
    }


    @PostMapping("/{productId}")
    public String order(@PathVariable UUID productId,
                        @ModelAttribute AddCartRequest request, Model model) {
        try {
            orderService.order(productId, request);
            return "redirect:/orders";
        } catch (RuntimeException e) {
            String errorMessage = "ไม่สามารถเพิ่มสินค้าลงตะกร้าได้"; // ข้อความข้อผิดพลาด
            model.addAttribute("errorMessage", errorMessage);
            return "redirect:/"; // กลับไปที่หน้าหลัก
        }
    }




}

