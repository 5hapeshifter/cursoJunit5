package br.ce.wcaquino.barriga.infra;

import br.ce.wcaquino.barriga.domain.Usuario;
import br.ce.wcaquino.barriga.domain.builders.UsuarioBuilder;
import br.ce.wcaquino.barriga.service.repositories.UsuarioRepository;

import java.util.Optional;

public class UsuarioDummyRespository implements UsuarioRepository {
    @Override
    public Usuario salvar(Usuario usuario) {
        return UsuarioBuilder.getInstance()
                .comNome(usuario.getNome())
                .comEmail(usuario.getEmail())
                .comSenha(usuario.getSenha())
                .build();
    }

    @Override
    public Optional<Usuario> getUserByEmail(String email) {
        if ("usuario@email.com".equals(email)) {
            return Optional.of(UsuarioBuilder.getInstance().comEmail(email).build());
        }
        return Optional.empty();
    }

}
