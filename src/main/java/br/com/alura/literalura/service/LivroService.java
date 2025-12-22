package br.com.alura.literalura.service;

import br.com.alura.literalura.model.*;
import br.com.alura.literalura.model.dto.DadosLivro;
import br.com.alura.literalura.model.dto.GutendexResponse;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class LivroService {

    private static final String ENDERECO =
            "https://gutendex.com/books?search=";

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final ConsumoApi consumo;
    private final ConverteDados conversor;

    public LivroService(
            LivroRepository livroRepository,
            AutorRepository autorRepository,
            ConsumoApi consumo,
            ConverteDados conversor
    ) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.consumo = consumo;
        this.conversor = conversor;
    }

    /* ================= BUSCAR NA API ================= */

    public List<DadosLivro> buscarLivrosPorTitulo(String titulo) {
        String tituloCodificado = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
        String json = consumo.obterDados(ENDERECO + tituloCodificado);

        GutendexResponse resposta =
                conversor.obterDados(json, GutendexResponse.class);

        return resposta.results() != null
                ? resposta.results()
                : Collections.emptyList();
    }


    /* ================= SALVAR LIVRO ================= */

    public void salvarLivroPorId(long id, List<DadosLivro> resultados) {

        DadosLivro dados = resultados.stream()
                .filter(d -> d.id() == id)
                .findFirst()
                .orElse(null);

        if (dados == null) {
            System.out.println("ID inválido.");
            return;
        }

        if (livroRepository.findByIdGutendex(dados.id()).isPresent()) {
            System.out.println("Livro já cadastrado.");
            return;
        }

        Livro livro = converterParaLivro(dados);
        livroRepository.save(livro);

        System.out.println("Livro salvo com sucesso!");
    }

    /* ================= CONSULTAS ================= */

    public List<Livro> listarLivrosRegistrados() {
        return livroRepository.buscarLivrosComAutores();
    }

    public List<Autor> listarAutoresRegistrados() {
        return autorRepository.buscarAutoresComLivros();
    }

    public List<Autor> listarAutoresVivosNoAno(int ano) {
        return autorRepository.findAll()
                .stream()
                .filter(a -> a.getAnoNascimento() != null && a.getAnoNascimento() <= ano)
                .filter(a -> a.getAnoFalecimento() == null || a.getAnoFalecimento() > ano)
                .toList();
    }

    public List<Livro> buscarLivrosPorIdioma(Idioma idioma) {
        return livroRepository.buscarPorIdiomaComAutores(idioma);
    }

    /* ================= CONVERSÃO DTO → ENTITY ================= */

    private Livro converterParaLivro(DadosLivro dados) {

        Livro livro = new Livro();
        livro.setIdGutendex(dados.id());
        livro.setTitulo(dados.titulo());
        livro.setDownloads(dados.downloads());

        if (dados.idiomas() != null && !dados.idiomas().isEmpty()) {
            livro.setIdioma(Idioma.fromString(dados.idiomas().get(0)));
        }

        Set<Autor> autores = new HashSet<>();

        if (dados.autores() != null) {
            for (var dadosAutor : dados.autores()) {

                String nomeFormatado = Autor.formatarNomeAutor(dadosAutor.nome());
                Autor autor = autorRepository
                        .findByNomeAndAnoNascimentoAndAnoFalecimento(
                                nomeFormatado,
                                dadosAutor.anoNascimento(),
                                dadosAutor.anoFalecimento()
                        )
                        .orElseGet(() -> new Autor(dadosAutor));

                autor.getLivros().add(livro);
                autores.add(autor);
            }
        }

        livro.setAutores(autores);
        return livro;
    }
}
