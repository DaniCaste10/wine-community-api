package com.sos.winecommunity.assembler;

import com.sos.winecommunity.controller.UsuarioVinoControlador;
import com.sos.winecommunity.model.UsuarioVino;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioVinoModelAssembler extends RepresentationModelAssemblerSupport<UsuarioVino, UsuarioVino> {
    public UsuarioVinoModelAssembler() {
        super(UsuarioVinoControlador.class, UsuarioVino.class);
    }

    @Override
    public UsuarioVino toModel(UsuarioVino usuarioVino) {
        usuarioVino.add(linkTo(methodOn(UsuarioVinoControlador.class).obtenerVinos(usuarioVino.getUsuario().getId(), null, null, null, null, null, null, null, 0, 10)).withSelfRel());
        return usuarioVino;
    }
}
