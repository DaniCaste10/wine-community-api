package com.sos.winecommunity.controller;

import com.sos.winecommunity.assembler.UsuarioVinoModelAssembler;
import com.sos.winecommunity.model.Usuario;
import com.sos.winecommunity.model.UsuarioVino;
import com.sos.winecommunity.model.Vino;
import com.sos.winecommunity.service.UsuarioServicio;
import com.sos.winecommunity.service.UsuarioVinoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.net.URI;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/usuarios/{usuarioId}/vinos")
@RequiredArgsConstructor

public class UsuarioVinoControlador {
    private final UsuarioVinoServicio usuarioVinoServicio;
    private final UsuarioServicio usuarioServicio;
    private final UsuarioVinoModelAssembler assembler;
    private final PagedResourcesAssembler<UsuarioVino> pagedAssembler;

    // POST /usuarios/{usuarioId}/vinos — Añadir vino a la lista
    @PostMapping
    public ResponseEntity<?> añadirVino(@PathVariable Long usuarioId, @RequestParam Double puntuacion, @RequestBody Vino vino) {
        try {
            UsuarioVino nuevo = usuarioVinoServicio.añadirVino(usuarioId, vino, puntuacion);
            URI location = linkTo(methodOn(UsuarioVinoControlador.class).obtenerVinos(usuarioId, null, null, null, null, null, null, null, 0, 10)).toUri();
            return ResponseEntity.created(location).build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /usuarios/{usuarioId}/vinos/{usuarioVinoId} — Eliminar vino de la lista
    @DeleteMapping("/{usuarioVinoId}")
    public ResponseEntity<Void> eliminarVino(@PathVariable Long usuarioVinoId) {
        try {
            usuarioVinoServicio.eliminarVino(usuarioVinoId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT /usuarios/{usuarioId}/vinos/{usuarioVinoId}/puntuacion — Modificar puntuación
    @PutMapping("/{usuarioVinoId}/puntuacion")
    public ResponseEntity<?> modificarPuntuacion(@PathVariable Long usuarioVinoId, @RequestParam Double puntuacion) {
        try {
            return ResponseEntity.ok(assembler.toModel(usuarioVinoServicio.modificarPuntuacion(usuarioVinoId, puntuacion)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /usuarios/{usuarioId}/vinos — lista con todos los vinos de un usuario con filtros y paginación
    @GetMapping
    public ResponseEntity<PagedModel<UsuarioVino>> obtenerVinos(
            @PathVariable Long usuarioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String bodega,
            @RequestParam(required = false) String origen,
            @RequestParam(required = false) Integer anada,
            @RequestParam(required = false) String uva,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioVino> pageResult = usuarioVinoServicio.obtenerVinosDeUsuario(usuarioId, desde, hasta, tipo, bodega, origen, anada, uva, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pageResult, assembler));
    }

    // GET /usuarios/{usuarioId}/vinos/recomendaciones — Recomendaciones personalizadas
    @GetMapping("/recomendaciones")
    public ResponseEntity<?> obtenerRecomendaciones(@PathVariable Long usuarioId) {
        try {
            Usuario usuario = usuarioServicio.obtenerUsuario(usuarioId);
            Map<String, Object> recomendacion = new HashMap<>();
            recomendacion.put("usuario", usuario);
            recomendacion.put("ultimosVinos", usuarioVinoServicio.obtenerUltimosVinos(usuarioId));
            recomendacion.put("topVinos", usuarioVinoServicio.obtenerTopVinos(usuarioId));
            recomendacion.put("topVinosAmigos", usuarioVinoServicio.obtenerTopVinosAmigos(usuarioId));
            return ResponseEntity.ok(recomendacion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /usuarios/{usuarioId}/vinos/estadisticas — Estadísticas de puntuaciones. Obtener nota media de los vinos de un usuario con filtros
    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas(
            @PathVariable Long usuarioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String bodega,
            @RequestParam(required = false) String origen,
            @RequestParam(required = false) Integer anada,
            @RequestParam(required = false) String uva) {

        Double media = usuarioVinoServicio.obtenerMediaPuntuaciones(usuarioId, desde, hasta, tipo, bodega, origen, anada, uva);
        return ResponseEntity.ok(media);
    }
}