package com.parameta.parameta_test.controller;

import com.parameta.parameta_test.entity.Employee;
import com.parameta.parameta_test.model.TimePeriod;
import com.parameta.parameta_test.response.EmployeeResponse;
import com.parameta.parameta_test.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@RestController
@RequestMapping("/api/parameta/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/getEmployee")
    public ResponseEntity<?> getEmployee(@Valid @RequestParam(value = "nombre") String name,
                                         @Valid @RequestParam(value = "apellido") String lastName,
                                         @Valid @RequestParam(value = "tipoDocumento") String docType,
                                         @Valid @RequestParam(value = "numDocumento") String docNumber,
                                         @Valid @RequestParam(value = "fechaNacimiento") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate birthDate,
                                         @Valid @RequestParam(value = "fechaVinculacion") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate joinDate,
                                         @Valid @RequestParam(value = "cargo") String role,
                                         @Valid @RequestParam(value = "salario") double salary) throws NoSuchFieldException {

        Employee employee = createEmployee(name, lastName, docType, docNumber, birthDate, joinDate, role, salary);

        Map<String, String> errors = employee.isValid();
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }


        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setEmployee(employee);
        employeeResponse.setEmploymentDuration(calculateTimeDifference(employee.getJoinDate(), LocalDate.now()));
        employeeResponse.setCurrentAge(calculateTimeDifference(employee.getBirthDate(), LocalDate.now()));
        //TODO: Store employee using SOAP service
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeResponse);
    }

    private Employee createEmployee(String name, String lastName, String docType, String docNumber, LocalDate birthDate,
                                    LocalDate joinDate, String role, double salary) {

        Employee employee = new Employee();
        employee.setName(name);
        employee.setLastName(lastName);
        employee.setDocumentType(docType);
        employee.setDocumentNumber(docNumber);
        employee.setBirthDate(birthDate);
        employee.setJoinDate(joinDate);
        employee.setRole(role);
        employee.setSalary(salary);
        return employee;
    }

    private TimePeriod calculateTimeDifference(LocalDate startDate, LocalDate endDate) {
        Period period = Period.between(startDate, endDate);
        TimePeriod employmentDuration = new TimePeriod();
        employmentDuration.setDay(period.getDays());
        employmentDuration.setMonth(period.getMonths());
        employmentDuration.setYear(period.getYears());
        return employmentDuration;
    }

    @GetMapping
    public List<Employee> getAll() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Long id) {
        Optional<Employee> employeeOptional = employeeService.findById(id);
        if (employeeOptional.isPresent()) {
            return ResponseEntity.ok(employeeOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Employee employee, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.save(employee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Employee employee, BindingResult result, @PathVariable Long id) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        Optional<Employee> employeeOptional = employeeService.update(id, employee);
        if (employeeOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(employeeOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Employee> employeeOptional = employeeService.delete(id);
        if (employeeOptional.isPresent()) {
            return ResponseEntity.ok(employeeOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
