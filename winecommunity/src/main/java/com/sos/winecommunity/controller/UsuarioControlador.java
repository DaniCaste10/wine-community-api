package com.sos.winecommunity.controller;

import com.sos.winecommunity.assembler.UsuarioModelAssembler;
import com.sos.winecommunity.model.Usuario;
import com.sos.winecommunity.service.UsuarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor

public class UsuarioControlador {
    private final UsuarioServicio usuarioServicio;
    private final UsuarioModelAssembler assembler;
    private final PagedResourcesAssembler<Usuario> pagedAssembler;

    // POST /usuarios — Añadir usuario nuevo
    @PostMapping
    public ResponseEntity<?> añadirUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioServicio.añadirUsuario(usuario);
            URI location = linkTo(methodOn(UsuarioControlador.class).obtenerUsuario(nuevo.getId())).toUri();
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /usuarios/{id} — Ver datos de un usuario
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioServicio.obtenerUsuario(id);
            return ResponseEntity.ok(assembler.toModel(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT /usuarios/{id} — Actualizar datos de un usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario datosNuevos) {
        try {
            Usuario usuario = usuarioServicio.actualizarUsuario(id, datosNuevos);
            return ResponseEntity.ok(assembler.toModel(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /usuarios/{id} — Borrar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarUsuario(@PathVariable Long id) {
        try {
            usuarioServicio.borrarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /usuarios — Listar todos los usuarios con paginación y filtro por nombre
    @GetMapping
    public ResponseEntity<?> obtenerTodos(@RequestParam(required = false) String nombre, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Usuario> pageResult = usuarioServicio.obtenerTodos(nombre, pageable);
            return ResponseEntity.ok(pagedAssembler.toModel(pageResult, assembler));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}