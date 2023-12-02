package com.fintech.msavaliadorcredito.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartaoCliente {

    private String name;
    private String bandeira;
    private BigDecimal limiteLiberado;
}
