package br.edu.ifpi.ads.redesocial.service;

import br.edu.ifpi.ads.redesocial.domain.User;
import br.edu.ifpi.ads.redesocial.dto.ChangePasswordWrapper;
import br.edu.ifpi.ads.redesocial.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username);
    }

    public User findByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User addRefreshTokenToUser(User user, String refreshToken){
        String token = JWT.require(Algorithm.HMAC256("secret".getBytes()))
                .build()
                .verify(refreshToken)
                .getToken();

        log.info("calling addRefreshTokenToUser({})", token);

        user.setRefreshToken(token);
        return userRepository.save(user);
    }

    public void changePassword(HttpServletRequest request, ChangePasswordWrapper changePasswordWrapper) {
        try{
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

            String authorizationHeader = request.getHeader("Authorization");
            String accessToken = authorizationHeader.replace("Bearer ", "");

            DecodedJWT verify = JWT.require(Algorithm.HMAC256("secret".getBytes()))
                    .build()
                    .verify(accessToken);

            String subject = verify.getSubject();

            User user = findByUsername(subject);
            user.setPassword(passwordEncoder.encode(changePasswordWrapper.getPassword()));
            userRepository.save(user);

        }catch(Exception exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }
}
