package neocamp_teamcubation.projeto_final.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClubeCepDTO {
    private String nome;
    private String cep;
    private LocalDate dataCriacao;
    private boolean ativo = true;
}
