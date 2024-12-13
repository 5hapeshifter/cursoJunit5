import br.ce.wcaquino.barriga.domain.Usuario;
import br.ce.wcaquino.barriga.domain.builders.UsuarioBuilder;
import br.ce.wcaquino.barriga.service.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalculadoraMockTest {

    @Spy
    private Calculadora calc;

    @Mock
    private UsuarioRepository repository;

    @Test
    public void test() {


        /*
         QUANDO MOCKAMOS UM COMPORTAMENTO OS PARAMETROS SAO IMPORTANTES, se não usarmos os parametros mockados o SPY
         executara o metodo de verdade, mockamos 1 + 1 = 5, na seguinte linha usamos 1 + 2, como ele nao sabe o que fazer
         utilizara o metodo real.
         NAO FACA MOCK DO METODO QUE VC ESTA TESTANDO SEM UMA BOA RAZAO.
         Definimos comportamento a nivel de metodos com parametros, se atente aos parametros.
         */
//        when(calc.soma(1, 1)).thenReturn(5);
        when(calc.soma(anyInt(), eq(2)))
                .thenReturn(5)
                .thenReturn(6)
                .thenCallRealMethod();
                //.thenThrow(new RuntimeException("Erro qualquer"));
        System.out.println(calc.soma(1, 1)); // metodo real
        System.out.println(calc.soma(1, 2)); // o mock do metodo satisfez essa condicao = 5
        System.out.println(calc.soma(10, 2)); // o mock do metodo satisfez essa condicao = 6
        System.out.println(calc.soma(14, 2)); // chama o metodo real
        //System.out.println(calc.soma(167, 2)); // lanca exception

        when(repository.salvar(any()))
                .thenReturn(UsuarioBuilder.getInstance().build())
                .thenCallRealMethod(); // Cuidado ao chamar o metodo real de uma interface
        System.out.println(repository.salvar(null)); // o mock funciona mesmo mandando o nulo
    }

}
