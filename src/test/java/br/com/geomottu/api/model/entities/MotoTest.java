package br.com.geomottu.api.model.entities;
import br.com.geomottu.api.dto.moto.MotoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import br.com.geomottu.api.model.enums.EstadoMoto;
import br.com.geomottu.api.model.enums.TipoMoto;

public class MotoTest {

    private Moto moto;

    @BeforeEach
    public void setUp() {
        moto = new Moto();
    }

    @Test
    public void deveAtribuirEDevolverValoresCorretamenteComSettersEGetters() {
        Patio patio = new Patio();

        moto.setId(1L);
        moto.setPlaca("ABC1234");
        moto.setChassi("CHASSI123ABC");
        moto.setTipoMoto(TipoMoto.MOTTUPOP);
        moto.setEstadoMoto(EstadoMoto.LIVRE);
        moto.setPatio(patio);

        assertEquals(1L, moto.getId());
        assertEquals("ABC1234", moto.getPlaca());
        assertEquals("CHASSI123ABC", moto.getChassi());
        assertEquals(TipoMoto.MOTTUPOP, moto.getTipoMoto());
        assertEquals(EstadoMoto.LIVRE, moto.getEstadoMoto());
        assertEquals(patio, moto.getPatio());
    }

    @Test
    public void deveInstanciarCorretamenteComConstrutorDeTodosArgumentos() {
        Patio patio = new Patio();

         moto.setId(2L);
         moto.setPlaca("XYZ5678");
         moto.setChassi("CHASSI456XYZ");
         moto.setTipoMoto(TipoMoto.MOTTUE);
         moto.setEstadoMoto(EstadoMoto.LIVRE);
         moto.setPatio(patio);

        // ASSERT
        assertEquals(2L, moto.getId());
        assertEquals("XYZ5678", moto.getPlaca());
        assertEquals("CHASSI456XYZ", moto.getChassi());
        assertEquals(TipoMoto.MOTTUE, moto.getTipoMoto());
        assertEquals(EstadoMoto.LIVRE, moto.getEstadoMoto());
        assertEquals(patio, moto.getPatio());
    }

    @Test
    public void deveInstanciarCorretamenteAPartirDeUmMotoDtoEPatio() {
        // ARRANGE
        // Cria os objetos de entrada (DTO e Patio)
        Patio patio = new Patio();
        MotoDto dto = new MotoDto(
                "DTO987",
                "CHASSIDTO",
                TipoMoto.MOTTUE,
                EstadoMoto.MANUTENCAO,
                1L
        );

        Moto motoDoDto = new Moto(dto, patio);

        assertEquals("DTO987", motoDoDto.getPlaca());
        assertEquals("CHASSIDTO", motoDoDto.getChassi());
        assertEquals(TipoMoto.MOTTUE, motoDoDto.getTipoMoto());
        assertEquals(EstadoMoto.MANUTENCAO, motoDoDto.getEstadoMoto());
        assertEquals(patio, motoDoDto.getPatio());

        assertNull(motoDoDto.getId());
    }
}