import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

public class CalculadoraTest {

    @BeforeEach
    public void setUp() {
        System.out.println("^^^");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("vvv");
    }

    @BeforeAll
    public static void setUpAll() {
        System.out.println("--- Before All ---");
    }

    @AfterAll
    public static void tearDownAll() {
        System.out.println("--- After All ---");
    }


    private Calculadora calc = new Calculadora();
    // Usado para verificar que a variável é resetada automaticamente pelo Junit a cada teste
    private int contador = 0;
    // Com o contador/objeto estático o estado do objeto não é resetado
    private static int contadorEstatico = 0; // Usado para verificar que a variável é resetada automaticamente pelo Junit a cada teste

    @Test
    public void testeSomar() {
        System.out.println(++contador);
        System.out.println("Contador estatico: " + ++contadorEstatico);
        Assertions.assertTrue(() -> calc.soma(2, 3) == 5);
        Assertions.assertTrue(calc.soma(2, 3) == 5);
        Assertions.assertEquals(5, calc.soma(2, 3));

    }

    @Test
    public void assertivas() {
        Assertions.assertEquals("Casa", "Casa");
        Assertions.assertNotEquals("Casa", "casa");
        Assertions.assertTrue("casa".equalsIgnoreCase("Casa"));

        List<String> s1 = new ArrayList<>();
        List<String> s2 = new ArrayList<>();
        List<String> s3 = null;

        Assertions.assertEquals(s1, s2); // this works because the object has his own form to comparate
        // Assertions.assertSame(s1, s2); // THIS IS THE CORRECT FORM TO COMPARE ARRAYS AND OTHER OBJECTS, BECAUSE THE REFERENCE IS COMPARED
        s2.add("Suleiman");
        // Assertions.assertEquals(s1, s2); // this will fail because has 1 object
        Assertions.assertNull(s3);
    }

    @Test
    public void deveRetornarNumeroInteiroNaDivisao() {
        System.out.println(++contador);
        System.out.println("Contador estatico: " + ++contadorEstatico);
        float resultado = calc.dividir(6, 2);
        Assertions.assertEquals(3, resultado);
    }

    @Test
    public void deveRetornarNumeroNegativoNaDivisao() {
        System.out.println(++contador);
        System.out.println("Contador estatico: " + ++contadorEstatico);
        float resultado = calc.dividir(6, -2);
        Assertions.assertEquals(-3, resultado);
    }

    @Test
    public void deveRetornarNumeroDecimalNaDivisao() {
        System.out.println(++contador);
        System.out.println("Contador estatico: " + ++contadorEstatico);
        float resultado = calc.dividir(10, 3);
        Assertions.assertEquals(3.3333332538604736, resultado);
        Assertions.assertEquals(3.3333332538604736, resultado, 0.01); // usando a margem de erro
    }

    @Test
    public void deveRetornarZeroComNumeradorZeroNaDivisao() {
        System.out.println(++contador);
        System.out.println("Contador estatico: " + ++contadorEstatico);
        float resultado = calc.dividir(0, 2);
        Assertions.assertEquals(0, resultado);
    }

    @Test
    public void deveLançarExcecaoQuandoDividirPorZero_Junit4() {
        try {
            //float resultado = 10 / 10; // Esse teste só falha usando o Assertions.fail(), pois nao chega a entrar no bloco catch
            float resultado = 10 / 0;
            Assertions.fail("Deveria ter sido lançado uma exceção");
        } catch (ArithmeticException e) {
            Assertions.assertEquals("/ by zero", e.getMessage()); // Com isso garantimos que a exececao foi lancada pelo motivo correto
        }
    }

    @Test
    public void deveLançarExcecaoQuandoDividirPorZero_Junit5() {
        ArithmeticException result = Assertions.assertThrows(ArithmeticException.class, () -> {
            float resultado = 10 / 0;
        });
        Assertions.assertEquals("/ by zero", result.getMessage());
    }



}
