package service.service;

import model.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    Category getCategoryById(int id);

    String addCategory(String categoryName, String description);

    String updateCategory(int id, String categoryName, String description);

    String deleteCategory(int id);
}
