package br.com.alura.literalura.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    private Long idGutendex;
    /*@GeneratedValue(strategy = GenerationType.IDENTITY)*/
    private Long id;
    private  String titulo;

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores;

    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private Integer downloads;

    public Livro(){}

    public Livro (DadosLivro dadosLivro){
        this.idGutendex = dadosLivro.id();
        this.titulo = dadosLivro.titulo();
        this.autores = dadosLivro.autores()
                .stream()
                .map(Autor::new)
                .toList();
        this.autores.forEach(a -> a.setLivro(this));

        this.idioma = Idioma.fromString(dadosLivro.idiomas().get(0));
        this.downloads = dadosLivro.downloads();
    }

    public Long getIdGutendex() {
        return idGutendex;
    }

    public void setIdGutendex(Long idGutendex) {
        this.idGutendex = idGutendex;
    }

/*    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }*/

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }
}
