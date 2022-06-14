package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.generation.blogpessoal.model.Postagem;

/*
 	Tudo que vai para o banco de dados passa pelo Repository.
	Anotação que define que PostagemRepository é um repositório de queries
	dentro da tabela Postagem
	
	Serve para consulta, inserção e deleção de dados no banco de dados
 */


@Repository
//Vamos passar a Model e o tipo de ID no argumento do JpaRepository
public interface PostagemRepository extends JpaRepository <Postagem, Long>{

/*
	O Jpa converte através do FindAll tudo que está no Java 
	para a linguagem MySQL
 */
	//Buscar todos pelo Título
	//Containing é o Like do MySQL
	//IgnoreCase não considera maiúsculo e minúsculo
	public List<Postagem> findAllByTituloContainingIgnoreCase(String titulo);
}
