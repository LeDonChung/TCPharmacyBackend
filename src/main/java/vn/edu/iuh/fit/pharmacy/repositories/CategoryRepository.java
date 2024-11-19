package vn.edu.iuh.fit.pharmacy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.pharmacy.POJOs.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByFullPathSlug(String fullPathSlug);
}