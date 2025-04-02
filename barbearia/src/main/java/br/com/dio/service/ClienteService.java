package br.com.dio.service;

import br.com.dio.exception.ObjectNotFoundExceptions;
import br.com.dio.models.Cliente;
import br.com.dio.repository.AgendamentoRepository;
import br.com.dio.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private AgendamentoRepository agendamentoRepository;


    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Cliente findById(Long id) {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isPresent()) {
            return optionalCliente.get();
        }
        throw new ObjectNotFoundExceptions("Cliente não encontrado!!");
    }

    public Cliente create(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente update(Long id, Cliente cliente) {
        if (clienteRepository.existsById(id)) {
            cliente.setId(id);
            return clienteRepository.save(cliente);
        }
        return null; // Cliente não encontrado
    }

    public boolean delete(Long id) {
        if (clienteRepository.existsById(id)) {
            // Verifica se o cliente tem agendamentos associados
            if (agendamentoRepository.existsByClienteId(id)) {
                // Lança uma exceção personalizada ou uma exceção do Spring
                throw new DataIntegrityViolationException("Cliente está em Agendamento");
            }

            // Exclui o cliente se não houver agendamentos
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

