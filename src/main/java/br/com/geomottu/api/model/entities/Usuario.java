package br.com.geomottu.api.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TAB_GEOMOTTU_USUARIO")
@SequenceGenerator(name = "user", sequenceName = "SQ_TAB_GEOMOTTU_USUARIO", allocationSize = 1)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @Column(name = "id_usuario")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user")
    private Long id;

    @Column(name = "nm_usuario", unique = true, nullable = false, length = 25)
    private String nome;

    @Column(name = "ds_senha", nullable = false, length = 100)
    private String senha;

    @Column(name = "tp_perfil", nullable = false, precision = 1)
    private Integer tipoPerfil;

    @ManyToOne
    @JoinColumn(name = "id_filial")
    private Filial filial;
}
