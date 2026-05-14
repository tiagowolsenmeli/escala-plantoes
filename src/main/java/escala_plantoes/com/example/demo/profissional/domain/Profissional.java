package escala_plantoes.com.example.demo.profissional.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profissional")
@Getter
@Setter
@NoArgsConstructor
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "cadastro_profissional", nullable = false, unique = true)
    private String cadastroProfissional;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "carga_horaria", nullable = false)
    private CargaHoraria cargaHoraria;
}
