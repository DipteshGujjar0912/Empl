package com.employee.Entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orgName;
    private String orgUniqueId;
    private String email;
    private String contact;
    private String type;
    private String address;
    private String password;

    @OneToMany(mappedBy = "organization" ,cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>();

}