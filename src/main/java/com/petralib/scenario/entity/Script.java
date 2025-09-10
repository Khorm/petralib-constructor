package com.petralib.scenario.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "scripts")
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class Script {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "script_id")
    Long id;

    @Column(updatable = false, nullable = false)
    String script;

    @OneToOne
    @JoinColumn(name = "scenario_variable", updatable = false, nullable = false)
    ScenarioVariableEntity scenarioVariable;

    public void setScenarioVariable(ScenarioVariableEntity scenarioVariable) {
        this.scenarioVariable = scenarioVariable;
    }
}
