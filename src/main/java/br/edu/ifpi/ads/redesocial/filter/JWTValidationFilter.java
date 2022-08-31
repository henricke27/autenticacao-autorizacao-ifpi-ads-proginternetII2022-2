package br.edu.ifpi.ads.redesocial.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Log4j2
public class JWTValidationFilter extends BasicAuthenticationFilter {

    public JWTValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException, JWTVerificationException {
        String header = request.getHeader(JWTUtil.HEADER);

        if(header == null || !header.startsWith(JWTUtil.TYPE)){
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(JWTUtil.TYPE, "");

        log.info("Token {}", token);
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(token));
        log.info("Authentication from SecurityContextHolder: {}", SecurityContextHolder.getContext().getAuthentication());
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = JWT.require(JWTUtil.SIGN)
                .build()
                .verify(token);

        String subject = decodedJWT.getSubject();
        List<SimpleGrantedAuthority> authorities = decodedJWT.getClaim("authorities").asList(SimpleGrantedAuthority.class);

        if(decodedJWT.getSubject() == null){
            return null;
        }

        return new UsernamePasswordAuthenticationToken(subject, null, authorities);
    }
}
