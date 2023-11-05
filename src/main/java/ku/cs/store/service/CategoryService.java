package ku.cs.store.service;

import ku.cs.store.entity.Category;
import ku.cs.store.entity.Unit;
import ku.cs.store.model.ProductRequest;
import ku.cs.store.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class CategoryService {


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
    public boolean categoryNameIsExisted(Category category) {
        Optional<Category> existingCategory = categoryRepository.findByName(category.getName());
        return existingCategory.isPresent();
    }
    public Category getOneById(UUID id){return categoryRepository.findById(id).get();}

    public void editCategory(UUID id, Category updateCategory){
        Category existingCategory = categoryRepository.findById(id).orElseThrow();
        if(!updateCategory.getName().equals(existingCategory.getName())){
            existingCategory.setName(updateCategory.getName());
        }
        categoryRepository.save(existingCategory);
    }
}
