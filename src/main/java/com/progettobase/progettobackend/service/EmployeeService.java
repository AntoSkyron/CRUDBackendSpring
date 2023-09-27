package com.progettobase.progettobackend.service;

import com.progettobase.progettobackend.entity.Employee;

public interface EmployeeService {
    public void deleteEmployee(Long id);
    public void aggiungiEmployee(Employee e);
    public Employee updateEmployee(Employee e);
    public Employee findEmployeeById(Long id);

}
