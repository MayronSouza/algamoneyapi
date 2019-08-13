package com.algamoney.api.token;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)// dá uma alta precedência à classe
public class RefreshTokenPreProcessorFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// faz casting no request
		HttpServletRequest req = (HttpServletRequest) request;
		
		// compara duas strings ignorando o case sensitive
		if("/oauth/token".equalsIgnoreCase(req.getRequestURI())) {
			String refreshToken = Stream.ofNullable(req.getCookies())// Retorna um Stream do array, se estiver nulo, um Stream vazio será retornado
				.flatMap(Arrays::stream)// Como estamos tratando, ainda, do array, precisamos torná-lo flat para acessar os elementos do array
				.filter(cookie -> "refreshToken".equals(cookie.getName()))// filtra os dados do Stream tranzendo somente refreshTonken
				.findFirst()// obtém o 1º objeto do Stream, caso exista
				.map(cookie -> cookie.getValue())// transforma de Stream pra String
				.orElse(null);// caso não encontre o cookie, retorna null
				
			req = new MyServletRequestWrapper(req, refreshToken);
				
		}
		
		chain.doFilter(req, response);
	}
	
	static class MyServletRequestWrapper extends HttpServletRequestWrapper {
		private String refreshToken;

		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken = refreshToken;
		}
		
		@Override
		public Map<String, String[]> getParameterMap() {
			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
			map.put("refresh_token", new String[] { refreshToken });
			map.setLocked(true);
			return map;

		}
		
	}

}
