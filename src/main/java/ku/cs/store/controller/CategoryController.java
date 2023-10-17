package ku.cs.store.controller;


import ku.cs.store.model.CategoryRequest;
import ku.cs.store.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


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
    public String createCategory(@ModelAttribute CategoryRequest category,
                                 Model model) {
        categoryService.createCategory(category);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/products";
    }
}
