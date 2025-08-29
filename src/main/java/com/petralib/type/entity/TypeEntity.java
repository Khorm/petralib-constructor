package com.petralib.type.entity;

import com.petralib.project.entity.ProjectEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;


@Entity
@Table(name = "work_type")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class TypeEntity {
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

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    Collection<TypeVariableEntity> variables = new ArrayList<>();

    public TypeEntity(Long id){
        this.id = id;
    }


}
