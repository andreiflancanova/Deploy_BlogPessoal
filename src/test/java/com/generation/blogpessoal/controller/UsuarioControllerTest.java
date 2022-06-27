package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.UserLogin;
import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
/*	Esta annotation faz com que o Spring procure outra porta, para o caso
 * 	da porta 8080 já estar em uso */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
/*	Esta annotation especifica que o ciclo de vida da Classe Teste
 * 	será por classe. Cada vez que eu executar, ela cria o BD para
 * 	testar, e depois que já testei, ela desmonta a estrutura mon-
 * 	tada. */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
/*	Especifica a forma como os testes serão ordenados durante a
 *  execução. Order 1, Order 2 */

public class UsuarioControllerTest {
	@Autowired
	private TestRestTemplate testRestTemplate;
	/*	Envia as requisições dos Requests para dentro da nossa 
	 * 	aplicação.
	 */

	@Autowired
	private UsuarioService usuarioService;
	/* Injetando o objeto da classe UsuarioService para persistir
	 * o objeto no banco de dados de Teste com a senha criptografada. */

	@Autowired
	private UsuarioRepository usuarioRepository; 
	/* Injetando objeto da Interface UsuarioRepository para limpar
	 * o banco de dados de Teste. */

	@BeforeAll
	void start() {
		usuarioRepository.deleteAll();
	}

	@Test
	@Order(1)
	@DisplayName("Cadastrar um Usuario")
	public void deveCriarUmUsuario() {
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(
				0L,"Paulo Antunes","paulo@gmail.com","1234567890","https://i.imgur.com/FETvs20.jpg"));
		/*	Cria um objeto da Classe Usuario e o insere dentro de
		 * 	um objeto da classe HttpEntity. */

		ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange
				("/users/register",HttpMethod.POST,corpoRequisicao,Usuario.class);
		/*	Cria o objeto "corpoResposta" da Classe Response Entity, que recebe a resposta da Requisição
		 *  realizada pelo Objeto da Classe TestRestTemplate. */

		assertEquals(HttpStatus.CREATED,corpoResposta.getStatusCode());
		/*	Verifica se a requisição retornou o Status "CREATED" (código 201)
		 * 	Se a condição for válida, o código passa nesse teste.
		 * 	Senão, o teste falha.	*/

		assertEquals(corpoRequisicao.getBody().getNome(),corpoResposta.getBody().getNome());
		/*	Verifica se o atributo Nome do "corpoRequisicao" é igual ao atributo Nome
		 * 	do "corpoResposta".	Se a condição for válida, o código passa nesse teste.
		 * 	Senão, o teste falha.	*/

