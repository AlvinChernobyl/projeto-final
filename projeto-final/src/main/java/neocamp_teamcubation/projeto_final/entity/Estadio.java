package neocamp_teamcubation.projeto_final.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estadios", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estadio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Obrigatório")
    @Size(min = 3, message = "Min 3 letras")
    private String nome;
}
