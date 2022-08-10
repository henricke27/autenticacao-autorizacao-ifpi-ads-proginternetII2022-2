package br.edu.ifpi.ads.redesocial.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

        return http.build();
    }

    /*@Bean
    public InMemoryUserDetailsManager userDetailsManager(){
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info(passwordEncoder.encode("12345"));

        UserDetails userDetails = User.builder()
                .username("pedro")
                .password(passwordEncoder.encode("12345"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }*/
}
