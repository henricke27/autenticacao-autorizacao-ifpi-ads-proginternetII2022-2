package br.edu.ifpi.ads.redesocial.controller;

import br.edu.ifpi.ads.redesocial.dto.UserDto;
import br.edu.ifpi.ads.redesocial.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    @PostMapping("signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid UserDto userDto){
        signupService.signup(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
