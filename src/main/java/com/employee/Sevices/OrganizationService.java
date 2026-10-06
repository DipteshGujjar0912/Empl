package com.employee.Sevices;


import com.employee.DTO.OrganizationDto;
import com.employee.Entities.Organization;



public interface OrganizationService {

    Organization register(OrganizationDto dto);

    Organization login(String email, String password);
}