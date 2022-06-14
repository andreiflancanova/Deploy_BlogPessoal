package com.generation.blogpessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.blogpessoal.model.Usuario;

//Comunicação da API com a base de dados


/*
 	Constrói um Repository que herda as propriedades do JpaRepository e usa como tipo
 	"Usuario" que foi o tipo que utilizamos nas anotações da dependência Validation (@Entity)
 */
public interface UsuarioRepository extends JpaRepository <Usuario,Long>{
	/*Criação do Método de Captura de Usuário e Username
	 	Aqui é usada uma Classe Optional, porque ela permite que o retorno seja nulo
	 */
	public Optional <Usuario> findByUsuario(String usuario);
	
	public List <Usuario> findAllByNomeContainingIgnoreCase(String nome);
	
}
