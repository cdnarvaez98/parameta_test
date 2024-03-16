package com.parameta.parameta_test.service;

import com.parameta.parameta_test.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<Employee> findAll();

    Optional<Employee> findById(Long id);

    Employee save(Employee employee);

    Optional<Employee> update(Long id, Employee employee);

    Optional<Employee> delete(Long id);
}
