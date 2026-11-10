package com.sos.winecommunity.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "seguidores")

public class Seguimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario seguidor;

    @ManyToOne
    @JoinColumn(name = "seguido_id", nullable = false)
    private Usuario seguido;
}
