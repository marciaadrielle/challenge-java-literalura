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

    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER)
    private Set<Livro> livros = new HashSet<>();


    public Autor() {}

    public Autor(DadosAutor dadosAutor) {
        this.nome = formatarNomeAutor(dadosAutor.nome());
        this.anoNascimento = dadosAutor.anoNascimento();
        this.anoFalecimento = dadosAutor.anoFalecimento();
    }

    /* ================= FORMATAR NOME DO AUTOR================= */

    public static String formatarNomeAutor (String nome) {
        if (nome != null && nome.contains(",")) {
            String[] partes = nome.split(",", 2);
            return partes[1].trim() + " " + partes[0].trim();
        }
        return nome;

    };

    /* ================= GETTERS E SETTERS ================= */

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getAnoNascimento() { return anoNascimento; }
    public void setAnoNascimento(Integer anoNascimento) { this.anoNascimento = anoNascimento; }

    public Integer getAnoFalecimento() { return anoFalecimento; }
    public void setAnoFalecimento(Integer anoFalecimento) { this.anoFalecimento = anoFalecimento; }

    public Set<Livro> getLivros() {
        return livros;
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
