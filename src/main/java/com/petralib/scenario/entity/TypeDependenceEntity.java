package com.petralib.scenario.entity;

import com.petralib.ctype.entity.CTypeFieldEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Immutable
@Table(name = "type_dependencies")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeDependenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_dep_id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "scenario_variable", updatable = false, nullable = false)
    @Setter
    ScenarioVariableEntity scenarioVariable;

    @ManyToOne
    @JoinColumn(name = "current_ctype_field", updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    CTypeFieldEntity currentField;


    @Column(name = "dep_count", updatable = false, nullable = false)
    Integer count;

}
