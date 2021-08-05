package com.algaworks.algafood.api.exceptionhandler;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

//ResponseEntityExceptionHandler é uma implementação padrão que trata exceptions internas do Spring MVC.
@ControllerAdvice // centraliza o tratamento de excessões disparadas pelos Controlles.
@JsonPOJOBuilder
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. "
			+ "Tente novamente e se o problema persistir, entre em contato com o administrador do sistema.";

	// interface para resolver mensagens.
	private MessageSource messageSource;

	private String messagem;

	public ApiExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Throwable rootCause = ExceptionUtils.getRootCause(ex);

		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
		}

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
		String userMensage = MSG_ERRO_GENERICA_USUARIO_FINAL;
		LocalDateTime timestamp = LocalDateTime.now();
		List<Problem.Object> problemFields = null;

		Problem problem = createProblem(status, problemType, detail, userMensage, timestamp, problemFields);
		problem.getUserMensage();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
	}

	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		// Criei o método joinPath para reaproveitar em todos os métodos que precisam
		// concatenar os nomes das propriedades (separando por ".")
		String path = joinPath(ex.getPath());

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format(
				"A Propriedade 's% não existe." + "corrija ou remova essa propriedade e tente novamente.", path);
		String userMensage = MSG_ERRO_GENERICA_USUARIO_FINAL;
		LocalDateTime timestamp = LocalDateTime.now();
		List<Problem.Object> problemFields = null;

		Problem problem = createProblem(status, problemType, detail, userMensage, timestamp, problemFields);

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		// String path = ex.getPath().stream().map(ref ->
		// ref.getFieldName()).collect(Collectors.joining("."));

		String path = joinPath(ex.getPath());

		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format(
				"A propriedade '%s' recebeu o valor '%s', "
						+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());
		String userMensage = MSG_ERRO_GENERICA_USUARIO_FINAL;
		LocalDateTime timestamp = LocalDateTime.now();
		List<Problem.Object> problemFields = null;

		Problem problem = createProblem(status, problemType, detail, userMensage, timestamp, problemFields);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
	}

	// 1. MethodArgumentTypeMismatchException é um subtipo de TypeMismatchException
	// 2. ResponseEntityExceptionHandler já trata TypeMismatchException de forma
	// mais abrangente
	// 3. Então, especializamos o método handleTypeMismatch e verificamos se a
	// exception
	// é uma instância de MethodArgumentTypeMismatchException
	// 4. Se for, chamamos um método especialista em tratar esse tipo de exception
	// 5. Poderíamos fazer tudo dentro de handleTypeMismatch, mas preferi separar em
	// outro método
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
		}

		return super.handleTypeMismatch(ex, headers, status, request);
	}

	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
		String detail = String.format(
				"O parâmetro de URL '%s' recebeu o valor '%s', "
						+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
		String userMensage = MSG_ERRO_GENERICA_USUARIO_FINAL;
		LocalDateTime timestamp = LocalDateTime.now();
		List<Problem.Object> problemFields = null;

		Problem problem = createProblem(status, problemType, detail, userMensage, timestamp, problemFields);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = String.format("O Recurso '%s', que você tentou passar, é inexistente", ex.getRequestURL());
		String userMensage = MSG_ERRO_GENERICA_USUARIO_FINAL;
		LocalDateTime timestamp = LocalDateTime.now();
		List<Problem.Object> problemFields = null;

		Problem problem = createProblem(status, problemType, detail, userMensage, timestamp, problemFields);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);

	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = ex.getMessage();
		String userMensage = detail;
		LocalDateTime timestamp = LocalDateTime.now();
		List<Problem.Object> problemFields = null;

		Problem problem = createProblem(status, problemType, detail, userMensage, timestamp, problemFields);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);

	}

	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUso(EntidadeEmUsoException ex, HttpServletRequest request) {

		HttpStatus status = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
		String detail = ex.getMessage();
		String userMensage = detail;
		LocalDateTime timestamp = LocalDateTime.now();
		List<Problem.Object> problemFields = null;

		Problem problem = createProblem(status, problemType, detail, userMensage, timestamp, problemFields);

		return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);

	}

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<Problem> handleNegocioException(NegocioException ex, HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = ex.getMessage();
		String userMensage = detail;
		LocalDateTime timestemp = LocalDateTime.now();
		List<Problem.Object> problemFields = null;

		Problem problem = createProblem(status, problemType, detail, userMensage, timestemp, problemFields);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);

	}

	// Adicionamos o método que tratará Exceptions genéricas não tratadas.
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, HttpServletRequest request) {

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;
		String userMensage = detail;
		LocalDateTime timestamp = LocalDateTime.now();
		List<Problem.Object> problemFields = null;

		// Importante colocar o printStackTrace (pelo menos por enquanto, que não
		// estamos
		// fazendo logging) para mostrar a stacktrace no console
		// Se não fizer isso, você não vai ver a stacktrace de exceptions que seriam
		// importantes
		// para você durante, especialmente na fase de desenvolvimento
		ex.printStackTrace();

		Problem problem = createProblem(status, problemType, detail, userMensage, timestamp, problemFields);
		problem.setUserMensage(detail);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
	}

	// É onde é capiturada a exception é lançada a execessão quando uma regra de
	// validação é violada.
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

		LocalDateTime timestamp = LocalDateTime.now();

		BindingResult bindingResult = ex.getBindingResult();

		List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream().map(objectError -> {
			String messagem = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

			String name = objectError.getObjectName();

			if (objectError instanceof FieldError) {
				name = ((FieldError) objectError).getField();
			}

			return new Problem.Object(name, messagem);

		}).collect(Collectors.toList());

		Problem problem = createProblem(status, problemType, detail, messagem, timestamp, problemObjects);
		problem.setObjects(problemObjects);
		problem.setUserMensage(messagem);
		problem.setUserMensage(detail);

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		Problem problem = new Problem();

		if (body == null) {

			body = problem;
			problem.setTimestamp(OffsetDateTime.now());
			problem.setTitle(status.getReasonPhrase());
			problem.setStatus(status.value());
			problem.setUserMensage(MSG_ERRO_GENERICA_USUARIO_FINAL);

		} else if (body instanceof String) {

			body = problem;
			problem.setTimestamp(OffsetDateTime.now());
			problem.setTitle((String) body);
			problem.setStatus(status.value());
			problem.setUserMensage(MSG_ERRO_GENERICA_USUARIO_FINAL);
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private Problem createProblem(HttpStatus status, ProblemType problemType, String detail, String userMensage,
			LocalDateTime timestamp, List<Problem.Object> problemFields) {

		Problem problem = new Problem();

		problem.setStatus(status.value());
		problem.setTimestamp(OffsetDateTime.now());
		problem.setType(problemType.getUri());
		problem.setTitle(problemType.getTitle());
		problem.setDetail(detail);
		problem.setUserMensage(userMensage);

		return problem;
	}

	// O método joinPath irá concatenar os nomes das propriedades, separando-as por
	// ".".
	private String joinPath(List<Reference> references) {

		return references.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
	}

}