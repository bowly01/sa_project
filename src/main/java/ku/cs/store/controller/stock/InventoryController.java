package ku.cs.store.controller.stock;

import ku.cs.store.entity.Product;
import ku.cs.store.entity.ProductLog;
import ku.cs.store.model.ProductRequest;
import ku.cs.store.service.CategoryService;
import ku.cs.store.service.ProductLogService;
import ku.cs.store.service.ProductService;
import ku.cs.store.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private ProductLogService productLogService;
    @GetMapping
    public String getAllProduct(Model model,  @RequestParam(name = "page", defaultValue = "0") int page) {
        int pageSize = 5; // Number of products per page
        Page<Product> productPage = productService.findAll(PageRequest.of(page, pageSize));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("page", productPage);

        return "stock/inventory";
    }
    @GetMapping("/history")
    public String getHistory(Model model){
        model.addAttribute("histories",productLogService.getAllHistory());
        return "products/history";
    }
    @GetMapping("/add/{id}")
    public String getProductToAdd(@PathVariable UUID id, Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("units", unitService.getAllUnit());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("productsIndex", productService.getOneById(id));

        return "stock/add";
    }
    @PostMapping("/add/{id}")
    public String addProductToStock(@PathVariable UUID id, Model model,
                                    @ModelAttribute ProductRequest product,
                                    Authentication authentication) {
        String username = authentication.getName();

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("productsIndex", productService.getOneById(id));
        model.addAttribute("units", unitService.getAllUnit());
        productService.addStock(id,product.getStock(),product.getUnitId(),username);
        return "redirect:/";
    }
}
