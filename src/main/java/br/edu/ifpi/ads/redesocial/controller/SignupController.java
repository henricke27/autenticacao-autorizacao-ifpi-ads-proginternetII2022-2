package br.edu.ifpi.ads.redesocial.controller;

import br.edu.ifpi.ads.redesocial.dto.UserDto;
import br.edu.ifpi.ads.redesocial.service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {

    private final SignupService signupService;

    @PostMapping
    @Operation(
            summary = "Cadastra um usuário autenticável.",
            description = "Este método recebe os dados cadastrais do usuário em JSON, e o persiste no banco de dados.",
            tags = {"Sign up"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Quando o cadastro é efetivado."),
            @ApiResponse(responseCode = "409", description = "Quando o nome de usuário já está cadastrado no banco.")
    })
    public ResponseEntity<Void> signup(@RequestBody @Valid UserDto userDto){
        signupService.signup(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
