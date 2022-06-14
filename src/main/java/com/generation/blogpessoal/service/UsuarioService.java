package com.generation.blogpessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;
import org.apache.commons.codec.binary.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.generation.blogpessoal.model.UserLogin;
import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	public Optional <Usuario> CadastrarUsuario(Usuario usuario){
		/*	Aqui, estamos instanciando um objeto que é responsável por
	 	encriptar a senha do usuário e salvar no Banco de Dados a 
	 	senha já encriptada. */

		/*	Instanciamento do Objeto: Aqui, "BCryptPasswordEncoder" tem
		 *  que ser igual ao que está na classe "BasicSecurityConfig". */
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		//Encriptação da senha do usuário
		String senhaEncoder = encoder.encode(usuario.getSenha());

		//Passagem da senha encriptada para o Banco de Dados
		usuario.setSenha(senhaEncoder);
		return Optional.of(repository.save(usuario));
	}

	public Optional<UserLogin> Logar(Optional<UserLogin> user){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		Optional<Usuario> usuario = repository.findByUsuario(user.get().getUsuario());

		//Verificação de existência do usuário
		if(usuario.isPresent()) 
		{
			//Comparação da senha digitada com a senha encriptada salva no BD
			if(encoder.matches(user.get().getSenha(), usuario.get().getSenha())) 
			{
				String auth = user.get().getUsuario() + ":" + user.get().getSenha();

				//Criação do Array de Byte
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));

				String authHeader = "Basic " + new String(encodedAuth);

				/* 	Como o user.get é um Optional, devemos acessá-lo pelo método Set
				 	e colocar as informações que vêm através do username.*/
				user.get().setToken(authHeader);
				user.get().setNome(usuario.get().getNome());

				return user;
			}
		}
		/* 	Se não satisfazer a condição do if, o programa vai retornar um
	 	valor nulo.	 */
		return null;
	}
}
