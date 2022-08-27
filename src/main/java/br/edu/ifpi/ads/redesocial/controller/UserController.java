package br.edu.ifpi.ads.redesocial.controller;

import br.edu.ifpi.ads.redesocial.domain.User;
import br.edu.ifpi.ads.redesocial.dto.UserDto;
import br.edu.ifpi.ads.redesocial.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

@Log4j2
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
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
    public ResponseEntity<String> whoami(HttpServletRequest httpServletRequest) {
        String username = httpServletRequest.getUserPrincipal().getName();
        User user = userService.findByUsername(username);
        return new ResponseEntity<>(user.getName(), HttpStatus.OK);
    }

    @GetMapping("/data")
    public ResponseEntity<LocalDateTime> data() throws IOException {
        return ResponseEntity.ok(LocalDateTime.now());
    }
}
