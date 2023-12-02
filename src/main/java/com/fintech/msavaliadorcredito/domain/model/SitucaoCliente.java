package com.fintech.msavaliadorcredito.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SitucaoCliente {

    private DadosCliente cliente;
    private List<CartaoCliente> cartoes;
}
