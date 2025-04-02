package br.com.dio.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "yyyy - MM - dd HH:ss")
    private LocalDateTime dataHora = LocalDateTime.now();

    @ManyToOne
    private Cliente cliente;
    @ManyToOne
    private Servico servico;

    public Agendamento() {
    }

    public Agendamento(Long id, LocalDateTime dataHora, Cliente cliente, Servico servico) {
        this.id = id;
        this.dataHora = dataHora;
        this.cliente = cliente;
        this.servico = servico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
