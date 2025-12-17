package br.com.alura.literalura.service;

import br.com.alura.literalura.repository.IConverteDados;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverteDados implements IConverteDados {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JacksonException e) {
            throw new RuntimeException("Erro ao converter JSON para " + classe.getSimpleName(), e);
        }
    }
}

