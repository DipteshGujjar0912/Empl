package com.employee.Sevices;

import java.util.List;

import com.employee.DTO.EmployeeDto;
import com.employee.Entities.Employee;
import com.employee.Entities.Organization;


public interface EmployeeService {

    Employee register(Organization organization ,EmployeeDto dto);
    List<Employee> fondByOrganization(Organization organization);
    void deleteById(Long id);
    Boolean updateEmployee(Employee employee);
}
