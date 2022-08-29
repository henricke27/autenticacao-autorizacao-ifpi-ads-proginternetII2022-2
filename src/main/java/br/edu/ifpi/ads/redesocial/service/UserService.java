package br.edu.ifpi.ads.redesocial.service;

import br.edu.ifpi.ads.redesocial.domain.User;
import br.edu.ifpi.ads.redesocial.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    public void changePassword(String newPassword) {

    }
}
