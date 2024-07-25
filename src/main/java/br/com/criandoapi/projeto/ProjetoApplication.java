package br.com.criandoapi.projeto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
// Esse código impede que a verificação de login aconteça, no começo, evitando a geração de uma senha pelo spring secutiry.  (exclude = {SecurityAutoConfiguration.class})
public class ProjetoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetoApplication.class, args);
    }

}
