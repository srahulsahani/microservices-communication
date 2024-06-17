package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "Customer",
        description = "Schema to hold Customer and Account information"
)
public class CustomerDto {

    @Schema(description = "Name of the customer",example = "Eazy Bytes")
    @NotEmpty(message = "Name cannot be a null or empty")
    @Size(min = 5,max = 30,message = "The length of the customer should be between 5 and 30")
    private String name;

    @Schema(description = "Email of the customer",example = "tutor@eazybytes.com")
    @NotEmpty(message = "Email address be a null or empty")
    @Email(message = "Email address should be valid")
    private String email;

    @Schema(description = "Mobile Number of the customer",example = "9876543210")
    @NotEmpty
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile number must be of 10 digit")
    private String mobileNumber;

    @Schema(description = "Account Details of the customer")
    private AccountsDto accountsDto;
}
