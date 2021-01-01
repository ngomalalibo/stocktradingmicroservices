package com.stocktrading.customer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientChangeModel
{
    private String type;
    private String action;
    private String clientId;
    private String correlationId;
}