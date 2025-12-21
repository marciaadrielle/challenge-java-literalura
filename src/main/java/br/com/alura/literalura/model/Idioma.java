package br.com.alura.literalura.model;

public enum Idioma {

    PT("pt", "Português"),
    FR("fr", "Francês"),
    EN("en", "Inglês"),
    ES("es", "Espanhol");

    private final String codigoGutendex;
    private final String descricao;

    Idioma(String codigoGutendex, String descricao) {
        this.codigoGutendex = codigoGutendex;
        this.descricao = descricao;
    }

    public static Idioma fromString(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.codigoGutendex.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Idioma não encontrado: " + text);
    }

    public static Idioma fromDescricao(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.descricao.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Idioma não encontrado: " + text);
    }

    public String getCodigoGutendex() {
        return codigoGutendex;
    }

    public String getDescricao() {
        return descricao;
    }
}
