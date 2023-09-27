package com.progettobase.progettobackend.repository;

import com.progettobase.progettobackend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findEmployeeById(Long id);
    Employee findEmployeeByCognome(String Cognome);
}
