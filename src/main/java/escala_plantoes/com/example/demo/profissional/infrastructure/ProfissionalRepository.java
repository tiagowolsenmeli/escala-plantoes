package escala_plantoes.com.example.demo.profissional.infrastructure;

import escala_plantoes.com.example.demo.profissional.domain.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
}
