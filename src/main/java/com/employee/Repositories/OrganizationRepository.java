package com.employee.Repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employee.Entities.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findByEmail(String email);

    Optional<Organization> findByOrgUniqueId(String orgUniqueId);
}
