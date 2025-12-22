package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNomeAndAnoNascimentoAndAnoFalecimento(
            String nome,
            Integer anoNascimento,
            Integer anoFalecimento
    );

    @Query("""
        SELECT DISTINCT a
        FROM Autor a
        LEFT JOIN FETCH a.livros
    """)
    List<Autor> buscarAutoresComLivros();
}
