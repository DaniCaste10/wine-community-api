package com.sos.winecommunity.service;

import com.sos.winecommunity.model.Usuario;
import com.sos.winecommunity.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor

public class UsuarioServicio {
    private final UsuarioRepository usuarioRepository;

    // Añadir usuario comprobando mayoría de edad
    public Usuario añadirUsuario(Usuario usuario) {
        int edad = Period.between(usuario.getFechaNacimiento(), LocalDate.now()).getYears();
        if (edad < 18) {
            throw new IllegalArgumentException("El usuario debe ser mayor de edad");
        }
        return usuarioRepository.save(usuario);
    }
    
    // Obtener usuario por id
    public Usuario obtenerUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Actualizar usuario
    public Usuario actualizarUsuario(Long id, Usuario datosNuevos) {
        Usuario usuario = obtenerUsuario(id);
        usuario.setNombreUsuario(datosNuevos.getNombreUsuario());
        usuario.setEmail(datosNuevos.getEmail());
        usuario.setFechaNacimiento(datosNuevos.getFechaNacimiento());
        return usuarioRepository.save(usuario);
    }

    // Borrar usuario
    public void borrarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    // Listar todos los usuarios con paginación y filtro por nombre
    public Page<Usuario> obtenerTodos(String nombre, Pageable pageable) {
        Page<Usuario> resultado;
        if (nombre != null) {
            resultado = usuarioRepository.findByNombreUsuarioContaining(nombre, pageable);
        } else {
            resultado = usuarioRepository.findAll(pageable);
        }
        if (resultado.isEmpty()) {
            throw new RuntimeException("No se encontraron usuarios");
        }
        return resultado;
    }
}
