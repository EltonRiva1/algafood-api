package com.algaworks.algafood.domain.service;

import java.io.InputStream;
import java.util.UUID;

public interface FotoStorageService {
	FotoRecuperada recuperar(String nomeArquivo);

	void armazenar(NovaFoto novaFoto);

	void remover(String nomeArquivo);

	default void substituir(String nomeArquivoAntigo, NovaFoto novaFoto) {
		this.armazenar(novaFoto);
		if (nomeArquivoAntigo != null)
			this.remover(nomeArquivoAntigo);
	}

	default String gerarNomeArquivo(String nomeOriginal) {
		return UUID.randomUUID().toString() + "_" + nomeOriginal;
	}

	class NovaFoto {
		private final String nomeArquivo;
		private final InputStream inputStream;

		private NovaFoto(Builder builder) {
			this.nomeArquivo = builder.nomeArquivo;
			this.inputStream = builder.inputStream;
		}

		public String getNomeArquivo() {
			return nomeArquivo;
		}

		public InputStream getInputStream() {
			return inputStream;
		}

		public static class Builder {
			private String nomeArquivo;
			private InputStream inputStream;

			public Builder nomeArquivo(String nomeArquivo) {
				this.nomeArquivo = nomeArquivo;
				return this;
			}

			public Builder inputStream(InputStream inputStream) {
				this.inputStream = inputStream;
				return this;
			}

			public NovaFoto build() {
				return new NovaFoto(this);
			}
		}
	}

	class FotoRecuperada {
		private final InputStream inputStream;
		private final String url;

		private FotoRecuperada(Builder builder) {
			this.inputStream = builder.inputStream;
			this.url = builder.url;
		}

		public InputStream getInputStream() {
			return inputStream;
		}

		public String getUrl() {
			return url;
		}

		public static class Builder {
			private InputStream inputStream;
			private String url;

			public Builder inputStream(InputStream inputStream) {
				this.inputStream = inputStream;
				return this;
			}

			public Builder url(String url) {
				this.url = url;
				return this;
			}

			public FotoRecuperada build() {
				return new FotoRecuperada(this);
			}
		}
	}
}
