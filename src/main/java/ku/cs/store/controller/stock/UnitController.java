package ku.cs.store.controller.stock;

import ku.cs.store.entity.Unit;
import ku.cs.store.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/unit")
public class UnitController {
    @Autowired
    private UnitService unitService;
    @GetMapping("/add")
    public String getUnitFrom(Model model){return "products/unit-custom";}

    @PostMapping("/add")
    public String createUnit(@ModelAttribute Unit unit,Model model){
        unitService.createUnit(unit);
        model.addAttribute("units",unitService.getAllUnit());
        return "redirect:/products";
    }
}
