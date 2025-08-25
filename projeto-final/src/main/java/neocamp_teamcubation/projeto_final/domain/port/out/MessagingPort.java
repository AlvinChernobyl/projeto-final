package neocamp_teamcubation.projeto_final.domain.port.out;

import neocamp_teamcubation.projeto_final.entity.Partida;

public interface MessagingPort {
    void publicarPartidaCriada(Partida partida);
}
