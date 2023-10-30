package ku.cs.store.service;

import ku.cs.store.entity.Category;
import ku.cs.store.entity.ProductLog;
import ku.cs.store.repository.CategoryRepository;
import ku.cs.store.repository.ProductLogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductLogService {
    @Autowired
    private ProductLogRepository productLogRepository;


    @Autowired
    private ModelMapper modelMapper;


    public List<ProductLog> getAllHistory() {
        return productLogRepository.findAll();
    }
}
