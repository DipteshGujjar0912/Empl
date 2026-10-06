package com.employee.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employee.Entities.Employee;
import com.employee.Entities.Organization;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

    List<Employee> findByOrganization(Organization organization);

    Employee findByOrganizationAndEmail(Organization organization, String email);

    Employee findByOrganizationAndEmpId(Organization organization, String empId);

}
