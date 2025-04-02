package br.com.dio.repository;

import br.com.dio.models.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    boolean existsByClienteId(Long clienteId);
}
