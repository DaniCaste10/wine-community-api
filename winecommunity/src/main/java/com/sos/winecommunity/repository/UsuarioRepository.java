package com.sos.winecommunity.repository;

import com.sos.winecommunity.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Page<Usuario> findByNombreUsuarioContaining(String patron, Pageable pageable);
}