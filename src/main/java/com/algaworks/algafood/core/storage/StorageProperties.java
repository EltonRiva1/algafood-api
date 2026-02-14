package com.algaworks.algafood.core.storage;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("algafood.storage")
public class StorageProperties {
	private Local local = new Local();

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public static class Local {
		private Path diretorioFotos;

		public Path getDiretorioFotos() {
			return diretorioFotos;
		}

		public void setDiretorioFotos(Path diretorioFotos) {
			this.diretorioFotos = diretorioFotos;
		}
	}
}
