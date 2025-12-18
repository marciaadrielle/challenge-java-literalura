package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.*;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books?search=";

    private LivroRepository repositorio;
    private List<Livro> livros = new ArrayList<>();
    private Optional<Livro> livroBusca;

    public Principal(LivroRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = 1;
        while (opcao != 0) {
            var menu = """
                    BEM-VINDO(A) AO LITERALURA
                    POR FAVOR DIGITE A OPÇÃO DESEJADA
                    
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    0 - sair
                    
                    """;
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();
            switch (opcao) {
                case 1:
                    buscarLivroTitulo();
                    break;
                case 2:
                    listarLivros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivosEmDeterminadoAno();
                    break;
                case 5:
                    listarLivrosPoridioma();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }


        }
    }

    private void buscarLivroTitulo() {
        System.out.println("Digite o nome do livro que deseja buscar");
        var nomeLivro = leitura.nextLine();

        var json = consumo.obterDados(ENDERECO + nomeLivro.replace(" ", "%20"));

        GutendexResponse resposta = conversor.obterDados(json, GutendexResponse.class);

        if (resposta.results().isEmpty()) {
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

    private void listarLivros() {
        livros = repositorio.findAll();
        livros.stream()
                .sorted(Comparator.comparing(Livro::getTitulo))
                .forEach(System.out::println);
    }

    private void listarAutores() {
    }

    private void listarAutoresVivosEmDeterminadoAno() {
    }

    private void listarLivrosPoridioma() {
    }
}
