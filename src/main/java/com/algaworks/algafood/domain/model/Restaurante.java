package com.algaworks.algafood.domain.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Restaurante {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String nome;
	@Column(nullable = false)
	private BigDecimal taxaFrete;
	@ManyToOne
	@JoinColumn(nullable = false)
	private Cozinha cozinha;
	@ManyToMany
	@JoinTable(name = "restaurante_forma_pagamento", joinColumns = @JoinColumn(name = "restaurante_id"), inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
	private Set<FormaPagamento> formasPagamento = new HashSet<>();
	@Embedded
	private Endereco endereco;
	@Column(nullable = false, columnDefinition = "datetime")
	@CreationTimestamp
	private OffsetDateTime dataCadastro;
	@Column(nullable = false, columnDefinition = "datetime")
	@UpdateTimestamp
	private OffsetDateTime dataAtualizacao;
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>();
	private Boolean ativo = Boolean.TRUE, aberto = Boolean.FALSE;
	@ManyToMany
	@JoinTable(name = "restaurante_usuario_responsavel", joinColumns = @JoinColumn(name = "restaurante_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
	private Set<Usuario> responsaveis = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getTaxaFrete() {
		return taxaFrete;
	}

	public void setTaxaFrete(BigDecimal taxaFrete) {
		this.taxaFrete = taxaFrete;
	}

	public Cozinha getCozinha() {
		return cozinha;
	}

	public void setCozinha(Cozinha cozinha) {
		this.cozinha = cozinha;
	}

	public Set<FormaPagamento> getFormasPagamento() {
		return formasPagamento;
	}

	public void setFormasPagamento(Set<FormaPagamento> formasPagamento) {
		this.formasPagamento = formasPagamento;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public OffsetDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(OffsetDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public OffsetDateTime getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(OffsetDateTime dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getAberto() {
		return aberto;
	}

	public void setAberto(Boolean aberto) {
		this.aberto = aberto;
	}

	public Set<Usuario> getResponsaveis() {
		return responsaveis;
	}

	public void setResponsaveis(Set<Usuario> responsaveis) {
		this.responsaveis = responsaveis;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Restaurante other = (Restaurante) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Restaurante [id=" + id + ", nome=" + nome + ", taxaFrete=" + taxaFrete + ", cozinha=" + cozinha
				+ ", formasPagamento=" + formasPagamento + ", endereco=" + endereco + ", dataCadastro=" + dataCadastro
				+ ", dataAtualizacao=" + dataAtualizacao + ", produtos=" + produtos + ", ativo=" + ativo + ", aberto="
				+ aberto + ", responsaveis=" + responsaveis + "]";
	}

	public void ativar() {
		this.setAtivo(true);
	}

	public void inativar() {
		this.setAtivo(false);
	}

	public void removerFormaPagamento(FormaPagamento formaPagamento) {
		this.getFormasPagamento().remove(formaPagamento);

	}

	public void adicionarFormaPagamento(FormaPagamento formaPagamento) {
		this.getFormasPagamento().add(formaPagamento);

	}

	public void abrir() {
		this.setAberto(true);
	}

	public void fechar() {
		this.setAberto(false);
	}

	public void removerResponsavel(Usuario usuario) {
		this.getResponsaveis().remove(usuario);
	}

	public void adicionarResponsavel(Usuario usuario) {
		this.getResponsaveis().add(usuario);
	}

	public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
		return this.getFormasPagamento().contains(formaPagamento);
	}

	public boolean naoAceitaFormaPagamento(FormaPagamento formaPagamento) {
		return !this.aceitaFormaPagamento(formaPagamento);
	}

	public boolean ativacaoPermitida() {
		return this.isInativo();
	}

	private boolean isInativo() {
		return !isAtivo();
	}

	public boolean inativacaoPermitida() {
		return this.isAtivo();
	}

	private boolean isAtivo() {
		return this.ativo;
	}

	public boolean aberturaPermitida() {
		return this.isAtivo() && this.isFechado();
	}

	private boolean isFechado() {
		return !isAberto();
	}

	public boolean fechamentoPermitido() {
		return isAberto();
	}

	private boolean isAberto() {
		return this.aberto;
	}
}
