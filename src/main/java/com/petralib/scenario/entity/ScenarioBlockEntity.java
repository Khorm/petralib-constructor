package com.petralib.scenario.entity;

import com.petralib.block.enitity.BlockEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "scenarioBlock")
    Collection<ScenarioVariableEntity> variables = new ArrayList<>();


    public ScenarioBlockEntity(Long scenarioBlockId) {
        this.id = scenarioBlockId;
    }

}
