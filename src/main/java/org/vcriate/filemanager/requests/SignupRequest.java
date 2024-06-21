package org.vcriate.filemanager.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    @NotBlank(message = "Email is required..")
    @Email(message = "Provide valid Email..")
    private String email;

    private String username;
    private String password;
}
