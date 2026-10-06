package com.employee.Repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employee.Entities.Organization;
import com.employee.Entities.Payroll;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    List<Payroll> findByMonthAndOrganization(String month, Organization org);
}
