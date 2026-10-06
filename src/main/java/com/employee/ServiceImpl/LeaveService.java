package com.employee.ServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.employee.Entities.Leave;
import com.employee.Entities.Organization;
import com.employee.Repositories.LeaveRepository;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository repo;

    public List<Leave> getAllLeaves(Organization org) {
        return repo.findByOrganization(org);
    }

    public void applyLeave(Leave leave) {
        leave.setStatus("Pending");
        repo.save(leave);
    }

    public void updateStatus(Long id, String status) {
        Leave leave = repo.findById(id).orElse(null);
        if (leave != null) {
            leave.setStatus(status);
            repo.save(leave);
        }
    }
}
