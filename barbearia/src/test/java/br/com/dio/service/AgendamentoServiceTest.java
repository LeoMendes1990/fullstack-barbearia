package br.com.dio.service;

import br.com.dio.models.Agendamento;
import br.com.dio.models.Cliente;
import br.com.dio.models.Servico;
import br.com.dio.repository.AgendamentoRepository;
import br.com.dio.repository.ClienteRepository;
import br.com.dio.repository.ServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AgendamentoServiceTest {

    private static final Long ID = 1L;
    private static final LocalDateTime DATA_HORA = LocalDateTime.now();
    private static final Long CLIENTE_ID = 1L;
    private static final Long SERVICO_ID = 1L;

    @InjectMocks
    private AgendamentoService service;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ServicoRepository servicoRepository;

    private Agendamento agendamento;
    private Cliente cliente;
    private Servico servico;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startAgendamento();
    }

    @Test
    void whenSalvarAgendamentoThenReturnSuccess() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.of(servico));
        when(agendamentoRepository.save(any())).thenReturn(agendamento);

        Agendamento response = service.salvarAgendamento(agendamento);

        assertNotNull(response);
        assertEquals(Agendamento.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(DATA_HORA, response.getDataHora());
        assertEquals(cliente, response.getCliente());
        assertEquals(servico, response.getServico());
    }

    @Test
    void whenSalvarAgendamentoWithInvalidClienteThenThrowException() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.salvarAgendamento(agendamento));
        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    void whenSalvarAgendamentoWithInvalidServicoThenThrowException() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.salvarAgendamento(agendamento));
        assertEquals("Serviço não encontrado", exception.getMessage());
    }

    @Test
    void whenFindAllThenReturnAnListOfAgendamentos() {
        when(agendamentoRepository.findAll()).thenReturn(List.of(agendamento));

        List<Agendamento> response = service.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(Agendamento.class, response.get(0).getClass());
        assertEquals(ID, response.get(0).getId());
        assertEquals(DATA_HORA, response.get(0).getDataHora());
        assertEquals(cliente, response.get(0).getCliente());
        assertEquals(servico, response.get(0).getServico());
    }

    private void startAgendamento() {
        cliente = new Cliente(CLIENTE_ID, "João", "joao@email.com", "11999999999");
        servico = new Servico(SERVICO_ID, "Corte", 50.00);
        agendamento = new Agendamento();
        agendamento.setId(ID);
        agendamento.setDataHora(DATA_HORA);
        agendamento.setCliente(cliente);
        agendamento.setServico(servico);
    }
}
