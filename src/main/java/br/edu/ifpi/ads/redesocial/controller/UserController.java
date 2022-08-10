package br.edu.ifpi.ads.redesocial.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class UserController {

    @GetMapping(path = "signin")
    public ResponseEntity<String> signIn(@AuthenticationPrincipal UserDetails user){
        log.info(user);
        return new ResponseEntity<>("Autenticado!", HttpStatus.OK);
    }
}
