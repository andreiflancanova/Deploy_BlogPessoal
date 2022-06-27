package com.generation.blogpessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.generation.blogpessoal.model.UserLogin;
import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Optional <Usuario> cadastrarUsuario(Usuario usuario){
	
        if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {

        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Usuário já existe",null);
        }

            

        usuario.setSenha(criptografarSenha(usuario.getSenha()));

        return Optional.of(usuarioRepository.save(usuario));
		
	}

	public Optional<UserLogin> loginUsuario(Optional<UserLogin> userLogin){
		Optional<Usuario> usuario = usuarioRepository.findByUsuario(userLogin.get().getUsuario());
		if (usuario.isPresent()) {
            if (compararSenhas(userLogin.get().getSenha(), usuario.get().getSenha())) {

                userLogin.get().setId(usuario.get().getId());
                userLogin.get().setNome(usuario.get().getNome());
                userLogin.get().setFoto(usuario.get().getFoto());
                userLogin.get().setToken(gerarBasicToken(userLogin.get().getUsuario(), userLogin.get().getSenha()));
                userLogin.get().setSenha(usuario.get().getSenha());

                return userLogin;
            }
        }
        return Optional.empty();
	}
	
	public Optional<Usuario> atualizaUsuario(Usuario usuario) {
		if(usuarioRepository.findById(usuario.getId()).isPresent()) {
			
			Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());
			
			if((buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != usuario.getId()))
				new ResponseStatusException(HttpStatus.BAD_REQUEST,"Usuário já cadastrado!",null);
			
			usuario.setSenha(criptografarSenha(usuario.getSenha()));
			
			return Optional.ofNullable(usuarioRepository.save(usuario));
		}
		return Optional.empty();
	}
	
	private String criptografarSenha(String senha) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.encode(senha);
    }
	
	private boolean compararSenhas(String senhaDigitada, String senhaBD) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.matches(senhaDigitada, senhaBD);
    }
	
	private String gerarBasicToken(String usuario, String senha) {

        String token = usuario + ":" + senha;
        byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(tokenBase64);
    }
	
}
