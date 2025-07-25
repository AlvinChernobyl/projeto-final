package neocamp_teamcubation.projeto_final.service;

import neocamp_teamcubation.projeto_final.DTO.ViaCepDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCepService {

    private final RestTemplate restTemplate = new RestTemplate();

    public ViaCepDTO consultarCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        ViaCepDTO response = restTemplate.getForObject(url, ViaCepDTO.class);

        if (response == null || Boolean.TRUE.equals(response.getErro())) {
            throw new IllegalArgumentException("CEP inválido ou não encontrado");
        }

        return response;
    }
}
