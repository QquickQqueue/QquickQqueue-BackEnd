package com.example.qquickqqueue.exception;

import com.example.qquickqqueue.util.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import java.security.InvalidParameterException;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
	@InjectMocks
	private GlobalExceptionHandler globalExceptionHandler;

	@Nested
	@DisplayName("globalExceptionHandler Test")
	class ExceptionHandler {
		@Test
		@DisplayName("handleException Test")
		void handleExceptionTest() {
			// given
			Exception exception = new Exception("test exception");

			// when
			ResponseEntity<Message> response = globalExceptionHandler.handleException(exception);

			// then
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
			assertEquals("test exception", Objects.requireNonNull(response.getBody()).getMessage());
		}

		@Test
		@DisplayName("handleRuntimeException Test")
		void handleRuntimeExceptionTest() {
			// given
			RuntimeException exception = new RuntimeException("test runtime exception");

			// when
			ResponseEntity<Message> response = globalExceptionHandler.handleRuntimeException(exception);

			// then
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
			assertEquals("test runtime exception", Objects.requireNonNull(response.getBody()).getMessage());
		}

		@Test
		@DisplayName("handleJsonProcessingException Test")
		void handleJsonProcessingExceptionTest() {
			// given
			JsonProcessingException exception = mock(JsonProcessingException.class);
			when(exception.getMessage()).thenReturn("test handleJsonProcessing exception");

			// when
			ResponseEntity<Message> response = globalExceptionHandler.handleJsonProcessingException(exception);

			// then
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
			assertEquals("test handleJsonProcessing exception", Objects.requireNonNull(response.getBody()).getMessage());
		}

		@Test
		@DisplayName("handleRestClientException Test")
		void handleRestClientExceptionTest() {
			// given
			RestClientException exception = new RestClientException("test rest client exception");

			// when
			ResponseEntity<Message> response = globalExceptionHandler.handleRestClientException(exception);

			// then
			assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
			assertEquals("test rest client exception", Objects.requireNonNull(response.getBody()).getMessage());
		}

		@Test
		@DisplayName("handleHttpClientErrorException Test")
		void handleHttpClientErrorExceptionTest() {
			// given
			HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_GATEWAY, "test http client error exception");

			// when
			ResponseEntity<Message> response = globalExceptionHandler.handleHttpClientErrorException(exception);

			// then
			assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
			assertEquals("502 test http client error exception", Objects.requireNonNull(response.getBody()).getMessage());
		}

		@Test
		@DisplayName("handleEntityNotFoundException Test")
		void handleEntityNotFoundExceptionTest() {
			// given
			EntityNotFoundException exception = new EntityNotFoundException("test entity not found exception");

			// when
			ResponseEntity<Message> response = globalExceptionHandler.handleEntityNotFoundException(exception);

			// then
			assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
			assertEquals("test entity not found exception", Objects.requireNonNull(response.getBody()).getMessage());
		}

		@Test
		@DisplayName("handleUsernameNotFoundException Test")
		void handleUsernameNotFoundExceptionTest() {
			// given
			UsernameNotFoundException exception = new UsernameNotFoundException("test username not found exception");

			// when
			ResponseEntity<Message> response = globalExceptionHandler.handleUsernameNotFoundException(exception);

			// then
			assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
			assertEquals("test username not found exception", Objects.requireNonNull(response.getBody()).getMessage());
		}

		@Test
		@DisplayName("handleInvalidParameterException Test")
		void handleInvalidParameterExceptionTest() {
			// given
			InvalidParameterException exception = new InvalidParameterException("test invalid parameter exception");

			// when
			ResponseEntity<Message> response = globalExceptionHandler.handleInvalidParameterException(exception);

			// then
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
			assertEquals("test invalid parameter exception", Objects.requireNonNull(response.getBody()).getMessage());
		}

		@Test
		@DisplayName("handleIllegalArgumentException Test")
		void handleIllegalArgumentExceptionTest() {
			// given
			IllegalArgumentException exception = new IllegalArgumentException("test illegal argument exception");

			// when
			ResponseEntity<Message> response = globalExceptionHandler.handleIllegalArgumentException(exception);

			// then
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
			assertEquals("test illegal argument exception", Objects.requireNonNull(response.getBody()).getMessage());
		}

		@Test
		@DisplayName("handleIllegalStateException Test")
		void handleIllegalStateExceptionTest() {
			// given
			IllegalStateException exception = new IllegalStateException("test illegal state exception");

			// when
			ResponseEntity<Message> response = globalExceptionHandler.handleIllegalStateException(exception);

			// then
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
			assertEquals("test illegal state exception", Objects.requireNonNull(response.getBody()).getMessage());
		}

		@Test
		@DisplayName("handleValidationException Test")
		void handleValidationExceptionTest() {
			// given
			ValidationException exception = new ValidationException("test validation exception");

			// when
			ResponseEntity<Message> response = globalExceptionHandler.handleValidationException(exception);

			// then
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
			assertEquals("test validation exception", Objects.requireNonNull(response.getBody()).getMessage());
		}
	}
}