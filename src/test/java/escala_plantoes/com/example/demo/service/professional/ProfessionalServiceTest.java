package escala_plantoes.com.example.demo.service.professional;

import escala_plantoes.com.example.demo.domain.professional.Professional;
import escala_plantoes.com.example.demo.infrastructure.professional.ProfessionalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessionalServiceTest {

    @Mock
    private ProfessionalRepository repository;

    @InjectMocks
    private ProfessionalService service;

    @Test
    void register_delegatesToRepository() {
        Professional professional = new Professional();
        Professional saved = new Professional();
        saved.setId(1L);
        when(repository.save(professional)).thenReturn(saved);

        assertSame(saved, service.register(professional));
        verify(repository).save(professional);
    }

    @Test
    void list_delegatesToRepository() {
        List<Professional> professionals = List.of(new Professional());
        when(repository.findAll()).thenReturn(professionals);

        assertSame(professionals, service.list());
    }

    @Test
    void listByCategory_delegatesToRepository() {
        List<Professional> professionals = List.of(new Professional());
        when(repository.findAllByRegistration_Category("MÉDICO")).thenReturn(professionals);

        assertSame(professionals, service.listByCategory("MÉDICO"));
    }

    @Test
    void findById_returnsProfessional_whenFound() {
        Professional professional = new Professional();
        professional.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(professional));

        assertSame(professional, service.findById(1L));
    }

    @Test
    void findById_throwsIllegalArgumentException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> service.findById(99L));
        assertEquals("Professional not found: 99", ex.getMessage());
    }
}
