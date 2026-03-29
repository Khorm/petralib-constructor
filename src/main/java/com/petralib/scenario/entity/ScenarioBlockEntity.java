package com.petralib.scenario.entity;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.scenario.enums.BeginEndType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "scenario_blocks")
@Getter
@Setter
@NoArgsConstructor
public class ScenarioBlockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scenario_block_id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "block_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    BlockEntity block;

    @ManyToOne
    @JoinColumn(name = "parent_workflow_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    BlockEntity parentWorkflow;

    Long x;

    Long y;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_scenario_block")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    ScenarioBlockEntity previousScenarioBlock;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_scenario_block")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    ScenarioBlockEntity nextScenarioBlock;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "scenarioBlock", orphanRemoval = true)
    Collection<ScenarioVariableEntity> variables = new ArrayList<>();

    @Column(name = "var_version", nullable = false)
    Long varVersion = 0L;

    @OneToOne(mappedBy = "connectedBlock", orphanRemoval = true)
    BeginEndEntity beginEnd;


    public ScenarioBlockEntity(Long scenarioBlockId) {
        this.id = scenarioBlockId;
    }

    public boolean isWorkflowEnd() {
        return block.getId().equals(parentWorkflow.getId())
                && beginEnd != null
                && beginEnd.getPointType() == BeginEndType.END;
    }

    public boolean isWorkflowStart() {
        return block.getId().equals(parentWorkflow.getId())
                && beginEnd != null
                && beginEnd.getPointType() == BeginEndType.START;
    }

    public Collection<VariableEntity> getContextVariables() {
        Collection<VariableEntity> contextVars = block.getInVariables();
        contextVars.addAll(block.getLocalVariables(id));
        contextVars.addAll(getPreviousVariables());
        return contextVars;
    }

    /**
     * Get all the previous variable entities that are in scope for a given workflow
     *
     * @return
     */
    public Collection<VariableEntity> getPreviousVariables() {

        ScenarioBlockEntity previousBlock = getPreviousScenarioBlock();

        if (isWorkflowEnd()) {
            previousBlock = beginEnd.getConnectedBlock();
        }

        Collection<VariableEntity> prevVariables = new ArrayList<>();
        while (previousBlock != null) {
            if (previousBlock.isWorkflowStart()){
                prevVariables.addAll(previousBlock.getBlock().getInVariables());
            }else {
                prevVariables.addAll(previousBlock.getBlock().getOutVariables());
            }

            previousBlock = previousBlock.getPreviousScenarioBlock();
        }

        return prevVariables;
    }

}
