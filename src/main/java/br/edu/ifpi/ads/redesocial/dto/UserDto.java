package br.edu.ifpi.ads.redesocial.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    @NotEmpty
    @Size(min = 5, max = 50)
    private String username;

    @NotEmpty
    @Size(min = 5, max = 50)
    private String password;

    @NotEmpty
    @Size(min = 5, max = 50)
    private String name;

}
