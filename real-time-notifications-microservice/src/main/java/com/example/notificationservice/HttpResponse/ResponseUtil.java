package com.example.notificationservice.HttpResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public class ResponseUtil {
    // Método genérico para crear respuestas con datos
    public static <T> Mono<ResponseEntity<ApiResponse<T>>> createResponse(String status, String message, T data, HttpStatus httpStatus) {
        return Mono.just(ResponseEntity.status(httpStatus).body(new ApiResponse<>(status, message, data)));
    }

    // Respuesta de éxito con datos
    public static <T> Mono<ResponseEntity<ApiResponse<T>>> createSuccessResponse(String message, T data) {
        return createResponse("success", message, data, HttpStatus.OK);
    }

    // Respuesta de error con mensaje y código HTTP (sin datos)
    public static <T> Mono<ResponseEntity<ApiResponse<T>>> createErrorResponse(String message, HttpStatus httpStatus) {
        return createResponse("error", message, null, httpStatus);
    }
}
