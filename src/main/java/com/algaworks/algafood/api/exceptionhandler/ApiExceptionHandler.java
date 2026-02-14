package com.algaworks.algafood.api.exceptionhandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	private static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se o problema persistir, entre em contato com o administrador do sistema.";
	private final MessageSource messageSource;

	public ApiExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e,
			WebRequest webRequest) {
		var problem = this
				.createProblemBuilder(HttpStatus.NOT_FOUND, ProblemType.RECURSO_NAO_ENCONTRADO, e.getMessage())
				.userMessage(e.getMessage()).build();
		return this.handleExceptionInternal(e, problem, new HttpHeaders(), HttpStatus.NOT_FOUND, webRequest);
	}

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException e, WebRequest webRequest) {
		var problem = this.createProblemBuilder(HttpStatus.BAD_REQUEST, ProblemType.ERRO_NEGOCIO, e.getMessage())
				.userMessage(e.getMessage()).build();
		return this.handleExceptionInternal(e, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
	}

	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException e, WebRequest webRequest) {
		var problem = this.createProblemBuilder(HttpStatus.CONFLICT, ProblemType.ENTIDADE_EM_USO, e.getMessage())
				.userMessage(e.getMessage()).build();
		return this.handleExceptionInternal(e, problem, new HttpHeaders(), HttpStatus.CONFLICT, webRequest);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode statusCode, WebRequest request) {
		if (body instanceof String)
			body = new Problem.Builder().timestamp(OffsetDateTime.now()).title((String) body).status(statusCode.value())
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		return super.handleExceptionInternal(ex, body, headers, statusCode, request);
	}

	private Problem.Builder createProblemBuilder(HttpStatus httpStatus, ProblemType problemType, String detail) {
		return new Problem.Builder().timestamp(OffsetDateTime.now()).status(httpStatus.value())
				.type(problemType.getUri()).title(problemType.getTitle()).detail(detail);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		var throwable = ExceptionUtils.getRootCause(ex);
		if (throwable instanceof InvalidFormatException) {
			return this.handleInvalidFormatException((InvalidFormatException) throwable, headers, status, request);
		} else if (throwable instanceof PropertyBindingException) {
			return handlePropertyBindingException((PropertyBindingException) throwable, headers, status, request);
		}
		var problem = this
				.createProblemBuilder((HttpStatus) status, ProblemType.MENSAGEM_INCOMPREENSIVEL,
						"O corpo da requisição está inválido. Verifique erro de sintaxe.")
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		return this.handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		String path = this.joinPath(ex.getPath()), detail = String.format(
				"A propriedade '%s' recebeu o valor '%s', "
						+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());
		var problem = this.createProblemBuilder((HttpStatus) status, ProblemType.MENSAGEM_INCOMPREENSIVEL, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		return this.handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException throwable,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String path = this.joinPath(throwable.getPath()), detail = String
				.format("A propriedade '%s' não existe. Corrija ou remova essa propriedade e tente novamente.", path);
		var problem = this.createProblemBuilder((HttpStatus) status, ProblemType.MENSAGEM_INCOMPREENSIVEL, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		return this.handleExceptionInternal(throwable, problem, headers, status, request);
	}

	private String joinPath(List<Reference> path) {
		return path.stream().map(Reference::getFieldName).collect(Collectors.joining("."));
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException typeMismatchException,
			HttpHeaders httpHeaders, HttpStatusCode httpStatusCode, WebRequest webRequest) {
		if (typeMismatchException instanceof MethodArgumentTypeMismatchException) {
			return this.handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) typeMismatchException,
					httpHeaders, (HttpStatus) httpStatusCode, webRequest);
		}
		return super.handleTypeMismatch(typeMismatchException, httpHeaders, httpStatusCode, webRequest);
	}

	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
			MethodArgumentTypeMismatchException typeMismatchException, HttpHeaders httpHeaders, HttpStatus httpStatus,
			WebRequest webRequest) {
		var detail = String.format(
				"O parâmetro de URL '%s' recebeu o valor '%s', que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				typeMismatchException.getName(), typeMismatchException.getValue(),
				typeMismatchException.getRequiredType().getSimpleName());
		var problem = this.createProblemBuilder(httpStatus, ProblemType.PARAMETRO_INVALIDO, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		return this.handleExceptionInternal(typeMismatchException, problem, httpHeaders, httpStatus, webRequest);
	}

	@Override
	protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException noResourceFoundException,
			HttpHeaders httpHeaders, HttpStatusCode httpStatusCode, WebRequest webRequest) {
		var detail = String.format("O recurso %s, que você tentou acessar, é inexistente.",
				noResourceFoundException.getResourcePath());
		var problem = this.createProblemBuilder((HttpStatus) httpStatusCode, ProblemType.RECURSO_NAO_ENCONTRADO, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		return this.handleExceptionInternal(noResourceFoundException, problem, httpHeaders, httpStatusCode, webRequest);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception exception, WebRequest webRequest) {
		var problem = this.createProblemBuilder(HttpStatus.INTERNAL_SERVER_ERROR, ProblemType.ERRO_DE_SISTEMA,
				MSG_ERRO_GENERICA_USUARIO_FINAL).userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		return this.handleExceptionInternal(exception, problem, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
				webRequest);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException methodArgumentNotValidException, HttpHeaders httpHeaders,
			HttpStatusCode httpStatusCode, WebRequest webRequest) {
		return this.handleValidationInternal(methodArgumentNotValidException,
				methodArgumentNotValidException.getBindingResult(), httpHeaders, httpStatusCode, webRequest);
	}

	private ResponseEntity<Object> handleValidationInternal(Exception exception, BindingResult bindingResult,
			HttpHeaders httpHeaders, HttpStatusCode httpStatusCode, WebRequest request) {
		var detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";
		List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream().map(objectError -> {
			var name = objectError.getObjectName();
			if (objectError instanceof FieldError)
				name = ((FieldError) objectError).getField();
			return new Problem.Object(name,
					this.messageSource.getMessage(objectError, LocaleContextHolder.getLocale()));
		}).collect(Collectors.toList());
		var problem = this.createProblemBuilder((HttpStatus) httpStatusCode, ProblemType.DADOS_INVALIDOS, detail)
				.userMessage(detail).objects(problemObjects).build();
		return this.handleExceptionInternal(exception, problem, httpHeaders, httpStatusCode, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		return ResponseEntity.status(status).headers(headers).build();
	}
}
