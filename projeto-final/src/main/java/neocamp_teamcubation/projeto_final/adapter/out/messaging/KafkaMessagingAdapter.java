package neocamp_teamcubation.projeto_final.adapter.out.messaging;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.domain.port.out.MessagingPort;
import neocamp_teamcubation.projeto_final.entity.Partida;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaMessagingAdapter implements MessagingPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publicarPartidaCriada(Partida partida) {
        try {
            String json = objectMapper.writeValueAsString(partida);
            kafkaTemplate.send("partidas-criadas", json);
        } catch (Exception e) {
            System.err.println("Falha ao publicar evento de partida criada: " + e.getMessage());
        }
    }
}
