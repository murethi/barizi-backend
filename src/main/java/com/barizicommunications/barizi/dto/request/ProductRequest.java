package com.barizicommunications.barizi.dto.request;

import lombok.Builder;

@Builder
public record ProductRequest(String name, String description) {
}
