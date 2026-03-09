package com.petralib.scenario.entity;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.scenario.enums.FunctionVariableType;
import com.petralib.scenario.enums.ScenarioVariableType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
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
     * Переменная, которая является источником данных для этой связи
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

    @Column(name = "function_variable_type")
    @Enumerated(EnumType.STRING)
    FunctionVariableType functionVariableType;
    /**
     * ID локальной переменной в сценарии
     */
//    @Column(name = "local_id", nullable = false)
//    Long localId;
//
//    @Column(name = "parent_id", nullable = true)
//    Collection<ScenarioVariableEntity> parentIds;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "scenario_variable_mapping",
//            joinColumns = {
//                    @JoinColumn(name = "child_id", referencedColumnName = "scenario_variable_id"),
//                    @JoinColumn(name = "child_local_id", referencedColumnName = "local_id")
//            },
//            inverseJoinColumns = {
//                    @JoinColumn(name = "parent_id", referencedColumnName = "scenario_variable_id"),
//                    @JoinColumn(name = "parent_local_id", referencedColumnName = "local_id")
//            }
//
//    )
//    Collection<ScenarioVariableEntity> parents = new ArrayList<>();

    /**
     * Дочерние переменные — те, кто зависят от этой переменной
     * Обратная сторона связи (инверсия parents)
     */
//    @ManyToMany(mappedBy = "parents", fetch = FetchType.LAZY)
//    @ManyToOne
//    @JoinTable(
//            name = "scenario_variable_mapping",
//            joinColumns = {
//                    @JoinColumn(name = "parent_id", referencedColumnName = "scenario_variable_id"),
//                    @JoinColumn(name = "parent_local_id", referencedColumnName = "local_id")
//            },
//            inverseJoinColumns = {
//                    @JoinColumn(name = "child_id", referencedColumnName = "scenario_variable_id"),
//                    @JoinColumn(name = "child_local_id", referencedColumnName = "local_id")
//            }
//    )
//    @OnDelete(action = OnDeleteAction.CASCADE)

//    Long child;
//
//    @OneToMany(mappedBy = "child", fetch = FetchType.LAZY)
//    Collection<ScenarioVariableEntity> parents = new ArrayList<>();

    public String getExtractionString() {
        return typeDependence.stream().sorted(Comparator.comparingInt(TypeDependenceEntity::getCount))
                .map(typeDependenceEntity -> typeDependenceEntity.getCurrentField().getName())
                .collect(Collectors.joining("."));
    }



}
