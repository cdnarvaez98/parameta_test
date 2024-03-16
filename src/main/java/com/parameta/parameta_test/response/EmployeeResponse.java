package com.parameta.parameta_test.response;

import com.parameta.parameta_test.entity.Employee;
import com.parameta.parameta_test.model.TimePeriod;
import lombok.Data;

@Data
public class EmployeeResponse {

    private Employee employee;

    private TimePeriod employmentDuration;

    private TimePeriod currentAge;
}
