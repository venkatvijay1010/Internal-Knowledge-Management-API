package org.godigit.ikm.repository;
import org.godigit.ikm.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface DepartmentRepository extends JpaRepository<Department, Long> {
  Optional<Department> findByCode(String code);
}
