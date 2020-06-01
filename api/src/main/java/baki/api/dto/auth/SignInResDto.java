package baki.api.dto.auth;

/**
 * SignInResDto
 */
public class SignInResDto {

    private String jwt;

    

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public SignInResDto(String jwt) {
        this.jwt = jwt;
    }

    
}