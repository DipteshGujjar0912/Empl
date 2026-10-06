package com.employee.ServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.employee.Entities.Employee;
import com.employee.Entities.Organization;
import com.employee.Entities.Payroll;
import com.employee.Repositories.PayrollRepository;


@Service
public class PayrollService {

    @Autowired
    private PayrollRepository repo;

    public List<Payroll> getByMonth(String month, Organization org) {
        return repo.findByMonthAndOrganization(month, org);
    }

    public void savePayroll(List<String> empId,
                            List<Double> basic,
                            List<Double> allowance,
                            List<Double> deduction,
                            String month,
                            Organization org,
                            List<Employee> employees) {

        for (int i = 0; i < empId.size(); i++) {
            final int index = i;

            Payroll p = new Payroll();

            Employee emp = employees.stream()
                    .filter(e -> e.getEmpId().equals(empId.get(index)))
                    .findFirst().orElse(null);

            p.setEmpId(empId.get(i));
            p.setName(emp.getName());
            p.setDepartment(emp.getDepartment());

            p.setBasicSalary(basic.get(i));
            p.setAllowance(allowance.get(i));
            p.setDeduction(deduction.get(i));

            double net = basic.get(i) + allowance.get(i) - deduction.get(i);
            p.setNetSalary(net);

            p.setMonth(month);
            p.setOrganization(org);

            repo.save(p);
        }
    }
}
