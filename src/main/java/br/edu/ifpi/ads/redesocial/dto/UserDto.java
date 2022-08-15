package br.edu.ifpi.ads.redesocial.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotEmpty
    @Size(min = 5, max = 50)
    @Schema(title = "Nome de usuário", description = "Nome utilizado para se autenticar", example = "pedro")
    private String username;

    @NotEmpty
    @Size(min = 5, max = 50)
    @Schema(title = "Senha", description = "Palavra-chave utilizada para se autenticar", example = "12345")
    private String password;

    @NotEmpty
    @Size(min = 5, max = 50)
    @Schema(title = "Apelido", description = "Nome que será exibido publicamente", example = "Pedro Henrique")
    private String name;

}
