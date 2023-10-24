package com.petralib.variable.entity;

import com.petralib.enitity.BlockEntity;
import com.petralib.project.entity.ProjectEntity;
import com.petralib.variable.Multiplicity;
import jakarta.persistence.*;

@Entity
@Table(name = "variable")
public class VariableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variable_id", nullable = false)
    Long id;

    @Column(name = "variable_name")
    String name;

    String description;

    @ManyToOne
    @JoinColumn(name = "type_id", insertable = false, updatable = false)
    TypeEntity type;

    @Enumerated(EnumType.STRING)
    Multiplicity multiplicity;

    @ManyToOne
    @JoinColumn(name = "block_id", insertable = false, updatable = false)
    BlockEntity block;


}
