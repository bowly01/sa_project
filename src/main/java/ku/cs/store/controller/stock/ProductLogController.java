package ku.cs.store.controller.stock;

import ku.cs.store.entity.ProductLog;
import ku.cs.store.service.ProductLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductLogController {
    @Autowired
    private ProductLogService productLogService;

    @GetMapping("/getHistories")
    public List<ProductLog> getHistoriesByOperationType(@RequestParam String operationType) {
        return productLogService.getHistoriesByOperationType(operationType);
    }
}

