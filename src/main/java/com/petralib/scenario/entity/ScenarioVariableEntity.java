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
 * Связь между объектами сценария
 */
@Entity
@Table(name = "scenario_variables")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class ScenarioVariableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scenario_variable_id", nullable = false)
    Long id;

    /**
     * Переменная, принимающая данные от поставщика
     */
    @ManyToOne
    @JoinColumn(name = "consumer_variable_id", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    VariableEntity consumerVariable;

    /**
     * Переменная высшего уровня, к которой принадлежит эта связь, т.е. переменная блока сценария
     */
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

    /**
     *Переменная, которая является источником данных для этой связи
     */
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

    /**
     * ID локальной переменной в сценарии
     */
    @Column(name = "local_id", nullable = false)
    Long localId;

    @Column(name = "parent_id", nullable = true)
    Long parentId;

    /**
     * Родительская переменная в дереве переменных сценария
     */
//    @ManyToOne
//    @JoinColumn(name = "parent_id", referencedColumnName = "local_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    ScenarioVariableEntity parent;

    public String getExtractionString() {
        return typeDependence.stream().sorted(Comparator.comparingInt(TypeDependenceEntity::getCount))
                .map(typeDependenceEntity -> typeDependenceEntity.getCurrentField().getName())
                .collect(Collectors.joining("."));
    }


}
