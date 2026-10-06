package com.employee.DTO;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDto {

    @NotBlank(message = "Organization name is required")
    private String orgName;

    @NotBlank(message = "Organization ID is required")
    @Size(min = 6, max = 6)
    private String orgUniqueId;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Contact number is required")
    @Size(max = 10, min = 10)
    private String contact;

    @NotBlank(message = "Organization type is required")
    private String type;

    @NotBlank(message = "Address is required")
    private String address;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

}