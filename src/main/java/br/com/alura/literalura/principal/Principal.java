package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.DadosLivro;
import br.com.alura.literalura.model.GutendexResponse;
import br.com.alura.literalura.service.ConsumoApi;
import br.com.alura.literalura.service.ConverteDados;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class Principal {
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private Scanner leitura = new Scanner(System.in);

    private final String ENDERECO = "https://gutendex.com/books?search=";


    private List<DadosLivro> livrosBuscados = new ArrayList<>();

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = ("""
                    ----------------------------------
                                 MENU
                    ----------------------------------
                    1 - Buscar livros por título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Lisar autores vivos em determinado ano
                    5 - Buscar livros por idioma
                    
                    0 - Sair
                    
                    """);

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivroPorTitulo();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosAno();
                    break;
                case 5:
                    buscarLivrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Encerrando a aplicação.");
                    break;
                default:
                    System.out.println("Opção inválida!");

            }
        }
    }

    private void buscarLivroPorTitulo() {
        System.out.println("Digite o nome do livro que deseja buscar: ");
        var nomeLivro = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeLivro.replace(" ","%20").toLowerCase());
        var resposta = conversor.obterDados(json, GutendexResponse.class);

        if (resposta.results() != null && !resposta.results().isEmpty()){
        DadosLivro dados = resposta.results().get(0);
        System.out.println(dados);
        } else {
            System.out.println("Livro não encontrado");
        }


    }

    private void listarLivrosRegistrados() {
    }

    private void listarAutoresRegistrados() {
    }

    private void listarAutoresVivosAno() {
    }

    private void buscarLivrosPorIdioma() {
    }
}