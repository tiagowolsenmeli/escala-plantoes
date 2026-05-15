package escala_plantoes.com.example.demo.infrastructure.professional;

import escala_plantoes.com.example.demo.domain.professional.Professional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {

    List<Professional> findAllByRegistration_Category(String category);
}
