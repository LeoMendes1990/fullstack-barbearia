package br.com.dio.service;

import br.com.dio.models.Servico;
import br.com.dio.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ServicoService {
    @Autowired
    private ServicoRepository servicoRepository;

    public Servico save(Servico servico) {
        return servicoRepository.save(servico);
    }

    public List<Servico> findAll() {
        return servicoRepository.findAll();
    }
    public Servico findById(Long id){
        Optional<Servico> optionalServico = servicoRepository.findById(id);
        Servico servico = optionalServico.get();
        return servico;
    }

    public Servico update(Long id, Servico servico) {
        if (servicoRepository.existsById(id)) {
            servico.setId(id);
            return servicoRepository.save(servico);
        }
        return null; // Atendente não encontrado
    }
    public boolean delete(Long id) {
        if (servicoRepository.existsById(id)) {
            servicoRepository.deleteById(id);
            return true;
        }
        return false; // Atendente não encontrado
    }
}
