package br.com.alura.forum.config.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationErrorHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ValidationErrorDTO> handleMethodArgumentException(MethodArgumentNotValidException exception) {
		List<ValidationErrorDTO> listErrorsDTO = new ArrayList<>();
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		
		fieldErrors.forEach(e -> {
			String message = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			ValidationErrorDTO erro = new ValidationErrorDTO(e.getField(), message);
			listErrorsDTO.add(erro);
		});
		
		return listErrorsDTO;
	}
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(PropertyReferenceException.class)
	public ValidationErrorDTO handlePropertyReferenceException(PropertyReferenceException exception) {		
		return new ValidationErrorDTO(exception.getPropertyName(), exception.getLocalizedMessage());
	}
}
