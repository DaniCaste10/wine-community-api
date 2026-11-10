package com.sos.winecommunity.repository;

import com.sos.winecommunity.model.UsuarioVino;
import com.sos.winecommunity.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface UsuarioVinoRepository extends JpaRepository<UsuarioVino, Long> {
    List<UsuarioVino> findByUsuario(Usuario usuario);

    List<UsuarioVino> findByUsuarioOrderByFechaAdicionDesc(Usuario usuario);

    // 5 últimos vinos añadidos por un usuario
    List<UsuarioVino> findTop5ByUsuarioOrderByFechaAdicionDesc(Usuario usuario);

    // 5 vinos con mayor puntuación de un usuario
    List<UsuarioVino> findTop5ByUsuarioOrderByPuntuacionDesc(Usuario usuario);

    @Query("SELECT uv FROM UsuarioVino uv WHERE uv.usuario = :usuario " +
            "AND uv.fechaAdicion >= :desde " +
            "AND uv.fechaAdicion <= :hasta " +
            "AND (CAST(:tipo AS string) IS NULL OR LOWER(uv.vino.tipo) = LOWER(CAST(:tipo AS string))) " +
            "AND (CAST(:bodega AS string) IS NULL OR LOWER(uv.vino.bodega) = LOWER(CAST(:bodega AS string))) " +
            "AND (CAST(:origen AS string) IS NULL OR LOWER(uv.vino.origen) = LOWER(CAST(:origen AS string))) " +
            "AND (:anada IS NULL OR uv.vino.anada = :anada)")

    Page<UsuarioVino> findByUsuarioWithFilters(
            @Param("usuario") Usuario usuario,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta,
            @Param("tipo") String tipo,
            @Param("bodega") String bodega,
            @Param("origen") String origen,
            @Param("anada") Integer anada,
            Pageable pageable);

    @Query(value = "SELECT uv.* FROM usuario_vinos uv " +
            "JOIN vino_uvas vu ON uv.vino_id = vu.vino_id " +
            "WHERE uv.usuario_id = :usuarioId " +
            "AND LOWER(vu.nombre_uva) = LOWER(:uva)", 
            nativeQuery = true)

    Page<UsuarioVino> findByUsuarioAndUva(@Param("usuarioId") Long usuarioId, @Param("uva") String uva, Pageable pageable);

    @Query(value = "SELECT AVG(uv.puntuacion) FROM usuario_vinos uv " +
            "JOIN vinos v ON v.id = uv.vino_id " +
            "WHERE uv.usuario_id = :usuarioId " +
            "AND (CAST(:desde AS date) IS NULL OR uv.fecha_adicion >= CAST(:desde AS date)) " +
            "AND (CAST(:hasta AS date) IS NULL OR uv.fecha_adicion <= CAST(:hasta AS date)) " +
            "AND (CAST(:tipo AS varchar) IS NULL OR LOWER(v.tipo) = LOWER(CAST(:tipo AS varchar))) " +
            "AND (CAST(:bodega AS varchar) IS NULL OR LOWER(v.bodega) = LOWER(CAST(:bodega AS varchar))) " +
            "AND (CAST(:origen AS varchar) IS NULL OR LOWER(v.origen) = LOWER(CAST(:origen AS varchar))) " +
            "AND (CAST(:anada AS integer) IS NULL OR v.anada = CAST(:anada AS integer))",
            nativeQuery = true)

    Double calcularMediaPuntuaciones(
            @Param("usuarioId") Long usuarioId,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta,
            @Param("tipo") String tipo,
            @Param("bodega") String bodega,
            @Param("origen") String origen,
            @Param("anada") Integer anada);
}