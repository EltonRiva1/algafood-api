package com.algaworks.algafood.infrastructure.service.storage;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.util.FileCopyUtils;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.service.FotoStorageService;

public class LocalFotoStorageService implements FotoStorageService {
	private final StorageProperties storageProperties;

	public LocalFotoStorageService(StorageProperties storageProperties) {
		this.storageProperties = storageProperties;
	}

	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
			FileCopyUtils.copy(novaFoto.getInputStream(),
					Files.newOutputStream(this.getArquivoPath(novaFoto.getNomeArquivo())));
		} catch (Exception e) {
			throw new StorageException("Não foi possível armazenar arquivo.", e);
		}
	}

	private Path getArquivoPath(String nomeArquivo) {
		return this.storageProperties.getLocal().getDiretorioFotos().resolve(Path.of(nomeArquivo));
	}

	@Override
	public void remover(String nomeArquivo) {
		try {
			Files.deleteIfExists(this.getArquivoPath(nomeArquivo));
		} catch (Exception e) {
			throw new StorageException("Não foi possível excluir arquivo.", e);
		}
	}

	@Override
	public FotoRecuperada recuperar(String nomeArquivo) {
		try {
			return new FotoStorageService.FotoRecuperada.Builder()
					.inputStream(Files.newInputStream(this.getArquivoPath(nomeArquivo))).build();
		} catch (Exception e) {
			throw new StorageException("Não foi possível recuperar arquivo.", e);
		}
	}
}
