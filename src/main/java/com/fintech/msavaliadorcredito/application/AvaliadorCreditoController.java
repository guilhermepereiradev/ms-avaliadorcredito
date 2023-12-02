package com.fintech.msavaliadorcredito.application;

import com.fintech.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import com.fintech.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicesException;
import com.fintech.msavaliadorcredito.domain.model.DadosAvaliacao;
import com.fintech.msavaliadorcredito.domain.model.RetornoAvaliacaoCliente;
import com.fintech.msavaliadorcredito.domain.model.SitucaoCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {

    private final AvaliadorCreditoService avaliadorCreditoService;
    @GetMapping
    public String status() {
        return "ok!";
    }

    @GetMapping(value = "/situacao-cliente", params = "cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf) {
        try{
            SitucaoCliente situcaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situcaoCliente);
        } catch (DadosClienteNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroservicesException e){
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dados) {
        try{
            RetornoAvaliacaoCliente retornoAvaliacaoCliente =
                    avaliadorCreditoService.retornoAvaliacao(dados.getCpf(), dados.getRenda());

            return ResponseEntity.ok(retornoAvaliacaoCliente);
        } catch (DadosClienteNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroservicesException e){
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }
}
