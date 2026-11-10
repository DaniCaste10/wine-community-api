package com.sos.winecommunity.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "usuarios")

public class Usuario extends RepresentationModel<Usuario> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombreUsuario;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(unique = true, nullable = false)
    private String email;
}