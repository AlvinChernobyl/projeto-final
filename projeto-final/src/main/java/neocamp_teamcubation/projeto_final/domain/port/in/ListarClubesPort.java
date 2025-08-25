package neocamp_teamcubation.projeto_final.domain.port.in;

import neocamp_teamcubation.projeto_final.entity.Clube;
import java.util.List;

public interface ListarClubesPort {
    List<Clube> todos(Boolean ativo);
}
