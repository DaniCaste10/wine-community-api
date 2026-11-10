package com.sos.winecommunity.controller;

import com.sos.winecommunity.assembler.UsuarioModelAssembler;
import com.sos.winecommunity.assembler.UsuarioVinoModelAssembler;
import com.sos.winecommunity.model.Usuario;
import com.sos.winecommunity.model.UsuarioVino;
import com.sos.winecommunity.service.SeguimientoServicio;
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
import java.time.LocalDate;

@RestController
@RequestMapping("/usuarios/{usuarioId}/seguidos")
@RequiredArgsConstructor

public class SeguimientoControlador {
    private final SeguimientoServicio seguimientoServicio;
    private final UsuarioVinoServicio usuarioVinoServicio;
    private final UsuarioModelAssembler usuarioAssembler;
    private final UsuarioVinoModelAssembler usuarioVinoAssembler;
    private final PagedResourcesAssembler<UsuarioVino> pagedVinoAssembler;
    private final PagedResourcesAssembler<Usuario> pagedAssembler;

    // DELETE /usuarios/{usuarioId}/seguidos/{seguidoId} — Dejar de seguir
    @DeleteMapping("/{seguidoId}")
    public ResponseEntity<Void> dejarDeSeguir(@PathVariable Long usuarioId, @PathVariable Long seguidoId) {
        seguimientoServicio.dejarDeSeguir(usuarioId, seguidoId);
        return ResponseEntity.noContent().build();
    }

    // GET /usuarios/{usuarioId}/seguidos — Listar usuarios a los que sigo con filtro por nombre
    @GetMapping
    public ResponseEntity<PagedModel<Usuario>> obtenerSeguidos(@PathVariable Long usuarioId, @RequestParam(required = false) String nombre, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Usuario> pageResult = seguimientoServicio.obtenerSeguidos(usuarioId, nombre, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pageResult, usuarioAssembler));
    }

    // GET /usuarios/{usuarioId}/seguidos/{seguidoId}/vinos — Ver vinos de un seguido
    @GetMapping("/{seguidoId}/vinos")
    public ResponseEntity<PagedModel<UsuarioVino>> obtenerVinosDeSeguido(
            @PathVariable Long usuarioId,
            @PathVariable Long seguidoId,
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
        Page<UsuarioVino> pageResult = usuarioVinoServicio.obtenerVinosDeUsuario(seguidoId, desde, hasta, tipo, bodega, origen, anada, uva, pageable);
        return ResponseEntity.ok(pagedVinoAssembler.toModel(pageResult, usuarioVinoAssembler));
    }
}