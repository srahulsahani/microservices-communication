package com.eazybytes.accounts.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDto {

    @NotEmpty(message = "Name cannot be a null or empty")
    @Size(min = 5,max = 30,message = "The length of the customer should be between 5 and 30")
    private String name;

    @NotEmpty(message = "Email address be a null or empty")
    @Email(message = "Email address should be valid")
    private String email;

    @NotEmpty
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile number must be of 10 digit")
    private String mobileNumber;

    private AccountsDto accountsDto;
}
