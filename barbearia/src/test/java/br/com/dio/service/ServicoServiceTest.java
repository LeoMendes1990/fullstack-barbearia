package br.com.dio.service;

import br.com.dio.models.Servico;
import br.com.dio.repository.ServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ServicoServiceTest {

    private static final Long ID = 1L;
    private static final String NOME = "Corte de Cabelo";
    private static final Double PRECO = 50.00;

    @InjectMocks
    private ServicoService service;

    @Mock
    private ServicoRepository repository;

    private Servico servico;
    private Optional<Servico> optionalServico;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startServico();
    }

    @Test
    void whenFindByIdThenReturnAnServicoInstance() {
        when(repository.findById(anyLong())).thenReturn(optionalServico);

        Servico response = service.findById(ID);

        assertNotNull(response);
        assertEquals(Servico.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NOME, response.getNome());
        assertEquals(PRECO, response.getPreco());
    }

    @Test
    void whenFindAllThenReturnAnListOfServicos() {
        when(repository.findAll()).thenReturn(List.of(servico));

        List<Servico> response = service.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(Servico.class, response.get(0).getClass());
        assertEquals(ID, response.get(0).getId());
        assertEquals(NOME, response.get(0).getNome());
        assertEquals(PRECO, response.get(0).getPreco());
    }

    @Test
    void whenSaveThenReturnSuccess() {
        when(repository.save(any())).thenReturn(servico);

        Servico response = service.save(servico);

        assertNotNull(response);
        assertEquals(Servico.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NOME, response.getNome());
        assertEquals(PRECO, response.getPreco());
    }

    @Test
    void whenUpdateThenReturnSuccess() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.save(any())).thenReturn(servico);

        Servico response = service.update(ID, servico);

        assertNotNull(response);
        assertEquals(Servico.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NOME, response.getNome());
        assertEquals(PRECO, response.getPreco());
    }

    @Test
    void whenUpdateThenReturnNull() {
        when(repository.existsById(anyLong())).thenReturn(false);

        Servico response = service.update(ID, servico);

        assertNull(response);
    }

    @Test
    void deleteWithSuccess() {
        when(repository.existsById(anyLong())).thenReturn(true);
        doNothing().when(repository).deleteById(anyLong());

        boolean response = service.delete(ID);

        assertTrue(response);
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteWithFailure() {
        when(repository.existsById(anyLong())).thenReturn(false);

        boolean response = service.delete(ID);

        assertFalse(response);
        verify(repository, never()).deleteById(anyLong());
    }

    private void startServico() {
        servico = new Servico(ID, NOME, PRECO);
        optionalServico = Optional.of(servico);
    }
}
