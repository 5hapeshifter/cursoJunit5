package br.ce.wcaquino.barriga.service;

import br.ce.wcaquino.barriga.domain.Conta;
import br.ce.wcaquino.barriga.domain.exceptions.ValidationException;
import br.ce.wcaquino.barriga.service.external.ContaEvento;
import br.ce.wcaquino.barriga.service.repositories.ContaRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ContaService {
    private ContaRepository repository;
    private ContaEvento evento;

    public ContaService(ContaRepository repository, ContaEvento evento) {
        this.repository = repository;
        this.evento = evento;
    }

    public Conta salvar(Conta conta) {
        List<Conta> contas = repository.obterContasPorUsuario(conta.getUsuario().getId());
        contas.forEach(contaExistente -> {
                    if (conta.getNome().equals(contaExistente.getNome())) {
                        throw new ValidationException("Usuário já possui uma conta com este nome");
                    }
                });
         Conta contaPersistida = repository.salvar(conta);
        // Usar a conta persistida e a novaConta abaixo com o teste argument matcher deveSalvarPrimeiraContaComSucessoUsandoOArgumentMatchers
//        Conta novaConta = new Conta(conta.getId(),conta.getNome() + LocalDateTime.now(),conta.getUsuario());
//        System.out.println(novaConta);
//        Conta contaPersistida = repository.salvar(novaConta);
        try {
            evento.dispatch(contaPersistida, ContaEvento.EventType.CREATED);
        } catch (Exception e) {
            repository.delete(contaPersistida);
            throw new RuntimeException("Falha na criação da conta, tente novamente");
        }
        return contaPersistida;
    }

}
