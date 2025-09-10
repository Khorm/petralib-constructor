package com.petralib.ctype.entity;

import com.petralib.project.entity.ProjectEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collection;


@Entity
@Table(name = "ctypes")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class CTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id", nullable = false)
    Long id;

    @Column(name = "type_name", nullable = false)
    String name;

    String description;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    ProjectEntity project;

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    Collection<CTypeFieldEntity> variables = new ArrayList<>();

    public CTypeEntity(Long id){
        this.id = id;
    }


}
