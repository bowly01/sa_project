package ku.cs.store.service;

import ku.cs.store.entity.Product;
import ku.cs.store.entity.Category;
import ku.cs.store.model.ProductRequest;
import ku.cs.store.repository.CategoryRepository;
import ku.cs.store.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    @Autowired
    private ModelMapper modelMapper;
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public Product getOneById(UUID id) {
        return productRepository.findById(id).get();
    }

    public void createProduct(ProductRequest product) {
        Product record = modelMapper.map(product, Product.class);
        Category category =
                categoryRepository.findById(product.getCategoryId()).get();
        record.setCategory(category);
        productRepository.save(record);
    }
}
