package neocamp_teamcubation.projeto_final.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "clubes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nome", "sigla_estado"})
})

@JsonPropertyOrder({
        "id",
        "nome",
        "siglaEstado",
        "dataCriacao",
        "logradouro",
        "bairro",
        "cidade",
        "ativo"
})

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clube {

    private String logradouro;
    private String bairro;
    private String cidade;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Nome obrigatorio")
    @Size(min = 2, message = "Invalido, deve conter duas letras")
    private String nome;

    @Column(name = "sigla_estado")
    @NotBlank(message = "Sigla do estado obrigatoria")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Sigla invalida ex: PA - SC ")
    private String siglaEstado;

    @NotNull(message = "Data obrigatoria")
    @PastOrPresent(message = "Data invalida")
    private LocalDate dataCriacao;

    @NotNull
    private boolean ativo;

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}

