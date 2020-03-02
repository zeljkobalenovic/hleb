package baki.api.dto;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import baki.api.validator.annotation.FieldsMatch;
import baki.api.validator.annotation.RoleNamePresent;
import lombok.Data;

/**
 * SignUpReqDto
 */

@Data
// maksimalno komplikovana custom validacija za vezbu
@FieldsMatch.List({
    @FieldsMatch(field = "email" , fieldMatch = "confirmEmail" , message = "Email addresses do not match!"),
    @FieldsMatch(field = "password" , fieldMatch = "confirmPassword" , message = "Passwords do not match!")
})
public class SignUpReqDto {
    @NotBlank
    @Size(min = 6,max = 50)
    private String username;
    
    @NotBlank
    @Size(min = 6,max = 50)
    @Email
    private String email;
    
    @NotBlank
    @Size(min = 6,max = 50)
    @Email
    private String confirmEmail;
    
    @NotBlank
    @Size(min = 6,max = 50)
    private String password;
      
    @NotBlank
    @Size(min = 6,max = 50)
    private String confirmPassword;
    
    
    // jednostavna custom validacija
    @RoleNamePresent
    @NotEmpty
    private Set<String> roles;       
}