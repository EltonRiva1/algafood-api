package com.algaworks.algafood.domain.service;

import jakarta.annotation.Nonnull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface EnvioEmailService {
	void enviar(Mensagem mensagem);

	class Mensagem {
		private final Set<String> destinatarios;
		@Nonnull
		private final String assunto, corpo;
		private final Map<String, Object> variaveis;

		private Mensagem(Builder builder) {
			this.destinatarios = Set.copyOf(builder.destinatarios);
			this.assunto = builder.assunto;
			this.corpo = builder.corpo;
			this.variaveis = Map.copyOf(builder.variaveis);
		}

		public Set<String> getDestinatarios() {
			return destinatarios;
		}

		@Nonnull
		public String getAssunto() {
			return assunto;
		}

		@Nonnull
		public String getCorpo() {
			return corpo;
		}

		public Map<String, Object> getVariaveis() {
			return variaveis;
		}

		public static class Builder {
			private final Set<String> destinatarios = new HashSet<>();
			private String assunto, corpo;
			private final Map<String, Object> variaveis = new HashMap<>();

			public Builder destinatario(String destinatario) {
				this.destinatarios.add(destinatario);
				return this;
			}

			public Builder destinatarios(Set<String> destinatarios) {
				if (destinatarios != null) {
					this.destinatarios.addAll(destinatarios);
				}
				return this;
			}

			public Builder assunto(String assunto) {
				this.assunto = assunto;
				return this;
			}

			public Builder corpo(String corpo) {
				this.corpo = corpo;
				return this;
			}

			public Builder variavel(String nome, Object valor) {
				this.variaveis.put(nome, valor);
				return this;
			}

			public Builder variaveis(Map<String, Object> variaveis) {
				if (variaveis != null) {
					this.variaveis.putAll(variaveis);
				}
				return this;
			}

			public Mensagem build() {
				return new Mensagem(this);
			}
		}
	}
}