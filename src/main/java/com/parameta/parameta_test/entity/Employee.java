package com.parameta.parameta_test.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "tbl_employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @NotBlank(message = "{NotEmpty.employee.name}")
    @Size(min = 3, max = 20)
    @JsonProperty("name")
    private String name;

    @NotBlank
    @Size(min = 3, max = 20)
    @JsonProperty("lastName")
    private String lastName;

    @NotBlank
    @Size(min = 2, max = 3)
    @JsonProperty("documentType")
    private String documentType;

    @NotBlank
    @Size(max = 10)
    @JsonProperty("documentNumber")
    private String documentNumber;

    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonProperty("birthDate")
    private LocalDate birthDate;

    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonProperty("joinDate")
    private LocalDate joinDate;

    @NotBlank
    @JsonProperty("role")
    private String role;

    @NotNull
    @JsonProperty("salary")
    private Double salary;

    public Map<String, String> isValid() throws NoSuchFieldException {

        Map<String, String> errors = new HashMap<>();

        checkMandatoryField(name, "name", errors);
        checkMandatoryField(lastName, "lastName", errors);
        checkMandatoryField(documentType, "documentType", errors);
        checkMandatoryField(documentNumber, "documentNumber", errors);
        checkMandatoryField(birthDate, "birthDate", errors);
        checkMandatoryField(joinDate, "joinDate", errors);
        checkMandatoryField(role, "role", errors);
        checkMandatoryField(salary, "salary", errors);

        //checkDateFormat(birthDate.toString(), errors);

        if (birthDate.isAfter(LocalDate.now())) {
            errors.put("birthDate", "Birth date can't be after the current date");
        }

       if (Period.between(birthDate, LocalDate.now()).getYears() < 18) {
           errors.put("birthDate", "You need to be 18 or older to work with us!");
       }

       return errors;
    }

    private void checkMandatoryField(Object field, String fieldName, Map<String, String> errors) {
        if (field == null) {
            errors.put(fieldName, "Field " + fieldName + " it's mandatory");
        }
        if (field instanceof String && ((String) field).isEmpty()) {
            errors.put(fieldName, "Field " + fieldName + " can't be empty");
        }
    }
}
