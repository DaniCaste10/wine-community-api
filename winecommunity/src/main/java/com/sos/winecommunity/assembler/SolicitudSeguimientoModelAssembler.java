package com.sos.winecommunity.assembler;

import com.sos.winecommunity.controller.SolicitudSeguimientoControlador;
import com.sos.winecommunity.model.SolicitudSeguimiento;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class SolicitudSeguimientoModelAssembler extends RepresentationModelAssemblerSupport<SolicitudSeguimiento, SolicitudSeguimiento> {
    public SolicitudSeguimientoModelAssembler() {
        super(SolicitudSeguimientoControlador.class, SolicitudSeguimiento.class);
    }

    @Override
    public SolicitudSeguimiento toModel(SolicitudSeguimiento solicitud) {
        solicitud.add(linkTo(methodOn(SolicitudSeguimientoControlador.class).obtenerSolicitudes(solicitud.getReceptor().getId(), 0, 10)).withSelfRel());
        return solicitud;
    }
}