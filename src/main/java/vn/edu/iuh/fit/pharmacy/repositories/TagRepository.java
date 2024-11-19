package vn.edu.iuh.fit.pharmacy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.pharmacy.POJOs.Medicine;
import vn.edu.iuh.fit.pharmacy.POJOs.Tag;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
}