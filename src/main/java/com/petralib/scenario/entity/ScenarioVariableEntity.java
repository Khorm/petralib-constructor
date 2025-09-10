package com.petralib.scenario.entity;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.scenario.enums.ScenarioVariableType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "scenario_variables")
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
    @JoinColumn(name = "consumer_variable_id", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    VariableEntity consumerVariable;

    @ManyToOne
    @JoinColumn(name = "owner_variable", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    VariableEntity ownerVariable;

    @ManyToOne
    @JoinColumn(name = "scenario_block_id", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    ScenarioBlockEntity scenarioBlock;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_var", updatable = false, nullable = false)
    ScenarioVariableType type;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true,
            mappedBy = "scenarioVariable")
    Collection<TypeDependenceEntity> typeDependence;

    @ManyToOne
    @JoinColumn(name = "producer_variable_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    VariableEntity producerVariable;

    @ManyToOne
    @JoinColumn(name = "producer_source_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    BlockEntity producerSource;

//    @OneToOne(mappedBy = "scenarioVariable", cascade = CascadeType.PERSIST)
    @Column(name = "producer_script")
    String producerScript;

    @Column(name = "local_id", nullable = false)
    Long localId;

    @Column(name = "parent_id", nullable = false)
    Long parentId;

    public String getExtractionString() {
        return typeDependence.stream().sorted(Comparator.comparingInt(TypeDependenceEntity::getCount))
                .map(typeDependenceEntity -> typeDependenceEntity.getCurrentField().getName())
                .collect(Collectors.joining("."));
    }


}
