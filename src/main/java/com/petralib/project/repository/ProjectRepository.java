package com.petralib.project.repository;

import com.petralib.project.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

//    @Query(value = "SELECT * FROM projects as p INNER JOIN (SELECT * FROM project_user_authorities where " +
//            " auth_user_id = :userId) as pua on p.project_id = pua.auth_project_id", nativeQuery = true)
    @Query("select pua.project from ProjectUserRolesEntity pua where pua.user.id = :userId")
    List<ProjectEntity> getProjectsByUser(@Param("userId") Long userId);
}
