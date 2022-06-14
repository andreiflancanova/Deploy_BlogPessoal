package com.generation.blogpessoal.configuration;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Configuration
/*	Define uma classe como fonte de definições de Beans e é
 *  uma das anotações essenciais se você estiver usando a 
 *  configuração baseada em Java.	 */
public class SwaggerConfig {

	@Bean
	/*	Indica ao Spring que ele deve invocar o método
	 *  logo abaixo e gerenciar o objeto retornado por
	 *  ele, ou seja, agora este objeto pode ser 
	 *  injetado em qualquer ponto da sua aplicação.*/
	public OpenAPI springBlogPessoalOpenAPI() {
		
		return new OpenAPI()
		/*	Cria um Objeto da Classe OpenAPI, que gera a
		 *  documentação no Swagger utilizando a especi-
		 *  ficação OpenAPI. */
			
			//Informações do Projeto
			.info(new Info()
				.title("Blog Pessoal - Andrei")
				.description("Projeto: Blog Pessoal - Generation Brasil")
				.version("v0.0.1")
				
			//Licença da API
			.license(new License()
				.name("Generation Brasil")
				.url("https://brazil.generation.org/"))
			
			//Contato do Desenvolvedor
			.contact(new Contact()
				.name("Andrei Ferreira Lançanova")
				.url("https://github.com/andreiflancanova")
				.email("andreiflancanova@hotmail.com")))
			
			//Documentações Externas
			.externalDocs(new ExternalDocumentation()
				.description("Github")
				.url("https://github.com/andreiflancanova/Blog-Pessoal"));
	}
	
	@Bean
	public OpenApiCustomiser customerGlobalHeaderOpenApiCustomiser() {
	/*	A Classe OpenApiCustomiser permite personalizar o Swagger,
	 *  baseado na Especificação OpenAPI. O Método acima, altera algumas
	 *  mensagens do tipo HTTPResponses (Respostas das requisições HTTP)
	 *  do Swagger.	 */

		return openApi -> {
		/*	Cria um Objeto da Classe OpenAPI, que gera a documentação no
		 * 	Swagger utilizando a especificação OpenAPI.	 */
			
			openApi.getPaths().values()
				.forEach(pathItem -> pathItem
				/*	Cria um primeiro loop que fará a leitura de todos 
				 * 	os recursos (Paths) através do Método getPaths(),
				 *  que retorna o caminho de cada endpoint.	*/
						
				.readOperations().forEach(operation -> {
				 /* Cria um segundo loop que identificará qual Método HTTP
				 *  (Operations), está sendo executado em cada endpoint através
				 *  do Método readOperations(). Para cada Método a seguir, todas
				 *  as mensagens serão lidas e substituídas pelas novas mensagens.*/

					ApiResponses apiResponses = operation.getResponses();
					/*	Cria um Objeto da Classe ApiResponses, que receberá
					 *  as Respostas HTTP de cada endpoint (Paths) através
					 *  do método getResponses(). */

					apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
					apiResponses.addApiResponse("201", createApiResponse("Objeto Persistido!"));
					apiResponses.addApiResponse("204", createApiResponse("Objeto Excluído!"));
					apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição!"));
					apiResponses.addApiResponse("401", createApiResponse("Acesso Não Autorizado!"));
					apiResponses.addApiResponse("404", createApiResponse("Objeto Não Encontrado!"));
					apiResponses.addApiResponse("500", createApiResponse("Erro na Aplicação!"));
					/* Substitui as respostas padrão dos Endpoints pelas respostas
					 * previstas em cada objeto pelo método "addApiResponse".	*/
			}));
		};
	}

	private ApiResponse createApiResponse(String message) {
	/*	Este método adiciona uma descrição "message" para cada
	 *	resposta HTTP.	 */
		return new ApiResponse().description(message);
	}
}
