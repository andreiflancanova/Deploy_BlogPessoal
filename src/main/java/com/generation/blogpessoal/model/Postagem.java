package com.generation.blogpessoal.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity

/*
 * @Entity indica para o SpringTools que o objeto criado vai se tornar uma
 * tabela no banco de dados. Uma classe do JPA Hibernate
 */

@Table(name = "tb_postagem")

public class Postagem {
//Estruturar a Model como um Banco de Dados

	// Indica que o campo de id será uma chave primária.

	@Id

	// Auto-increment do MySQL no Java:
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;

	@NotNull

	// @NotBlank: Não pode ser nulo e nem vazio
	@Size(min = 5, max = 100)
	private String titulo;

	@Size(min = 5, max = 500)
	private String texto;

	// Captura a hora exata de inserção dos dados no programa.
	@Temporal(TemporalType.TIMESTAMP)
	private Date data = new java.sql.Date(System.currentTimeMillis());

	// Criando a tabela Tema e relacionando como uma ManyToOne com a Postagem
	@ManyToOne
	@JsonIgnoreProperties("postagem")
	private Tema tema;
	
	@ManyToOne
	@JsonIgnoreProperties("postagem")
	private Usuario usuario;

	// Sem os Gets e Sets, a estrutura não vai funcionar
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	public Date getData() {

		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
