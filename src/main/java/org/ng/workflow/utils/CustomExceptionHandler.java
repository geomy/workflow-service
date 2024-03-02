package org.ng.workflow.utils;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ControllerAdvice
public class CustomExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ResponseEntity<?> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {

	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });

		Optional<ObjectError> errorOptional = ex.getBindingResult().getAllErrors().stream().findFirst();
		String errorMessage = null;
		if(errorOptional.isPresent()){
			errorMessage = errorOptional.get().getDefaultMessage();
		}

	    ApplicationResponse<Map<String, String>> errorDetails =   new ApplicationResponse<>(ResponseCode.BAD_REQUEST,errors, errorMessage);
	    return new ResponseEntity<>(errorDetails, HttpStatus.UNPROCESSABLE_ENTITY);
   }
}
