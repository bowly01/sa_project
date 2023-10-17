package ku.cs.store.controller;

import ku.cs.store.model.ProductRequest;
import ku.cs.store.entity.Product;
import ku.cs.store.service.CategoryService;
import ku.cs.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping
    public String getAllProduct(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product-all";
    }

    @GetMapping("/{id}")
    public String getOneProduct(@PathVariable UUID id, Model model) {
        Product product = productService.getOneById(id);
        model.addAttribute("product", product);
        return "products/index";
    }
    @GetMapping("/add")
    public String getProductToAdd(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product-add";
    }
    @GetMapping("/create")
    public String getProductForm(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/create";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute ProductRequest product, Model model) {
        productService.createProduct(product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/products";
    }
}
