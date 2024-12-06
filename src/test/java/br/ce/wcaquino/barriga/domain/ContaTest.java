package br.ce.wcaquino.barriga.domain;

import br.ce.wcaquino.barriga.domain.builders.ContaBuilder;
import br.ce.wcaquino.barriga.domain.builders.UsuarioBuilder;
import br.ce.wcaquino.barriga.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class ContaTest {

    @Test
    public void deveCriarContaValida() {
        Conta conta = ContaBuilder.getInstance().build();
        Assertions.assertAll("deveCriarContaValida",
                () -> Assertions.assertEquals(1L, conta.getId()),
                () -> Assertions.assertEquals("Conta Valida", conta.getNome()),
                () -> Assertions.assertEquals(UsuarioBuilder.getInstance().build(), conta.getUsuario())
        );
    }

    @ParameterizedTest
    @MethodSource(value = "dataProvider")
    public void deveRejeitarContaInvalida(Long id, String nome, Usuario usuario, String msg) {
        String errorMsg = Assertions.assertThrows(ValidationException.class, () ->
                ContaBuilder.getInstance().comId(id).comNome(nome).comUsuario(usuario).build()).getMessage();
        Assertions.assertEquals(msg, errorMsg);
    }

    // QUANDO USAMOS O @MethodSource o metodo que fornece os dedos deve ser estatico.
    public static Stream<Arguments> dataProvider() {
        return Stream.of(
                // Cada linha de Arguments.of() é um teste que será executado.
                Arguments.of(1L, null, UsuarioBuilder.getInstance().build(), "Nome é obrigatório"),
                Arguments.of(1L, "Conta Valida", null, "Usuário é obrigatório")
        );
    }
}

