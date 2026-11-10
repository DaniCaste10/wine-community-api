package com.sos.winecommunity.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable

public class PorcentajeUva {
    private String nombreUva;
    private Double porcentaje;
}
