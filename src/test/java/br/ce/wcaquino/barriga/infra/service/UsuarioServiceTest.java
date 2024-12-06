package br.ce.wcaquino.barriga.infra.service;

import br.ce.wcaquino.barriga.domain.Usuario;
import br.ce.wcaquino.barriga.domain.builders.UsuarioBuilder;
import br.ce.wcaquino.barriga.domain.exceptions.ValidationException;
import br.ce.wcaquino.barriga.service.UsuarioService;
import br.ce.wcaquino.barriga.service.repositories.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks private UsuarioService service;
    @Mock private UsuarioRepository repository;

    @BeforeEach
    void setUp() {

        // Com a anotação a nível de classe, não precisamos mais usar 'MockitoAnnotations.openMocks(this);'
        // MockitoAnnotations.openMocks(this);

        // com as anotações '@mock' e '@InjectMocks' não precisamos mais das linhas abaixo
        // repository = mock(UsuarioRepository.class);
        // service = new UsuarioService(repository);
    }



//    Se utilizar o método abaixo, teremos que implementar o "verifyNoMoreInteractions(repository);" em todos o testes
//      pois ele nos obriga a isso.
//    @AfterEach
//    void tearDown() {
//        verifyNoMoreInteractions(repository);
//    }

    @Test
    public void deveRetornarEmptyQuandoUsuarioInexistente() {
        // when(repository.getUserByEmail("mail@mail.com")).thenReturn(Optional.empty());

        Optional<Usuario> user = service.getUserByEmail("mail@mail.com");
        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    public void deveRetornarUsuarioPorEmail() {

        when(repository.getUserByEmail("mail@mail.com"))
                .thenReturn(Optional.of(UsuarioBuilder.getInstance().build()))
                .thenReturn(Optional.of(UsuarioBuilder.getInstance().build()))
                .thenReturn(null); // podemos definir resultados sequenciais

        Optional<Usuario> user = service.getUserByEmail("mail@mail.com");
        System.out.println(user);
        Assertions.assertTrue(user.isPresent());
        // TODO - IMPORTANTE RESSALTAR QUE OS PARAMETROS DE ENTRADA SAO IMPORTANTES, SE MUDAR O MOCK NAO SABERA O QUE FAZER
        user = service.getUserByEmail("mailERRADO@mail.com");
        System.out.println(user);
        user = service.getUserByEmail("mail@mail.com");
        System.out.println(user);

        /*
            Segundo o professor Wagner não é bom usar isso porque amarra muito o código, é uma forma mais restritiva de
            testar, pois em refatorações qualquer coisa amarrada pode quebrar e teremos que ajustar

         */
        verify(repository, times(2)).getUserByEmail("mail@mail.com");
        verify(repository, times(1)).getUserByEmail("mailERRADO@mail.com");
//        verify(repository, atLeast(1)).getUserByEmail("mail@mail.com");
//        verify(repository, atLeastOnce()).getUserByEmail("mail@mail.com");
//        verify(repository, never()).getUserByEmail("nuncaOcorreu@mail.com");
//        verifyNoMoreInteractions(repository);
    }

    @Test
    public void deveSalvarUsuarioComSucesso() {
        Usuario userToSave = UsuarioBuilder.getInstance().comId(null).build();

        /*
            Como o comportamento básico do mock é retornar nulo quando trabalhar com retornos de objetos e não fizermos
            o mock do comportamento, a definição abaixo é desenecessária.
         */
//        when(repository.getUserByEmail(userToSave.getEmail()))
//                .thenReturn(Optional.empty());

        when(repository.salvar(userToSave))
                .thenReturn(UsuarioBuilder.getInstance().build());

        Usuario savedUser = service.salvar(userToSave);

        /*
            É IMPORTANTE LEMBRAR QUE O MOCKITO UTILIZA O MÉTODO "EQUALS" PARA FAZER AS COMPARAÇÕES DO OBJETO QUE FOI
            UTILIZADO NO MOCK DO MÉTODO E DO QUE FOI UTILIZADO NA CHAMADA DO MÉTODO, SE FOREM DIVERGENTES DA ERRADO.
        */
        //Usuario savedUser = service.salvar(UsuarioBuilder.getInstance().comId(null).build());
        Assertions.assertNotNull(savedUser.getId());

        verify(repository).getUserByEmail(userToSave.getEmail());

        // O único motivo válido, segundo o professor, para utilizar a validação abaixo é assegurar que o objeto que
        // está sendo salvo é o mesmo que foi usado para determinar o comportamento.
        verify(repository).salvar(userToSave);

    }

    @Test
    public void deveRejeitarUsuarioExistente() {
        Usuario userTosave = UsuarioBuilder.getInstance().comId(null).build();
        when(repository.getUserByEmail(userTosave.getEmail())).thenReturn(Optional.of(UsuarioBuilder.getInstance().build()));
        ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> service.salvar(userTosave));
        Assertions.assertTrue( ex.getMessage().endsWith("já cadastrado!"));
    }
}
