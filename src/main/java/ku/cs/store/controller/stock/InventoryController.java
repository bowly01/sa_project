package ku.cs.store.controller.stock;

import ku.cs.store.entity.Product;
import ku.cs.store.entity.ProductLog;
import ku.cs.store.entity.Unit;
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

import java.util.List;
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
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("page", productPage);

        return "stock/inventory";
    }
    @GetMapping("/history")
    public String getHistory(@RequestParam(value = "operationType", required = false) String operationType,
                             Model model){
        List<ProductLog> productLogs = productLogService.getHistoriesByOperationType(operationType);
        model.addAttribute("operationType", operationType);
        model.addAttribute("histories",productLogs);
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

        if(product.getStock() < 1 ){
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("productsIndex", productService.getOneById(id));
            model.addAttribute("units", unitService.getAllUnit());
            model.addAttribute("stockError", product.getStock() < 1 ? "กรุณากรอกจำนวนสินค้ามากกว่าเท่ากับ 1" : null);
//            model.addAttribute("stockErrorOver", !productService.validAddStock(id,product.getStock(),product.getUnit().getId()) ? "จำนวนสินค้ามากเกินความต้องการกรุณาเพิ่มจำนวนความต้องการ" : null);

            return "stock/add";
        }
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("productsIndex", productService.getOneById(id));
        model.addAttribute("units", unitService.getAllUnit());
        productService.addStock(id,product.getStock(),product.getUnitId(), username);
        return "redirect:/inventory";
    }
}
