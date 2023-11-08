package ku.cs.store.service;

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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ProductLogRepository productLogRepository;
    @Autowired
    private ModelMapper modelMapper;
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getOneById(UUID id) {
        return productRepository.findById(id).get();
    }

    public void createProduct(ProductRequest product, MultipartFile file,String username) {
        Product record = modelMapper.map(product, Product.class);
        record.setCreatedDateTime(new Date());
        record.setLastModifiedDateTime(new Date());
        record.setDateStock(new Date());
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

        // Save the product
        productRepository.save(record);
        // Save History
        ProductLog history = new ProductLog();
        history.setProductId(record.getId());
        history.setProductName(record.getName());
        history.setOperationType("CREATE");
        history.setDetail("สร้างสินค้าใหม่");
        history.setUser(username);
        history.setTimestamp(new Date());
        productLogRepository.save(history);



    }
    public void deleteProductById(UUID productId,String username){
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            productRepository.delete(product);

            // Save History
            ProductLog history = new ProductLog();
            history.setProductId(product.getId());
            history.setProductName(product.getName());
            history.setOperationType("DELETE");
            history.setDetail("ลบสินค้าชิ้นนี้");
            history.setUser(username);
            history.setTimestamp(new Date());
            productLogRepository.save(history);
        } else {
            // Handle the case where the product with the given ID doesn't exist
            // You can throw an exception or handle it in some other way.
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
        product.setDateStock(new Date());
        product.setUnit(unit);

        // Save the updated product back to the database
        productRepository.save(product);
        // Save History
        ProductLog history = new ProductLog();
        history.setProductId(product.getId());
        history.setProductName(product.getName());
        history.setOperationType("ADD");
        history.setDetail("เพิ่มสินค้าจำนวน"+total+"ชิ้น");
        history.setUser(username);
        history.setTimestamp(new Date());
        productLogRepository.save(history);
    }

    public void updateProduct(ProductRequest updatedProduct, MultipartFile imageFile,UUID id,String username) {
        Product existingProduct = productRepository.findById(id).orElseThrow();
        existingProduct.setId(id);
        String test = "";
        if(!updatedProduct.getName().equals(existingProduct.getName())){
            existingProduct.setName(updatedProduct.getName());
            test+= "แก้ชื่อเป็น: "+updatedProduct.getName()+" ,";
        }
        if(!updatedProduct.getDetail().equals(existingProduct.getDetail())){
            existingProduct.setDetail(updatedProduct.getDetail());
            test+= "แก้รายละเอียดเป็น: "+updatedProduct.getDetail()+" ,";
        }
        if(updatedProduct.getStock()!=(existingProduct.getStock())){
            test+= "แก้จำนวนสินค้าในคลังเป็น: "+updatedProduct.getStock()+" ,";
        }
        if(updatedProduct.getPrice()!=(existingProduct.getPrice())){
            existingProduct.setPrice(updatedProduct.getPrice());
            test+= "แก้ราคาเป็น: "+updatedProduct.getPrice()+" ,";
        }
        if(updatedProduct.getRequireProduct()!=(existingProduct.getRequireProduct())){
            test+= "แก้จำนวนสินค้าที่ต้องการเป็น: "+updatedProduct.getRequireProduct()+" ,";
        }
        Category category = categoryRepository.findById(updatedProduct.getCategoryId()).get();
        existingProduct.setLastModifiedDateTime(new Date());
        existingProduct.setStock(updatedProduct.getStock());
        existingProduct.setRequireProduct(updatedProduct.getRequireProduct());
        if(!imageFile.isEmpty()) {
            try {
                byte[] imageBytes = imageFile.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                existingProduct.setImageFile(base64Image);
                test+="แก้ไขรูปภาพ";
            } catch (IOException e) {
                e.printStackTrace();
        }

    }
        existingProduct.setCategory(category);
        // Save the updated product back to the database
        productRepository.save(existingProduct);
        // Save History
        ProductLog history = new ProductLog();
        history.setProductId(existingProduct.getId());
        history.setProductName(existingProduct.getName());
        history.setOperationType("EDIT");
        history.setDetail(test);
        history.setUser(username);
        history.setTimestamp(new Date());

        productLogRepository.save(history);

}
    public boolean productNameIsExisted(ProductRequest productRequest) {
        Optional<Product> existingProduct = productRepository.findByName(productRequest.getName());
        return existingProduct.isPresent();
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}

