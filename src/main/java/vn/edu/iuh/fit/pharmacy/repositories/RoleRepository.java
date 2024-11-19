package vn.edu.iuh.fit.pharmacy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.pharmacy.POJOs.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    public Role findByCode(String code);
}
