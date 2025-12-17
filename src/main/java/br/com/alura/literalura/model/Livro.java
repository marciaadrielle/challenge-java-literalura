package br.com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String idioma;

    @ManyToOne(cascade = CascadeType.ALL)
    private Autor autor;


    public Livro() {}

    public Livro(DadosLivro dados) {
        this.titulo = dados.titulo();
        this.idioma = dados.idiomas().get(0);
        this.autor = new Autor(dados.autores().get(0));
    }

    @Override
    public String toString() {
        return "Livro{" +
                "titulo='" + titulo + '\'' +
                ", idioma='" + idioma + '\'' +
                ", autor=" + autor.getNome() +
                '}';
    }
}
