package neocamp_teamcubation.projeto_final.adapter.in.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.entity.Partida;
import neocamp_teamcubation.projeto_final.usecase.PartidaService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartidaConsumer {

    private final PartidaService partidaService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "partidas", groupId = "futebol-group")
    public void consumir(String mensagemJson) {
        try {

            Partida partida = objectMapper.readValue(mensagemJson, Partida.class);

            partidaService.salvarSomente(partida);
            System.out.println("Partida consumida e salva: " + partida);
        } catch (Exception e) {
            System.err.println("Erro ao processar mensagem Kafka: " + e.getMessage());
        }
    }
}