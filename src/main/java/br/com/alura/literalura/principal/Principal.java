package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.DadosLivro;
import br.com.alura.literalura.model.GutendexResponse;
import br.com.alura.literalura.service.ConsumoApi;
import br.com.alura.literalura.service.ConverteDados;

import java.util.Scanner;

public class Principal {
    private ConsumoApi consumo = new ConsumoApi();

    private ConverteDados conversor = new ConverteDados();

    private Scanner leitura = new Scanner(System.in);

    private final String ENDERECO = "https://gutendex.com/books?search=";

    public void exibeMenu(){
        System.out.println("Digite o nome do livro que deseja buscar");
        var nomeLivro = leitura.nextLine();

        var json = consumo.obterDados(ENDERECO + nomeLivro.replace(" ", "%20"));

        DadosLivro dados = conversor.obterDados(json, DadosLivro.class);

        GutendexResponse resposta = conversor.obterDados(json, GutendexResponse.class);
        System.out.println("INFORMAÇÕES DO RESPOSTA GUTENDEX  " + resposta);
    }
}
