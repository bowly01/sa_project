package ku.cs.store.controller.stock;

import ku.cs.store.entity.Unit;
import ku.cs.store.repository.ProductRepository;
import ku.cs.store.repository.UnitRepository;
import ku.cs.store.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/unit")
public class UnitController {
    @Autowired
    private UnitService unitService;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private ProductRepository productRepository;
    @GetMapping
    public String getUnitFrom(Model model){
        model.addAttribute("units",unitService.getAllUnit());
        return "products/unit-custom";}
    @GetMapping("/edit/{id}")
    public String getUnitToEdit(@PathVariable Long id,Model model){
        model.addAttribute("units",unitService.getAllUnit());
        model.addAttribute("unitIndex",unitService.getOneById(id));
        return "products/unit-update";
    }
    @PostMapping("/edit/{id}")
    public String editUnit(@PathVariable("id") Long id, @ModelAttribute Unit updateUnit, Model model){

        // Check if the unit name already exists
        if (unitService.unitNameIsExisted(updateUnit)) {
            model.addAttribute("nameError", "มีหน่วยสินค้าชื่อนี้แล้ว");
            model.addAttribute("units", unitService.getAllUnit());
            return "products/unit-update"; // Return to the form page with error messages
        }
        model.addAttribute("units",unitService.getAllUnit());
        unitService.editUnit(id,updateUnit);
        return "products/unit-custom";
    }
//    @PostMapping("/delete/{unitId}")
//    public String deleteCategory(@PathVariable Long unitId,
//                                 Model model) {
//        model.addAttribute("units", unitService.getAllUnit());
//        if (productRepository.existsByUnitId(unitId)) {
//            model.addAttribute("units", unitService.getAllUnit());
//            model.addAttribute("deleteC", "มีสินค้าที่อ้างอิงถึงประเภทนี้ ไม่สามารถลบ");
//            return "products/unit-custom";
//        }
//        unitService.deleteUnitById(unitId);
//        System.out.print("ควรลบแล้ว");
//        return "redirect:/unit";
//    }

    @PostMapping("/add")
    public String createUnit(@ModelAttribute Unit updateUnit,Model model){
        if (unitService.unitNameIsExisted(updateUnit) ||updateUnit.getQuantity() < 1 )
        {
            model.addAttribute("nameError",unitService.unitNameIsExisted(updateUnit)?"มีหน่วยสินค้าชื่อนี้แล้ว":null);
            model.addAttribute("quantityError", updateUnit.getQuantity() < 1 ? "กรุณากรอกจำนวนสินค้ามากกว่าเท่ากับ 1" : null);
            model.addAttribute("units", unitService.getAllUnit());
            return "products/unit-custom"; // Return to the form page with error messages
        }
        unitService.createUnit(updateUnit);
        model.addAttribute("units",unitService.getAllUnit());
        return "redirect:/unit";
    }
}
