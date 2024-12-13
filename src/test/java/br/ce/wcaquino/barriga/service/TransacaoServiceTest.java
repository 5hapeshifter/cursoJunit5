package br.ce.wcaquino.barriga.service;

import br.ce.wcaquino.barriga.domain.Conta;
import br.ce.wcaquino.barriga.domain.Transacao;
import br.ce.wcaquino.barriga.domain.builders.ContaBuilder;
import br.ce.wcaquino.barriga.domain.builders.TransacaoBuilder;
import br.ce.wcaquino.barriga.domain.exceptions.ValidationException;
import br.ce.wcaquino.barriga.service.external.ClockService;
import br.ce.wcaquino.barriga.service.repositories.TransacaoDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

// @EnabledOnJre(value = {JRE.JAVA_17, JRE.JAVA_11})
// @EnabledOnOs(value = OS.WINDOWS)
// @EnabledIf(value = "isHoraValida") // DEVE SER USADO COM O METODO 'isHoraValida'
@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @InjectMocks
    @Spy // TESTANDO MÉTODO PROTECTED 'getTime' com '@Spy'
    private TransacaoService service;

    @Mock
    private TransacaoDao dao;

    @Mock
    private ClockService clock;

    @BeforeEach
    void setUp() {
        /*
            Aqui estamos usando a condicao para que o codigo seja executado ou nao, podemos usar quando sabemos que um
            teste em determinadas condicoes falhara, podemos fazer dessa forma para que o teste nao seja executado
         */
//        Assumptions.assumeTrue(LocalDateTime.now().getHour() > 5);

        // TESTANDO COM A INTERFACE CLOCK
//        when(clock.getCurrentTime()).thenReturn(LocalDateTime.of(2023, 1, 1, 9, 30, 28));

        // TESTANDO MÉTODO PROTECTED 'getTime' com '@Spy'
        when(service.getTime()).thenReturn(LocalDateTime.of(2023, 1, 1, 14, 30, 28));

    }

    @Test
    public void deveSalvarTransacaoValida() {
        Transacao transacaoParaSalvar = TransacaoBuilder.getInstance().comId(null).build();
        when(dao.salvar(transacaoParaSalvar)).thenReturn(TransacaoBuilder.getInstance().build());

//        USAR COM O MOCKED STATIC
//        LocalDateTime dataDesejada = LocalDateTime.of(2023, 1, 1, 14, 30, 28);
//        System.out.println(dataDesejada);
//        try (MockedStatic<LocalDateTime> ldt = Mockito.mockStatic(LocalDateTime.class)) {
//            ldt.when(() -> LocalDateTime.now()).thenReturn(dataDesejada);
//            System.out.println(LocalDateTime.now());

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
//            ldt.verify(() -> LocalDateTime.now(), Mockito.times(2)); // USAR COM O MOCKED STATIC
//        }
    }

    @ParameterizedTest(name = "{6}")
    @MethodSource(value = "cenariosObrigatorios")
    public void deveValidarCamposObrigatoriosAoSalvar(Long id, String descricao, Double valor, LocalDate data,
                                                      Conta conta, Boolean status, String mensagem) {
        String exMessage = Assertions.assertThrows(ValidationException.class, () -> {
            Transacao transacao = TransacaoBuilder.getInstance().comId(id).comDescricao(descricao).comValor(valor)
                    .comData(data).comStatus(status).comConta(conta).build();
            service.salvar(transacao);
        }).getMessage();
        Assertions.assertEquals(mensagem, exMessage);
    }

    static Stream<Arguments> cenariosObrigatorios() {
        return Stream.of(
                Arguments.of(1L, null, 10D, LocalDate.now(), ContaBuilder.getInstance().build(), true, "Descrição inexistente"),
                Arguments.of(1L, "Descrição", null, LocalDate.now(), ContaBuilder.getInstance().build(), true, "Valor inexistente"),
                Arguments.of(1L, "Descrição", 10D, null, ContaBuilder.getInstance().build(), true, "Data inexistente"),
                Arguments.of(1L, "Descrição", 10D, LocalDate.now(), null, true, "Conta inexistente")
        );
    }

//    @Test
//    public void deveRejeitarTransacaoSemValor() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        Transacao transacao = TransacaoBuilder.getInstance().comValor(null).build();
//        Method metodo = TransacaoService.class.getDeclaredMethod("validarCamposObrigatorios", Transacao.class);
//        metodo.setAccessible(true);
//        Exception ex = assertThrows(Exception.class,
//                () -> metodo.invoke(new TransacaoService(), transacao));
//        Assertions.assertEquals("Valor inexistente", ex.getCause().getMessage());
//    }

//    public static boolean isHoraValida() {
//        return LocalDateTime.now().getHour() > 5;
//    }
}
