package br.edu.ifpi.ads.redesocial.controller;

import br.edu.ifpi.ads.redesocial.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping(path = "whoami")
    @Operation(
            summary = "Retorna o nome do usuário autenticado",
            description = "Este método retorna o nome ou alcunha do usuário que está autenticado.",
            tags = {"User resources"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quando retornado o nome é retornado com sucesso.",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "name",
                                    value = "xpto",
                                    description = "Valor de exemplo"
                            )
                    )
            ),
            @ApiResponse(responseCode = "201", description = "Acesso negado. Usuário não autenticado!",
                    content = @Content(
                            schema = @Schema(
                                    hidden = true
                            )
                    )
            )
    })
    public ResponseEntity<String> whoami(@AuthenticationPrincipal User userDetails) {
        return new ResponseEntity<>(userDetails.getName(), HttpStatus.OK);
    }

}
