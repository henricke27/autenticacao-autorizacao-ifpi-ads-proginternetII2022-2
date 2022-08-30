package br.edu.ifpi.ads.redesocial.filter;

import br.edu.ifpi.ads.redesocial.domain.User;
import br.edu.ifpi.ads.redesocial.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withClaim("name",user.getName())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .sign(Algorithm.HMAC256("secret".getBytes()));

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .sign(Algorithm.HMAC256("secret".getBytes()));

        userService.addRefreshTokenToUser(user, refreshToken);

        String tokens = new ObjectMapper().writeValueAsString(
                Map.of("access-token",accessToken, "refresh-token",refreshToken));

        response.getOutputStream().write(tokens.getBytes());
    }
}
