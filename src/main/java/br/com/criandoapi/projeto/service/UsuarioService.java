package br.com.criandoapi.projeto.service;

import br.com.criandoapi.projeto.dto.UsuarioDto;
import br.com.criandoapi.projeto.model.Usuario;
import br.com.criandoapi.projeto.repository.IUsuario;
import br.com.criandoapi.projeto.security.Token;
import br.com.criandoapi.projeto.security.TokenUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private IUsuario repository;
    //ferramenta para criptografar a senha.
    private PasswordEncoder passwordEncoder;

    private UsuarioService(IUsuario repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        //esse this.passwordEcoder, é o código que iremos utilizar para fazer a Cryptografia.
        // a partir disso, quando irmos no localhost:8080/usuarios, ou no postman, não veremos mais a senha.
    }

    public List<Usuario> listarUsuario() {
        List<Usuario> lista = repository.findAll();
        return lista;
    }

    public Usuario criarUsuario(Usuario usuario) {
        String encoder = this.passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(encoder);
        Usuario usuarioNovo = repository.save(usuario);
        return usuarioNovo;
    }
    //se atentar no banco de dados, pois como a senha será criptografada, 10 caracteres não serão suficientes.

    public Usuario editarUsuario(Usuario usuario) {
        String encoder = this.passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(encoder);
        Usuario usuarioNovo = repository.save(usuario);
        return usuarioNovo;
    }

    public Boolean excluirUsuario(Integer id) {
        repository.deleteById(id);
        return true;
    }

    public Boolean validarSenha(Usuario usuario) {
        String senha = repository.getById(usuario.getId()).getSenha();
        Boolean valid = passwordEncoder.matches(usuario.getSenha(), senha);
        return valid;
    }

    public Token gerarToken(UsuarioDto usuario) {
       Usuario user = repository.findBynomeOrEmail(usuario.getNome(), usuario.getEmail());
       if(user !=null){
           Boolean valid = passwordEncoder.matches(usuario.getSenha(),user.getSenha());
           if (valid) {
               return new Token(TokenUtil.createToken(user));
           }
       }
        return null;
    }
}
