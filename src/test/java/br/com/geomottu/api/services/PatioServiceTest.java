package br.com.geomottu.api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import br.com.geomottu.api.config.security.SecurityUtils;
import br.com.geomottu.api.dto.patio.PatioDto;
import br.com.geomottu.api.dto.patio.PatioOcupacaoDto;
import br.com.geomottu.api.exceptions.IdNaoEncontradoException;
import br.com.geomottu.api.model.entities.Filial;
import br.com.geomottu.api.model.entities.Patio;
import br.com.geomottu.api.model.entities.Usuario;
import br.com.geomottu.api.repository.FilialRepository;
import br.com.geomottu.api.repository.PatioRepository;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PatioServiceTest {

    @Mock
    private PatioRepository patioRepository;

    @Mock
    private FilialRepository filialRepository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private PatioService patioService;

    private Usuario usuarioAdmin;
    private Usuario usuarioComum;
    private Filial filialAdmin;
    private Filial filialComum;
    private Patio patio;
    private PatioDto patioDto;

    @BeforeEach
    public void setUp() {
        filialAdmin = new Filial();
        filialAdmin.setId(1L);
        filialAdmin.setNome("Filial Admin");
        filialAdmin.setPatios(new ArrayList<>());

        usuarioAdmin = new Usuario();
        usuarioAdmin.setNome("admin");
        usuarioAdmin.setTipoPerfil(1);
        usuarioAdmin.setFilial(filialAdmin);

        filialComum = new Filial();
        filialComum.setId(2L);
        filialComum.setNome("Filial Comum");

        usuarioComum = new Usuario();
        usuarioComum.setNome("user");
        usuarioComum.setTipoPerfil(2);
        usuarioComum.setFilial(filialComum);

        patioDto = new PatioDto("Patio Central", 100, 1L);

        patio = new Patio(patioDto);
        patio.setId(10L);
        patio.setFilial(filialAdmin);
        patio.setMotos(new ArrayList<>());
    }

    @Test
    public void deveSalvarPatioComoAdmin() throws IdNaoEncontradoException {
        when(securityUtils.getUsuarioLogado()).thenReturn(usuarioAdmin);
        when(securityUtils.isAdmin(usuarioAdmin)).thenReturn(true);
        when(filialRepository.findById(1L)).thenReturn(Optional.of(filialAdmin));
        when(patioRepository.save(any(Patio.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0, Patio.class);
        });

        Patio patioSalvo = patioService.save(patioDto);

        assertNotNull(patioSalvo);
        assertEquals("Patio Central", patioSalvo.getNome());
        assertEquals(filialAdmin, patioSalvo.getFilial());
    }

    @Test
    public void deveSalvarPatioComoUsuarioComum() throws IdNaoEncontradoException {
        PatioDto dtoUsuario = new PatioDto("Patio do User", 50, 99L);
        when(securityUtils.getUsuarioLogado()).thenReturn(usuarioComum);
        when(securityUtils.isAdmin(usuarioComum)).thenReturn(false);
        when(patioRepository.save(any(Patio.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0, Patio.class);
        });

        Patio patioSalvo = patioService.save(dtoUsuario);

        assertNotNull(patioSalvo);
        assertEquals("Patio do User", patioSalvo.getNome());
        assertEquals(filialComum, patioSalvo.getFilial());
    }

    @Test
    public void deveLancarExcecaoAoSalvarAdminComFilialInexistente() {
        when(securityUtils.getUsuarioLogado()).thenReturn(usuarioAdmin);
        when(securityUtils.isAdmin(usuarioAdmin)).thenReturn(true);
        when(filialRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IdNaoEncontradoException.class, () -> {
            patioService.save(patioDto);
        });
    }

    @Test
    public void deveRetornarTodosPatiosParaAdmin() {
        when(securityUtils.getUsuarioLogado()).thenReturn(usuarioAdmin);
        when(securityUtils.isAdmin(usuarioAdmin)).thenReturn(true);
        when(patioRepository.findAll()).thenReturn(List.of(patio, new Patio()));

        List<Patio> patios = patioService.getAll();

        assertEquals(2, patios.size());
        verify(patioRepository).findAll();
        verify(patioRepository, never()).findAllByFilial(any());
    }

    @Test
    public void deveRetornarApenasPatiosDaFilialParaUsuarioComum() {
        when(securityUtils.getUsuarioLogado()).thenReturn(usuarioComum);
        when(securityUtils.isAdmin(usuarioComum)).thenReturn(false);
        when(patioRepository.findAllByFilial(filialComum)).thenReturn(List.of(patio));

        List<Patio> patios = patioService.getAll();

        assertEquals(1, patios.size());
        verify(patioRepository, never()).findAll();
        verify(patioRepository).findAllByFilial(filialComum);
    }


    @Test
    public void deveRetornarPatioPorIdParaAdmin() throws IdNaoEncontradoException {
        when(securityUtils.getUsuarioLogado()).thenReturn(usuarioAdmin);
        when(securityUtils.isAdmin(usuarioAdmin)).thenReturn(true);
        when(patioRepository.findById(10L)).thenReturn(Optional.of(patio));

        Patio patioEncontrado = patioService.getById(10L);

        assertNotNull(patioEncontrado);
        assertEquals(10L, patioEncontrado.getId());
    }

    @Test
    public void deveLancarExcecaoGetByIdParaUsuarioComumSePatioNaoPertencerAFillial() {
        when(securityUtils.getUsuarioLogado()).thenReturn(usuarioComum);
        when(securityUtils.isAdmin(usuarioComum)).thenReturn(false);
        when(patioRepository.findByIdAndFilial(10L, filialComum)).thenReturn(Optional.empty());

        assertThrows(IdNaoEncontradoException.class, () -> {
            patioService.getById(10L);
        });
    }

    @Test
    public void deveAtualizarPatioComoAdminTrocandoAFilial() throws IdNaoEncontradoException {
        PatioDto dtoUpdate = new PatioDto("Patio Atualizado", 200, 2L);

        when(securityUtils.getUsuarioLogado()).thenReturn(usuarioAdmin);
        when(securityUtils.isAdmin(usuarioAdmin)).thenReturn(true);
        when(patioRepository.findById(10L)).thenReturn(Optional.of(patio));

        when(filialRepository.findById(2L)).thenReturn(Optional.of(filialComum));
        when(patioRepository.save(any(Patio.class))).thenAnswer(i -> i.getArgument(0));

        Patio patioAtualizado = patioService.update(10L, dtoUpdate);

        assertEquals("Patio Atualizado", patioAtualizado.getNome());
        assertEquals(200, patioAtualizado.getCapacidadeTotal());
        assertEquals(filialComum, patioAtualizado.getFilial());
    }

    @Test
    public void deveLancarExcecaoAoAtualizarUsuarioComumTentandoMudarFilial() {
        PatioDto dtoUpdate = new PatioDto("Patio Malicioso", 150, 1L);

        patio.setFilial(filialComum);
        when(securityUtils.getUsuarioLogado()).thenReturn(usuarioComum);
        when(securityUtils.isAdmin(usuarioComum)).thenReturn(false);
        when(patioRepository.findByIdAndFilial(10L, filialComum)).thenReturn(Optional.of(patio));

        assertThrows(AccessDeniedException.class, () -> {
            patioService.update(10L, dtoUpdate);
        });
    }

    @Test
    public void deveDeletarPatioVazio() throws IdNaoEncontradoException {
        patio.setMotos(new ArrayList<>());
        filialAdmin.getPatios().add(patio);

        when(securityUtils.getUsuarioLogado()).thenReturn(usuarioAdmin);
        when(securityUtils.isAdmin(usuarioAdmin)).thenReturn(true);
        when(patioRepository.findById(10L)).thenReturn(Optional.of(patio));

        patioService.delete(10L);
        assertTrue(filialAdmin.getPatios().isEmpty());
    }

    @Test
    public void deveLancarExcecaoAoDeletarPatioComMotos() {
        patio.getMotos().add(new br.com.geomottu.api.model.entities.Moto());

        when(securityUtils.getUsuarioLogado()).thenReturn(usuarioAdmin);
        when(securityUtils.isAdmin(usuarioAdmin)).thenReturn(true);
        when(patioRepository.findById(10L)).thenReturn(Optional.of(patio));

        assertThrows(DataIntegrityViolationException.class, () -> {
            patioService.delete(10L);
        });
    }

    @Test
    public void deveContarTotalPatios() {
        when(patioRepository.count()).thenReturn(5L);

        long total = patioService.countTotal();

        assertEquals(5L, total);
        verify(securityUtils).checkAdminAccess();
    }

    @Test
    public void deveRetornarOcupacaoPatios() {
        Pageable pageable = PageRequest.of(0, 5);
        List<PatioOcupacaoDto> listaDto = List.of(
                new PatioOcupacaoDto("Patio A", 10, 10, 100.0)
        );
        when(patioRepository.findPatiosByOcupacao(pageable)).thenReturn(listaDto);

        List<PatioOcupacaoDto> resultado = patioService.getOcupacaoPatios();

        assertFalse(resultado.isEmpty());
        assertEquals(100.0, resultado.get(0).percentualOcupacao());
        verify(securityUtils).checkAdminAccess();
        verify(patioRepository).findPatiosByOcupacao(pageable);
    }
}