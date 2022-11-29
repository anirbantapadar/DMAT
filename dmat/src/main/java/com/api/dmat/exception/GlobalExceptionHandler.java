package com.api.dmat.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.api.dmat.responses.saveResponseAPIResponse.SaveResponseAPIResponseClass;


@Configuration
@ConfigurationProperties(prefix = "response.messages")
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	@Value(value = "${invalidAssessmentId}")
	private String invalidAssessmentIdMessage ;
	@Value(value = "${invalidRequest}")
	private String invalidRequestMessage ;
	@Value(value="${attemptsExceeded}")
	private String attemptsExceededMessage ;
	@Value(value = "${unknownError}")
	private String unknownErrorMessage ;

	
	@ExceptionHandler(value = InvalidAssessmentIdException.class)
	public ResponseEntity<SaveResponseAPIResponseClass> invalidAssessmentIdResponse(InvalidAssessmentIdException exception ){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new SaveResponseAPIResponseClass(invalidAssessmentIdMessage, 
						HttpStatus.BAD_REQUEST ,
						HttpStatus.BAD_REQUEST.value()));
	}
	
	@ExceptionHandler(value = InvalidRequestException.class)
	public ResponseEntity<SaveResponseAPIResponseClass> invalidResponse(InvalidRequestException exception ){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new SaveResponseAPIResponseClass(invalidRequestMessage,
						HttpStatus.BAD_REQUEST,
						HttpStatus.BAD_REQUEST.value()));
	}
	
	@ExceptionHandler(value = AttemptsExceededException.class)
	public ResponseEntity<SaveResponseAPIResponseClass> attemptsExceededResponse(AttemptsExceededException exception ){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new SaveResponseAPIResponseClass(attemptsExceededMessage,
						HttpStatus.BAD_REQUEST,
						HttpStatus.BAD_REQUEST.value()));
	}

}
