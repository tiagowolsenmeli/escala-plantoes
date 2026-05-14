package escala_plantoes.com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "professional_registration")
@Getter
@Setter
@NoArgsConstructor
public class ProfessionalRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String type;

    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;
}
