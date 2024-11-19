package vn.edu.iuh.fit.pharmacy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.pharmacy.POJOs.MedicineImage;

public interface MedicineImageRepository extends JpaRepository<MedicineImage, Long> {
}