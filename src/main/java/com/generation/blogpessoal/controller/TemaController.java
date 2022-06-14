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

import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.TemaRepository;

@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping("/tema")

public class TemaController {

	@Autowired
	private TemaRepository repository;
	
	@GetMapping
	public ResponseEntity<List<Tema>> getAll(){
		return ResponseEntity.ok(repository.findAll());
	}
	
	//Criar uma sub-rota
	@GetMapping ("/{id}")
	/*
	 	Quando usamos a anotação @PathVariable, estamos fazendo ela pegar o valor
	 	diretamente pela URL.
	*/
	
	public ResponseEntity<Tema> getById(@PathVariable Long id){
		/*
		 	Este método faz o seguinte:
		 	1) Se a URL no Front-End (Postman) contiver o /id, acessa ele;
		 	2) Usa o findById para trazer a entrada do BD correspondente
		 	a este ID;
		 */
		return repository.findById(id)
				//Método Option: Map e Função Lambda: resposta
				//Retorna um objeto do tipo Tema e renderiza na Body
				.map(resposta -> ResponseEntity.ok(resposta))
				//Retorna um "Not Found" no código do Postman se não houver nada
				.orElse(ResponseEntity.notFound().build());
				
	/*
	 	Fazendo dessa forma, estamos fazendo uma sub-rota do primeiro Get.
	*/
	}
	
	//Criar uma sub-rota para busca pelo nome
		@GetMapping ("/nome/{nome}")
		/*
		 	Quando usamos a anotação @PathVariable, estamos fazendo ela pegar o valor
		 	diretamente pela URL.
		*/
		
		public ResponseEntity<List<Tema>> getByName(@PathVariable String nome){
			/*
			 	Este método faz o seguinte:
			 	1) Se a URL no Front-End (Postman) contiver o /nome, acessa ele;
			 	2) Usa o findAllByDescricao para trazer a entrada do BD correspondente
			 	a este ID;
			 */
			return ResponseEntity.ok(repository.findAllByDescricaoContainingIgnoreCase(nome));
		}
		
		@PostMapping
		public ResponseEntity<Tema> adicionaTema(@Valid @RequestBody Tema tema){
			return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(tema));
		}
		
		@PutMapping
		public ResponseEntity<Tema> atualizaTema(@Valid @RequestBody Tema tema){
			return ResponseEntity.ok(repository.save(tema));
		}
		
		@DeleteMapping ("/{id}")
		public void delete(@PathVariable Long id) {
			repository.deleteById(id);
		}
		
}
