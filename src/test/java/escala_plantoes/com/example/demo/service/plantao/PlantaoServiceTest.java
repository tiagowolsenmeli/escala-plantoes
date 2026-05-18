package escala_plantoes.com.example.demo.service.plantao;

import escala_plantoes.com.example.demo.domain.plantao.Plantao;
import escala_plantoes.com.example.demo.domain.plantao.PlantaoNotFoundException;
import escala_plantoes.com.example.demo.domain.plantao.Turno;
import escala_plantoes.com.example.demo.infrastructure.plantao.PlantaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlantaoServiceTest {

    @Mock
    private PlantaoRepository repository;

    @InjectMocks
    private PlantaoService service;

    private static final LocalDate DATE = LocalDate.of(2026, 6, 1);

    @Test
    void register_delegatesToRepository() {
        Plantao plantao = new Plantao();
        Plantao saved = new Plantao();
        saved.setId(1L);
        when(repository.save(plantao)).thenReturn(saved);

        assertSame(saved, service.register(plantao));
        verify(repository).save(plantao);
    }

    @Test
    void existsByProfessionalAndTurnoAndData_delegatesToRepository() {
        when(repository.existsByProfessional_IdAndTurnoAndData(1L, Turno.MANHA, DATE)).thenReturn(true);

        assertTrue(service.existsByProfessionalAndTurnoAndData(1L, Turno.MANHA, DATE));
    }

    @Test
    void listByPeriod_delegatesToRepository() {
        List<Plantao> plantoes = List.of(new Plantao());
        when(repository.findAllByDataBetweenOrderByDataAscTurnoAsc(DATE, DATE.plusDays(6))).thenReturn(plantoes);

        assertSame(plantoes, service.listByPeriod(DATE, DATE.plusDays(6)));
    }

    @Test
    void listByProfessionalAndPeriod_delegatesToRepository() {
        List<Plantao> plantoes = List.of(new Plantao());
        when(repository.findAllByProfessional_IdAndDataBetween(1L, DATE, DATE.plusDays(3))).thenReturn(plantoes);

        assertSame(plantoes, service.listByProfessionalAndPeriod(1L, DATE, DATE.plusDays(3)));
    }

    @Test
    void delete_callsDeleteById_whenExists() {
        when(repository.existsById(5L)).thenReturn(true);

        service.delete(5L);

        verify(repository).deleteById(5L);
    }

    @Test
    void delete_throwsPlantaoNotFoundException_whenNotExists() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(PlantaoNotFoundException.class, () -> service.delete(99L));
        verify(repository, never()).deleteById(any());
    }
}
