package com.sos.winecommunity.assembler;

import com.sos.winecommunity.controller.SeguimientoControlador;
import com.sos.winecommunity.controller.SolicitudSeguimientoControlador;
import com.sos.winecommunity.controller.UsuarioControlador;
import com.sos.winecommunity.controller.UsuarioVinoControlador;
import com.sos.winecommunity.model.Usuario;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioModelAssembler extends RepresentationModelAssemblerSupport<Usuario, Usuario> {

    public UsuarioModelAssembler() {
        super(UsuarioControlador.class, Usuario.class);
    }

    @Override
    public Usuario toModel(Usuario usuario) {
        usuario.add(linkTo(methodOn(UsuarioControlador.class).obtenerUsuario(usuario.getId())).withSelfRel());
        usuario.add(linkTo(methodOn(UsuarioVinoControlador.class).obtenerVinos(usuario.getId(), null, null, null, null, null, null, null, 0, 10)).withRel("vinos"));
        usuario.add(linkTo(methodOn(SeguimientoControlador.class).obtenerSeguidos(usuario.getId(), null, 0, 10)).withRel("seguidos"));
        usuario.add(linkTo(methodOn(SolicitudSeguimientoControlador.class).obtenerSolicitudes(usuario.getId(), 0, 10)).withRel("solicitudes"));
        return usuario;
    }
}
