package com.patrick.algafoodapi.api.exceptionhandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.patrick.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.patrick.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.patrick.algafoodapi.domain.exception.NegocioException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String MSG_ERRO_GENERICO_USURIO_FINAL = "Ocorreu um erro interno inesperado no sistema." +
            " Tente novamente e se o problema persistir, entre em contato" +
            " com o administrador do sistema.";

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleUncaught(NoHandlerFoundException ex,
                                                    HttpHeaders headers, HttpStatus status, WebRequest request){

        ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;

        String detail = String.format(MSG_ERRO_GENERICO_USURIO_FINAL);
        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .timestamp(LocalDateTime.now())
                .build();

        return handleExceptionInternal(ex, problem,headers, status, request);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex
            , HttpHeaders headers, HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.DADOS_INVALIDOS;

        String detail = String.format("Um ou mais campos  estão inválidos. Faça o preenchimento correto e tente novamente");

        BindingResult bindResult = ex.getBindingResult();

        List<Problem.Object> problemObjects = bindResult.getAllErrors().stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                    String name = objectError.getObjectName();

                    if(objectError instanceof FieldError){
                        name = ((FieldError) objectError).getField();
                    }

                    return Problem.Object.builder().name(name)
                            .userMessage(message)
                            .build();
                }).collect(Collectors.toList());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .objects(problemObjects)
                .timestamp(LocalDateTime.now())
                .build();

        return handleExceptionInternal(ex, problem,headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers, HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;

        String detail = String.format("Recurso %s, que você tentou acessar, é inexistente.", ex.getRequestURL());
        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICO_USURIO_FINAL)
                .timestamp(LocalDateTime.now())
                .build();

        return handleExceptionInternal(ex, problem,headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if(ex instanceof MethodArgumentTypeMismatchException ){
            return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                    HttpHeaders headers, HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

        String detail = String.format("O parâmetro da url '%s' recebeu o valor '%s', que é de um tipo inválido." +
                " Corrija e informe um valor compatível com o tipo %s.", ex.getName(),
                ex.getValue(), ex.getRequiredType().getSimpleName());
        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICO_USURIO_FINAL)
                .timestamp(LocalDateTime.now())
                .build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        }

        if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
        }

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICO_USURIO_FINAL)
                .timestamp(LocalDateTime.now())
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s' não existe na entidade '%s. Corrija ou remova essa propriedade."
                , path, ex.getReferringClass().getSimpleName());

        String userMessage = String.format(MSG_ERRO_GENERICO_USURIO_FINAL);

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(userMessage)
                .timestamp(LocalDateTime.now())
                .build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s' recebeu valor '%s', que é de um tipo inválido. " +
                "Corrija e informe o valor compatível com o tipo %s.", path, ex.getValue(), ex.getTargetType().getSimpleName());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICO_USURIO_FINAL)
                .timestamp(LocalDateTime.now())
                .build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;

        Problem problem = createProblemBuilder(status, problemType, ex.getMessage())
                .userMessage(MSG_ERRO_GENERICO_USURIO_FINAL)
                .timestamp(LocalDateTime.now())
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        ProblemType problemType = ProblemType.ERRO_NEGOCIO;

        Problem problem = createProblemBuilder(status, problemType, ex.getMessage())
                .userMessage(MSG_ERRO_GENERICO_USURIO_FINAL)
                .timestamp(LocalDateTime.now())
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        ProblemType problemType = ProblemType.ENTIDADE_EM_USO;

        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .timestamp(LocalDateTime.now())
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status
            , WebRequest request) {
        if (body == null) {
            body = Problem.builder()
                    .title(status.getReasonPhrase())
                    .status(status.value())
                    .build();
        } else if (body instanceof String) {
            body = Problem.builder()
                    .title((String) body)
                    .status(status.value())
                    .build();
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
        return Problem.builder().status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail);
    }

}