		assertEquals(corpoRequisicao.getBody().getUsuario(),corpoResposta.getBody().getUsuario());
		/*	Verifica se o atributo Usuario do "corpoRequisicao" é igual ao atributo Usuario
		 * 	do "corpoResposta".
		 * 	Se a condição for válida, o código passa nesse teste.
		 * 	Senão, o teste falha.	*/
	}

	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação de usuário")
	public void naoDeveDuplicarUsuario() {
		usuarioService.cadastrarUsuario(new Usuario(0L,"Paulo Antunes",
				"paulo@gmail.com","1234567890","https://i.imgur.com/FETvs20.jpg"));
		/*	Persiste um objeto da Classe Usuario no Banco de dados através do
		 * 	Objeto da Classe UsuarioService. */

		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(
		new Usuario(0L,"Paulo Antunes",
				"paulo@gmail.com","1234567890","https://i.imgur.com/FETvs20.jpg"));
		/* Cria o mesmo objeto da Classe Usuario que foi persistido no Banco de dados
		 * na linha anterior para simular uma duplicação de usuário e insere dentro de
		 * um Objeto da Classe HttpEntity (Entidade HTTP). */

		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.exchange("/users/register",HttpMethod.POST,corpoRequisicao,Usuario.class);
		/* Cria um Objeto da Classe ResponseEntity (corpoResposta), que receberá a
		 * Resposta da Requisição que será enviada pelo Objeto da Classe TestRestTemplate.
		 * Na requisição HTTP será enviada a URL do recurso (/usuarios/cadastrar),
		 * o verbo (POST), a entidade HTTP criada acima (corpoRequisicao) e a 
		 * Classe de retornos da Resposta (Usuario). */

		assertEquals(HttpStatus.BAD_REQUEST,corpoResposta.getStatusCode());
		/* Verifica se a requisição retornou o Status Code BAD_REQUEST (400), que
		 * indica que o usuário já existe no Banco de dados. Se for verdadeira,
		 * o teste passa, senão o teste falha. */
	}

	@Test
	@Order(3)
	@DisplayName("Atualizar um usuário")
	public void deveAtualizarUmUsuario() {
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(
				new Usuario(0L,"Juliana Andreia","juliana_andreia@gmail.com",
						"j1234567890","https://i.imgur.com/h4t8loa.jpg"));
		
		/*	Persiste um Objeto da Classe Usuario no BD a partir do objeto da Classe
		 *  UsuarioService. Guarda o objeto persistido no Banco de Dados no Objeto 
		 *  "usuarioCadastrado"	
		 *  O Objeto usuarioCadastrado será do tipo Optional porque caso o usuário
		 *  não seja persistido no Banco de dados, o Optional evitará o erro 
		 *  NullPointerException (Objeto Nulo).	*/

		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(), 
				"Juliana Andrews Ramos","juliana_andrews@gmail.com",
				"j1234567890","https://i.imgur.com/h4t8loa.jpg");
		/*	Cria um Objeto da Classe Usuario a partir da usuarioCadastrado, e atualiza
		 * 	os atributos Nome e Usuario.
		 */
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);
		/* Insere o objeto da Classe Usuario (usuarioUpdate) dentro de um Objeto da
		 * Classe HttpEntity. */
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
		/* 	Cria um Objeto da Classe ResponseEntity (corpoResposta), que receberá a Resposta
		 	da Requisição que será enviada pelo Objeto da Classe TestRestTemplate.	 */
				
				.withBasicAuth("root", "root")
				/*	Observe que o Método Atualizar não está liberado de autenticação (Login do usuário),
				 *  por isso utilizamos o Método withBasicAuth para autenticar o usuário em memória,
				 *  criado na BasicSecurityConfig.
				 * 	Usuário: root |	Senha: root	 */
				
				
				.exchange("/users/update", HttpMethod.PUT, corpoRequisicao, Usuario.class);
				/*	Na requisição HTTP será enviada a URL do recurso (/usuarios/atualizar),
				 *  o verbo (PUT), a entidade HTTP criada acima (corpoRequisicao) e a Classe
				 *  de retornos da Resposta (Usuario).	 */
		
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
		/* Verifica se a requisição retornou o Status Code OK (200). 
		 * Se for verdadeira, o teste passa, se não, o teste falha.	*/
		
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
		/*	Verifica se o Atributo Nome do Objeto da Classe Usuario retornado no Corpo da Requisição 
		 * é igual ao Atributo Nome do Objeto da Classe Usuario, retornado no Corpo da Resposta.
		 * Se for verdadeiro, o teste passa, senão o teste falha.	*/
		
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
		/* Verifica se o Atributo "Usuario" do Objeto da Classe Usuario retornado em "corpoRequisicao" 
		 * é igual ao Atributo Usuario do Objeto da Classe Usuario retornado em "corpoResposta".
		 * Se for verdadeiro, o teste passa, senão o teste falha.	*/
	}
	
	@Test
	@Order(4)
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() {
		/*	Persistir 02 objetos diferentes da Classe Usuario BD
		 *  através de Objetos da Classe UsuarioService. */
		usuarioService.cadastrarUsuario(
		new Usuario(0L, "Sabrina Menezes", "sabrina_menezes@gmail.com",
				"sabrina123", "https://i.imgur.com/5M2p5Wb.jpg"));
		
		usuarioService.cadastrarUsuario(
		new Usuario(0L, "Ricardo Marques", "ricardo.marques@hotmail.com", 
				"ricardo123", "https://i.imgur.com/Sk5SjWE.jpg"));
		
		ResponseEntity<String> corpoResposta = testRestTemplate
		/*	Cria um Objeto da Classe ResponseEntity (corpoResposta),
		 *  que receberá a Resposta da Requisição que será 
		 *  enviada pelo Objeto da Classe TestRestTemplate.	 */
		
				.withBasicAuth("root", "root")
				//Credenciais de acesso ao BD
				.exchange("/users/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
		/*	Verifica se a requisição retornou o Status Code OK (200). 
		 * 	Se satisfizer, passa no teste, se não, o teste falha.	*/
	}
	
	@Test
	@Order(5)
	@DisplayName("Listar Um Usuário Específico")
	public void deveListarApenasUmUsuario() {
		Optional<Usuario> usuarioBusca = usuarioService.cadastrarUsuario(
		new Usuario(0L,"Laura Santana", "laura.santana@hotmail.com", 
				"laura12345", "https://i.imgur.com/EcJG8kB.jpg"));
		
		ResponseEntity<String> resposta = testRestTemplate
				
				.withBasicAuth("root", "root")
				
				.exchange("/users/" + usuarioBusca
				.get().getId(), HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		/*	Verifica se a requisição retornou o Status Code OK (200). 
		 * 	Se satisfizer, passa no teste, se não, o teste falha.	*/
	}
	
	@Test
	@Order(6)
	@DisplayName("Login do Usuário")
	public void deveAutenticarUsuario() {
		usuarioService.cadastrarUsuario(
		new Usuario(0L, "Marilza Souza", "marilza.souza@gmail.com",
				"13465278", "https://i.imgur.com/T12NIp9.jpg"));
		
		HttpEntity<UserLogin> corpoRequisicao = new HttpEntity<UserLogin>(
		new UserLogin(0L, "Marilza Souza", "marilza.souza@gmail.com", "13465278","",""));
		
		ResponseEntity<UserLogin> corpoResposta = testRestTemplate
				.exchange("/users/login", HttpMethod.POST, corpoRequisicao, UserLogin.class);
		
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
	}
}

