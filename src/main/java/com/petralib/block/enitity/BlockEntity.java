package com.petralib.block.enitity;

import com.petralib.block.BlockType;
import com.petralib.project.entity.ProjectEntity;
import com.petralib.variable.dto.VariableDto;
import com.petralib.variable.entity.VariableEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "block")
@Getter
@Setter
@ToString
public class BlockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", updatable = false)
    ProjectEntity project;

    @Column(name = "block_name" , nullable = false)
    String name;

    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "block_type", nullable = false)
    BlockType type;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "block_id", nullable = false)
    Collection<VariableEntity> variables = new ArrayList<>();
}
