package br.ce.wcaquino.barriga.service;

import br.ce.wcaquino.barriga.domain.Transacao;
import br.ce.wcaquino.barriga.domain.exceptions.ValidationException;
import br.ce.wcaquino.barriga.service.repositories.TransacaoDao;

public class TransacaoService {
    private TransacaoDao transacaoDao;

    public TransacaoService(TransacaoDao transacaoDao) {
        this.transacaoDao = transacaoDao;
    }

    public Transacao salvar(Transacao transacao) {
        if(transacao.getDescricao() == null) throw new ValidationException("Descrição inexistene");
        if(transacao.getValor() == null) throw new ValidationException("Valor inexistene");
        if(transacao.getData() == null) throw new ValidationException("Data inexistene");
        if(transacao.getConta() == null) throw new ValidationException("Conta inexistene");
        if(transacao.getStatus() == null) transacao.setStatus(false);
        return transacaoDao.salvar(transacao);
    }
}
