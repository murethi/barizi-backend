package com.barizicommunications.barizi.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductResponse(UUID id, String name, String description,int currentStock,int minimumStockLevel) {
}
