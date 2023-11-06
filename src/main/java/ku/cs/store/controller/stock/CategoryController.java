package ku.cs.store.controller.stock;


import ku.cs.store.entity.Category;
import ku.cs.store.repository.CategoryRepository;
import ku.cs.store.repository.ProductRepository;
import ku.cs.store.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/add")
    public String getCategoryForm(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories-tem/add";
    }


    @PostMapping("/add")
    public String createCategory(@ModelAttribute Category category,
                                 Model model) {
        if(categoryService.categoryNameIsExisted(category.getCategoryName())){
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("nameError","มีประเภทสินค้าชื่อนี้แล้ว");
            return "categories-tem/add";
        }
        categoryService.createCategory(category);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/categories/add";
    }
    // not yet
    @PostMapping ("/delete/{categoryId}")
    public String deleteCategory(@PathVariable UUID categoryId,
                                  Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        if (productRepository.existsByCategoryId(categoryId)) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("deleteC", "มีสินค้าที่อ้างอิงถึงประเภทนี้ ไม่สามารถลบ");
            return "categories-tem/add";
        }
        categoryService.deleteCategoryById(categoryId);
        System.out.print("ควรลบแล้ว");
        return "redirect:/categories/add";
    }

    @GetMapping("/update/{categoryId}")
    public String updatePage(@PathVariable UUID categoryId,Model model){
        model.addAttribute("categoryIndex",categoryService.getOneById(categoryId));
        model.addAttribute("categories",categoryService.getAllCategories());

        return "categories-tem/update";
    }
    @PostMapping("/update/{categoryId}")
    public String updateCategory(@PathVariable("categoryId") UUID categoryId,
                                 @ModelAttribute("updateCategory") Category updateCategory,
                                 Model model, Authentication authentication){
        String username = authentication.getName();
        if(categoryService.categoryNameIsExisted(updateCategory.getCategoryName())){
            model.addAttribute("categories",categoryService.getAllCategories());
            model.addAttribute("categoryIndex",categoryService.getOneById(categoryId));
            model.addAttribute("categoryNameError","มีประเภทสินค้าชื่อนี้แล้ว");
            return "categories-tem/update";
        }
        model.addAttribute("categories",categoryService.getAllCategories());
        categoryService.editCategory(categoryId,updateCategory,username);
        return "redirect:/categories/add";
    }

}
