package com.progettobase.progettobackend.service;

import com.progettobase.progettobackend.entity.Employee;
import com.progettobase.progettobackend.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @Transactional
    public void aggiungiEmployee(Employee e) {
        employeeRepository.save(e);
    }

    @Transactional
    public Employee updateEmployee(Employee e) {
        return employeeRepository.save(e);
    }


    @Transactional
    public Employee findEmployeeById(Long id) {
        return employeeRepository.findEmployeeById(id);
    }



}
