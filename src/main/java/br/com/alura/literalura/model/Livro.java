package br.com.alura.literalura.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String titulo;

    @OneToMany
    private List<Autor> autores;
   @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private Integer downloads;

    public Livro(){}

    public Livro (DadosLivro dadosLivro){
        this.titulo = dadosLivro.titulo();
        this.autores = dadosLivro.autores()
                .stream()
                .map(Autor::new)
                .toList();

        this.idioma = Idioma.fromString(dadosLivro.idiomas().get(0));
        this.downloads = dadosLivro.downloads();
    }


}
