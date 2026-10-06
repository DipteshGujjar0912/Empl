package com.employee.ServiceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.employee.DTO.OrganizationDto;
import com.employee.Entities.Organization;
import com.employee.Exception.EmailNotFoundException;
import com.employee.Exception.InvalidPasswordException;
import com.employee.Repositories.OrganizationRepository;
import com.employee.Sevices.OrganizationService;



@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository repo;

    @Override
    public Organization register(OrganizationDto dto) {

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        
        Organization organization = repo.findByEmail(dto.getEmail())
            .orElse(null);
        
        Organization organization2 = repo.findByOrgUniqueId(dto.getOrgUniqueId())
            .orElse(null);

        if (ObjectUtils.isEmpty(organization)) {
            if (ObjectUtils.isEmpty(organization2)) {
                
                Organization org = new Organization();
                org.setOrgName(dto.getOrgName());
                org.setOrgUniqueId(dto.getOrgUniqueId());
                org.setEmail(dto.getEmail());
                org.setContact(dto.getContact());
                org.setType(dto.getType());
                org.setAddress(dto.getAddress());
                org.setPassword(dto.getPassword()); 

                return repo.save(org);
            } else {
                throw new RuntimeException("Organization id already exist !");
            }
        } else {
            throw new RuntimeException("Email already exist !");
        }
    }

    @Override
    public Organization login(String email, String password) {

        System.out.println("Email : "+email);
        System.out.println("Password : "+password);
        
        Organization org = repo.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("Email not registered"));



        if (!password.equals(org.getPassword())) {
            throw new InvalidPasswordException("Incorrect password");
        }
        // if (!org.getPassword().equals(password)) {
        //     throw new InvalidPasswordException("Incorrect password");
        // }

        return org;
    }
}