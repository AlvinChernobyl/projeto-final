package neocamp_teamcubation.projeto_final.usecase;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "partidas", groupId = "futebol-group")
    public void consumir(String mensagem) {
        System.out.println("📥 Mensagem recebida no tópico 'partidas': " + mensagem);
    }
}
