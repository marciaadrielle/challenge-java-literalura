package br.com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    private String idioma;

    @Column(length = 2000) // para textos maiores
    private String resumo;

    // construtores
    public Livro() {}

    public Livro(String titulo, String idioma, String resumo) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.resumo = resumo;
    }

    // getters e setters
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public String getResumo() { return resumo; }
    public void setResumo(String resumo) { this.resumo = resumo; }
}
