package br.ce.wcaquino.barriga.infra;

import br.ce.wcaquino.barriga.domain.Usuario;
import br.ce.wcaquino.barriga.domain.builders.UsuarioBuilder;
import br.ce.wcaquino.barriga.domain.exceptions.ValidationException;
import br.ce.wcaquino.barriga.service.UsuarioService;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Forma de ordenar a execução dos testes
public class UserServiceComUserMemoryRepositoryTest {
    private UsuarioService service = new UsuarioService(new UsuarioMemoryRepository());

    @Test
    @Order(1)
    public void deveSalvarUsuarioValido() {
        Usuario user = service.salvar(UsuarioBuilder.getInstance().comId(null).build());
        Assertions.assertNotNull(user.getId());
    }

    // Esse teste não é ideal porque está dependendo da execução do anterior
    @Test
    @Order(2)
    public void deveRejeitarUsuarioExistente() {
        ValidationException ex = Assertions.assertThrows(ValidationException.class,
                () -> service.salvar(UsuarioBuilder.getInstance().comId(null).comEmail("usuario@email.com").build()));
        Assertions.assertEquals("Usuário user@mail.com já cadastrado!", ex.getMessage());
    }
}
