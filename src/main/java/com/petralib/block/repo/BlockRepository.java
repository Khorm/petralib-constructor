package com.petralib.block.repo;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enums.BlockType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BlockRepository extends JpaRepository<BlockEntity, Long> {
    @Query("FROM BlockEntity be WHERE be.project.projectId = :projectId AND lower(be.name) like" +
            " lower(CONCAT('%', :blockName, '%')) AND be.type = :blockType")
    Page<BlockEntity> findBlocksByName(@Param("projectId") Long projectId,
                                       @Param("blockName") String blockName,
                                       @Param("blockType") BlockType blockType, Pageable pageable);

    @Query("FROM BlockEntity be WHERE be.project.projectId = :projectId AND be.type = :blockType")
    Page<BlockEntity> findBlocks(@Param("projectId") Long projectId, @Param("blockType") BlockType blockType,
                                 Pageable pageable);

    @Query("FROM BlockEntity be WHERE be.project.projectId = :projectId AND be.type = 'SOURCE' AND lower(be.name) like " +
            " lower(CONCAT('%', :name, '%'))")
    Collection<BlockEntity> findSourcesByNameLike(@Param("projectId") Long projectId, @Param("name") String name);

//    @Query("SELECT DISTINCT source FROM BlockEntity source " +
//            "JOIN FETCH source.variables vars " +
//            "WHERE EXISTS ( " +
//            "SELECT 1 FROM VariableEntity ve WHERE ve.block = source AND ve.multiplicity = :multiplicity " +
//            "AND ve.varType.id = :varTypeId AND ve.pinType = 'OUT' " +
//            ") AND " +
//            "source.project.id = :projectId ")
//    Collection<BlockEntity> findSourcesWithMultiplicityAndTypeRetVariable(@Param("multiplicity") Multiplicity multiplicity,
//                                                                          @Param("varTypeId") Long varTypeId,
//                                                                          @Param("projectId") Long projectId);

    @Query("FROM BlockEntity WHERE service.id = :serviceId")
    Collection<BlockEntity> findBlocksByService(@Param("serviceId") Long serviceId);
}
