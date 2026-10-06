package com.employee.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

    @NotBlank
    private String name;

    @Size(min = 6, max = 6)
    private String empId;

    @Email
    @NotBlank
    private String email;

    @Size(min = 10, max = 10)
    private String contact;

    @NotBlank
    private String department;

    @NotBlank
    private String role;

    @NotBlank
    private String address;

    @NotBlank
    private String joiningDate;

    @NotNull
    @Positive
    private Double salary;
}
