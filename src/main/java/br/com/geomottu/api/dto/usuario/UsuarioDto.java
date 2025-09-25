package br.com.geomottu.api.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioDto(
        @NotBlank(message = "O nome não pode estar vazio")
        @Size(max = 25, message = "O máximo de caracteres do nome é 25")
        String nome,
        @NotBlank(message = "A senha não pode ser nula")
        @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
        String senha,
        @NotNull(message = "O tipo de perfil não pode ser nulo")
        Integer tipoPerfil,
        Long filialId
) {
}
