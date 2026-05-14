package escala_plantoes.com.example.demo.infrastructure;

import escala_plantoes.com.example.demo.domain.Professional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
}
