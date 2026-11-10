package com.sos.winecommunity.repository;

import com.sos.winecommunity.model.Vino;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VinoRepository extends JpaRepository<Vino, Long> {
}
