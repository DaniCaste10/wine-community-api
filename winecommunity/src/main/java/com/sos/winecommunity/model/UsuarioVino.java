package com.sos.winecommunity.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "usuario_vinos")

public class UsuarioVino extends RepresentationModel<UsuarioVino> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "vino_id", nullable = false)
    private Vino vino;

    @Column(nullable = false)
    private Double puntuacion;

    @Column(nullable = false)
    private LocalDate fechaAdicion;
}