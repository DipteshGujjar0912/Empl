package com.employee.ServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.Entities.Attendance;
import com.employee.Entities.Organization;
import com.employee.Repositories.AttendanceRepository;
import com.employee.Sevices.AttendanceService;

@Service
public class AttendanceServiceImpl implements AttendanceService{

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public List<Attendance> getAttendanceByDateAndOrganization(LocalDate date, Organization organization) {
        return attendanceRepository.findByDateAndOrganization(date, organization);
    }

    @Override
    public Boolean markOrUpdateAttendance(String empId, String status,
                                        LocalDate date, Organization organization) {

        Optional<Attendance> existing =
                attendanceRepository.findByEmpIdAndDateAndOrganization(empId, date, organization);

        Attendance attendance;

        if (existing.isPresent()) {
            // 🔥 UPDATE
            attendance = existing.get();
            attendance.setStatus(status);
        } else {
            // 🔥 INSERT
            attendance = new Attendance();
            attendance.setEmpId(empId);
            attendance.setDate(date);
            attendance.setOrganization(organization);
            attendance.setStatus(status);
        }

        attendanceRepository.save(attendance);
        return true;
    }

    @Override
    public Boolean markAttendance(String empId, String status, LocalDate date, Organization organization) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'markAttendance'");
    }

}
