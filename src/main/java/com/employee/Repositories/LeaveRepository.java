package com.employee.Repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employee.Entities.Leave;
import com.employee.Entities.Organization;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    List<Leave> findByOrganization(Organization org);

}