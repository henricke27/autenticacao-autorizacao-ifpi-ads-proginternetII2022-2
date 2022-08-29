package br.edu.ifpi.ads.redesocial.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Log4j2
public class JWTValidationFilter extends BasicAuthenticationFilter {

    public JWTValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer")){
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");

        log.info("Token {}", token);
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(token));
        log.info("Authentication from SecurityContextHolder: {}", SecurityContextHolder.getContext().getAuthentication());
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token){
        String subject = JWT.require(Algorithm.HMAC256("secret".getBytes()))
                .build()
                .verify(token)
                .getSubject();

        if(subject == null){
            return null;
        }

        //User.withUsername(subject);

        return new UsernamePasswordAuthenticationToken(subject, null, new ArrayList<>());
    }
}
