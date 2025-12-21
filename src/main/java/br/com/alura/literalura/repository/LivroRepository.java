package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    /* ================= BUSCA POR ID GUTENDEX ================= */

    Optional<Livro> findByIdGutendex(Long idGutendex);

    /* ================= LISTAR LIVROS COM AUTORES ================= */

    @Query("""
        SELECT DISTINCT l
        FROM Livro l
        LEFT JOIN FETCH l.autores
    """)
    List<Livro> buscarLivrosComAutores();

    /* ================= FILTRAR POR IDIOMA ================= */

    @Query("""
        SELECT DISTINCT l
        FROM Livro l
        LEFT JOIN FETCH l.autores
        WHERE l.idioma = :idioma
    """)
    List<Livro> buscarPorIdiomaComAutores(br.com.alura.literalura.model.Idioma idioma);
}
