package br.com.dio.repository;

import br.com.dio.models.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico,Long> {
}
