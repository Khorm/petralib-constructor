package com.petralib.ctype.repository;

import com.petralib.ctype.entity.CTypeFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeFieldRepo extends JpaRepository<CTypeFieldEntity, Long> {

}
