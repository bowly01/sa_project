package ku.cs.store.service;

import ku.cs.store.entity.Product;
import ku.cs.store.entity.Category;
import ku.cs.store.entity.Unit;
import ku.cs.store.model.ProductRequest;
import ku.cs.store.repository.CategoryRepository;
import ku.cs.store.repository.ProductRepository;
import ku.cs.store.repository.UnitRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private ModelMapper modelMapper;
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getOneById(UUID id) {
        return productRepository.findById(id).get();
    }

    public void createProduct(ProductRequest product, MultipartFile file) {
        Product record = modelMapper.map(product, Product.class);
        record.setCreatedDateTime(new Date());
        record.setLastModifiedDateTime(new Date());
        Category category = categoryRepository.findById(product.getCategoryId()).get();
        Unit unit = unitRepository.findById(product.getUnitId()).get();
        record.setStock(0);
        record.setUnit(unit);
        record.setCategory(category);
        try {
            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            record.setImageFile(base64Image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        productRepository.save(record);
    }


    public void addStock(UUID productId, int amountToAdd, Long unitId) {
        // Retrieve the product from the database
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        Unit unit = unitRepository.findById(unitId).get();
        int quantityPerUnit = unit.getQuantity();
        // Update the stock
        int newStock = product.getStock() + amountToAdd*quantityPerUnit;
        product.setStock(newStock);
        product.setDateStock(new Date());
        product.setUnit(unit);

        // Save the updated product back to the database
        productRepository.save(product);
    }

    public void updateProduct(ProductRequest updatedProduct, MultipartFile imageFile,UUID id) {
        Product existingProduct = productRepository.findById(id).orElseThrow();
//        Product existingProduct = productRepository.findByName(updatedProduct.getName()).orElseThrow();
        existingProduct.setId(id);
//        Unit unit = unitRepository.findById(updatedProduct.getUnitId()).get();
//        existingProduct.setUnit(unit);
        Category category = categoryRepository.findById(updatedProduct.getCategoryId()).get();
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setLastModifiedDateTime(new Date());
        existingProduct.setRequireProduct(updatedProduct.getRequireProduct());
        if(!imageFile.isEmpty()) {
            try {
                byte[] imageBytes = imageFile.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                existingProduct.setImageFile(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
        }

    }
        existingProduct.setCategory(category);

        productRepository.save(existingProduct);
}
    public boolean productNameIsExisted(ProductRequest productRequest) {
        Optional<Product> existingProduct = productRepository.findByName(productRequest.getName());
        return existingProduct.isPresent();
    }
    public boolean isProductNameUnique(String name) {
        Optional<Product> productsWithSameName = productRepository.findByName(name);
        return productsWithSameName.isEmpty();
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

}

