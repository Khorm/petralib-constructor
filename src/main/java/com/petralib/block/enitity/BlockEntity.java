package com.petralib.block.enitity;

import com.petralib.block.enums.BlockType;
import com.petralib.block.enums.PinType;
import com.petralib.project.entity.ProjectEntity;
import com.petralib.service.entity.ServiceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Table(name = "blocks")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BlockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    ProjectEntity project;

    @ManyToOne
    @JoinColumn(name = "service_id")
    ServiceEntity service;

    @Column(name = "block_name" , nullable = false)
    String name;

    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "block_type", nullable = false)
    BlockType type;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "block", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @ToString.Exclude
    Collection<VariableEntity> variables = new ArrayList<>();


    public BlockEntity(Long id) {
        this.id = id;
    }

    public Collection<VariableEntity> getOutVariables(){
        return variables.stream().filter(entity -> entity.getPinType() == PinType.OUT && entity.getScenarioBlock() == null ).collect(Collectors.toList());
    }

    public Collection<VariableEntity> getInVariables(){
        return variables.stream().filter(entity -> entity.getPinType() == PinType.IN && entity.getScenarioBlock() == null ).collect(Collectors.toList());
    }

    public Collection<VariableEntity> getTimedVariables(Long scenarioVariableId){
        return variables.stream().filter(entity -> entity.getScenarioBlock() != null && scenarioVariableId.equals(entity.getScenarioBlock().getId()))
                .collect(Collectors.toList());
    }

    public Collection<VariableEntity> getAllTimedVariables(){
        return variables.stream().filter(entity -> entity.getScenarioBlock() != null )
                .collect(Collectors.toList());
    }

    public void setLocalVariables(Collection<VariableEntity> newLocalVariables){
        variables.removeIf(variableEntity -> {
            boolean find = false;
            for (VariableEntity newLocalVariable : newLocalVariables) {
                if (newLocalVariable.getName().equals(variableEntity.getName()) && variableEntity.getScenarioBlock() != null
                        && variableEntity.getScenarioBlock().getId().equals(newLocalVariable.getScenarioBlock().getId())) {
                    find = true;
                    break;
                }
            }
            return find;
        });
        variables.addAll(newLocalVariables);
    }

    public void deleteLocalVariable(Long variableId) {
        variables.removeIf(variableEntity -> variableEntity.getScenarioBlock() != null && variableEntity.getId().equals(variableId));
    }
}
