package br.com.transoft.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @JsonProperty(namespace = "email")
    @Email(message = "The email is not in the right format")
    @NotBlank(message = "The email must be informed.")
    private String email;

    @JsonProperty(namespace = "password")
    @NotBlank(message = "The password must be informed.")
    private String password;

}
