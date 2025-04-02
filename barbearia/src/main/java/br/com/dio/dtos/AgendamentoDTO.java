package br.com.dio.dtos;

import br.com.dio.models.Cliente;
import br.com.dio.models.Servico;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

public class AgendamentoDTO {
    private LocalDateTime dataHora = LocalDateTime.now();


    private Cliente cliente;

    private Servico servico;

    public AgendamentoDTO() {
    }

    public AgendamentoDTO(LocalDateTime dataHora, Cliente cliente, Servico servico) {
        this.dataHora = dataHora;
        this.cliente = cliente;
        this.servico = servico;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }
}
