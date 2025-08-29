package com.petralib.scenario.entity;

import com.petralib.type.entity.TypeEntity;
import com.petralib.type.entity.TypeVariableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Immutable
@Table(name = "type_dependence")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TypeDependenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_dep_id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "scenario_variable", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    ScenarioVariableEntity scenarioVariable;

    @ManyToOne
    @JoinColumn(name = "current_type_variable", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    TypeVariableEntity currentTypeVariable;

    @ManyToOne
    @JoinColumn(name = "owner_type", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    TypeEntity ownerType;

    @Column(name = "dep_count", updatable = false, nullable = false)
    Integer count;

    public void setScenarioVariable(ScenarioVariableEntity scenarioVariable) {
        this.scenarioVariable = scenarioVariable;
    }
}
