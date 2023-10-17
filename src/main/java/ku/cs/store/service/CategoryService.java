package ku.cs.store.service;

import ku.cs.store.entity.Category;
import ku.cs.store.repository.CategoryRepository;
import ku.cs.store.model.CategoryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryService {


    @Autowired
    private CategoryRepository categoryRepository;


    @Autowired
    private ModelMapper modelMapper;


    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }


    public void createCategory(CategoryRequest category) {
        Category record = modelMapper.map(category, Category.class);
        categoryRepository.save(record);
    }
}
