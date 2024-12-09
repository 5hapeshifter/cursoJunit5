package br.ce.wcaquino.barriga.infra.service;

import br.ce.wcaquino.barriga.domain.Transacao;
import br.ce.wcaquino.barriga.domain.builders.TransacaoBuilder;
import br.ce.wcaquino.barriga.service.TransacaoService;
import br.ce.wcaquino.barriga.service.repositories.TransacaoDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @InjectMocks
    private TransacaoService service;

    @Mock
    private TransacaoDao dao;

    private Transacao transacaoComIdNullo;

    @BeforeEach
    void setUp() {
        transacaoComIdNullo = TransacaoBuilder.getInstance().comId(null).build();
    }

    @Test
    public void deveSalvarTransacaoValida() {
        Transacao transacaoParaSalvar = TransacaoBuilder.getInstance().comId(null).build();
        Mockito.when(dao.salvar(transacaoParaSalvar)).thenReturn(TransacaoBuilder.getInstance().build());
        Transacao transacaoSalva = service.salvar(transacaoParaSalvar);
        assertEquals(TransacaoBuilder.getInstance().build(), transacaoSalva);
        Assertions.assertAll("Transacao",
                () -> assertEquals(1L, transacaoSalva.getId()),
                () -> assertEquals("Transação Válida", transacaoSalva.getDescricao()),
                () -> {
                    assertAll("Conta",
                            () -> assertEquals("Conta Valida", transacaoSalva.getConta().getNome()),
                            () -> {
                                assertAll("Usuário",
                                        () -> assertEquals("Usuario Valido", transacaoSalva.getConta().getUsuario().getNome()),
                                        () -> assertEquals("123456", transacaoSalva.getConta().getUsuario().getSenha())
                                );
                            }
                    );
                }
        );
    }
}
