package br.com.geomottu.api.model.entities;

import br.com.geomottu.api.model.enums.PaisesFilial;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "TAB_GEOMOTTU_FILIAL")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "filial", sequenceName = "SQ_TAB_GEOMOTTU_FILIAL", allocationSize = 1)
public class Filial {

    @Id @Column(name = "id_filial")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "filial")
    private Long id;

    @Column(name = "nm_filial", nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "pais_filial", nullable = false)
    private PaisesFilial pais;

    @Embedded
    private Endereco endereco;

    @Column(name = "nm_telefone", length = 15)
    private String telefone;

    @Column(name = "ds_email", length = 30)
    private String email;

    @OneToMany(mappedBy = "filial", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "filial", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Patio> patios;

}
