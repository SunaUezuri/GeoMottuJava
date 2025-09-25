package br.com.geomottu.api.dto.usuario;

import br.com.geomottu.api.annotations.interfaces.Unique;
import br.com.geomottu.api.model.entities.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioDto(
        @NotBlank(message = "O nome não pode estar vazio")
        @Size(max = 25, message = "O máximo de caracteres do nome é 25")
        @Unique(fieldName = "nome", domainClass = Usuario.class, message = "Usuário já cadastrado")
        String nome,
        Integer tipoPerfil,
        @NotBlank(message = "A senha não pode ser nula")
        @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
        String senha,
        Long filialId
) {
}
