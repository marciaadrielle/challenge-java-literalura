package br.com.alura.literalura.model;

public enum Idioma {

    PT("pt", "Português"),
    FR("fr", "Francês"),
    EN("en", "InglÊs"),
    ES("es", "Espanhol");
    private String idiomaGutendex;
    private String idioma;

    Idioma(String idiomaGutendex, String idioma) {
        this.idiomaGutendex = idiomaGutendex;
        this.idioma = idioma;
    }

    public static Idioma fromString(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.idiomaGutendex.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Idioma não encontrado: " + text);
    }

    public static Idioma fromEspanol(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.idioma.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Idioama não encontrado " + text);
    }

    public String getIdiomaGutendex() {
        return idiomaGutendex;
    }

    public String getIdiomaEspanol() {
        return idioma;
    }

}