package br.com.alura.forum.config.validation;

public class ValidationErrorDTO {
	
	private String field;
	private String message;
	
	public ValidationErrorDTO(String field, String message) {
		super();
		this.field = field;
		this.message = message;
	}

	public String getField() {
		return field;
	}

	public String getMessage() {
		return message;
	}
	
	
}
