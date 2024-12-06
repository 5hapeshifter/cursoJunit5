package br.ce.wcaquino.barriga.domain.builders;

import br.ce.wcaquino.barriga.domain.Usuario;

public class UsuarioBuilder {

    private Long id;
    private String nome;
    private String email;
    private String senha;

    private UsuarioBuilder() {
    }

    private UsuarioBuilder(Long id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public static UsuarioBuilder getInstance() {
        UsuarioBuilder builder = new UsuarioBuilder();
        inicializarDadosPadroes(builder);
        return builder;
    }

    private static void inicializarDadosPadroes(UsuarioBuilder builder) {
        builder.id = 1L;
        builder.nome = "Usuario Valido";
        builder.email = "usuario@email.com";
        builder.senha = "123456";
    }

    public UsuarioBuilder comId(Long id) {
        this.id = id;
        return this;
    }

    public UsuarioBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public UsuarioBuilder comEmail(String email) {
        this.email = email;
        return this;
    }

    public UsuarioBuilder comSenha(String senha) {
        this.senha = senha;
        return this;
    }

    public Usuario build() {
        return new Usuario(id, nome, email, senha);
    }
}
