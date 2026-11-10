package com.sos.winecommunity.service;

import com.sos.winecommunity.model.Seguimiento;
import com.sos.winecommunity.model.SolicitudSeguimiento;
import com.sos.winecommunity.model.SolicitudSeguimiento.EstadoSolicitud;
import com.sos.winecommunity.model.Usuario;
import com.sos.winecommunity.repository.SeguimientoRepository;
import com.sos.winecommunity.repository.SolicitudSeguimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor

public class SolicitudSeguimientoServicio {
    private final SolicitudSeguimientoRepository solicitudRepository;
    private final SeguimientoRepository seguimientoRepository;
    private final UsuarioServicio usuarioServicio;

    // Enviar solicitud de seguimiento
    public SolicitudSeguimiento enviarSolicitud(Long solicitanteId, Long receptorId) {
        Usuario solicitante = usuarioServicio.obtenerUsuario(solicitanteId);
        Usuario receptor = usuarioServicio.obtenerUsuario(receptorId);
        SolicitudSeguimiento solicitud = new SolicitudSeguimiento();
        solicitud.setSolicitante(solicitante);
        solicitud.setReceptor(receptor);
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        solicitud.setFechaSolicitud(LocalDate.now());
        return solicitudRepository.save(solicitud);
    }

    // Obtener solicitudes pendientes de un usuario
    public Page<SolicitudSeguimiento> obtenerSolicitudesPendientes(Long usuarioId, Pageable pageable) {
        Usuario usuario = usuarioServicio.obtenerUsuario(usuarioId);
        return solicitudRepository.findByReceptorAndEstado(usuario, EstadoSolicitud.PENDIENTE, pageable);
    }

    // Aceptar solicitud de seguimiento
    public SolicitudSeguimiento aceptarSolicitud(Long solicitudId) {
        SolicitudSeguimiento solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        solicitud.setEstado(EstadoSolicitud.ACEPTADA);
        solicitudRepository.save(solicitud);
        Seguimiento seguimiento = new Seguimiento();
        seguimiento.setSeguidor(solicitud.getSolicitante());
        seguimiento.setSeguido(solicitud.getReceptor());
        seguimientoRepository.save(seguimiento);
        return solicitud;
    }

    // Rechazar solicitud de seguimiento
    public SolicitudSeguimiento rechazarSolicitud(Long solicitudId) {
        SolicitudSeguimiento solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        solicitud.setEstado(EstadoSolicitud.RECHAZADA);
        return solicitudRepository.save(solicitud);
    }
}