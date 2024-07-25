package br.com.criandoapi.projeto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
//com o lombok, conseguimos diminuir bastante o código, pois os geters e seters já são parte da anotação do lombok.
@Entity
@Table(name = "usuarios") // Aqui tem que ser igual ao banco de dados, pois se não a conexão não acontece.
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "O nome é obrigatório!")
    @Size(min=3, message="O nome deve ter no mínimo 3 caracteres")
    @Column(name = "nome", length = 200, nullable = false)
    private String nome;

    @Email(message = "Insita um email válido")
    @NotBlank(message = "O email é obrigatório!")
    @Column(name = "email", length = 100, nullable = false)
    private String email;


    @NotBlank(message = "A senha é obrigatório!")
    @Column(name = "senha", columnDefinition = "TEXT", nullable = false)
    private String senha;

    @NotBlank(message = "O telefone é obrigatório!")
    @Column(name = "telefone", length = 15, nullable = false)
    private String telefone;

}
