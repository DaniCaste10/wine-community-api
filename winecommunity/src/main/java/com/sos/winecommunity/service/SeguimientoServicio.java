package com.sos.winecommunity.service;

import com.sos.winecommunity.model.Seguimiento;
import com.sos.winecommunity.model.Usuario;
import com.sos.winecommunity.repository.SeguimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor

public class SeguimientoServicio {
    private final SeguimientoRepository seguimientoRepository;
    private final UsuarioServicio usuarioServicio;

    // Dejar de seguir a un usuario
    @Transactional
    public void dejarDeSeguir(Long usuarioId, Long seguidoId) {
        Usuario usuario = usuarioServicio.obtenerUsuario(usuarioId);
        Usuario seguido = usuarioServicio.obtenerUsuario(seguidoId);
        seguimientoRepository.deleteBySeguidorAndSeguido(usuario, seguido);
    }

    // Listar usuarios a los que sigo con filtro por nombre
    public Page<Usuario> obtenerSeguidos(Long usuarioId, String patron, Pageable pageable) {
        Usuario usuario = usuarioServicio.obtenerUsuario(usuarioId);
        Page<Seguimiento> seguimientos;
        if (patron != null && !patron.isEmpty())
            seguimientos = seguimientoRepository.findBySeguidorAndSeguidoNombreUsuarioContaining(usuario, patron, pageable);
        else
            seguimientos = seguimientoRepository.findBySeguidor(usuario, pageable);

        return seguimientos.map(Seguimiento::getSeguido);
    }
}