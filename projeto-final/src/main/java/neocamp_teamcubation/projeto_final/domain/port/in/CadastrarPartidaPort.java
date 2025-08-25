package neocamp_teamcubation.projeto_final.domain.port.in;
import neocamp_teamcubation.projeto_final.entity.Partida;

public interface CadastrarPartidaPort {
    Partida executar(Partida partida);
}
