package com.algaworks.algafood.domain.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity
public class FotoProduto {
	@Id
	@Column(name = "produto_id")
	private Long id;
	private String nomeArquivo, descricao, contentType;
	private Long tamanho;
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private Produto produto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getTamanho() {
		return tamanho;
	}

	public void setTamanho(Long tamanho) {
		this.tamanho = tamanho;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FotoProduto other = (FotoProduto) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "FotoProduto [id=" + id + ", nomeArquivo=" + nomeArquivo + ", descricao=" + descricao + ", contentType="
				+ contentType + ", tamanho=" + tamanho + ", produto=" + produto + "]";
	}

	public Long getRestauranteId() {
		if (this.getProduto() != null)
			return this.getProduto().getRestaurante().getId();
		return null;
	}
}
