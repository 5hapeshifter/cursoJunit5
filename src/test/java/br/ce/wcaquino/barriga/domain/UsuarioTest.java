package br.ce.wcaquino.barriga.domain;

import br.ce.wcaquino.barriga.domain.builders.UsuarioBuilder;
import br.ce.wcaquino.barriga.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Domínio usuário")
class UsuarioTest {

    @Test
    @DisplayName("Deve criar um usuário válido.")
    public void deveCriarUmUsuario() {
        Usuario usuario = UsuarioBuilder.getInstance().build();
        // forma de fazer varios asserts e descobrir quais deles estao falhando
        Assertions.assertAll("Usuario",
                () -> assertEquals(1L, usuario.getId()),
                () -> assertEquals("Usuario Valido", usuario.getNome()),
                () -> assertEquals("usuario@email.com", usuario.getEmail()),
                () -> assertEquals("123456", usuario.getSenha())
        );
    }

    @Test
    public void deveRejeitarUsuarioSemNome() {
        ValidationException result = assertThrows(ValidationException.class, () ->
                UsuarioBuilder.getInstance().comNome(null).build());
        assertEquals("O nome é obrigatório", result.getMessage());
    }

    @Test
    public void deveRejeitarUsuarioSemEmail() {
        ValidationException result = assertThrows(ValidationException.class, () ->
                UsuarioBuilder.getInstance().comEmail(null).build());
        assertEquals("O email é obrigatório", result.getMessage());
    }

    @Test
    public void deveRejeitarUsuarioSemSenha() {
        ValidationException result = assertThrows(ValidationException.class, () ->
                UsuarioBuilder.getInstance().comSenha(null).build());
        assertEquals("A senha é obrigatória", result.getMessage());
    }

    //@ParameterizedTest(name = "{4}") // TAMBEM FUNCIONA
    // PARA VALORES NULLOS PODEMOS USAR DA FORMA ABAIXO TAMBÉM
//    @CsvSource(value = {
//            "1, , usuario@email.com, 123456, O nome é obrigatório",
//            "1, Nome Valido, , 123456, O email é obrigatório",
//            "1, Nome Valido, usuario@email.com, , A senha é obrigatória"
//    })
    @ParameterizedTest(name = "{index} - {4}")
    @CsvSource(value = {
            "1, , usuario@email.com, 123456, O nome é obrigatório",
            "1, Nome Valido, , 123456, O email é obrigatório",
            "1, Nome Valido, usuario@email.com, , A senha é obrigatória"
    }, nullValues = "null")
    public void testeDeUsuario(Long id, String nome, String email, String senha, String msg) {
        ValidationException validationException = assertThrows(ValidationException.class, () ->
                UsuarioBuilder.getInstance().comId(id).comNome(nome).comEmail(email).comSenha(senha).build());
        assertEquals(msg, validationException.getMessage());
    }

    @ParameterizedTest(name = "{index} - {4}")
    @CsvFileSource(files = "src/test/resources/camposObrigatoriosUsuario.csv",
            nullValues = "null",
            //numLinesToSkip = 1, // definimos quantas linhas queremos pular
            useHeadersInDisplayName = true)
    public void testeDeUsuarioComArquivoExterno(Long id, String nome, String email, String senha, String msg) {
        ValidationException validationException = assertThrows(ValidationException.class, () ->
                UsuarioBuilder.getInstance().comId(id).comNome(nome).comEmail(email).comSenha(senha).build());
        assertEquals(msg, validationException.getMessage());
    }
}