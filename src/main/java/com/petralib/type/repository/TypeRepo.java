package com.petralib.type.repository;

import com.petralib.type.entity.TypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TypeRepo extends JpaRepository<TypeEntity, Long> {
    @Query("FROM TypeEntity te WHERE te.project.projectId = :projectId AND lower(te.name) like" +
            " lower(CONCAT('%', :blockName, '%'))")
    Page<TypeEntity> findTypesByName(@Param("projectId") Long projectId,
                                     @Param("blockName") String typeName,
                                     Pageable pageable);

    @Query("FROM TypeEntity te WHERE te.project.projectId = :projectId")
    Page<TypeEntity> findTypes(@Param("projectId") Long projectId, Pageable pageable);

    @Query("FROM TypeEntity te WHERE te.project.projectId = :projectId")
    Collection<TypeEntity> findTypes(@Param("projectId") Long projectId);

    @Query("SELECT COUNT(te) > 0 FROM TypeEntity te where te.name = :name AND te.project.id = :projectId ")
    boolean existsByName(@Param("name") String name, @Param("projectId") Long projectId);
}
