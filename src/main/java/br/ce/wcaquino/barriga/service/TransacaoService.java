package br.ce.wcaquino.barriga.service;

import br.ce.wcaquino.barriga.domain.Transacao;
import br.ce.wcaquino.barriga.domain.exceptions.ValidationException;
import br.ce.wcaquino.barriga.service.external.ClockService;
import br.ce.wcaquino.barriga.service.repositories.TransacaoDao;

import java.time.LocalDateTime;
import java.util.Date;

public class TransacaoService {
    private TransacaoDao transacaoDao;
//    private ClockService clock;

//    public TransacaoService(TransacaoDao transacaoDao, ClockService clock) {
//        this.transacaoDao = transacaoDao;
//        this.clock = clock;
//    }

    public Transacao salvar(Transacao transacao) {
//        if(clock.getCurrentTime().getHour() > 10) throw new RuntimeException("Tente novamente amanhã."); // USAR COM O CLOCK SERVICE NA CLASSSE DE TESTE
//        if(LocalDateTime.now().getHour() < 10) throw new RuntimeException("Tente novamente amanhã."); // Condicao para limitar a execucao e testar
        if(getTime().getHour() < 12) throw new RuntimeException("Tente novamente amanhã."); // TESTANDO MÉTODO PROTECTED 'getTime' com '@Spy'
//// DEVE SER USADO COM O METODO 'isHoraValida' e @EnabledIf no teste
//        if(LocalDateTime.now().getHour() < 5) throw new RuntimeException("Tente novamente amanhã."); // Condicao para limitar a execucao e testar

//         COMENTADO PARA TESTAR METODO PRIVADO
        if(transacao.getDescricao() == null) throw new ValidationException("Descrição inexistente");
        if(transacao.getValor() == null) throw new ValidationException("Valor inexistente");
        if(transacao.getData() == null) throw new ValidationException("Data inexistente");
        if(transacao.getConta() == null) throw new ValidationException("Conta inexistente");
        if(transacao.getStatus() == null) transacao.setStatus(false);

//        validarCamposObrigatorios(transacao);
        return transacaoDao.salvar(transacao);
    }

    // TESTANDO MÉTODO PROTECTED 'getTime' com '@Spy'
    protected LocalDateTime getTime() {
        return LocalDateTime.now();
    }

    private void validarCamposObrigatorios(Transacao transacao) {
        if(transacao.getDescricao() == null) throw new ValidationException("Descrição inexistente");
        if(transacao.getValor() == null) throw new ValidationException("Valor inexistente");
        if(transacao.getData() == null) throw new ValidationException("Data inexistente");
        if(transacao.getConta() == null) throw new ValidationException("Conta inexistente");
        if(transacao.getStatus() == null) transacao.setStatus(false);
    }
}
