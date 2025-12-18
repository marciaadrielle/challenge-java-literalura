package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.DadosAutor;
import br.com.alura.literalura.model.DadosLivro;
import br.com.alura.literalura.model.GutendexResponse;
import br.com.alura.literalura.service.ConsumoApi;
import br.com.alura.literalura.service.ConverteDados;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {
    private ConsumoApi consumo = new ConsumoApi();

    private ConverteDados conversor = new ConverteDados();

    private Scanner leitura = new Scanner(System.in);

    private final String ENDERECO = "https://gutendex.com/books?search=";

    public void exibeMenu(){
        System.out.println("Digite o nome do livro que deseja buscar");
        var nomeLivro = leitura.nextLine();

        var json = consumo.obterDados(ENDERECO + nomeLivro.replace(" ", "%20"));

        GutendexResponse resposta = conversor.obterDados(json, GutendexResponse.class);

        if (resposta.results().isEmpty()){
            System.out.println("Livro não encontrado");
            return;
        }

        DadosLivro livro = resposta.results().get(0);
        System.out.println("Título " + livro.titulo());
        String nomesAutores = livro.autores()
                .stream()
                .map(DadosAutor::nome)
                .collect(Collectors.joining(", "));
        System.out.println("Autor(es): " + nomesAutores);
        System.out.println("Idiomas " + livro.idiomas());
        if (livro.resumos() != null && !livro.resumos().isEmpty()) {
            System.out.println("Sinopse: " + livro.resumos().get(0));
        }
    }
}
