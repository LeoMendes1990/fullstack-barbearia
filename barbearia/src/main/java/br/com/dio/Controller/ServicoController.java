package br.com.dio.Controller;

import br.com.dio.dtos.ServicoDTO;
import br.com.dio.models.Servico;
import br.com.dio.service.ServicoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
@CrossOrigin(origins = "http://localhost:3000")
public class ServicoController {
    @Autowired
    private ServicoService servicoService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ServicoDTO> create(@RequestBody ServicoDTO servicoDTO) {
        Servico servico = modelMapper.map(servicoDTO, Servico.class);
        Servico servicoSalvo = servicoService.save(servico);
        ServicoDTO servicoDTO1 = modelMapper.map(servicoSalvo, ServicoDTO.class);
        return ResponseEntity.ok(servicoDTO1);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoDTO> findById(@PathVariable Long id) {
        Servico servico = servicoService.findById(id);
        return ResponseEntity.ok().body(modelMapper.map(servico, ServicoDTO.class));
    }

    @GetMapping
    public ResponseEntity<List<ServicoDTO>> findAll() {
        List<Servico> list = servicoService.findAll();
        return ResponseEntity.ok().body(list.stream()
                .map(obj -> modelMapper.map(obj, ServicoDTO.class))
                .toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoDTO> update(@PathVariable Long id, @RequestBody ServicoDTO servicoDTO) {
        Servico servico = modelMapper.map(servicoDTO, Servico.class);
        Servico servicoAtualizado = servicoService.update(id, servico);
        if (servicoAtualizado != null) {
            ServicoDTO servicoDtoAtualizado = modelMapper.map(servicoAtualizado, ServicoDTO.class);
            return ResponseEntity.ok(servicoDtoAtualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            servicoService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
