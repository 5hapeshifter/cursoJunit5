package br.ce.wcaquino.barriga.infra.service;

import br.ce.wcaquino.barriga.domain.Conta;
import br.ce.wcaquino.barriga.domain.builders.ContaBuilder;
import br.ce.wcaquino.barriga.domain.exceptions.ValidationException;
import br.ce.wcaquino.barriga.service.ContaService;
import br.ce.wcaquino.barriga.service.external.ContaEvento;
import br.ce.wcaquino.barriga.service.repositories.ContaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ContaServiceTest {
    @InjectMocks private ContaService contaService;
    @Mock private ContaRepository repository;
    @Mock private ContaEvento contaEvento;
    @Captor private ArgumentCaptor<Conta> contaCaptor;

    @Test
    public void deveSalvarPrimeiraContaComSucesso() throws Exception {
        Conta contaToSave = ContaBuilder.getInstance().comId(null).build();
        when(repository.salvar(contaToSave)).thenReturn(ContaBuilder.getInstance().build());
        doNothing().when(contaEvento).dispatch(contaToSave, ContaEvento.EventType.CREATED);
        Conta savedConta = contaService.salvar(contaToSave);
        Assertions.assertNotNull(savedConta.getId());
    }

    /*
        Exemplo usando o argument matchers, que deve ser usado quando estamos lidando com valores randômicos, que não
            conseguimos controlar.
        Uma vez que não temos controle da instância, mas alguns valores podem ser validados e são importantes, devemos
            utilizar o ArgumentCaptor para validar.
     */
    @Test
    public void deveSalvarPrimeiraContaComSucessoUsandoOArgumentMatchers() throws Exception {
        Conta contaToSave = ContaBuilder.getInstance().comId(null).build();
        when(repository.salvar(Mockito.any(Conta.class))).thenReturn(ContaBuilder.getInstance().build());
        doNothing().when(contaEvento).dispatch(contaToSave, ContaEvento.EventType.CREATED);
        Conta savedConta = contaService.salvar(contaToSave);
        Assertions.assertNotNull(savedConta.getId());

        Mockito.verify(repository).salvar(contaCaptor.capture());
        Assertions.assertTrue(contaCaptor.getValue().getNome().startsWith("Conta"));
        Assertions.assertNull(contaCaptor.getValue().getId());
    }

    @Test
    public void deveSalvarSegundaContaComSucesso() throws Exception {
        Conta contaToSave = ContaBuilder.getInstance().comId(null).build();
        doNothing().when(contaEvento).dispatch(contaToSave, ContaEvento.EventType.CREATED);
        when(repository.obterContasPorUsuario(contaToSave.getUsuario().getId()))
                .thenReturn(Arrays.asList(ContaBuilder.getInstance().comNome("Outra conta").build()));
        when(repository.salvar(contaToSave)).thenReturn(ContaBuilder.getInstance().build());
        Conta savedConta = contaService.salvar(contaToSave);
        Assertions.assertNotNull(savedConta.getId());
    }

    @Test
    public void deveRejeitarContaRepetida() {
        Conta contaToSave = ContaBuilder.getInstance().comId(null).build();
        when(repository.obterContasPorUsuario(contaToSave.getUsuario().getId()))
                .thenReturn(Arrays.asList(ContaBuilder.getInstance().build()));
        ValidationException mensagem = Assertions.assertThrows(ValidationException.class,
                () -> contaService.salvar(contaToSave));
        Assertions.assertEquals("Usuário já possui uma conta com este nome", mensagem.getMessage());
    }

    @Test
    public void naoDeveManterContaSemEvento() throws Exception {
        Conta contaToSave = ContaBuilder.getInstance().comId(null).build();
        Conta contaSalva = ContaBuilder.getInstance().build();;

        when(repository.salvar(contaToSave)).thenReturn(contaSalva);
        doThrow(new Exception("Falha catastrófica"))
                .when(contaEvento).dispatch(contaSalva, ContaEvento.EventType.CREATED);

        String mensagem = Assertions.assertThrows(Exception.class,
                () -> contaService.salvar(contaToSave)).getMessage();
        Assertions.assertEquals("Falha na criação da conta, tente novamente", mensagem);

        verify(repository).delete(contaSalva);
    }
}
