package com.employee.Sevices;

import java.time.LocalDate;
import java.util.List;

import com.employee.Entities.Attendance;
import com.employee.Entities.Organization;

public interface AttendanceService {

    List<Attendance> getAttendanceByDateAndOrganization(LocalDate date, Organization organization);
    Boolean markAttendance(String empId, String status, LocalDate date, Organization organization);
    public Boolean markOrUpdateAttendance(String empId, String status, LocalDate date, Organization organization);
}
