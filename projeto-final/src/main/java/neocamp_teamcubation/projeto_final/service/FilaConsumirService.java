package neocamp_teamcubation.projeto_final.service;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.queue.FilaPartida;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilaConsumirService {
    private final FilaPartida filaPartida;
    private final PartidaService partidaService;

    @Scheduled(fixedRate = 60000)
    public void processarFila() {
        while (!filaPartida.isVazia()) {
            var partida = filaPartida.consumir();
            try {
                partidaService.cadastrarPartida(partida);
                System.out.println("Partida salva com sucesso: " + partida);
            } catch (Exception e) {
                System.out.println("Erro ao salvar partida: " + e.getMessage());
            }
        }
    }
}
