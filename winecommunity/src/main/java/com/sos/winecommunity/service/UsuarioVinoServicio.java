package com.sos.winecommunity.service;

import com.sos.winecommunity.model.Usuario;
import com.sos.winecommunity.model.UsuarioVino;
import com.sos.winecommunity.model.Vino;
import com.sos.winecommunity.repository.UsuarioVinoRepository;
import com.sos.winecommunity.repository.VinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class UsuarioVinoServicio {
    private final UsuarioVinoRepository usuarioVinoRepository;
    private final VinoRepository vinoRepository;
    private final UsuarioServicio usuarioServicio;
    private final SeguimientoServicio seguimientoServicio;

    // Añadir vino a la lista de un usuario
    public UsuarioVino añadirVino(Long usuarioId, Vino vino, Double puntuacion) {
        Usuario usuario = usuarioServicio.obtenerUsuario(usuarioId);
        Vino vinoGuardado = vinoRepository.save(vino);
        UsuarioVino usuarioVino = new UsuarioVino();
        usuarioVino.setUsuario(usuario);
        usuarioVino.setVino(vinoGuardado);
        usuarioVino.setPuntuacion(puntuacion);
        usuarioVino.setFechaAdicion(LocalDate.now());
        return usuarioVinoRepository.save(usuarioVino);
    }

    // Eliminar vino de la lista
    public void eliminarVino(Long id) {
        if (!usuarioVinoRepository.existsById(id)) {
            throw new RuntimeException("Vino no encontrado en la lista");
        }
        usuarioVinoRepository.deleteById(id);
    }

    // Modificar puntuación de un vino
    public UsuarioVino modificarPuntuacion(Long usuarioVinoId, Double nuevaPuntuacion) {
        UsuarioVino usuarioVino = usuarioVinoRepository.findById(usuarioVinoId)
                .orElseThrow(() -> new RuntimeException("Vino no encontrado en la lista"));
        usuarioVino.setPuntuacion(nuevaPuntuacion);
        return usuarioVinoRepository.save(usuarioVino);
    }

    // Obtener lista de vinos de un usuario con filtros y paginación
    public Page<UsuarioVino> obtenerVinosDeUsuario(Long usuarioId, LocalDate desde, LocalDate hasta, String tipo, String bodega, String origen, Integer anada, String uva, Pageable pageable) {
        Usuario usuario = usuarioServicio.obtenerUsuario(usuarioId);
        if (uva != null) 
            return usuarioVinoRepository.findByUsuarioAndUva(usuarioId, uva, pageable);
        else {
            LocalDate desdeEfectiva = (desde != null) ? desde : LocalDate.of(1900, 1, 1);
            LocalDate hastaEfectiva = (hasta != null) ? hasta : LocalDate.of(9999, 12, 31);
            return usuarioVinoRepository.findByUsuarioWithFilters(usuario, desdeEfectiva, hastaEfectiva, tipo, bodega, origen, anada, pageable);
        }
    }

    // Estadísticas de puntuaciones. Obtener nota media de los vinos de un usuario con filtros
    public Double obtenerMediaPuntuaciones(Long usuarioId, LocalDate desde, LocalDate hasta, String tipo, String bodega, String origen, Integer anada, String uva) {
        Double media = usuarioVinoRepository.calcularMediaPuntuaciones(usuarioId, desde, hasta, tipo, bodega, origen, anada);
        if (media != null)
            return media;
        else 
            return 0.0;
    }

    // Obtener los 5 últimos vinos añadidos por un usuario
    public List<UsuarioVino> obtenerUltimosVinos(Long usuarioId) {
        Usuario usuario = usuarioServicio.obtenerUsuario(usuarioId);
        return usuarioVinoRepository.findTop5ByUsuarioOrderByFechaAdicionDesc(usuario);
    }

    // Obtener los 5 vinos con mayor puntuación de un usuario
    public List<UsuarioVino> obtenerTopVinos(Long usuarioId) {
        Usuario usuario = usuarioServicio.obtenerUsuario(usuarioId);
        return usuarioVinoRepository.findTop5ByUsuarioOrderByPuntuacionDesc(usuario);
    }

    // Obtener los 5 mejores vinos de todos los seguidos de un usuario
    public List<UsuarioVino> obtenerTopVinosAmigos(Long usuarioId) {
        Page<Usuario> seguidos = seguimientoServicio.obtenerSeguidos(usuarioId, null, Pageable.unpaged());
        List<UsuarioVino> topVinos = new ArrayList<>();
        for (Usuario seguido : seguidos) {
            topVinos.addAll(usuarioVinoRepository.findTop5ByUsuarioOrderByPuntuacionDesc(seguido));
        }
        topVinos.sort((a, b) -> Double.compare(b.getPuntuacion(), a.getPuntuacion()));
        if (topVinos.size() > 5) {
            return topVinos.subList(0, 5);
        }
        return topVinos;
    }
}
