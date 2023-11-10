package ku.cs.store.controller.stock;

import ku.cs.store.common.OperationType;
import ku.cs.store.common.StatusProduct;
import ku.cs.store.entity.ProductLog;
import ku.cs.store.service.ProductLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/productLog")
public class ProductLogController {
    @Autowired
    private ProductLogService productLogService;
//    @GetMapping
//    public String getAllProductLog(Model model){
//        List<ProductLog> productLogs = productLogService.getAllHistory();
//        model.addAttribute("productLogs",productLogs);
//        return "products/history";
//    }
@GetMapping("/history")
public String getHistory(Model model){
    List<ProductLog> productLogs = productLogService.getAllHistory();
//        model.addAttribute("operationType", operationType);
    model.addAttribute("productLogs",productLogs);
    return "products/history";
}

//    @GetMapping
//    public String getHistory getHistory(@RequestParam OperationType operationType, Model model) {
//        List<ProductLog> productLogs = productLogService.getHistoriesByOperationType(operationType);
//        model.addAttribute("productLogs",productLogs);
//
//        return "history";
//    }
}

