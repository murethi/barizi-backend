package com.barizicommunications.barizi.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ExceptionResponse(String message, HttpStatus status) {
}
