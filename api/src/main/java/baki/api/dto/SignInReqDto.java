package baki.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * SignInReqDto
 */
@Data
 public class SignInReqDto {
    @NotBlank
    @Size(min = 6,max = 50)
    private String username;
    @NotBlank
    @Size(min = 6,max = 50)
    private String password;
}