package escala_plantoes.com.example.demo.domain.plantao;

import escala_plantoes.com.example.demo.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatePlantaoException extends BusinessException {

    public DuplicatePlantaoException() {
        super("Plantao already exists for this professional, turno and data");
    }
}
