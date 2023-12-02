package com.fintech.msavaliadorcredito.application;

import com.fintech.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import com.fintech.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicesException;
import com.fintech.msavaliadorcredito.domain.model.*;
import com.fintech.msavaliadorcredito.infra.clients.CartoesControllerClient;
import com.fintech.msavaliadorcredito.infra.clients.ClienteControllerClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteControllerClient clienteClient;
    private final CartoesControllerClient cartoesClient;

    public SitucaoCliente obterSituacaoCliente(String cpf)
            throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteClient.dadosDoCliente(cpf);
            ResponseEntity<List<CartaoCliente>> dadosCartoesResponse = cartoesClient.getCartoesByCliente(cpf);

            return SitucaoCliente
                    .builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(dadosCartoesResponse.getBody())
                    .build();

        } catch (FeignException.FeignClientException e) {
            int status = e.status();

            if(HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }

            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }
    public RetornoAvaliacaoCliente retornoAvaliacao(String cpf, Long renda)
            throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {

        try{
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteClient.dadosDoCliente(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse = cartoesClient.getCartoesRendaAte(renda);

            List<CartaoAprovado> cartoesAprovados = cartoesResponse.getBody().stream().map(
                    cartao -> {
                        DadosCliente dadosCliente = dadosClienteResponse.getBody();
                        BigDecimal limiteBasico = cartao.getLimiteBasico();
                        BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());

                        BigDecimal fator = idadeBD.divide(BigDecimal.valueOf(10));
                        BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                        CartaoAprovado cartaoAprovado = new CartaoAprovado();
                        cartaoAprovado.setCartao(cartao.getNome());
                        cartaoAprovado.setBandeira(cartao.getBandeira());
                        cartaoAprovado.setLimiteAprovado(limiteAprovado);

                        return cartaoAprovado;
                    }
            ).toList();

            return new RetornoAvaliacaoCliente(cartoesAprovados);
        } catch (FeignException.FeignClientException e) {
            int status = e.status();

            if(HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }

            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }
}
