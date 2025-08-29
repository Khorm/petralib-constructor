package com.petralib.scenario.entity;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.scenario.enums.ScenarioVariableType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 *
 */
@Entity
@Table(name = "scenario_variable")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ScenarioVariableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scenario_variable_id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "producer_variable_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    VariableEntity producerVariable;

    @ManyToOne
    @JoinColumn(name = "consumer_variable_id", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    VariableEntity consumerVariable;

    @ManyToOne
    @JoinColumn(name = "scenario_block_id", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    ScenarioBlockEntity scenarioBlock;


    @Enumerated(EnumType.STRING)
    @Column(name = "type_var", updatable = false, nullable = false)
    ScenarioVariableType type;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true,
            mappedBy = "scenarioVariable")
    Collection<TypeDependenceEntity> typeDependence;

    @ManyToOne
    @JoinColumn(name = "source_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    BlockEntity source;

    @OneToOne(mappedBy = "scenarioVariable", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    Script script;

    public String getExtractionString() {
        return typeDependence.stream().sorted(Comparator.comparingInt(TypeDependenceEntity::getCount))
                .map(typeDependenceEntity -> typeDependenceEntity.getCurrentTypeVariable().getName())
                .collect(Collectors.joining("."));
    }


}
