package com.sos.winecommunity.repository;

import com.sos.winecommunity.model.SolicitudSeguimiento;
import com.sos.winecommunity.model.SolicitudSeguimiento.EstadoSolicitud;
import com.sos.winecommunity.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudSeguimientoRepository extends JpaRepository<SolicitudSeguimiento, Long> {
    Page<SolicitudSeguimiento> findByReceptorAndEstado(Usuario receptor, EstadoSolicitud estado, Pageable pageable);
}