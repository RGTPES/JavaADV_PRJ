package dao.dao;

import model.Category;
import java.util.List;

public interface CategoryDAO {
    List<Category> findAll();

    Category findById(int id);

    boolean existsById(int id);

    boolean existsByName(String categoryName);

    boolean insert(Category category);

    boolean update(Category category);

    boolean softDelete(int id);

    boolean existsByNameAndNotId(String categoryName, int id);
}
