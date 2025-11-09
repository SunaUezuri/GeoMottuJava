package br.com.geomottu.api.model.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.geomottu.api.dto.patio.PatioDto;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PatioTest {

    private Patio patio;

    @BeforeEach
    public void setup() {
        patio = new Patio();
    }

    @Test
    public void deveAtribuirEDevolverValoresCorretamenteComSettersEGetters() {
        Filial filial = new Filial();
        List<Moto> motos = new ArrayList<>();
        patio.setId(1L);
        patio.setNome("Patio Central");
        patio.setCapacidadeTotal(100);
        patio.setFilial(filial);
        patio.setMotos(motos);

        assertEquals(1L, patio.getId());
        assertEquals("Patio Central", patio.getNome());
        assertEquals(100, patio.getCapacidadeTotal());
        assertEquals(filial, patio.getFilial());
        assertEquals(motos, patio.getMotos());
    }

    @Test
    public void deveInstanciarCorretamenteComConstrutorDeTodosArgumentos() {
        Filial filial = new Filial();
        List<Moto> motos = new ArrayList<>();

        Patio patioCompleto = new Patio(
                2L,
                "Patio Anexo",
                50,
                filial,
                motos
        );

        assertEquals(2L, patioCompleto.getId());
        assertEquals("Patio Anexo", patioCompleto.getNome());
        assertEquals(50, patioCompleto.getCapacidadeTotal());
        assertEquals(filial, patioCompleto.getFilial());
        assertEquals(motos, patioCompleto.getMotos());
    }

    @Test
    public void deveInstanciarCorretamenteAPartirDeUmPatioDto() {
        PatioDto dto = new PatioDto("Patio do DTO", 75, 99L);
        Patio patioDoDto = new Patio(dto);

        assertEquals("Patio do DTO", patioDoDto.getNome());
        assertEquals(75, patioDoDto.getCapacidadeTotal());

        assertNull(patioDoDto.getId());
        assertNull(patioDoDto.getFilial());
        assertNull(patioDoDto.getMotos());
    }
}