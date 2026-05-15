package escala_plantoes.com.example.demo.infrastructure.plantao;

import escala_plantoes.com.example.demo.domain.plantao.Plantao;
import escala_plantoes.com.example.demo.domain.plantao.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PlantaoRepository extends JpaRepository<Plantao, Long> {

    List<Plantao> findAllByDataBetweenOrderByDataAscTurnoAsc(LocalDate start, LocalDate end);

    boolean existsByProfessional_IdAndTurnoAndData(Long professionalId, Turno turno, LocalDate data);
}
