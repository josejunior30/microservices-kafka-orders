package com.junior.Pedido.exceptions;

import java.time.Instant;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.junior.Pedido.DTO.ApiError;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
		ApiError err = new ApiError(Instant.now(), HttpStatus.NOT_FOUND.value(), "Resource not found", ex.getMessage(),
				req.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiError> handleBusiness(BusinessException ex, HttpServletRequest req) {
		ApiError err = new ApiError(Instant.now(), HttpStatus.UNPROCESSABLE_ENTITY.value(), "Business rule violation",
				ex.getMessage(), req.getRequestURI());
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiError> handleIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
		ApiError err = new ApiError(Instant.now(), HttpStatus.CONFLICT.value(), "Data integrity violation",
				"Operação não pôde ser concluída por restrição de integridade.", req.getRequestURI());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
	}


}