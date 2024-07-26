package br.com.criandoapi.projeto.controller;


import br.com.criandoapi.projeto.dto.UsuarioDto;
import br.com.criandoapi.projeto.model.Usuario;
import br.com.criandoapi.projeto.security.Token;
import br.com.criandoapi.projeto.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
//significa que todas as entradas que vierem da maquina estarão liberadas
@RequestMapping("/usuarios")
public class UsuarioController {


    //No começo das aulas, fizemos com o dao, mas agora estamos atualizando para fazer com o usuarioService, para usar os padrões.
    public UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> ListaUsuarios() {
        return ResponseEntity.status(200).body(usuarioService.listarUsuario());

    }

    @PostMapping //serve para criarmos um usuario novo
    public ResponseEntity<Usuario> criarUsuario(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(201).body(usuarioService.criarUsuario(usuario));
    }
    //não estava funcionando, pois eu não tive colocado esse metodo de criar um usuario, assim não daria nunca.

    @PutMapping
    public ResponseEntity<Usuario> editarUsuario(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(200).body(usuarioService.editarUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirUsuario(@Valid @PathVariable Integer id) {
        usuarioService.excluirUsuario(id);
        return ResponseEntity.status(204).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Token> validarSenha(@Valid @RequestBody UsuarioDto usuario) {
        Token token = usuarioService.gerarToken(usuario);
        if (token != null) {
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(403).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

