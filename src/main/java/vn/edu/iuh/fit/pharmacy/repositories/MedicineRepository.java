package vn.edu.iuh.fit.pharmacy.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.iuh.fit.pharmacy.POJOs.Medicine;
import vn.edu.iuh.fit.pharmacy.POJOs.Tag;

import java.net.ContentHandler;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Optional<Medicine> findBySku(String sku);
    
    @Query("SELECT m FROM Medicine m JOIN m.tags t WHERE t.id = :tagId")
    Page<Medicine> findAllByTagId(Long tagId, Pageable pageable);

    @Query("SELECT m FROM Medicine m JOIN m.tags t WHERE t.id IN :tagIds")
    Page<Medicine> findAllByTagIds(List<Long> tagIds,
                                   Pageable pageable);

    List<Medicine> findByIdIn(List<Long> ids);
}