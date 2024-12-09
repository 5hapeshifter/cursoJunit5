package br.ce.wcaquino.barriga.service.repositories;

import br.ce.wcaquino.barriga.domain.Conta;

import java.util.List;

public interface ContaRepository {

    Conta salvar(Conta conta);

    List<Conta> obterContasPorUsuario(Long usuarioId);

    void delete(Conta conta);
}
