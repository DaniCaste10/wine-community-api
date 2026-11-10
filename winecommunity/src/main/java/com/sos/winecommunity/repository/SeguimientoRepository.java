package com.sos.winecommunity.repository;

import com.sos.winecommunity.model.Seguimiento;
import com.sos.winecommunity.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {
    Page<Seguimiento> findBySeguidor(Usuario seguidor, Pageable pageable);
    Page<Seguimiento> findBySeguidorAndSeguidoNombreUsuarioContaining(Usuario seguidor, String patron, Pageable pageable);
    boolean existsBySeguidorAndSeguido(Usuario seguidor, Usuario seguido);
    void deleteBySeguidorAndSeguido(Usuario seguidor, Usuario seguido);
}