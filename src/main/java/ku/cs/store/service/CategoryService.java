package ku.cs.store.service;

import ku.cs.store.entity.Category;
import ku.cs.store.entity.Product;
import ku.cs.store.entity.Unit;
import ku.cs.store.model.ProductRequest;
import ku.cs.store.repository.CategoryRepository;
import ku.cs.store.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class CategoryService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    @Autowired
    private ModelMapper modelMapper;


    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }


    public void createCategory(Category category) {
        Category record = modelMapper.map(category, Category.class);
        categoryRepository.save(record);
    }
    public boolean categoryNameIsExisted(String categoryName) {
        Optional<Category> existingCategory = categoryRepository.findByCategoryName(categoryName);
        return existingCategory.isPresent();
    }

    public Category getOneById(UUID id){return categoryRepository.findById(id).get();}

    public void editCategory(UUID cId,Category updateCategory, String username) {
        Category existingCategory = categoryRepository.findById(cId).orElse(null);
        existingCategory.setCategoryName(updateCategory.getCategoryName());
        categoryRepository.save(existingCategory);
    }

    public void deleteCategoryById(UUID id) {
        // ตรวจสอบว่าข้อมูลในตาราง product มีการอ้างอิงถึงประเภทที่กำลังจะลบหรือไม่
        List<Product> products = productRepository.findByCategoryId(id);
        if (!products.isEmpty()) {
            // ข้อมูลในตาราง product มีการอ้างอิงถึงประเภทที่กำลังจะลบ
            // ไม่สามารถลบข้อมูลได้
            return;
        }
        // ลบข้อมูลในตาราง category
        categoryRepository.deleteById(id);
    }

}
