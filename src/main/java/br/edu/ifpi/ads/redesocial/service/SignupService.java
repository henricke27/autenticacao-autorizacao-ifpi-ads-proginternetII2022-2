package br.edu.ifpi.ads.redesocial.service;

import br.edu.ifpi.ads.redesocial.domain.User;
import br.edu.ifpi.ads.redesocial.dto.UserDto;
import br.edu.ifpi.ads.redesocial.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserRepository userRepository;
    public void signup(UserDto userDto) {
        User userFind = userRepository.findByUsername(userDto.getUsername());

        Optional.ofNullable(userFind).ifPresent((user) -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, user.getUsername() + " já está cadastrado");
        });

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .authorities("ROLE_USER")
                .build();

        userRepository.save(user);
    }
}
