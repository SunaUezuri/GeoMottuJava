package br.com.geomottu.api.model.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Endereco {

    private String estado;
    private String siglaEstado;
    private String cidade;
    private String rua;
}
