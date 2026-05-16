package escala_plantoes.com.example.demo.domain.plantao;

import escala_plantoes.com.example.demo.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class CargaHorariaExceededException extends BusinessException {

    public CargaHorariaExceededException(int horasUsadas, int horasNovoPlantao, int cargaHoraria) {
        super(String.format(
            "Carga horária excedida: o profissional já possui %dh nos 7 dias em torno da data, " +
            "e o novo plantão adicionaria mais %dh, ultrapassando o limite de %dh",
            horasUsadas, horasNovoPlantao, cargaHoraria
        ));
    }
}
