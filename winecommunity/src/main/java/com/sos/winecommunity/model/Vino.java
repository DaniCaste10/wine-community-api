package com.sos.winecommunity.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "vinos")

public class Vino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String bodega;

    @Column(nullable = false)
    private Integer anada;

    @Column(nullable = false)
    private String origen;

    @Column(nullable = false)
    private String tipo;

    @ElementCollection
    @CollectionTable(name = "vino_uvas", joinColumns = @JoinColumn(name = "vino_id"))
    private List<PorcentajeUva> uvas;
}
