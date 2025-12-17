package br.com.alura.literalura;

import br.com.alura.literalura.model.DadosLivro;
import br.com.alura.literalura.model.GutendexResponse;
import br.com.alura.literalura.service.ConsumoApi;
import br.com.alura.literalura.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        var consumoApi = new ConsumoApi();
        var json = consumoApi.obterDados("https://gutendex.com/books?search=dom%20casmurro");
        System.out.println(json);


      /*  ConverteDados conversor = new ConverteDados();
        DadosLivro dados = conversor.obterDados(json, DadosLivro.class);
        System.out.println("Teste" + dados);*/

        ConverteDados conversor = new ConverteDados();
        GutendexResponse resposta = conversor.obterDados(json, GutendexResponse.class);

        if (!resposta.results().isEmpty()) {
            DadosLivro livro = resposta.results().get(0);
            System.out.println("Teste: " + livro);

    }
}
}
