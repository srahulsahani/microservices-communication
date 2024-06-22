package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(
        name = "Accounts",
        description = "Schema to hold Account information"
)
@Data
public class AccountsDto {


    @NotEmpty
    @Schema(description = "Account Number of the customer",example = "1023456789")
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile number must be of 10 digit")
    private Long accountNumber;

    @NotEmpty(message = "AccountType cannot be null or empty")
    @Schema(description = "AccountType of Eazy Bank Account",example = "Savings")
    private String accountType;

    @NotEmpty(message = "branchAddress cannot be null or empty")
    @Schema(description = "Eazy Bank branch address",example = "123 New York")
    private String branchAddress;
}
