package com.fintech.msavaliadorcredito.infra.clients;

import com.fintech.msavaliadorcredito.domain.model.DadosCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ms-clientes", path = "/clientes")
public interface ClienteControllerClient {

    @GetMapping(params = "cpf")
    ResponseEntity<DadosCliente> dadosDoCliente(@RequestParam("cpf") String cpf);
}
