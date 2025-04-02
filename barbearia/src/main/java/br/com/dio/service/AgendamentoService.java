package br.com.dio.service;

import br.com.dio.exception.ObjectNotFoundExceptions;
import br.com.dio.models.Agendamento;
import br.com.dio.models.Cliente;
import br.com.dio.models.Servico;
import br.com.dio.repository.AgendamentoRepository;
import br.com.dio.repository.ClienteRepository;
import br.com.dio.repository.ServicoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class AgendamentoService {
    @Autowired
    private AgendamentoRepository agendamentoRepository;

   // @Autowired
   // private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    public Agendamento salvarAgendamento(Agendamento agendamento) {
        // Valida se Cliente existe
        Cliente cliente = clienteRepository.findById(agendamento.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Valida se Serviço existe
        Servico servico = servicoRepository.findById(agendamento.getServico().getId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        // Associa objetos ao agendamento
        agendamento.setCliente(cliente);
        agendamento.setServico(servico);

        // Salva no banco
        return agendamentoRepository.save(agendamento);
    }


    public List<Agendamento> findAll() {
        return agendamentoRepository.findAll();
    }

    public Agendamento findById(Long id) {
        Optional<Agendamento> optionalAgendamento = agendamentoRepository.findById(id);
        if (optionalAgendamento.isPresent()){
            return optionalAgendamento.get();
        }
        throw new ObjectNotFoundExceptions("Agendamento não encontrado!!");
    }

    public Agendamento create(Agendamento agendamento) {
        return agendamentoRepository.save(agendamento);
    }

    public Agendamento update(Long id, Agendamento agendamento) {
        Optional<Agendamento>optionalAgendamento =agendamentoRepository.findById(id);

        if (optionalAgendamento.isPresent()) {
            Agendamento agendamento1 = optionalAgendamento.get();
            BeanUtils.copyProperties(agendamento,agendamento1);
            return agendamentoRepository.save(agendamento1);
        }
        return null; // Cliente não encontrado
    }

    public boolean delete(Long id) {
        if (agendamentoRepository.existsById(id)){
        throw new IllegalArgumentException("Agendamento não pode ser Excluido porque tem cliente em atendimento!!");

        }
       agendamentoRepository.deleteById(id);
        return true;
    }
}
