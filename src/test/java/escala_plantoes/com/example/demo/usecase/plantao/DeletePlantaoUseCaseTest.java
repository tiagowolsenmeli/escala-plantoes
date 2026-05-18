package escala_plantoes.com.example.demo.usecase.plantao;

import escala_plantoes.com.example.demo.domain.plantao.PlantaoNotFoundException;
import escala_plantoes.com.example.demo.service.plantao.PlantaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletePlantaoUseCaseTest {

    @Mock
    private PlantaoService plantaoService;

    @InjectMocks
    private DeletePlantaoUseCase useCase;

    @Test
    void execute_delegatesToService() {
        useCase.execute(42L);

        verify(plantaoService).delete(42L);
    }

    @Test
    void execute_propagatesPlantaoNotFoundException() {
        doThrow(new PlantaoNotFoundException(99L)).when(plantaoService).delete(99L);

        assertThrows(PlantaoNotFoundException.class, () -> useCase.execute(99L));
    }
}
