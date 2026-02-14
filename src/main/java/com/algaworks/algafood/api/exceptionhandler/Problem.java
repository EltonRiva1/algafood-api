package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Problem {
	private final Integer status;
	private final OffsetDateTime timestamp;
	private final String type, title, detail, userMessage;
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

	public record Object(String name, String userMessage) {
	}
}
