package br.com.alura.literalura.repository;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
