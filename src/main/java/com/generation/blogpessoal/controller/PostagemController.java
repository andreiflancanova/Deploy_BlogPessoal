package com.generation.blogpessoal.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

/*
 	@RestController serve para configurar essa classe como classe 
 	controladora da API (Onde ficam os Endpoints)
 */

@RestController

/*
	@CrossOrigin serve para que a classe aceite requisições de qualquer
	origem. Serve para mudar as portas de requisição de Back-End que o
	Front-End pede.
*/

/*
 	Quando o Front-End começar a consumir a nossa API, provavelmente ela
 	virá do Angular ou do React, razão pela qual é preciso que, independen-
 	temente da origem, nossa API aceite, e para isso usamos a anotação
 	@CrossOrigin
 */

@CrossOrigin(origins = "*")

/*
  	Cria um Endpoint (O "/" a gente coloca para fazer a separação da url)
 	Aqui NÃO PODE COLOCAR Letra maiúscula ou espaço
 */
@RequestMapping("/posts")
public class PostagemController {
/*
 	Endpoints são caminhos que vão levar o meu programa a executar
 	alguma função na minha execução
 	
 	Como o controller é o que executa as funções, ele tem que trazer 
 	as funções que foram criadas em outro lugar. Nesse caso, criamos 
 	no Repository, porque o Repository conecta com o Banco de Dados.
 	
 	Para isso,temos que fazer uma injeção de dependência e fazemos isso
 	com o @Autowired, porque o Spring faz o instanciamento do Repository
 	toda vez sem precisarmos fazer isso manualmente. Dessa forma, ele transfere
 	a responsabilidade de instanciar o PostagemRepository várias vezes, sem
 	que eu tenha que fazer este instanciamento manualmente.
 */
	@Autowired
	private PostagemRepository postagemRepository;
	//Colocando private só funciona dentro do PostagemController
	
	@Autowired
	private TemaRepository temaRepository;
	
	@GetMapping("/all")
	public ResponseEntity<List<Postagem>> buscaPostagem(){
		return ResponseEntity.ok(postagemRepository.findAll());
	/*
	 	O GetMapping indica o verbo para requisição. Quando eu indicar o 
	 	GetMapping quero que ele indique o verbo que quero usar nessa requisição.
	 	Com isso, preparamos a requisição para encontrar um tipo de resposta
	 	"Postagem" e receber um ou vários valores, através do List (Lista de
	 	Postagens).
	 	
	 	Quando coloco o @GetMapping, vinculo o Get da API (PostMan) para fazer
	 	com que ele retorne o método buscaPostagem quando eu der o Get na API.
	 	
	 	Para criar algo:
	 	@PostMapping - Criar
	 	@PutMapping - Atualizar
	 	@DeleteMapping - Deletar
	*/
	}
	
	@GetMapping ("/{id}")
	/*
	 	Quando usamos a anotação @PathVariable, estamos fazendo ela pegar o valor
	 	diretamente pela URL.
	*/
	
	public ResponseEntity<Postagem> buscaPostagemPorId(@PathVariable Long id){
		/*
		 	Este método faz o seguinte:
		 	1) Se a URL no Front-End (Postman) contiver o /id, acessa ele;
		 	2) Usa o findById para trazer a entrada do BD correspondente
		 	a este ID;
		 */
		return postagemRepository.findById(id)
				//Método Option: Map e Função Lambda: resposta
				//Retorna um objeto do tipo Postagem
				.map(resposta -> ResponseEntity.ok(resposta))
					//O map aqui converte o objeto resposta em ResponseEntity
				
				//Retorna um "Not Found" no código do Postman se não houver nada
				.orElse(ResponseEntity.notFound().build());
				
	/*
	 	Fazendo dessa forma, estamos fazendo uma sub-rota do primeiro Get.
	*/
	}
	
	/*
	 	Coloco o "/titulo/" para que o Back-End não confunda a rota a ser tomada
	 	Depois da última / o Back-End entende o último dado (@PathVariable) como
	 	um atributo e não o nome em si.
	 	
	 */
	@GetMapping("/title/{title}")
	public ResponseEntity<List<Postagem>> buscaPostagemPorTitulo(@PathVariable String title){
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(title));
	}
	
	
	@PostMapping
	public ResponseEntity<Postagem> adicionaPostagem(@Valid @RequestBody Postagem postagem){
	/*
	 	Quando se fala de Post, temos, obrigatoriamente, que enviar
	 	informações para o meu banco de dados.A annotation @RequestBody
	 	pega a Body (o que vem no corpo da requisição)
	*/
		if (temaRepository.existsById(postagem.getTema().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@PutMapping ("/{id}")
	public ResponseEntity<Postagem> atualizaPostagem(@PathVariable Long id, @Valid @RequestBody  Postagem postagem){
		/*
	 	Quando se fala de Post, temos, obrigatoriamente, que enviar
	 	informações para o meu banco de dados.A annotation @RequestBody
	 	pega a Body (o que vem no corpo da requisição)
		 */

		if (postagemRepository.existsById(id)){
			if (temaRepository.existsById(postagem.getTema().getId())){
				return postagemRepository.findById(id)
						.map(post -> {
							post.setId(id);
							post.setTitulo(postagem.getTitulo());
							post.setTexto(postagem.getTexto());
							post.setData(postagem.getData());
							post.setTema(postagem.getTema());
							post.setUsuario(postagem.getUsuario());
							return ResponseEntity.ok(post);})
						.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
			}
		}
		return 	ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	/*
	  	O nome das chaves {} é "Interpolação" ou "Template Literal"
	 */
	@DeleteMapping("/{id}")
public ResponseEntity<?> deletePostagem(@PathVariable Long id) {
	return postagemRepository.findById(id)
			.map(resposta -> {
				postagemRepository.deleteById(id);
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			})
			.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
}
