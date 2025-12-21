package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.*;
import br.com.alura.literalura.repository.*;
import br.com.alura.literalura.service.ConsumoApi;
import br.com.alura.literalura.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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
                    4 - Listar autores vivos em determinado ano
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
        var nomeLivro = URLEncoder.encode(leitura.nextLine(), StandardCharsets.UTF_8);

        var json = consumo.obterDados(ENDERECO + nomeLivro);
        var resposta = conversor.obterDados(json, GutendexResponse.class);

        if (resposta.results() != null && !resposta.results().isEmpty()) {

            System.out.println("Livros encontrados: ");
            resposta.results().stream()
                    .limit(5) // exibe apenas os 5 primeiros
                    .forEach(dados -> {
                        String autores = (dados.autores() != null && !dados.autores().isEmpty())
                                ? dados.autores().stream()
                                .map(DadosAutor::nome)
                                .collect(Collectors.joining(", "))
                                : "Autor desconhecido";

                        System.out.printf("ID: %d | Título: %s | Autores: %s%n",
                                dados.id(),
                                dados.titulo(),
                                autores);
                    });

            // Usuário escolhe o ID
            System.out.println("Digite o ID do livro que deseja salvar: ");
            long idEscolhido = leitura.nextLong();
            leitura.nextLine(); // consumir quebra de linha

            // Busca o livro correspondente
            DadosLivro escolhido = resposta.results().stream()
                    .filter(d -> d.id() == idEscolhido)
                    .findFirst()
                    .orElse(null);

            if (escolhido == null) {
                System.out.println("ID inválido. Nenhum livro salvo.");
                return;
            }

            Optional<Livro> livroCadastrado = livroRepository.findByIdGutendex(escolhido.id());

            if (livroCadastrado.isPresent()) {
                System.out.println("➡ Livro já cadastrado: " + livroCadastrado.get().getTitulo());
            } else {
                // Cria o livro
                Livro livro = new Livro(escolhido);

                // Corrige autores: reutiliza se já existir no banco
                Set<Autor> autoresCorrigidos = new HashSet<>();
                for (Autor autor : livro.getAutores()) {
                    Optional<Autor> existente = autorRepository.findByNomeAndAnoNascimentoAndAnoFalecimento(
                            autor.getNome(),
                            autor.getAnoNascimento(),
                            autor.getAnoFalecimento()
                    );

                    if (existente.isPresent()) {
                        autoresCorrigidos.add(existente.get()); // reutiliza autor já salvo
                    } else {
                        autor.setLivro(livro);
                        autoresCorrigidos.add(autor); // novo autor
                    }
                }

                livro.setAutores(autoresCorrigidos);
                livroRepository.save(livro);

                // Exibe informações do livro salvo
                String autores = livro.getAutores().isEmpty()
                        ? "Autor desconhecido"
                        : livro.getAutores().stream()
                        .map(Autor::getNome)
                        .collect(Collectors.joining(", "));

                String idioma = escolhido.idiomas() != null && !escolhido.idiomas().isEmpty()
                        ? escolhido.idiomas().get(0)
                        : "Idioma não informado";

                System.out.printf("Livro salvo: %s | Autores: %s | Idioma: %s | Downloads: %d%n",
                        escolhido.titulo(),
                        autores,
                        livro.getIdioma().getDescricao(),
                        escolhido.downloads() != null ? escolhido.downloads() : 0);
            }
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
                        : livro.getAutores().iterator().next().getNome();

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
        System.out.println("Digite o ano que deseja verificar: ");
        int ano = leitura.nextInt();
        leitura.nextLine();

        List<Autor> autores = autorRepository.findAll();

        List<Autor> vivos = autores.stream()
                .filter(a -> a.getAnoNascimento() != null && a.getAnoNascimento() <= ano)
                .filter(a -> a.getAnoFalecimento() == null || a.getAnoFalecimento() > ano)
                .toList();

        if (vivos.isEmpty()) {
            System.out.println("Nenhum autor vivo encontrado nesse ano.");
        } else {
            System.out.println("Autores vivos em " + ano + ":");
            vivos.forEach(a -> System.out.printf("- %s (Nascimento: %s, Falecimento: %s)%n",
                    a.getNome(),
                    a.getAnoNascimento() != null ? a.getAnoNascimento() : "Desconhecido",
                    a.getAnoFalecimento() != null ? a.getAnoFalecimento() : "Ainda vivo"));
        }
    }


    private void buscarLivrosPorIdioma() {
        System.out.println("Digite o idioma (ex: en, pt, fr, es): ");
        String sigla = leitura.nextLine().trim();

        Idioma idioma;
        try {
            idioma = Idioma.fromString(sigla);
        } catch (IllegalArgumentException e) {
            System.out.println("Idioma inválido.");
            return;
        }

        List<Livro> livros = livroRepository.findAll()
                .stream()
                .filter(l -> l.getIdioma() == idioma)
                .toList();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma " + idioma.getDescricao());
        } else {
            System.out.println("Livros em " + idioma.getDescricao() + ":");
            livros.forEach(l -> {
                String autores = l.getAutores().isEmpty()
                        ? "Autor desconhecido"
                        : l.getAutores().stream()
                        .map(Autor::getNome)
                        .collect(Collectors.joining(", "));
                System.out.printf("- %s | Autores: %s | Downloads: %d%n",
                        l.getTitulo(),
                        autores,
                        l.getDownloads() != null ? l.getDownloads() : 0);
            });
        }
    }



}