package cloud.mmda.core.api;

import cloud.mmda.core.entities.ValidationError;
import cloud.mmda.core.file.exceptions.ExcelException;
import cloud.mmda.core.services.exceptions.DataInvalidException;
import cloud.mmda.core.services.exceptions.DomainException;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局异常处理器。
 * <p>
 *     错误响应统一遵循{@link ErrorResponse}接口，你抛出{@link ErrorResponseException}异常即可。
 *     我们在这里统一处理业务层{@link DomainException}，渲染格式统一为：
 *     <ul>
 *         <li>status是HTTP状态</li>
 *         <li>title标题</li>
 *         <li>details错误详情，通过设置异常code自动将消息国际化</li>
 *         <li>type错误参考URL</li>
 *         <li>instance请求URL</li>
 *         <li>errors 自定义的错误列表</li>
 *     </ul>
 * </p>
 * @remark
 * <p>
 *     在WebFlux Config中注册WebExceptionHandler组件，使用@Order注解指示它们的处理顺序。
 *     其中内置的ResponseStatusExceptionHandler组件负责处理ResponseStatusException异常，
 *     WebFluxResponseStatusExceptionHandler扩展了它的功能，能自动确定使用了@ResponseStatus注解的异常类对应的HTTP Status。
 * </p>
 * @see <a href="https://www.baeldung.com/spring-boot-custom-webflux-exceptions">Custom WebFlux Exceptions in Spring Boot 3</a>
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


   private MessageSource messageSource;

   public GlobalExceptionHandler(MessageSource messageSource) {
       this.messageSource = messageSource;
   }

    @Override
    protected Mono<ResponseEntity<Object>> handleWebExchangeBindException(WebExchangeBindException ex, HttpHeaders headers, HttpStatusCode status, ServerWebExchange exchange) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problemDetail.setType(URI.create("https://www.mmda.cloud/problems/"));
        String code="invalid.request.content";
        problemDetail.setProperty("code", code);
        String detail = messageSource.getMessage(code,null,ex.getLocalizedMessage(), LocaleContextHolder.getLocale());
        problemDetail.setDetail(detail);
        List<ValidationError> errors = ex.getFieldErrors().stream()
                .map(fieldError ->  ValidationError.valueOf(formatFieldName(fieldError.getField()), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        problemDetail.setProperty("validationErrors", errors);
        return Mono.just(ResponseEntity.status(status).body(problemDetail));
    }

    @Override
    protected Mono<ResponseEntity<Object>> handleServerWebInputException(ServerWebInputException ex, HttpHeaders headers, HttpStatusCode status, ServerWebExchange exchange) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problemDetail.setType(URI.create("https://www.mmda.cloud/problems/"));
        String code="invalid.param";
        problemDetail.setProperty("code", code);
        String detail = messageSource.getMessage(code,null,ex.getLocalizedMessage(), LocaleContextHolder.getLocale());
        problemDetail.setDetail(detail);
        return Mono.just(ResponseEntity.status(status).body(problemDetail));
    }

    private static String formatFieldName(String fieldName) {
        if (fieldName.contains("[") && fieldName.contains("]")) {
            String prefix = fieldName.substring(0, fieldName.indexOf('['));
            String index = fieldName.substring(fieldName.indexOf('[') + 1, fieldName.indexOf(']'));
            String actualFieldName = fieldName.substring(fieldName.indexOf(']') + 1).replace(".", "/");
            // 格式化为 prefix/index/fieldName
            return prefix + "/"+index + actualFieldName;
        }
        return fieldName;
    }

    @ExceptionHandler(DomainException.class)
    protected ErrorResponse handleNotFound(DomainException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
        problemDetail.setType(URI.create("https://www.mmda.cloud/problems/"));
        problemDetail.setProperty("code", ex.getCode());
        if (!ex.getMessage().equals(ex.getLocalizedMessage())) problemDetail.setProperty("code", ex.getMessage());
        String detail = messageSource.getMessage(ex.getMessage(), ex.getArgs(),ex.getLocalizedMessage(), LocaleContextHolder.getLocale());
        problemDetail.setDetail(detail);

       if (ex instanceof DataInvalidException dataInvalidException) {
            problemDetail.setProperty("validationErrors", dataInvalidException.getValidationErrors());
        }

       return ErrorResponse.builder(ex, problemDetail).build();
    }
    @ExceptionHandler(JwtException.class)
    protected ErrorResponse handleJwtException(JwtException ex) {
       String errorCode="invalid.token";
        if (ex instanceof JwtValidationException jwtValidationException) {
            errorCode = jwtValidationException.getErrors().stream()
                    .findFirst()
                    .map(OAuth2Error::getErrorCode)
                    .orElse(errorCode);
        }
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problemDetail.setType(URI.create("https://www.mmda.cloud/problems/"));
        problemDetail.setProperty("code", errorCode);
        problemDetail.setDetail(messageSource.getMessage(errorCode,null ,ex.getLocalizedMessage(), LocaleContextHolder.getLocale()));//localized message
        return ErrorResponse.builder(ex, problemDetail).build();
    }
    @ExceptionHandler(ExcelException.class)
    protected ErrorResponse handleExcelException(ExcelException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create("https://www.mmda.cloud/problems/"));
        problemDetail.setProperty("code", ex.getCode());
        String detail = messageSource.getMessage(ex.getMessage(),null,ex.getLocalizedMessage(), LocaleContextHolder.getLocale());
        problemDetail.setDetail(detail);
        return ErrorResponse.builder(ex, problemDetail).build();
    }
}
