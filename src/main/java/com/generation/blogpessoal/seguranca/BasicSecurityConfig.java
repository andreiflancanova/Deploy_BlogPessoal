package com.generation.blogpessoal.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private UserDetailsService userDetailsService;
	
	/*	Criar Polimorfismo que permite a sobrescrita de Método
	 	O "throws Exception" faz uma tratativa de erros 		*/
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailsService);
	}
	
	/* 	O @Bean serve para exportar uma classe para o Spring, para que
	 	ele consiga carregar essa classe e fazer injeção de dependência
	 	dela em outras classes.	*/
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/*	Quando este método for iniciado, ele vai instanciar o objeto "http"
		do tipo HttpSecurity.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests()
		/*	Serve para liberar Endpoints e permitir que o Cliente
		 *  tenha acesso acesso a alguns caminhos dentro da Classe
		 *  Controller sem precisar passar uma chave do tipo Token.
		 *  Usamos isso tanto para logar quanto para cadastrar um
		 *  novo usuário.	*/
		.antMatchers("/usuarios/logar").permitAll()
		.antMatchers("/usuarios/cadastrar").permitAll()
		
		/* 	Especifica que as demais requisições deverão ser autenticadas.
		 	(Para o Header, terá que ser passada a chave)	*/
		.anyRequest().authenticated()
		
		// Especifica o padrão Basic para padrão de geração do Token
		.and().httpBasic()
		
		/*
		 * Aqui, estamos dizendo que o SessionCreationPolicy não irá guardar a Sessão,
		 * pois estamos criando uma API REST e uma das características dela é justamente
		 * ela ser STATELESS (não ser capaz de guardar sessão nenhuma).
		 */
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		
		// Habilitar o CORS dentro da camada de Security
		.and().cors()
		
		/*
		 * Usamos o ".csrf().disable()" para fazer com que o programa use as configurações
		 * padrão
		 */
		.and().csrf().disable();
	}
}
