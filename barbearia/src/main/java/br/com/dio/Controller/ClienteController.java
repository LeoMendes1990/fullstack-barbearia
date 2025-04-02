package br.com.dio.Controller;

import br.com.dio.dtos.ClienteDTO;
import br.com.dio.models.Cliente;
import br.com.dio.service.ClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")  
@CrossOrigin(origins = "http://localhost:3000")  
public class ClienteController {
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<ClienteDTO> findAll() {
        List<Cliente> clientes = clienteService.findAll();
        return clientes.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteDTO.class))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> findById(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id);
        return ResponseEntity.ok().body(modelMapper.map(cliente, ClienteDTO.class));
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> create(@RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);
        Cliente novoCliente = clienteService.create(cliente);
        ClienteDTO responseDTO = modelMapper.map(novoCliente, ClienteDTO.class);
        return ResponseEntity.ok(responseDTO); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> update(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);
        Cliente clienteAtualizado = clienteService.update(id, cliente);
        if (clienteAtualizado != null) {
            return ResponseEntity.ok(modelMapper.map(clienteAtualizado, ClienteDTO.class));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            boolean deleted = clienteService.delete(id);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest()
                .body("Não é possível excluir o cliente pois ele possui agendamentos.");
        }
    }
}
