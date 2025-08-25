package neocamp_teamcubation.projeto_final.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void enviarMensagem(String topico, String mensagem) {
        kafkaTemplate.send(topico, mensagem);
    }
}
