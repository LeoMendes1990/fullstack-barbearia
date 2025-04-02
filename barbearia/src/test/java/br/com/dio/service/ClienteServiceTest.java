package br.com.dio.service;

import br.com.dio.exception.ObjectNotFoundExceptions;
import br.com.dio.models.Cliente;
import br.com.dio.repository.AgendamentoRepository;
import br.com.dio.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    private static final Long ID = 1L;
    private static final String NOME = "João Silva";
    private static final String EMAIL = "joao@email.com";
    private static final String TELEFONE = "11999999999";

    @InjectMocks
    private ClienteService service;

    @Mock
    private ClienteRepository repository;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    private Cliente cliente;
    private Optional<Cliente> optionalCliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startCliente();
    }

    @Test
    void whenFindByIdThenReturnAnClienteInstance() {
        when(repository.findById(anyLong())).thenReturn(optionalCliente);

        Cliente response = service.findById(ID);

        assertNotNull(response);
        assertEquals(Cliente.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NOME, response.getNome());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(TELEFONE, response.getTelefone());
    }

    @Test
    void whenFindByIdThenReturnObjectNotFoundException() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundExceptions.class, () -> service.findById(ID));
    }

    @Test
    void whenFindAllThenReturnAnListOfClientes() {
        when(repository.findAll()).thenReturn(List.of(cliente));

        List<Cliente> response = service.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(Cliente.class, response.get(0).getClass());
        assertEquals(ID, response.get(0).getId());
        assertEquals(NOME, response.get(0).getNome());
        assertEquals(EMAIL, response.get(0).getEmail());
        assertEquals(TELEFONE, response.get(0).getTelefone());
    }

    @Test
    void whenCreateThenReturnSuccess() {
        when(repository.save(any())).thenReturn(cliente);

        Cliente response = service.create(cliente);

        assertNotNull(response);
        assertEquals(Cliente.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NOME, response.getNome());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(TELEFONE, response.getTelefone());
    }

    @Test
    void whenUpdateThenReturnSuccess() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.save(any())).thenReturn(cliente);

        Cliente response = service.update(ID, cliente);

        assertNotNull(response);
        assertEquals(Cliente.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NOME, response.getNome());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(TELEFONE, response.getTelefone());
    }

    @Test
    void whenUpdateThenReturnNull() {
        when(repository.existsById(anyLong())).thenReturn(false);

        Cliente response = service.update(ID, cliente);

        assertNull(response);
    }

    @Test
    void whenDeleteWithNoAgendamentosThenSuccess() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(agendamentoRepository.existsByClienteId(anyLong())).thenReturn(false);
        doNothing().when(repository).deleteById(anyLong());

        boolean response = service.delete(ID);

        assertTrue(response);
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void whenDeleteWithAgendamentosThenThrowException() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(agendamentoRepository.existsByClienteId(anyLong())).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> service.delete(ID));
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void whenDeleteWithInvalidIdThenReturnFalse() {
        when(repository.existsById(anyLong())).thenReturn(false);

        boolean response = service.delete(ID);

        assertFalse(response);
        verify(repository, never()).deleteById(anyLong());
    }

    private void startCliente() {
        cliente = new Cliente(ID, NOME, EMAIL, TELEFONE);
        optionalCliente = Optional.of(cliente);
    }
}
