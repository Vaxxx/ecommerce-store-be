package ng.com.createsoftware.ecommercestorebe.api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistrationBody {

    @NotNull
    @NotBlank
    @Size(max=255, min=6)
    private String username;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @Size(min=8)
    @NotNull
    @NotBlank
    //minimum 8 xters, at least 1 letter & 1 number
    //@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    //min 8 xter, at least 1 uppercase, 1 lowercase, 1 number & 1 special xter
    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s:])([^\\s]){8,16}$",
            message = "Minimum 8 characters and must contain Uppercase," +
                    " lowercase letters, numbers and special character")
    private String password;

    @NotNull
    @NotBlank
    @Size(min=2)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(min=2)
    private String lastName;
}
