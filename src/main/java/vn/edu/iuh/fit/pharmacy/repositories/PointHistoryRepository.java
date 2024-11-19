package vn.edu.iuh.fit.pharmacy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.pharmacy.POJOs.PointHistory;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}