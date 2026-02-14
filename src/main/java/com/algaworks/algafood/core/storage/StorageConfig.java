package com.algaworks.algafood.core.storage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.domain.service.FotoStorageService;
import com.algaworks.algafood.infrastructure.service.storage.LocalFotoStorageService;

@Configuration
public class StorageConfig {
	private final StorageProperties storageProperties;

	public StorageConfig(StorageProperties storageProperties) {
		this.storageProperties = storageProperties;
	}

	@Bean
	FotoStorageService fotoStorageService() {
		return new LocalFotoStorageService(this.storageProperties);
	}
}
