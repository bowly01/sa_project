package ku.cs.store.controller.stock;

import jakarta.validation.Valid;
import ku.cs.store.entity.Member;
import ku.cs.store.model.ProductRequest;
import ku.cs.store.service.CategoryService;
import ku.cs.store.service.ProductService;
import ku.cs.store.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UnitService unitService;
    //view index
    @GetMapping("/{id}")
    public String getOneProduct(@PathVariable UUID id, Model model) {
        model.addAttribute("products", productService.getOneById(id));
        return "products/index";
    }


    @GetMapping("/edit/{id}")
    public String getProductToEdit(@PathVariable UUID id,Model model) {
        model.addAttribute("products", productService.getOneById(id));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("units", unitService.getAllUnit());
        System.out.println("at edit get");

        return "products/edit";
    }
    @PostMapping ("/edit/{id}")
    public String updateProduct(@PathVariable UUID id,@ModelAttribute ProductRequest productRequest,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                Model model,Authentication authentication) {
        String username = authentication.getName();
        // Implement the update logic here, including updating the database and handling image uploads.
        if (productService.productNameIsExisted(productRequest)&&(!productRequest.getName().equals(productService.getOneById(id).getName()))) {
            model.addAttribute("products", productService.getOneById(id));
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("nameError","มีสินค้าชื้อนี้แล้ว");
            model.addAttribute("units", unitService.getAllUnit());
            return "products/edit";
        }
        productService.updateProduct(productRequest, imageFile, id,username);
        return "redirect:/inventory";
    }
    @GetMapping("/create")
    public String getProductForm(Model model,ProductRequest productRequest) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("units", unitService.getAllUnit());

        model.addAttribute("product", productRequest);
        return "products/create";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute ProductRequest productRequest,
                                @RequestParam("imageFile") MultipartFile file,
                                Model model,
                                Authentication authentication) {
        String username = authentication.getName();
        model.addAttribute("product", productRequest);
        if (productService.productNameIsExisted(productRequest)||productRequest.getPrice() < 1 || productRequest.getRequireProduct() < 1 || productRequest.getStock() < 1) {
            model.addAttribute("nameError",productService.productNameIsExisted(productRequest)?"มีสินค้าชื่อนี้แล้ว":null);
            model.addAttribute("priceError", productRequest.getPrice() < 1 ? "กรุณากรอกราคาสินค้ามากกว่าเท่ากับ 1" : null);
            model.addAttribute("requireError", productRequest.getRequireProduct() < 1 ? "กรุณากรอกจำนวนสินค้าที่ต้องการมากกว่าเท่ากับ 1" : null);
            model.addAttribute("stockError", productRequest.getStock() < 1 ? "กรุณากรอกจำนวนสินค้ามากกว่าเท่ากับ 1" : null);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("units", unitService.getAllUnit());
            return "products/create"; // Return to the form page with error messages
        }


        productService.createProduct(productRequest, file,username);
        model.addAttribute("units", unitService.getAllUnit());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/inventory";
    }
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable UUID id, @ModelAttribute ProductRequest productRequest,
                                Model model,Authentication authentication){
        String username = authentication.getName();
        productService.deleteProductById(id,username);
        model.addAttribute("units", unitService.getAllUnit());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("product", productRequest);

        return "redirect:/inventory";
    }
}