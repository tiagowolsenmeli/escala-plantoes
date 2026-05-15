package escala_plantoes.com.example.demo.domain.plantao;

import escala_plantoes.com.example.demo.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlantaoNotFoundException extends BusinessException {

    public PlantaoNotFoundException(Long id) {
        super("Plantao not found: " + id);
    }
}
