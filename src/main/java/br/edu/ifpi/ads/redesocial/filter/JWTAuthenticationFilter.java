package br.edu.ifpi.ads.redesocial.filter;

import br.edu.ifpi.ads.redesocial.domain.User;
import br.edu.ifpi.ads.redesocial.service.UserService;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        setFilterProcessesUrl("/signin");
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.info("processando attemptAuth");

        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            Authentication token = new UsernamePasswordAuthenticationToken
                    (user.getUsername(), user.getPassword(), null);
            return authenticationManager.authenticate(token);
        } catch (Exception e) {
            throw new BadCredentialsException("Authentication failed");
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();

        String accessToken = JWT.create()
                .withIssuer(request.getRequestURI())
                .withSubject(user.getUsername())
                .withExpiresAt(JWTUtil.ACCESS_EXP)
                .withClaim("name",user.getName())
                .withClaim("authorities", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(JWTUtil.SIGN);

        String refreshToken = JWT.create()
                .withIssuer(request.getRequestURI())
                .withSubject(user.getUsername())
                .withExpiresAt(JWTUtil.REFRESH_EXP)
                .sign(JWTUtil.SIGN);

        userService.addRefreshTokenToUser(user, refreshToken);

        String tokens = new ObjectMapper().writeValueAsString(
                Map.of("access-token",accessToken, "refresh-token",refreshToken));

        response.getOutputStream().write(tokens.getBytes());
    }
}
