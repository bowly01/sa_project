package ku.cs.store.controller.stock;


import ku.cs.store.entity.Category;
import ku.cs.store.entity.Unit;
import ku.cs.store.repository.CategoryRepository;
import ku.cs.store.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;


@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/add")
    public String getCategoryForm(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "category-add";
    }


    @PostMapping("/add")
    public String createCategory(@ModelAttribute Category category,
                                 Model model) {
        if(categoryService.categoryNameIsExisted(category)){
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("nameError","มีประเภทสินค้าชื่อนี้แล้ว");
            return "category-add";
        }
        categoryService.createCategory(category);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/categories/add";
    }
    // not yet
    @PostMapping("/delete")
    public String deleteCategory(@ModelAttribute Category category,
                                 Model model){
        categoryService.createCategory(category);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/inventory";
    }
    @GetMapping("/update/{id}")
    public ModelAndView editPage(@PathVariable UUID id){
       Category category = categoryRepository.findById(id).orElse(null);
       ModelAndView modelAndView = new ModelAndView("update");
       modelAndView.addObject("category", category);
        return modelAndView;
    }
    @PostMapping("/update")
    public String editCategory(@ModelAttribute Category updateCategory, Model model){
        if(categoryService.categoryNameIsExisted(updateCategory)){
            model.addAttribute("categories",categoryService.getAllCategories());
            model.addAttribute("nameError","มีประเภทสินค้าชื่อนี้แล้ว");
            return "category-add";
        }
        model.addAttribute("categories",categoryService.getAllCategories());
        categoryRepository.save(updateCategory);
        return "category-add";
    }

}
