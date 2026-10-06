package com.employee.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.employee.DTO.EmployeeDto;
import com.employee.Entities.Employee;
import com.employee.Entities.Organization;
import com.employee.Repositories.EmployeeRepository;
import com.employee.Sevices.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee register(Organization organization ,EmployeeDto dto) {

        Employee employee = employeeRepository.findByOrganizationAndEmail(organization, dto.getEmail());
        Employee employee2 = employeeRepository.findByOrganizationAndEmpId(organization, dto.getEmpId());

        if (ObjectUtils.isEmpty(employee)) {
            if (ObjectUtils.isEmpty(employee2)) {
                Employee employee3 = Employee.builder()
                    .name(dto.getName())
                    .address(dto.getAddress())
                    .contact(dto.getContact())
                    .department(dto.getDepartment())
                    .email(dto.getEmail())
                    .empId(dto.getEmpId())
                    .joiningDate(dto.getJoiningDate())
                    .organization(organization)
                    .role(dto.getRole())
                    .salary(dto.getSalary())
                    .build();

                return employeeRepository.save(employee3);
            } else {
                throw new RuntimeException("Employee id already exist !");
            }
        } else {
            throw new RuntimeException("Email already exist !");
        }

    }

    @Override
    public List<Employee> fondByOrganization(Organization organization) {
        return employeeRepository.findByOrganization(organization);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Boolean updateEmployee(Employee employee) {
        Employee emp = employeeRepository.findById(employee.getId()).orElse(null);
        if (emp != null) {
            emp.setName(employee.getName());
            emp.setAddress(employee.getAddress());
            emp.setContact(employee.getContact());
            emp.setDepartment(employee.getDepartment());
            emp.setRole(employee.getRole());
            emp.setSalary(employee.getSalary());
            emp.setId(employee.getId());
            emp.setOrganization(employee.getOrganization());
            emp.setJoiningDate(employee.getJoiningDate());
            emp.setEmpId(employee.getEmpId());
            emp.setEmail(employee.getEmail());

            employeeRepository.save(emp);
            return true;
        }
        return false;
    }
}
