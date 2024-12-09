package br.ce.wcaquino.barriga.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Transacao {

    private Long id;
    private String descricao;
    private Double valor;
    private Conta conta;
    private LocalDate data;
    private Boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transacao{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", conta=" + conta +
                ", data=" + data +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transacao transacao = (Transacao) o;
        return Objects.equals(id, transacao.id) && Objects.equals(descricao, transacao.descricao) && Objects.equals(valor, transacao.valor) && Objects.equals(conta, transacao.conta) && Objects.equals(data, transacao.data) && Objects.equals(status, transacao.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descricao, valor, conta, data, status);
    }
}
