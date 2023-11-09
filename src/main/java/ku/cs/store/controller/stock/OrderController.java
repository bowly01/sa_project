package ku.cs.store.controller.stock;

import ku.cs.store.model.AddCartRequest;
import ku.cs.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ku.cs.store.entity.PurchaseOrder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String submitOrder(Model model,Authentication authentication) {
        String username = authentication.getName();
        orderService.submitOrder();
        model.addAttribute("confirmOrder", true);
        model.addAttribute("username","username");
        return "redirect:/";
    }


    @PostMapping("/{productId}")
    public String order(@PathVariable UUID productId,
                        @ModelAttribute AddCartRequest request, Model model,     RedirectAttributes redirectAttributes) {
        try {
            orderService.order(productId, request);
            return "redirect:/orders";
        } catch (RuntimeException e) {
//            String errorMessage = "ไม่สามารถเพิ่มสินค้าลงตะกร้าได้"; // ข้อความข้อผิดพลาด
            redirectAttributes.addFlashAttribute("errorMessage", "ไม่สามารถเพิ่มสินค้าลงตะกร้าได้");
            return "redirect:/" ;// กลับไปที่หน้าหลัก
        }
    }

//    @PostMapping("/delete/{orderId}/{productId}")
//    public String deleteCartItem(@PathVariable UUID orderId, @PathVariable UUID productId, Model model) {
//        orderService.deleteCartItem(orderId, productId);
//        PurchaseOrder order = orderService.getCurrentOrder();
//        model.addAttribute("order", order);
//
//        return "redirect:/orders"; // หลังจากลบเสร็จให้กลับไปที่หน้า Order
//    }

    @DeleteMapping("/delete/{orderId}/{productId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable UUID orderId, @PathVariable UUID productId) {
        try {
            orderService.deleteCartItem(orderId, productId);
            return ResponseEntity.ok("รายการสินค้าถูกลบเรียบร้อย");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("เกิดข้อผิดพลาดในการลบรายการสินค้า");
        }
    }









}

