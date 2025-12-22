package br.com.alura.literalura.model;

import br.com.alura.literalura.model.dto.DadosAutor;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Integer anoNascimento;
    private Integer anoFalecimento;

    // Relacionamento com Livro
    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER)
    private Set<Livro> livros = new HashSet<>();


    public Autor() {}

    public Autor(DadosAutor dadosAutor) {
        this.nome = formatarNomeAutor(dadosAutor.nome());
        this.anoNascimento = dadosAutor.anoNascimento();
        this.anoFalecimento = dadosAutor.anoFalecimento();
    }

    private String formatarNomeAutor(String nome) {
        if (nome != null && nome.contains(",")) {
            String[] partes = nome.split(",");
            return partes[1].trim() + " " + partes[0].trim();
        }
        return nome;
    }


    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getAnoNascimento() { return anoNascimento; }
    public void setAnoNascimento(Integer anoNascimento) { this.anoNascimento = anoNascimento; }

    public Integer getAnoFalecimento() { return anoFalecimento; }
    public void setAnoFalecimento(Integer anoFalecimento) { this.anoFalecimento = anoFalecimento; }

    public Set<Livro> getLivros() {
        return livros;
    }

    public void setLivros(Set<Livro> livros) {
        this.livros = livros;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Autor)) return false;
        Autor autor = (Autor) o;
        return Objects.equals(nome, autor.nome) &&
                Objects.equals(anoNascimento, autor.anoNascimento) &&
                Objects.equals(anoFalecimento, autor.anoFalecimento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, anoNascimento, anoFalecimento);
    }

}
