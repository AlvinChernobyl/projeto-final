package neocamp_teamcubation.projeto_final.domain.port.in;

import neocamp_teamcubation.projeto_final.entity.Clube;

public interface BuscarClubePort {
    Clube porId(Long id);
}
