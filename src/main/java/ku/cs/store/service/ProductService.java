package ku.cs.store.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ku.cs.store.common.StatusProduct;
import ku.cs.store.entity.*;
import ku.cs.store.model.ProductRequest;
import ku.cs.store.repository.CategoryRepository;
import ku.cs.store.repository.ProductLogRepository;
import ku.cs.store.repository.ProductRepository;
import ku.cs.store.repository.UnitRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service

public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductLogService productLogService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UnitRepository unitRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProductLogRepository productLogRepository;
    @Autowired
    private ModelMapper modelMapper;
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public List<Product> getAvailableProducts(){return productRepository.findByStatusProduct(StatusProduct.AVAILABLE);}

    public Product getOneById(UUID id) {
        return productRepository.findById(id).get();
    }
    @Transactional
    public void createProduct(ProductRequest product, MultipartFile file,String username) {
        Product record = modelMapper.map(product, Product.class);
        record.setStatusProduct(StatusProduct.AVAILABLE);
        Category category = categoryRepository.findById(product.getCategoryId()).get();
        Unit unit = unitRepository.findById(product.getUnitId()).get();
        record.setStock(product.getStock());
        record.setRequireProduct(product.getRequireProduct());
        record.setUnit(unit);
        record.setDetail(product.getDetail());
        record.setCategory(category);
        try {
            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            record.setImageFile(base64Image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Reattach the Product entity to the current Hibernate session
        Product attachedProduct = entityManager.merge(record);
        //เก็บประวัติ
        productLogService.logCreation(attachedProduct, "สร้างสินค้าใหม่ชื่อ"+attachedProduct.getName(), username);


    }
    @Transactional
    public void updateProductStatus(UUID productId, String username) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setStatusProduct(StatusProduct.UNAVAILABLE);
            productRepository.save(product);
            productLogService.logUnavailable(product, "เปลี่ยนสถานะสินค้าชื่อ"+product.getName(), username);
        } else {
            // Handle the case where the product with the given id is not found
            throw new RuntimeException("Product not found with id: " + productId);
        }
    }
    public Boolean validAddStock(UUID id,int amountToAdd,Long unitId){
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
//        Product record = modelMapper.map(productRequest,Product.class);
        Unit unit = unitRepository.findById(unitId).get();
        if(amountToAdd>product.getStock()+(amountToAdd*unit.getQuantity())){
            return false;
        }
        return true;
    }
    public void addStock(UUID productId, int amountToAdd, Long unitId,String username) {
        // Retrieve the product from the database
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        Unit unit = unitRepository.findById(unitId).get();
        int quantityPerUnit = unit.getQuantity();
        // Update the stock
        int total=amountToAdd*quantityPerUnit;
        int newStock = product.getStock() + total;
        product.setStock(newStock);
        product.setUnit(unit);

        // Save the updated product back to the database
        productRepository.save(product);
        // Save History
        productLogService.logIncrease(product, "เพิ่มสินค้าจำนวน " + product.getStock()+"ชิ้น", username);
    }

    public void editProduct(ProductRequest request, MultipartFile file, UUID id, String username) {
        // Retrieve the existing product from the database
        Optional<Product> existingProductOptional = productRepository.findById(id);

        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();

            // Create a StringBuilder to store the details of changes
            StringBuilder changes = new StringBuilder();

            // Compare and update the fields
            if (!Objects.equals(request.getName(), existingProduct.getName())) {
                changes.append("เปลี่ยนชื่อจาก ").append(existingProduct.getName()).append(" เป็น ").append(request.getName()).append(";");
                existingProduct.setName(request.getName());
            }

            if (!Objects.equals(request.getDetail(), existingProduct.getDetail())) {
                changes.append("เปลี่ยนรายละเอียดจาก ").append(existingProduct.getDetail()).append(" เป็น ").append(request.getDetail()).append(";");
                existingProduct.setDetail(request.getDetail());
            }

            if (!Objects.equals(request.getStock(), existingProduct.getStock())) {
                changes.append("เปลี่ยนจำนวนคลังสินค้า ").append(existingProduct.getStock()).append(" เป็น ").append(request.getStock()).append(";");
                existingProduct.setStock(request.getStock());
            }

            if (!Objects.equals(request.getPrice(), existingProduct.getPrice())) {
                changes.append("เปลี่ยนราคาสินค้า ").append(existingProduct.getPrice()).append(" เป็น ").append(request.getPrice()).append(";");
                existingProduct.setPrice(request.getPrice());
            }

            if (!Objects.equals(request.getRequireProduct(), existingProduct.getRequireProduct())) {
                changes.append("เปลี่ยนจำนวนต้องการมากสุด ").append(existingProduct.getRequireProduct()).append(" เป็น ").append(request.getRequireProduct()).append(";");
                existingProduct.setRequireProduct(request.getRequireProduct());
            }

            // Retrieve the category with a safe optional handling
            Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);
            if (category != null) {
                existingProduct.setCategory(category);
            }

            // Check if the image is provided and update it
            if (!file.isEmpty()) {
                try {
                    byte[] imageBytes = file.getBytes();
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    existingProduct.setImageFile(base64Image);
                    changes.append("Image updated;");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Save the updated product back to the database
            productRepository.save(existingProduct);

            // Save the change details to the ProductLog
            if (changes.length() > 0) {
                // Save History
                productLogService.logIncrease(existingProduct, changes.toString(), username);
            }
        }
    }

    public boolean productNameIsExisted(ProductRequest productRequest) {
        Optional<Product> existingProduct = productRepository.findByName(productRequest.getName());
        return existingProduct.isPresent();
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}


