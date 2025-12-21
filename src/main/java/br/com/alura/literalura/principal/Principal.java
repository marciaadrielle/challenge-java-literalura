package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.Autor;
import br.com.alura.literalura.model.DadosLivro;
import br.com.alura.literalura.model.GutendexResponse;
import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.repository.*;
import br.com.alura.literalura.service.ConsumoApi;
import br.com.alura.literalura.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class Principal {
    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

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
        var json = consumo.obterDados(ENDERECO + nomeLivro.replace(" ", "%20").toLowerCase());
        var resposta = conversor.obterDados(json, GutendexResponse.class);

        if (resposta.results() != null && !resposta.results().isEmpty()) {
            DadosLivro dados = resposta.results().get(0);

            Livro livro = new Livro(dados);
            livro.getAutores().forEach(a -> a.setLivro(livro));
            livroRepository.save(livro);

            // Pega o primeiro autor (se existir)
            String autor = dados.autores() != null && !dados.autores().isEmpty()
                    ? dados.autores().get(0).nome()
                    : "Autor desconhecido";

            // Pega o idioma (primeiro da lista)
            String idioma = dados.idiomas() != null && !dados.idiomas().isEmpty()
                    ? dados.idiomas().get(0)
                    : "Idioma não informado";

            // Mostra os dados completos
            System.out.printf("Livro encontrado: %s | Autor: %s | Idioma: %s | Downloads: %d%n",
                    dados.titulo(),
                    autor,
                    idioma,
                    dados.downloads() != null ? dados.downloads() : 0);
        } else {
            System.out.println("Livro não encontrado");
        }
    }

        private void listarLivrosRegistrados() {
        List<Livro> livros = livroRepository.findAll();


        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado ainda.");
        } else {
            System.out.println("Livros registrados:");
            for (Livro livro : livros) {
                String autor = livro.getAutores().isEmpty()
                        ? "Autor Desconhecido"
                        : livro.getAutores().get(0).getNome();

                System.out.printf("- %s | Autor: %s | Idioma: %s | Downloads: %d%n",
                        livro.getTitulo(),
                        autor,
                        livro.getIdioma().getDescricao(),
                        livro.getDownloads() != null ? livro.getDownloads() : 0);
            }
        }
    }


    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado ainda.");
        } else {
            System.out.println("Autores cadastrados:");
            for (Autor autor : autores) {
                System.out.printf("- %s | Nascimento: %s | Falecimento: %s%n",
                        autor.getNome(),
                        autor.getAnoNascimento() != null ? autor.getAnoNascimento() : "Desconhecido",
                        autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Ainda vivo");
            }
        }
    }


    private void listarAutoresVivosAno() {
    }

    private void buscarLivrosPorIdioma() {
    }
}