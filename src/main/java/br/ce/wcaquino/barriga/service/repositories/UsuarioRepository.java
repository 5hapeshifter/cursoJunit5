package br.ce.wcaquino.barriga.service.repositories;

import br.ce.wcaquino.barriga.domain.Usuario;

import java.util.Optional;

public interface UsuarioRepository {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> getUserByEmail(String email);
}
