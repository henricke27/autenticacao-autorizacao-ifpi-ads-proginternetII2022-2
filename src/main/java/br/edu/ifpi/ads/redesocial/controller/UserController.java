package br.edu.ifpi.ads.redesocial.controller;

import br.edu.ifpi.ads.redesocial.domain.User;
import br.edu.ifpi.ads.redesocial.dto.ChangePasswordWrapper;
import br.edu.ifpi.ads.redesocial.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

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

    @GetMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try{
            String authorizationHeader = request.getHeader("Authorization");
            String refreshToken = authorizationHeader.replace("Bearer ", "");

            DecodedJWT verify = JWT.require(Algorithm.HMAC256("secret".getBytes()))
                    .build()
                    .verify(refreshToken);

            String subject = verify.getSubject();

            User user = userService.findByUsername(subject);

            if(!user.getRefreshToken().equals(refreshToken)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "token revogado!");
            }

            String newAccessToken = JWT.create()
                    .withSubject(subject)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                    .sign(Algorithm.HMAC256("secret".getBytes()));

            return new ResponseEntity<Map<String, String>>(
                    Map.of("access-token", newAccessToken), HttpStatus.OK);

        }catch(Exception exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    @PostMapping("/change/password")
    public ResponseEntity<Void> changePassword(HttpServletRequest request ,@RequestBody @Valid ChangePasswordWrapper changePasswordWrapper) {
        userService.changePassword(request, changePasswordWrapper);
        return ResponseEntity.noContent().build();
    }

}
