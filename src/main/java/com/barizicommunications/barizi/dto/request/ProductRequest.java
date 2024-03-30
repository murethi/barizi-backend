package com.barizicommunications.barizi.dto.request;

import lombok.Builder;

@Builder
/**
 * Object holds request data related to product
 */
public record ProductRequest(String name, String description,int minimumStockLevel) {
}
