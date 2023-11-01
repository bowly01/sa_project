package ku.cs.store.controller.stock;


import ku.cs.store.entity.Category;
import ku.cs.store.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/categories")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    @GetMapping("/add")
    public String getCategoryForm(Model model) {
        return "category-add";
    }


    @PostMapping("/add")
    public String createCategory(@ModelAttribute Category category,
                                 Model model) {
        if(categoryService.categoryNameIsExisted(category)){
            model.addAttribute("nameError","มีประเภทสินค้าชื่อนี้แล้ว");
            return "category-add";
        }
        categoryService.createCategory(category);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/inventory";
    }
    // not yet
    @PostMapping("/delete")
    public String deleteCategory(@ModelAttribute Category category,
                                 Model model){
        categoryService.createCategory(category);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/inventory";
    }
}
