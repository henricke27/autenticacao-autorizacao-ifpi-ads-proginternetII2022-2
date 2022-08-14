package br.edu.ifpi.ads.redesocial.controller;

import br.edu.ifpi.ads.redesocial.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping(path = "whoami")
    public ResponseEntity<String> whoami(@AuthenticationPrincipal User userDetails){
        return new ResponseEntity<>(userDetails.getName(), HttpStatus.OK);
    }

}
