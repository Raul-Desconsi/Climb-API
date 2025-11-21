package com.application.climb.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.climb.Model.Chamado;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Integer> {

    @Query(value = """
        SELECT DISTINCT c.*
        FROM climb_database.chamado c
        LEFT JOIN climb_database.atendimento_chamado ac 
            ON c.id = ac.chamado_id
        WHERE c.id NOT IN (
            SELECT ac2.chamado_id
            FROM climb_database.atendimento_chamado ac2
            INNER JOIN (
                SELECT chamado_id, MAX(data_atendimento) AS ultima_data
                FROM climb_database.atendimento_chamado
                GROUP BY chamado_id
            ) ult
            ON ac2.chamado_id = ult.chamado_id
            AND ac2.data_atendimento = ult.ultima_data
            WHERE ac2.conclusao_chamado = 1
        )
        AND (c.area_afetada_id = :areaId OR ac.setor_direcionado_id = :setorId)
        """, nativeQuery = true)
    List<Chamado> findAllSemConclusaoPorSetor(
            @Param("areaId") Integer areaId,
            @Param("setorId") Integer setorId
    );

    @Query(value = """
            SELECT DISTINCT c.*
FROM climb_database.chamado c
LEFT JOIN climb_database.atendimento_chamado ac 
    ON c.id = ac.chamado_id
WHERE c.id NOT IN (
    SELECT ac2.chamado_id
    FROM climb_database.atendimento_chamado ac2
    INNER JOIN (
        SELECT chamado_id, MAX(data_atendimento) AS ultima_data
        FROM climb_database.atendimento_chamado
        GROUP BY chamado_id
    ) ult
    ON ac2.chamado_id = ult.chamado_id
    AND ac2.data_atendimento = ult.ultima_data
    WHERE ac2.conclusao_chamado = 1
)
""", nativeQuery = true)
    List<Chamado> findAllSemConclusao();

    @Query("SELECT c FROM Chamado c WHERE c.responsavelAbertura.id = :responsavelId")
    List<Chamado> findByResponsavelAberturaId(@Param("responsavelId") Integer responsavelId);

}