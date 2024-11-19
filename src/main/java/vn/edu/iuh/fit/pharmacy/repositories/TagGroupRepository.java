package vn.edu.iuh.fit.pharmacy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.pharmacy.POJOs.TagGroup;

public interface TagGroupRepository extends JpaRepository<TagGroup, Long> {
}