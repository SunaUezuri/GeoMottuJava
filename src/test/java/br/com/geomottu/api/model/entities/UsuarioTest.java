package br.com.geomottu.api.model.entities;

import br.com.geomottu.api.dto.usuario.UsuarioDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario();
    }

    @Test
    public void deveRetornarRoleAdminParaTipoPerfil1() {
        usuario.setTipoPerfil(1);

        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    public void deveRetornarRoleUserParaTipoPerfil2() {
        usuario.setTipoPerfil(2);
        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void deveRetornarUsuarioESenhaCorretos() {
        usuario.setNome("usuario.teste");
        usuario.setSenha("senha123");

        String username = usuario.getUsername();
        String password = usuario.getPassword();

        assertEquals("usuario.teste", username);
        assertEquals("senha123", password);
    }


    @Test
    public void devePassarERecuperarDadosCorretamenteViaGettersESetters() {
        Filial filialMock = new Filial();

        usuario.setId(10L);
        usuario.setNome("teste.setter");
        usuario.setSenha("senhaSetter");
        usuario.setTipoPerfil(2);
        usuario.setFilial(filialMock);

        assertEquals(10L, usuario.getId());
        assertEquals("teste.setter", usuario.getNome());
        assertEquals("senhaSetter", usuario.getSenha());
        assertEquals(2, usuario.getTipoPerfil());
        assertEquals(filialMock, usuario.getFilial());
    }

    @Test
    public void deveInstanciarCorretamenteComConstrutorDeTodosOsArgumentos() {
        Filial filialMock = new Filial();

        Usuario usuarioCompleto = new Usuario(
                99L,
                "usuario.allargs",
                "senhaAllArgs",
                1,
                filialMock
        );

        assertEquals(99L, usuarioCompleto.getId());
        assertEquals("usuario.allargs", usuarioCompleto.getUsername());
        assertEquals("senhaAllArgs", usuarioCompleto.getPassword());
        assertEquals(1, usuarioCompleto.getTipoPerfil());
        assertEquals(filialMock, usuarioCompleto.getFilial());
        assertTrue(usuarioCompleto.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    public void deveInstanciarCOrretamenteAPartirDeUmDTO() {
        UsuarioDto dto = new UsuarioDto("usuario.dto", 2, "senhaDto", 1L);
        Filial filialMock = new Filial();

        Usuario usuarioDto = new Usuario(dto, filialMock);

        assertEquals("usuario.dto", usuarioDto.getNome());
        assertEquals("senhaDto", usuarioDto.getSenha());
        assertEquals(2, usuarioDto.getTipoPerfil());
        assertEquals(filialMock, usuarioDto.getFilial());
        assertTrue(usuarioDto.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }
}