package com.parameta.parameta_test.service;

import com.parameta.parameta_test.entity.Employee;
import com.parameta.parameta_test.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<Employee> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Employee> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public Employee save(Employee employee) {
        return repository.save(employee);
    }

    @Override
    public Optional<Employee> update(Long id, Employee employee) {
        Optional<Employee> employeeOptional = repository.findById(id);
        if (employeeOptional.isPresent()) {
            Employee employeeDb = employeeOptional.orElseThrow();

            employeeDb.setName(employee.getName());
            employeeDb.setLastName(employee.getLastName());
            employeeDb.setDocumentType(employee.getDocumentType());
            employeeDb.setDocumentNumber(employee.getDocumentNumber());
            employeeDb.setBirthDate(employee.getBirthDate());
            employeeDb.setJoinDate(employee.getJoinDate());
            employeeDb.setRole(employee.getRole());
            employeeDb.setSalary(employee.getSalary());

            return Optional.of(repository.save(employeeDb));
        }
        return employeeOptional;
    }

    @Transactional
    @Override
    public Optional<Employee> delete(Long id) {
        Optional<Employee> employeeOptional = repository.findById(id);
        employeeOptional.ifPresent(employeeDb -> {
            repository.delete(employeeDb);
        });
        return employeeOptional;
    }
}
