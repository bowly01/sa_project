package ku.cs.store.controller.stock;

import ku.cs.store.entity.Unit;
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
    @GetMapping
    public String getUnitFrom(Model model){
        model.addAttribute("units",unitService.getAllUnit());
        return "products/unit-custom";}
    @GetMapping("/edit/{id}")
    public String getUnitToEdit(@PathVariable Long id,Model model){
        model.addAttribute("units",unitService.getAllUnit());
        model.addAttribute("unitIndex",unitService.getOneById(id));
        return "products/unit-custom";
    }
    @PostMapping("/edit/{id}")
    public String editUnit(@PathVariable("id") Long id, @ModelAttribute Unit updateUnit, Model model){
        if (unitService.unitNameIsExisted(updateUnit)||updateUnit.getQuantity() < 1 ) {
            model.addAttribute("nameError",unitService.unitNameIsExisted(updateUnit)?"มีหน่วยสินค้าชื่อนี้แล้ว":null);
            model.addAttribute("quantityError", updateUnit.getQuantity() < 1 ? "กรุณากรอกจำนวนสินค้ามากกว่าเท่ากับ 1" : null);
            model.addAttribute("units", unitService.getAllUnit());
            return "products/unit-custom"; // Return to the form page with error messages
        }
        model.addAttribute("units",unitService.getAllUnit());
        unitService.editUnit(id,updateUnit);
        return "products/unit-custom";
    }
//    @PostMapping("/delete/{id}")
//    public String deleteUnit(@PathVariable Long id,@ModelAttribute  Unit unit,Model model){
//        model.addAttribute("units",unitService.getAllUnit());
//        unitService.deleteUnitById(id);
//        return "redirect:/unit";
//    }
    @DeleteMapping("/delete/{unitId}")
    public ResponseEntity<String> deleteUnit(@PathVariable("unitId") Long unitId) {
        System.out.print("gg");
        try {
            // ในส่วนนี้คุณควรเรียกเมธอดหรือฟังก์ชันที่ใช้ในการลบหน่วยนับ
            unitService.deleteUnitById(unitId);
//            unitRepository.deleteById(unitId);

            return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete unit", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
