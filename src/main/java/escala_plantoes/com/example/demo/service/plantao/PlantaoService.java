package escala_plantoes.com.example.demo.service.plantao;

import escala_plantoes.com.example.demo.domain.plantao.Plantao;
import escala_plantoes.com.example.demo.domain.plantao.PlantaoNotFoundException;
import escala_plantoes.com.example.demo.domain.plantao.Turno;
import escala_plantoes.com.example.demo.infrastructure.plantao.PlantaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PlantaoService {

    private final PlantaoRepository repository;

    public PlantaoService(PlantaoRepository repository) {
        this.repository = repository;
    }

    public Plantao register(Plantao plantao) {
        return repository.save(plantao);
    }

    public boolean existsByProfessionalAndTurnoAndData(Long professionalId, Turno turno, LocalDate data) {
        return repository.existsByProfessional_IdAndTurnoAndData(professionalId, turno, data);
    }

    public List<Plantao> listByPeriod(LocalDate start, LocalDate end) {
        return repository.findAllByDataBetweenOrderByDataAscTurnoAsc(start, end);
    }

    public List<Plantao> listByProfessionalAndPeriod(Long professionalId, LocalDate start, LocalDate end) {
        return repository.findAllByProfessional_IdAndDataBetween(professionalId, start, end);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new PlantaoNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
