package br.com.dio.Controller;

import br.com.dio.dtos.AgendamentoDTO;
import br.com.dio.models.Agendamento;
import br.com.dio.service.AgendamentoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agendamentos")
@CrossOrigin(origins = "http://localhost:3000")
public class AgendamentoController {
    @Autowired
    private AgendamentoService agendamentoService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<AgendamentoDTO> create(@RequestBody AgendamentoDTO agendamentoDTO) {
        try {
            Agendamento agendamento = modelMapper.map(agendamentoDTO, Agendamento.class);
            Agendamento novoAgendamento = agendamentoService.salvarAgendamento(agendamento);
            AgendamentoDTO agendamentoCriado = modelMapper.map(novoAgendamento, AgendamentoDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoCriado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoDTO> update(@PathVariable Long id, @RequestBody AgendamentoDTO agendamentoDTO) {
        try {
            Agendamento agendamento = modelMapper.map(agendamentoDTO, Agendamento.class);
            Agendamento agendamentoAtual = agendamentoService.update(id, agendamento);
            if (agendamentoAtual != null) {
                AgendamentoDTO agendamentoAtualizado = modelMapper.map(agendamentoAtual, AgendamentoDTO.class);
                return ResponseEntity.ok(agendamentoAtualizado);
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoDTO> findById(@PathVariable Long id) {
        try {
            Agendamento agendamento = agendamentoService.findById(id);
            return ResponseEntity.ok().body(modelMapper.map(agendamento, AgendamentoDTO.class));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoDTO>> findAll() {
        List<Agendamento> agendamentos = agendamentoService.findAll();
        List<AgendamentoDTO> dtos = agendamentos.stream()
                .map(obj -> modelMapper.map(obj, AgendamentoDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            agendamentoService.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
