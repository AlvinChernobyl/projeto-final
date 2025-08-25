package neocamp_teamcubation.projeto_final.adapter.in.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import neocamp_teamcubation.projeto_final.entity.Partida;
import neocamp_teamcubation.projeto_final.usecase.KafkaProducerService;
import neocamp_teamcubation.projeto_final.usecase.PartidaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fila-partidas")
@RequiredArgsConstructor
public class PartidaKafkaController {

    private final KafkaProducerService producer;
    private final ObjectMapper objectMapper;
    private final PartidaService partidaService;

    @PostMapping
    public ResponseEntity<String> enviarKafka(@RequestBody Partida partida) {
        try {

            String json = objectMapper.writeValueAsString(partida);
            producer.enviarMensagem("partidas", json);
            return ResponseEntity.ok("Mensagem enviada para Kafka");
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Erro ao serializar partida: " + e.getMessage());
        }
    }

    @PostMapping("/direct")
    public ResponseEntity<Partida> criarDireto(@RequestBody Partida partida) {
        Partida salva = partidaService.salvarSomente(partida);
        return ResponseEntity.status(201).body(salva);
    }
}
