package baki.api.service.jwtservice;



/**
 * JwtProperties
 */
public class JwtProperties {

    public static final String SECRET = "HlebAppSecretHlebAppSecretHlebAppSecret";
    public static final int EXPIRATION_TIME = 1000*60; // 1 sat
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}