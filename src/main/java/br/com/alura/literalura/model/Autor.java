package br.com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
    public class Autor {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String nome;
        private Integer anoNascimento;
        private Integer anoFalecimento;

        @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
        private List<Livro> livros = new ArrayList<>();


        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public Integer getAnoNascimento() {
            return anoNascimento;
        }

        public void setAnoNascimento(Integer anoNascimento) {
            this.anoNascimento = anoNascimento;
        }

        public Integer getAnoFalecimento() {
            return anoFalecimento;
        }

        public void setAnoFalecimento(Integer anoFalecimento) {
            this.anoFalecimento = anoFalecimento;
        }
    }

