package ku.cs.store.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ku.cs.store.common.OperationType;
import ku.cs.store.common.StatusProduct;
import ku.cs.store.entity.Product;
import ku.cs.store.entity.ProductLog;
import ku.cs.store.repository.ProductLogRepository;
import ku.cs.store.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
@Transactional
public class ProductLogService {
    @Autowired
    private ProductLogRepository productLogRepository;
    @Autowired
    private ProductRepository productRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;
    public List<ProductLog> getHistoriesByOperationType(OperationType operationType) {
            return productLogRepository.findByOperationType(operationType);

    }
    public void logIncrease(Product product, String detail, String username) {
        ProductLog increaseLog = new ProductLog();

        increaseLog.setOperationType(OperationType.INCREASE);
        increaseLog.setDetail(detail);
        increaseLog.setUser(username);
        increaseLog.setTimestamp(new Date());
        increaseLog.setStatus(product.getStatusProduct()); // Adjust as needed
        increaseLog.setProduct(product);

        productLogRepository.save(increaseLog);
    }

    public void logCreation(Product product, String detail, String username){
        ProductLog creationLog = new ProductLog();
        creationLog.setOperationType(OperationType.CREATE);
        creationLog.setDetail(detail);
        creationLog.setUser(username);
        creationLog.setTimestamp(new Date());
        creationLog.setStatus(product.getStatusProduct()); // Adjust as needed
        creationLog.setProduct(product);

        // Reattach the Product entity to the current Hibernate session
        Product attachedProduct = entityManager.merge(product);
        creationLog.setProduct(attachedProduct);

        // Persist the ProductLog entity
        productLogRepository.save(creationLog);
    }

    public void logUnavailable(Product product, String detail, String username){
        ProductLog unavailableLog = new ProductLog();
        unavailableLog.setOperationType(OperationType.UNAVAILABLE);
        unavailableLog.setDetail(detail);
        unavailableLog.setUser(username);
        unavailableLog.setTimestamp(new Date());
        unavailableLog.setStatus(product.getStatusProduct()); // Adjust as needed
        unavailableLog.setProduct(product);

        productLogRepository.save(unavailableLog);
    }
    public void logUpdate(Product product, String detail, String username){
        ProductLog updateLog = new ProductLog();
        updateLog.setOperationType(OperationType.UPDATE);
        updateLog.setDetail(detail);
        updateLog.setUser(username);
        updateLog.setTimestamp(new Date());
        updateLog.setStatus(product.getStatusProduct()); // Adjust as needed
        updateLog.setProduct(product);

        productLogRepository.save(updateLog);
    }



    public List<ProductLog> getAllHistory() {
        return productLogRepository.findAll();
    }
}
