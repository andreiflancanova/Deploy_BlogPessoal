package com.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
/*	Aqui é um import static porque é um método static que
 * 	foi importado da biblioteca junit. */

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.generation.blogpessoal.model.Usuario;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
/*	Esta annotation faz com que o Spring procure outra porta, para o caso
 * 	da porta 8080 já estar em uso */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
/*	Esta annotation especifica que o ciclo de vida da Classe Teste
 * 	será por classe. Cada vez que eu executar, ela cria o BD para
 * 	testar, e depois que já testei, ela desmonta a estrutura mon-
 * 	tada. */

public class UsuarioRepositoryTest {
	@Autowired
	/*	Esta annotation faz com que o instanciamento dentro da classe
	 * 	seja criado automaticamente. */
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		usuarioRepository.deleteAll();
		usuarioRepository.save(new Usuario(0L,"João da Silva","joao@gmail.com","1234567890","https://i.imgur.com/h4t8loa.jpg"));
		usuarioRepository.save(new Usuario(0L,"Manuela da Silva","manuela@gmail.com","1234567890","https://i.imgur.com/h4t8loa.jpg"));
		usuarioRepository.save(new Usuario(0L,"Adriana da Silva","adriana@gmail.com","1234567890","https://i.imgur.com/h4t8loa.jpg"));
		usuarioRepository.save(new Usuario(0L,"Paulo Antunes","paulo@gmail.com","1234567890","https://i.imgur.com/h4t8loa.jpg"));
		
	/*	A passagem do atributo id com 0L é uma forma de indicar 
	 * 	que é uma Primary Key do tipo Long	*/
	}
	
	@Test
	// Indica que este método vai executar um teste.
	
	@DisplayName("Retorna 01 usuário")
	public void deveRetornarUmUsuario() {
	/*	Executo o método no sistema e valido se há um usuário.
	 * 	É void porque não tem retorno. O método só verifica se
	 * 	há 01 usuário ou não. */
		
		Optional<Usuario> usuario = usuarioRepository.findByUsuario("joao@gmail.com");
		assertTrue(usuario.get().getUsuario().equals("joao@gmail.com"));
		/*	O assertTrue retorna a verificação do usuário espe-
		 *	cificado. Se for verdade
		 */
	}
	
	@Test
	// Indica que este método vai executar um teste.
	
	@DisplayName("Retorna 03 usuário")
	public void deveRetornarTresUsuario() {
		List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Silva");
		assertEquals(3,listaDeUsuarios.size());
		/*	Verifica se o tamanho da lista é igual a 3.
		 * 	O size informa o tamanho da lista */
		
		assertTrue(listaDeUsuarios.get(0).getNome().equals("João da Silva"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Manuela da Silva"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("Adriana da Silva"));
		/*	Faz o teste para comparar se a ordem da 
		 * 	lista de inserção de dados do método 
		 * 	start é igual ao da ordem passada no 
		 * 	método deveRetornarTresUsuarios
		 */
		
	}
}
