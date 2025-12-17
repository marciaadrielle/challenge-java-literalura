package br.com.alura.literalura.model;

import java.util.List;

public record GutendexResponse(
        int count,
        String next,
        String previous,
        List<DadosLivro> results
) {}
