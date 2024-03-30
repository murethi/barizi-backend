package com.barizicommunications.barizi.controllers;

import com.barizicommunications.barizi.dto.response.ProductResponse;
import com.barizicommunications.barizi.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PutMapping("{id}")
    public ProductResponse update(@PathVariable UUID id,@RequestParam int quantity){
        return inventoryService.updateStock(id,quantity);
    }
}
