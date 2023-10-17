package ku.cs.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/purchases")
public class PurchaseController {
    @GetMapping("/create")
    public String getCategoryForm(Model model) {
        return "create-purchase";
    }
}
