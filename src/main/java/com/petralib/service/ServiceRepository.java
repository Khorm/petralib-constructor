package com.petralib.service;

import com.petralib.service.entity.ServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    @Query("FROM ServiceEntity se WHERE se.project.projectId = :projectId AND lower(se.name) like" +
            " lower(CONCAT('%', :serviceName, '%'))")
    Page<ServiceEntity> findServiceByName(@Param("projectId") Long projectId,
                                       @Param("serviceName") String serviceName,
                                       Pageable pageable);

    @Query("FROM ServiceEntity se WHERE se.project.projectId = :projectId")
    Page<ServiceEntity> findService(@Param("projectId") Long projectId,
                                 Pageable pageable);

    @Query("FROM ServiceEntity s where s.name = :name AND s.project.id = :projectId ")
    Optional<ServiceEntity> getByName(@Param("name") String name, @Param("projectId") Long projectId);



    @Query("FROM ServiceEntity se where se.project.id = :projectId")
    Collection<ServiceEntity> findServicesOfProject(@Param("projectId") Long projectId);
}
