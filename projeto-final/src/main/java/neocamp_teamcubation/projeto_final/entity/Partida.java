package neocamp_teamcubation.projeto_final.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "partidas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mandante_id", nullable = false)
    private Clube mandante;

    @ManyToOne
    @JoinColumn(name = "visitante_id", nullable = false)
    private Clube visitante;

    @ManyToOne
    @JoinColumn(name = "estadio_id", nullable = false)
    private Estadio estadio;

    @NotNull(message = "data hora obrigatoria")
    private LocalDateTime dataHora;

    @Min(value = 0, message = "Gols n pode ser negativo")
    private int golsMandante;

    @Min(value = 0, message = "Gols n pode ser negativo")
    private int golsVisitante;





}
