package com.employee.Repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employee.Entities.Attendance;
import com.employee.Entities.Organization;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmpIdAndDateAndOrganization(
        String empId, LocalDate date, Organization organization);
    List<Attendance> findByDateAndOrganization(LocalDate date, Organization organization);
}
