package neocamp_teamcubation.projeto_final.queue;

import neocamp_teamcubation.projeto_final.entity.Partida;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

@Component
public class FilaPartida {

    private final Queue<Partida> fila = new LinkedList<>();

    public void adicionar(Partida partida) {
        fila.offer(partida);
    }

    public Partida consumir() {
        return fila.poll();
    }

    public boolean isVazia() {
        return fila.isEmpty();
    }
}
