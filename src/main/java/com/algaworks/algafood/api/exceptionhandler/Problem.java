package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Problema")
public class Problem {
	@Schema(example = "400")
	private final Integer status;
	@Schema(example = "2026-09-07T11:21:50.902245498Z")
	private final OffsetDateTime timestamp;
	@Schema(example = "https://algafood.com.br/dados-invalidos")
	private final String type;
	@Schema(example = "Dados inválidos")
	private final String title;
	@Schema(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
	private final String detail;
	@Schema(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
	private final String userMessage;
	@Schema(description = "Lista de objetos ou campos que geraram o erro")
	private final List<Object> objects;

	private Problem(Builder builder) {
		this.type = builder.type;
		this.title = builder.title;
		this.detail = builder.detail;
		this.status = builder.status;
		this.userMessage = builder.userMessage;
		this.timestamp = builder.timestamp;
		this.objects = builder.objects;
	}

	public String getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public String getDetail() {
		return detail;
	}

	public Integer getStatus() {
		return status;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public OffsetDateTime getTimestamp() {
		return timestamp;
	}

	public List<Object> getObjects() {
		return objects;
	}

	public static class Builder {
		private Integer status;
		private String type, title, detail, userMessage;
		private OffsetDateTime timestamp;
		private List<Object> objects;

		public Builder type(String type) {
			this.type = type;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder detail(String detail) {
			this.detail = detail;
			return this;
		}

		public Builder status(Integer status) {
			this.status = status;
			return this;
		}

		public Builder userMessage(String userMessage) {
			this.userMessage = userMessage;
			return this;
		}

		public Builder timestamp(OffsetDateTime timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public Builder objects(List<Object> objects) {
			this.objects = objects;
			return this;
		}

		public Problem build() {
			return new Problem(this);
		}
	}

	@Schema(name = "ObjetoProblema")
	public record Object(@Schema(example = "preço") String name,
			@Schema(example = "O preço é inválido") String userMessage) {
	}
}
