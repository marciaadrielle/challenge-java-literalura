package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.*;
import br.com.alura.literalura.model.dto.DadosAutor;
import br.com.alura.literalura.model.dto.DadosLivro;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {


    /* ================= SCANNER  ================= */
    private Scanner leitura = new Scanner(System.in);
    @Autowired
    private LivroService livroService;
    private List<DadosLivro> livrosBuscados = new ArrayList<>();


    /* ================= MENU PRINCIPAL ================= */

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
                    4 - Listar autores vivos em determinado ano
                    5 - Buscar livros por idioma
                    
                    0 - Sair
                    
                    """);
            System.out.println(menu);
            System.out.print("Escolha uma opção: ");
            while (!leitura.hasNextInt()) {
                System.out.print("Digite um número válido: ");
                leitura.next();
            }

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

    /* ================= BUSCAR LIVROS NA API ================= */

    private void buscarLivroPorTitulo() {
        System.out.println("Digite o nome do livro:");
        String titulo = leitura.nextLine();

        List<DadosLivro> resultados = livroService.buscarLivrosPorTitulo(titulo);

        if (resultados.isEmpty()) {
            System.out.println("Livro não encontrado.");
            return;
        }

        resultados.stream()
                .limit(5)
                .forEach(d -> {
                    String autores = d.autores() == null || d.autores().isEmpty()
                            ? "Autor desconhecido"
                            : d.autores().stream()
                            .map(DadosAutor::nome)
                            .collect(Collectors.joining(", "));

                    System.out.printf("ID: %d | %s | %s%n",
                            d.id(), d.titulo(), autores);
                });


        System.out.println("Digite o ID do livro para salvar:");
        long id = leitura.nextLong();
        leitura.nextLine();

        livroService.salvarLivroPorId(id, resultados);
    }

    /* ================= LISTAR LIVROS REGISTRADOS NO BANCO ================= */
    private void listarLivrosRegistrados() {
        List<Livro> livros = livroService.listarLivrosRegistrados();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado ainda.");
            return;
        }

        livros.forEach(l -> {
            String autores = l.getAutores().isEmpty()
                    ? "Autor desconhecido"
                    : l.getAutores().stream()
                    .map(Autor::getNome)
                    .collect(Collectors.joining(", "));

            System.out.printf(" Livro:  %s%n Autor: %s%n Idioma: %s%n Downloads: %d%n ==================================%n",
                    l.getTitulo(),
                    autores,
                    l.getIdioma().getDescricao(),
                    l.getDownloads() != null ? l.getDownloads() : 0);
        });
    }


    /* ================= LISTAR AUTORES REGISTRADOS NO BANCO ================= */
    private void listarAutoresRegistrados() {
        List<Autor> autores = livroService.listarAutoresRegistrados();

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado ainda.");
            return;
        }

        autores.forEach(a -> {
            System.out.printf(
                    "%n Autor: %s%n Data de nascimento: %s%n Data de falecimento: %s%n ",
                    a.getNome(),
                    a.getAnoNascimento() != null ? a.getAnoNascimento() : "Desconhecido",
                    a.getAnoFalecimento() != null ? a.getAnoFalecimento() : "Ainda vivo"
            );

            if (a.getLivros().isEmpty()) {
                System.out.println("  Livros: nenhum cadastrado");
            } else {
                System.out.println("  Livros:");
                a.getLivros().forEach(l ->
                        System.out.println("   • " + l.getTitulo())
                );

                System.out.println("==================================");

            }
        });


    }

    /* ================= LISTAR AUTORES VIVOS EM DETERMINADO ANO ================= */


    private void listarAutoresVivosAno() {
        System.out.println("Digite o ano:");
        int ano = leitura.nextInt();
        leitura.nextLine();

        List<Autor> vivos = livroService.listarAutoresVivosNoAno(ano);

        if (vivos.isEmpty()) {
            System.out.println("Nenhum autor vivo encontrado.");
            return;
        }

        vivos.forEach(a -> System.out.printf(
                "Nome: %s (Nascimento: %s, Falecimento: %s)%n",
                a.getNome(),
                a.getAnoNascimento(),
                a.getAnoFalecimento() != null ? a.getAnoFalecimento() : "Ainda vivo"
        ));
    }


    /* ================= BUSCAR LIVROS POR IDIOMA ================= */
    private void buscarLivrosPorIdioma() {
        System.out.println("Digite o idioma (en, pt, fr, es): ");
        String sigla = leitura.nextLine();

        Idioma idioma;
        try {
            idioma = Idioma.fromString(sigla);
        } catch (IllegalArgumentException e) {
            System.out.println("Idioma inválido.");
            return;
        }

        List<Livro> livros = livroService.buscarLivrosPorIdioma(idioma);

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
            return;
        }

        livros.forEach(l -> {
            String autores = l.getAutores().isEmpty()
                    ? "Autor desconhecido"
                    : l.getAutores().stream()
                    .map(Autor::getNome)
                    .collect(Collectors.joining(", "));

            System.out.printf("- %s%n | Autores: %s%n | Downloads: %d%n",
                    l.getTitulo(),
                    autores,
                    l.getDownloads() != null ? l.getDownloads() : 0);
        });
    }




}