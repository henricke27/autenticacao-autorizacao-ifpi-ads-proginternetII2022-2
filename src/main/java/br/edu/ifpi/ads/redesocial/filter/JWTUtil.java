package br.edu.ifpi.ads.redesocial.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


public class JWTUtil {

    public static final String HEADER = "Authorization";
    public static final String TYPE = "Bearer ";
    public static final String SECRET = "secret";
    public static final Algorithm SIGN = Algorithm.HMAC256(SECRET.getBytes());
    public static final Date ACCESS_EXP = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
    public static final Date REFRESH_EXP = new Date(System.currentTimeMillis() + 30 * 60 * 1000);

}
