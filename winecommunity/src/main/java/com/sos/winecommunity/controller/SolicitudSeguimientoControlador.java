package com.sos.winecommunity.controller;

import com.sos.winecommunity.assembler.SolicitudSeguimientoModelAssembler;
import com.sos.winecommunity.model.SolicitudSeguimiento;
import com.sos.winecommunity.service.SolicitudSeguimientoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.net.URI;

@RestController
@RequestMapping("/usuarios/{usuarioId}/solicitudes")
@RequiredArgsConstructor

public class SolicitudSeguimientoControlador {
    private final SolicitudSeguimientoServicio solicitudSeguimientoServicio;
    private final SolicitudSeguimientoModelAssembler solicitudAssembler;
    private final PagedResourcesAssembler<SolicitudSeguimiento> pagedAssembler;

    // POST /usuarios/{usuarioId}/solicitudes — Enviar solicitud de seguimiento
    @PostMapping
    public ResponseEntity<?> enviarSolicitud(@PathVariable Long usuarioId, @RequestBody Map<String, Long> body) {
        try {
            Long seguidoId = body.get("seguidoId");
            solicitudSeguimientoServicio.enviarSolicitud(usuarioId, seguidoId);
            URI location = linkTo(methodOn(SolicitudSeguimientoControlador.class).obtenerSolicitudes(usuarioId, 0, 10)).toUri();
            return ResponseEntity.created(location).build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /usuarios/{usuarioId}/solicitudes — Ver solicitudes pendientes
    @GetMapping
    public ResponseEntity<PagedModel<SolicitudSeguimiento>> obtenerSolicitudes(@PathVariable Long usuarioId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SolicitudSeguimiento> pageResult = solicitudSeguimientoServicio.obtenerSolicitudesPendientes(usuarioId, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pageResult, solicitudAssembler));
    }

    // PUT /usuarios/{usuarioId}/solicitudes/{solicitudId}/aceptar — Aceptar solicitud
    @PutMapping("/{solicitudId}/aceptar")
    public ResponseEntity<?> aceptarSolicitud(@PathVariable Long solicitudId) {
        try {
            return ResponseEntity.ok(solicitudSeguimientoServicio.aceptarSolicitud(solicitudId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT /usuarios/{usuarioId}/solicitudes/{solicitudId}/rechazar — Rechazar solicitud
    @PutMapping("/{solicitudId}/rechazar")
    public ResponseEntity<?> rechazarSolicitud(@PathVariable Long solicitudId) {
        try {
            return ResponseEntity.ok(solicitudSeguimientoServicio.rechazarSolicitud(solicitudId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}