package br.com.alura.literalura;

import br.com.alura.literalura.model.DadosLivro;
import br.com.alura.literalura.model.GutendexResponse;
import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.ConsumoApi;
import br.com.alura.literalura.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    @Autowired
    private LivroRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) {

        ConsumoApi consumoApi = new ConsumoApi();
        String json = consumoApi.obterDados(
                "https://gutendex.com/books?search=dom%20casmurro"
        );

        ConverteDados conversor = new ConverteDados();
        GutendexResponse resposta = conversor.obterDados(json, GutendexResponse.class);

        DadosLivro dadosLivro = resposta.results().get(0);
        Livro livro = new Livro(dadosLivro);

        repository.save(livro);

        System.out.println("Livro salvo com sucesso!");
    }
}
